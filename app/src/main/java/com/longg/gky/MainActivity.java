package com.longg.gky;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.longg.gky.adapters.CategoryAdapter;
import com.longg.gky.adapters.ProductAdapter;
import com.longg.gky.models.Category;
import com.longg.gky.models.Product;
import com.longg.gky.utils.CartManager;
import com.longg.gky.utils.DataUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements 
        CategoryAdapter.OnCategoryClickListener, 
        ProductAdapter.OnProductClickListener {

    // Views
    private EditText etSearch;
    private FrameLayout ivCart;
    private TextView tvCartBadge;
    private TextView tvSeeAllFeatured;
    private TextView tvSeeAllOffers;
    private RecyclerView rvCategories;
    private RecyclerView rvFeaturedProducts;
    private RecyclerView rvSpecialOffers;
    private BottomNavigationView bottomNavigation;

    // Adapters
    private CategoryAdapter categoryAdapter;
    private ProductAdapter featuredProductAdapter;
    private ProductAdapter specialOffersAdapter;

    // Data
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        initData();
        setupRecyclerViews();
        setupClickListeners();
        updateCartBadge();
    }

    private void initViews() {
        etSearch = findViewById(R.id.etSearch);
        ivCart = findViewById(R.id.ivCart);
        tvCartBadge = findViewById(R.id.tvCartBadge);
        tvSeeAllFeatured = findViewById(R.id.tvSeeAllFeatured);
        tvSeeAllOffers = findViewById(R.id.tvSeeAllOffers);
        rvCategories = findViewById(R.id.rvCategories);
        rvFeaturedProducts = findViewById(R.id.rvFeaturedProducts);
        rvSpecialOffers = findViewById(R.id.rvSpecialOffers);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void initData() {
        cartManager = CartManager.getInstance();
    }

    private void setupRecyclerViews() {
        // Categories RecyclerView
        LinearLayoutManager categoriesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvCategories.setLayoutManager(categoriesLayoutManager);
        
        List<Category> categories = DataUtils.getSampleCategories();
        categoryAdapter = new CategoryAdapter(this, categories);
        categoryAdapter.setOnCategoryClickListener(this);
        rvCategories.setAdapter(categoryAdapter);

        // Featured Products RecyclerView
        LinearLayoutManager featuredLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvFeaturedProducts.setLayoutManager(featuredLayoutManager);
        
        List<Product> featuredProducts = DataUtils.getFeaturedProducts();
        featuredProductAdapter = new ProductAdapter(this, featuredProducts, R.layout.item_product_featured);
        featuredProductAdapter.setOnProductClickListener(this);
        rvFeaturedProducts.setAdapter(featuredProductAdapter);

        // Special Offers RecyclerView
        GridLayoutManager offersLayoutManager = new GridLayoutManager(this, 2);
        rvSpecialOffers.setLayoutManager(offersLayoutManager);
        
        List<Product> discountedProducts = DataUtils.getDiscountedProducts();
        specialOffersAdapter = new ProductAdapter(this, discountedProducts, R.layout.item_product_grid);
        specialOffersAdapter.setOnProductClickListener(this);
        rvSpecialOffers.setAdapter(specialOffersAdapter);
    }

    private void setupClickListeners() {
        ivCart.setOnClickListener(v -> openCart());
        
        tvSeeAllFeatured.setOnClickListener(v -> {
            // Open all products activity
            Toast.makeText(this, "Opening all featured products...", Toast.LENGTH_SHORT).show();
        });
        
        tvSeeAllOffers.setOnClickListener(v -> {
            // Open all offers activity
            Toast.makeText(this, "Opening all special offers...", Toast.LENGTH_SHORT).show();
        });
        
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Already on home
                return true;
            } else if (itemId == R.id.nav_categories) {
                Toast.makeText(this, "Categories clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_favorites) {
                Toast.makeText(this, "Favorites clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_cart) {
                openCart();
                return true;
            } else if (itemId == R.id.nav_profile) {
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void openCart() {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }

    private void updateCartBadge() {
        int itemCount = cartManager.getCartItemCount();
        if (itemCount > 0) {
            tvCartBadge.setText(String.valueOf(itemCount));
            tvCartBadge.setVisibility(View.VISIBLE);
        } else {
            tvCartBadge.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
    }

    // CategoryAdapter.OnCategoryClickListener
    @Override
    public void onCategoryClick(Category category) {
        Toast.makeText(this, "Category: " + category.getName(), Toast.LENGTH_SHORT).show();
        // Open category products activity
    }

    // ProductAdapter.OnProductClickListener
    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product_id", product.getId());
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(Product product) {
        String message = product.isFavorite() ? "Added to favorites" : "Removed from favorites";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}