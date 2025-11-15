package com.longg.gky;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.longg.gky.adapters.CartAdapter;
import com.longg.gky.models.CartItem;
import com.longg.gky.utils.CartManager;

import java.text.DecimalFormat;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemClickListener {

    private Toolbar toolbar;
    private RecyclerView rvCartItems;
    private View tvEmptyCart;
    private TextView tvSubtotal;
    private TextView tvTax;
    private TextView tvTotal;
    private Button btnCheckout;
    private BottomNavigationView bottomNavigationView;

    private CartAdapter cartAdapter;
    private CartManager cartManager;
    private DecimalFormat priceFormat;
    private ActivityResultLauncher<Intent> checkoutLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupCheckoutLauncher();
        handleInitialIntent();
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartUI();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvCartItems = findViewById(R.id.rvCartItems);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTax = findViewById(R.id.tvTax);
        tvTotal = findViewById(R.id.tvTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        cartManager = CartManager.getInstance();
        priceFormat = new DecimalFormat("#,##0 ₫");
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.cart);
        }
    }

    private void setupRecyclerView() {
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(this, cartManager.getCartItems());
        cartAdapter.setOnCartItemClickListener(this);
        rvCartItems.setAdapter(cartAdapter);
    }

    private void setupCheckoutLauncher() {
        checkoutLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        finish();
                    }
                    updateCartUI();
                });
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void handleInitialIntent() {
        btnCheckout.setOnClickListener(v -> {
            if (!cartManager.getCartItems().isEmpty()) {
                Intent intent = new Intent(this, CheckoutActivity.class);
                checkoutLauncher.launch(intent);
            }
        });

        boolean checkoutNow = getIntent().getBooleanExtra("checkout_now", false);
        if (checkoutNow && !cartManager.getCartItems().isEmpty()) {
            Intent intent = new Intent(this, CheckoutActivity.class);
            checkoutLauncher.launch(intent);
        }
    }

    private void updateCartUI() {
        List<CartItem> cartItems = cartManager.getCartItems();
        cartAdapter.updateCartItems(cartItems);

        if (cartItems.isEmpty()) {
            rvCartItems.setVisibility(View.GONE);
            tvEmptyCart.setVisibility(View.VISIBLE);
            findViewById(R.id.priceContainer).setVisibility(View.GONE);
            btnCheckout.setEnabled(false);
        } else {
            rvCartItems.setVisibility(View.VISIBLE);
            tvEmptyCart.setVisibility(View.GONE);
            findViewById(R.id.priceContainer).setVisibility(View.VISIBLE);
            btnCheckout.setEnabled(true);
            updatePriceSummary();
        }
    }

    private void updatePriceSummary() {
        double subtotal = cartManager.getTotalPrice();
        double tax = subtotal * 0.1; // 10% tax
        double total = subtotal + tax;

        tvSubtotal.setText(priceFormat.format(subtotal));
        tvTax.setText(priceFormat.format(tax));
        tvTotal.setText(priceFormat.format(total));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // CartAdapter.OnCartItemClickListener
    @Override
    public void onQuantityChanged(CartItem cartItem, int newQuantity) {
        cartManager.updateQuantity(cartItem.getProduct(), newQuantity);
        updateCartUI();
    }

    @Override
    public void onRemoveItem(CartItem cartItem) {
        cartManager.removeFromCart(cartItem.getProduct());
        updateCartUI();
    }
}
