package com.longg.gky;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longg.gky.adapters.ProductAdapter;
import com.longg.gky.models.Product;
import com.longg.gky.viewmodels.MainViewModel;

import java.util.ArrayList;

public class ProductsListActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        RecyclerView rvProducts = findViewById(R.id.rv_products);
        ProductAdapter productAdapter = new ProductAdapter(this, new ArrayList<>(), R.layout.item_product_grid);
        productAdapter.setOnProductClickListener(this);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        rvProducts.setAdapter(productAdapter);

        mainViewModel.getProducts().observe(this, productAdapter::updateProducts);
    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product_id", product.getId());
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(Product product) {
        // Not implemented
    }
}
