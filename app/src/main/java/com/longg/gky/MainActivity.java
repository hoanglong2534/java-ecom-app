package com.longg.gky;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.SearchView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.longg.gky.adapters.ProductAdapter;
import com.longg.gky.models.Product;
import com.longg.gky.utils.AuthManager;
import com.longg.gky.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private ProductAdapter productAdapter;
    private MainViewModel mainViewModel;
    private TextView tvCartBadge, tvWelcomeMessage;
    private List<Product> allProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        initViews();
        setupRecyclerViews();
        setupBottomNavigation();
        setupSearch();
        observeViewModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainViewModel.loadCartItemCount();
        updateWelcomeMessage();
    }

    private void initViews() {
        tvWelcomeMessage = findViewById(R.id.tvWelcomeMessage);
        View cartContainer = findViewById(R.id.cart_container);
        tvCartBadge = findViewById(R.id.tv_cart_badge);

        cartContainer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void updateWelcomeMessage() {
        if (AuthManager.isLoggedIn(this)) {
            tvWelcomeMessage.setText("Xin chào, " + AuthManager.getUserName(this) + "!");
        } else {
            tvWelcomeMessage.setText("Đăng nhập để trải nghiệm");
        }
    }

    private void setupRecyclerViews() {
        RecyclerView rvProducts = findViewById(R.id.rv_new_products);
        productAdapter = new ProductAdapter(this, new ArrayList<>(), R.layout.item_product_grid);
        productAdapter.setOnProductClickListener(this);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        rvProducts.setAdapter(productAdapter);
    }

    private void setupSearch() {
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return true;
            }
        });
    }

    private void filterProducts(String query) {
        List<Product> filteredList = allProducts.stream()
                .filter(product -> product.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        productAdapter.updateProducts(filteredList);
    }

    private void observeViewModel() {
        mainViewModel.getProducts().observe(this, products -> {
            allProducts.clear();
            allProducts.addAll(products);
            productAdapter.updateProducts(products);
        });

        mainViewModel.getCartItemCount().observe(this, count -> {
            if (count > 0) {
                tvCartBadge.setText(String.valueOf(count));
                tvCartBadge.setVisibility(View.VISIBLE);
            } else {
                tvCartBadge.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FloatingActionButton fab = findViewById(R.id.fab_chat);
        if (fab != null) {
            fab.setScaleX(0f);
            fab.setScaleY(0f);
            fab.animate().scaleX(1f).scaleY(1f).setDuration(300).setStartDelay(200).start();
            fab.setOnClickListener(v -> startActivity(new android.content.Intent(MainActivity.this, com.longg.gky.chat.ChatActivity.class)));
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                return true;
            } else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });
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
