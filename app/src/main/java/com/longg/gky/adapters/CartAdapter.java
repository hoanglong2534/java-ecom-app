package com.longg.gky.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.longg.gky.R;
import com.longg.gky.models.CartItem;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private Context context;
    private OnCartItemClickListener listener;
    private DecimalFormat priceFormat;

    public interface OnCartItemClickListener {
        void onQuantityChanged(CartItem cartItem, int newQuantity);
        void onRemoveItem(CartItem cartItem);
    }

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
        this.priceFormat = new DecimalFormat("$#,##0.00");
    }

    public void setOnCartItemClickListener(OnCartItemClickListener listener) {
        this.listener = listener;
    }

    public void updateCartItems(List<CartItem> newCartItems) {
        this.cartItems = newCartItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    class CartViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvProductBrand;
        private TextView tvProductPrice;
        private TextView tvQuantity;
        private ImageView ivDecrease;
        private ImageView ivIncrease;
        private ImageView ivRemove;
        private TextView tvTotalPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews();
        }

        private void initViews() {
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductBrand = itemView.findViewById(R.id.tvProductBrand);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ivDecrease = itemView.findViewById(R.id.ivDecrease);
            ivIncrease = itemView.findViewById(R.id.ivIncrease);
            ivRemove = itemView.findViewById(R.id.ivRemove);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
        }

        public void bind(CartItem cartItem) {
            tvProductName.setText(cartItem.getProduct().getName());
            tvProductBrand.setText(cartItem.getProduct().getBrand());
            tvProductPrice.setText(priceFormat.format(cartItem.getProduct().getPrice()));
            tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
            tvTotalPrice.setText(priceFormat.format(cartItem.getTotalPrice()));

            // Set placeholder image
            ivProductImage.setImageResource(R.drawable.placeholder_product);

            // Set click listeners
            ivIncrease.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onQuantityChanged(cartItem, cartItem.getQuantity() + 1);
                }
            });

            ivDecrease.setOnClickListener(v -> {
                if (listener != null && cartItem.getQuantity() > 1) {
                    listener.onQuantityChanged(cartItem, cartItem.getQuantity() - 1);
                }
            });

            ivRemove.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRemoveItem(cartItem);
                }
            });
        }
    }
}
