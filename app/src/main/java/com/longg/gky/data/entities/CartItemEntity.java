package com.longg.gky.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

// Sử dụng PrimaryKey kép để đảm bảo mỗi sản phẩm là duy nhất cho mỗi user
@Entity(tableName = "cart_items", primaryKeys = {"productId", "userName"})
public class CartItemEntity {

    public int productId;

    @NonNull
    public String userName;

    public String name;
    public double price;
    public String imageUrl;
    public int quantity;
    public String selectedColor;
    public String selectedSize;

    public CartItemEntity() {}

}
