package me.lazy_assedninja.app.ui.promotion;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import me.lazy_assedninja.app.dto.PromotionDTO;
import me.lazy_assedninja.app.repository.PromotionRepository;
import me.lazy_assedninja.app.utils.AbsentLiveData;
import me.lazy_assedninja.app.vo.Promotion;
import me.lazy_assedninja.app.vo.Resource;

@HiltViewModel
public class PromotionViewModel extends AndroidViewModel {

    private PromotionRepository promotionRepository;

    private final MutableLiveData<PromotionDTO> promotionRequest = new MutableLiveData<>();

    @Inject
    public PromotionViewModel(@NonNull Application application, PromotionRepository promotionRepository) {
        super(application);
        this.promotionRepository = promotionRepository;
    }

    public final LiveData<Resource<List<Promotion>>> promotions = Transformations.switchMap(promotionRequest, request -> {
        if (request == null) {
            return AbsentLiveData.create();
        } else {
            return promotionRepository.loadPromotions();
        }
    });

    public void requestPromotion() {
        if (promotionRequest.getValue() == null) {
            promotionRequest.setValue(new PromotionDTO());
        }
    }
}