package com.longg.gky;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.longg.gky.models.Product;
import com.longg.gky.utils.CartManager;
import com.longg.gky.utils.DataUtils;

import java.text.DecimalFormat;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView ivProductImage;
    private TextView tvProductName;
    private TextView tvProductBrand;
    private TextView tvProductPrice;
    private TextView tvOriginalPrice;
    private TextView tvProductDescription;
    private TextView tvRating;
    private TextView tvReviewCount;
    private TextView tvStock;
    private ImageView ivFavorite;
    private Button btnAddToCart;
    private Button btnBuyNow;

    private Product product;
    private CartManager cartManager;
    private DecimalFormat priceFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initViews();
        setupToolbar();
        loadProductData();
        setupClickListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        ivProductImage = findViewById(R.id.ivProductImage);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductBrand = findViewById(R.id.tvProductBrand);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvOriginalPrice = findViewById(R.id.tvOriginalPrice);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        tvRating = findViewById(R.id.tvRating);
        tvReviewCount = findViewById(R.id.tvReviewCount);
        tvStock = findViewById(R.id.tvStock);
        ivFavorite = findViewById(R.id.ivFavorite);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);

        cartManager = CartManager.getInstance();
        priceFormat = new DecimalFormat("$#,##0.00");
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Product Details");
        }
    }

    private void loadProductData() {
        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId != -1) {
            List<Product> products = DataUtils.getSampleProducts();
            for (Product p : products) {
                if (p.getId() == productId) {
                    product = p;
                    break;
                }
            }
        }

        if (product != null) {
            displayProductInfo();
        } else {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayProductInfo() {
        tvProductName.setText(product.getName());
        tvProductBrand.setText(product.getBrand());
        tvProductPrice.setText(priceFormat.format(product.getPrice()));
        tvProductDescription.setText(product.getDescription());
        tvRating.setText(String.valueOf(product.getRating()));
        tvReviewCount.setText("(" + product.getReviewCount() + " reviews)");
        tvStock.setText(product.getStock() + " in stock");

        // Handle original price
        if (product.hasDiscount()) {
            tvOriginalPrice.setText(priceFormat.format(product.getOriginalPrice()));
            tvOriginalPrice.setVisibility(View.VISIBLE);
        } else {
            tvOriginalPrice.setVisibility(View.GONE);
        }

        // Set favorite icon
        updateFavoriteIcon();

        // Set placeholder image
        ivProductImage.setImageResource(R.drawable.placeholder_product);
    }

    private void setupClickListeners() {
        ivFavorite.setOnClickListener(v -> {
            product.setFavorite(!product.isFavorite());
            updateFavoriteIcon();
            String message = product.isFavorite() ? "Added to favorites" : "Removed from favorites";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        btnAddToCart.setOnClickListener(v -> {
            cartManager.addToCart(product);
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
        });

        btnBuyNow.setOnClickListener(v -> {
            cartManager.addToCart(product);
            Toast.makeText(this, "Proceeding to checkout...", Toast.LENGTH_SHORT).show();
            // Here you would typically navigate to checkout
        });
    }

    private void updateFavoriteIcon() {
        ivFavorite.setImageResource(product.isFavorite() ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
