package me.lazy_assedninja.app.ui.favorite;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import me.lazy_assedninja.app.R;
import me.lazy_assedninja.app.databinding.FragmentFavoriteBinding;
import me.lazy_assedninja.app.ui.store.StoreAdapter;
import me.lazy_assedninja.app.ui.store.home.HomeFragmentDirections;
import me.lazy_assedninja.app.vo.Resource;
import me.lazy_assedninja.library.ui.BaseFragment;
import me.lazy_assedninja.library.utils.ExecutorUtils;

import static java.util.Collections.emptyList;

public class FavoriteFragment extends BaseFragment {

    private FragmentFavoriteBinding binding;
    private FavoriteViewModel viewModel;

    private NavController navController;
    private StoreAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_favorite,
                container,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        navController = Navigation.findNavController(view);

        initView();
        initData();
    }

    private void initView() {
        adapter = new StoreAdapter(
                new ExecutorUtils(),
                viewModel.getLoggedInUserID(),
                (binding, favorite) -> { // FavoriteClickCallback
                    if (viewModel.getLoggedInUserID() == 0) {
                        showToast(R.string.error_please_login_first);
                        return;
                    }
                    if (getActivity() == null) return;
                    binding.btFavorite.setIcon(ContextCompat.getDrawable(getActivity(),
                            favorite.getStatus() ? R.drawable.ic_heart_checked : R.drawable.ic_heart_unchecked));
                    viewModel.setFavoriteRequest(favorite);
                },
                (binding, storeID) -> { // InformationClickCallback
                    FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                            .addSharedElement(binding.ivPicture, String.valueOf(storeID))
                            .build();
                    navController.navigate(HomeFragmentDirections.actionToFragmentStoreInformation(storeID), extras);
                }
        );
        binding.rv.setAdapter(adapter);
    }

    private void initData() {
        binding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel.store.observe(getViewLifecycleOwner(), list -> {
            Log.d("FavoriteFragment", "xxx");
            if (list != null) {
                adapter.submitList(list);
            } else {
                adapter.submitList(emptyList());
            }
        });
        viewModel.favorite.observe(getViewLifecycleOwner(), resultResource -> {
            if (resultResource.getStatus().equals(Resource.SUCCESS)) {
                showToast(resultResource.getData().getResult());
            } else if (resultResource.getStatus().equals(Resource.ERROR)) {
                showToast(resultResource.getMessage());
            }
        });
    }
}