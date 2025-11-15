package com.longg.gky.utils;

import android.content.Context;

import com.longg.gky.data.DBRepository;
import com.longg.gky.data.entities.CartItemEntity;
import com.longg.gky.models.CartItem;
import com.longg.gky.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<CartItem> cartItems = new ArrayList<>();
    private boolean loaded = false;

    private CartManager() {}

    public static CartManager getInstance() {
        if (instance == null) instance = new CartManager();
        return instance;
    }

    // Sửa lại logic init để tương thích với cấu trúc CSDL mới
    public void init(Context ctx) {
        String currentUserName = AuthManager.getUserName(ctx);
        // Nếu chưa đăng nhập, không cần tải giỏ hàng
        if (currentUserName == null || currentUserName.isEmpty()) {
            loaded = true;
            return;
        }
        // Nếu đã tải giỏ hàng của user này rồi, không cần tải lại
        if (loaded) return;

        try {
            DBRepository.init(ctx);
        } catch (Exception ignored) {}
        
        DBRepository.get().getAllCartItemsAsync(entities -> {
            cartItems.clear();
            if (entities != null) {
                for (CartItemEntity e : entities) {
                    Product p = new Product();
                    p.setId(e.productId);
                    p.setName(e.name);
                    p.setPrice(e.price);
                    p.setImageUrl(e.imageUrl);
                    // (Bạn có thể thêm các trường khác của Product nếu cần)

                    CartItem ci = new CartItem(p, e.quantity);
                    cartItems.add(ci);
                }
            }
            loaded = true;
        });
    }

    public void invalidate() {
        loaded = false;
        cartItems.clear();
    }

    public void addToCart(Product product) {
        addToCart(product, 1);
    }

    public void addToCart(Product product, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                persistCartItem(item);
                return;
            }
        }
        CartItem newItem = new CartItem(product, quantity);
        cartItems.add(newItem);
        persistCartItem(newItem);
    }

    public void removeFromCart(Product product) {
        cartItems.removeIf(item -> item.getProduct().getId() == product.getId());
        DBRepository.get().removeCartItemByProductIdAsync(product.getId());
    }

    public void updateQuantity(Product product, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == product.getId()) {
                if (quantity <= 0) {
                    removeFromCart(product);
                } else {
                    item.setQuantity(quantity);
                    persistCartItem(item);
                }
                return;
            }
        }
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public void getCartCountAsync(DBRepository.ValueCallback<Integer> callback) {
        DBRepository.get().getCartCountAsync(callback);
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) total += item.getTotalPrice();
        return total;
    }

    public void clearCart() {
        cartItems.clear();
        DBRepository.get().clearCartAsync();
    }

    // Sửa lại logic persistCartItem để tương thích với CSDL mới
    private void persistCartItem(CartItem item) {
        if (item == null || item.getProduct() == null) return;
        CartItemEntity e = new CartItemEntity();
        e.productId = item.getProduct().getId();
        e.name = item.getProduct().getName();
        e.price = item.getProduct().getPrice();
        e.imageUrl = item.getProduct().getImageUrl();
        e.quantity = item.getQuantity();
        // userName sẽ được gán tự động trong DBRepository
        DBRepository.get().addOrUpdateCartItemAsync(e);
    }
}
