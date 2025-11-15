package com.longg.gky.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.longg.gky.R;
import com.longg.gky.models.Product;
import com.longg.gky.utils.CartManager;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;
    private Context context;
    private OnProductClickListener listener;
    private int layoutResource;
    private DecimalFormat priceFormat;

    public interface OnProductClickListener {
        void onProductClick(Product product);
        void onFavoriteClick(Product product);
    }

    public ProductAdapter(Context context, List<Product> products, int layoutResource) {
        this.context = context;
        this.products = products;
        this.layoutResource = layoutResource;
        this.priceFormat = new DecimalFormat("#,##0 ₫");
    }

    public void setOnProductClickListener(OnProductClickListener listener) {
        this.listener = listener;
    }

    public void updateProducts(List<Product> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layoutResource, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvProductBrand;
        private TextView tvPrice;
        private TextView tvOriginalPrice;
        private TextView tvRating;
        private TextView tvReviewCount;
        private TextView tvDiscountBadge;
        private ImageView ivFavorite;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews();
            setClickListeners();
        }

        private void initViews() {
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductBrand = itemView.findViewById(R.id.tvProductBrand);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvReviewCount = itemView.findViewById(R.id.tvReviewCount);
            tvDiscountBadge = itemView.findViewById(R.id.tvDiscountBadge);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
        }

        private void setClickListeners() {
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onProductClick(products.get(getAdapterPosition()));
                }
            });

            if (ivFavorite != null) {
                ivFavorite.setOnClickListener(v -> {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        Product product = products.get(getAdapterPosition());
                        product.setFavorite(!product.isFavorite());
                        updateFavoriteIcon(product.isFavorite());
                        listener.onFavoriteClick(product);
                    }
                });
            }
        }

        public void bind(Product product) {
            if (tvProductName != null) {
                tvProductName.setText(product.getName());
            }

            if (tvProductBrand != null) {
                tvProductBrand.setText(product.getBrand());
            }

            if (tvPrice != null) {
                tvPrice.setText(priceFormat.format(product.getPrice()));
            }

            if (tvOriginalPrice != null) {
                if (product.hasDiscount()) {
                    tvOriginalPrice.setText(priceFormat.format(product.getOriginalPrice()));
                    tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tvOriginalPrice.setVisibility(View.VISIBLE);
                } else {
                    tvOriginalPrice.setVisibility(View.GONE);
                }
            }

            if (tvRating != null) {
                tvRating.setText(String.format(Locale.getDefault(), "%.1f", product.getRating()));
            }

            if (tvReviewCount != null) {
                String reviewText;
                if (product.getReviewCount() >= 1000) {
                    reviewText = String.format(Locale.getDefault(), "(%.1fk)", product.getReviewCount() / 1000.0);
                } else {
                    reviewText = String.format(Locale.getDefault(), "(%d)", product.getReviewCount());
                }
                tvReviewCount.setText(reviewText);
            }

            if (tvDiscountBadge != null) {
                if (product.hasDiscount()) {
                    tvDiscountBadge.setText(String.format(Locale.getDefault(), "-%.0f%%", product.getDiscountPercentage()));
                    tvDiscountBadge.setVisibility(View.VISIBLE);
                } else {
                    tvDiscountBadge.setVisibility(View.GONE);
                }
            }
              if (ivFavorite != null) {
                updateFavoriteIcon(product.isFavorite());
            }

            // Load product image with Glide (handle resource ids, http urls, and content/file URIs)
            if (ivProductImage != null) {
                String imageUrl = product.getImageUrl();
                if (imageUrl == null || imageUrl.isEmpty()) {
                    Glide.with(context).load(R.drawable.placeholder_product).into(ivProductImage);
                } else {
                    try {
                        // trim and strip quotes
                        imageUrl = imageUrl.trim();
                        if ((imageUrl.startsWith("\"") && imageUrl.endsWith("\"")) || (imageUrl.startsWith("'") && imageUrl.endsWith("'"))) {
                            imageUrl = imageUrl.substring(1, imageUrl.length() - 1).trim();
                        }
                        int resId = Integer.parseInt(imageUrl);
                        Glide.with(context)
                                .load(resId)
                                .placeholder(R.drawable.placeholder_product)
                                .error(R.drawable.placeholder_product)
                                .transform(new CenterCrop(), new RoundedCorners(24))
                                .into(ivProductImage);
                    } catch (NumberFormatException e) {
                        android.util.Log.d("ProductAdapter", "Loading image for product '" + product.getName() + "' -> '" + imageUrl + "'");
                        // If it's a content:// or file:// URI, parse to Uri; otherwise pass the string (http/https)
                        if (imageUrl.startsWith("content:") || imageUrl.startsWith("file:")) {
                            Glide.with(context)
                                    .load(android.net.Uri.parse(imageUrl))
                                    .placeholder(R.drawable.placeholder_product)
                                    .error(R.drawable.placeholder_product)
                                    .transform(new CenterCrop(), new RoundedCorners(24))
                                    .into(ivProductImage);
                        } else {
                            Glide.with(context)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.placeholder_product)
                                    .error(R.drawable.placeholder_product)
                                    .transform(new CenterCrop(), new RoundedCorners(24))
                                    .into(ivProductImage);
                        }
                    }
                }
            }
        }

        private void updateFavoriteIcon(boolean isFavorite) {
            if (ivFavorite != null) {
                ivFavorite.setImageResource(isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
            }
        }
    }
}
