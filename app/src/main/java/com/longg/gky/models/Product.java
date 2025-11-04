package com.longg.gky.models;

import java.util.List;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private double originalPrice;
    private String imageUrl;
    private List<String> imageUrls;
    private String category;
    private float rating;
    private int reviewCount;
    private boolean isFavorite;
    private int stock;
    private String brand;
    private List<String> colors;
    private List<String> sizes;

    public Product() {}

    public Product(int id, String name, String description, double price, 
                   double originalPrice, String imageUrl, String category, 
                   float rating, int reviewCount, int stock, String brand) {
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

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(double originalPrice) { this.originalPrice = originalPrice; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public List<String> getColors() { return colors; }
    public void setColors(List<String> colors) { this.colors = colors; }

    public List<String> getSizes() { return sizes; }
    public void setSizes(List<String> sizes) { this.sizes = sizes; }

    public double getDiscountPercentage() {
        if (originalPrice > 0 && price < originalPrice) {
            return ((originalPrice - price) / originalPrice) * 100;
        }
        return 0;
    }

    public boolean hasDiscount() {
        return originalPrice > 0 && price < originalPrice;
    }
}
