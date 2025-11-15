package com.longg.gky.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.longg.gky.data.DBRepository;
import com.longg.gky.data.entities.ProductEntity;
import com.longg.gky.models.Product;

import java.util.List;
import java.util.stream.Collectors;

public class MainViewModel extends ViewModel {
    private final DBRepository dbRepository;
    private final LiveData<List<Product>> products;
    private final MutableLiveData<Integer> cartItemCount = new MutableLiveData<>();
    private final MutableLiveData<Void> reloadTrigger = new MutableLiveData<>();

    public MainViewModel() {
        dbRepository = DBRepository.get();

        // Sử dụng Transformations.switchMap để tải lại dữ liệu khi cần
        products = Transformations.switchMap(reloadTrigger, input -> 
            Transformations.map(dbRepository.getAllProducts(), entities -> 
                entities.stream()
                        .map(Product::fromEntity)
                        .collect(Collectors.toList())
            )
        );

        // Tải dữ liệu lần đầu
        loadProducts();
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public LiveData<Integer> getCartItemCount() {
        return cartItemCount;
    }

    public void loadProducts() {
        reloadTrigger.setValue(null);
    }

    public void loadCartItemCount() {
        dbRepository.getCartCountAsync(cartItemCount::postValue);
    }
}
