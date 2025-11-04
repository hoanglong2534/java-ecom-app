package com.longg.gky;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.longg.gky.adapters.CartAdapter;
import com.longg.gky.models.CartItem;
import com.longg.gky.utils.CartManager;

import java.text.DecimalFormat;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemClickListener {

    private Toolbar toolbar;
    private RecyclerView rvCartItems;
    private TextView tvEmptyCart;
    private TextView tvSubtotal;
    private TextView tvTax;
    private TextView tvTotal;
    private Button btnCheckout;

    private CartAdapter cartAdapter;
    private CartManager cartManager;
    private DecimalFormat priceFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        setupToolbar();
        setupRecyclerView();
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

        cartManager = CartManager.getInstance();
        priceFormat = new DecimalFormat("$#,##0.00");
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Shopping Cart");
        }
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvCartItems.setLayoutManager(layoutManager);

        List<CartItem> cartItems = cartManager.getCartItems();
        cartAdapter = new CartAdapter(this, cartItems);
        cartAdapter.setOnCartItemClickListener(this);
        rvCartItems.setAdapter(cartAdapter);
    }

    private void updateCartUI() {
        List<CartItem> cartItems = cartManager.getCartItems();
        
        if (cartItems.isEmpty()) {
            rvCartItems.setVisibility(View.GONE);
            tvEmptyCart.setVisibility(View.VISIBLE);
            btnCheckout.setEnabled(false);
        } else {
            rvCartItems.setVisibility(View.VISIBLE);
            tvEmptyCart.setVisibility(View.GONE);
            btnCheckout.setEnabled(true);
            
            cartAdapter.updateCartItems(cartItems);
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
