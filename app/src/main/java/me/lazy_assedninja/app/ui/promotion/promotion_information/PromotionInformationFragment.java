package me.lazy_assedninja.app.ui.promotion.promotion_information;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import dagger.hilt.android.AndroidEntryPoint;
import me.lazy_assedninja.app.R;
import me.lazy_assedninja.app.databinding.PromotionInformationFragmentBinding;
import me.lazy_assedninja.library.ui.BaseFragment;

@AndroidEntryPoint
public class PromotionInformationFragment extends BaseFragment {

    private PromotionInformationFragmentBinding binding;
    private PromotionInformationViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.promotion_information_fragment,
                container,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(PromotionInformationViewModel.class);

        initView();
        initData();
    }

    private void initView() {
        binding.setLifecycleOwner(getViewLifecycleOwner());
    }

    private void initData() {
        int id = PromotionInformationFragmentArgs.fromBundle(getArguments()).getPromotionID();
        binding.setPromotion(viewModel.get(id));
    }
}