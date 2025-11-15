package com.longg.gky.data.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class ProductEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String description;
    public double price;
    public double originalPrice;
    public String imageUrl;
    public String category;
    public float rating;
    public int reviewCount;
    public boolean isFavorite;
    public int stock;
    public String brand;

    @Ignore
    public ProductEntity() {}

    public ProductEntity(int id, String name, String description, double price, double originalPrice, String imageUrl, String category, float rating, int reviewCount, int stock, String brand) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.originalPrice = originalPrice;
        this.imageUrl = imageUrl;
        this.category = category;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.stock = stock;
        this.brand = brand;
        this.isFavorite = false;
    }
}
