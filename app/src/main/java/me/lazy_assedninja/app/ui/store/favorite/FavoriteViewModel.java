package me.lazy_assedninja.app.ui.store.favorite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import me.lazy_assedninja.app.dto.FavoriteDTO;
import me.lazy_assedninja.app.repository.FavoriteRepository;
import me.lazy_assedninja.app.repository.UserRepository;
import me.lazy_assedninja.app.utils.AbsentLiveData;
import me.lazy_assedninja.app.vo.Favorite;
import me.lazy_assedninja.app.vo.Resource;
import me.lazy_assedninja.app.vo.Result;
import me.lazy_assedninja.app.vo.Store;

@HiltViewModel
public class FavoriteViewModel extends ViewModel {

    private final UserRepository userRepository;
    private FavoriteRepository favoriteRepository;

    private final MutableLiveData<FavoriteDTO> storeRequest = new MutableLiveData<>();
    private final MutableLiveData<Favorite> favoriteRequest = new MutableLiveData<>();

    @Inject
    public FavoriteViewModel(UserRepository userRepository, FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public boolean isLoggedIn() {
        return getUserID() == 0;
    }

    public int getUserID() {
        return userRepository.getUserID();
    }

    public LiveData<Resource<List<Store>>> stores = Transformations.switchMap(storeRequest, request -> {
        if (request == null) {
            return AbsentLiveData.create();
        } else {
            return favoriteRepository.loadFavorites(request);
        }
    });

    public void requestStore() {
        if (storeRequest.getValue() == null || storeRequest.getValue().getUserID() != getUserID()) {
            storeRequest.setValue(new FavoriteDTO(getUserID()));
        }
    }

    public void refresh() {
        if (storeRequest.getValue() != null) {
            storeRequest.setValue(storeRequest.getValue());
        }
    }

    public LiveData<Resource<Result>> result = Transformations.switchMap(favoriteRequest, favorite -> {
        if (favorite == null) {
            return AbsentLiveData.create();
        } else {
            return favoriteRepository.changeFavoriteStatus(favorite);
        }
    });

    public void setFavoriteRequest(int storeID, boolean isFavorite) {
        Favorite favorite = favoriteRequest.getValue();
        if (favorite == null || favorite.getStoreID() != storeID ||
                (favorite.getStoreID() == storeID && favorite.getStatus() == isFavorite)) {
            favoriteRequest.setValue(new Favorite(getUserID(), storeID, !isFavorite));
        }
    }
}