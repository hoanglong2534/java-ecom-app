package com.longg.gky.utils;

import com.longg.gky.models.Product;
import com.longg.gky.models.Category;
import com.longg.gky.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataUtils {
    
    public static List<Product> getSampleProducts() {
        List<Product> products = new ArrayList<>();
          // Sample smartphones
        Product phone1 = new Product(1, "iPhone 15 Pro", 
            "Latest iPhone with A17 Pro chip, titanium design, and advanced camera system", 
            999.0, 1199.0, "https://images.unsplash.com/photo-1678652197831-2d180705cd2c?w=400&h=400&fit=crop&crop=center", 
            "Smartphones", 4.8f, 2547, 50, "Apple");
        phone1.setColors(Arrays.asList("#000000", "#8B7355", "#F5F5DC", "#1E3A8A"));
        phone1.setSizes(Arrays.asList("128GB", "256GB", "512GB", "1TB"));
        products.add(phone1);
        
        Product phone2 = new Product(2, "Samsung Galaxy S24", 
            "Premium Android phone with AI features, excellent camera, and long battery life", 
            849.0, 999.0, "https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=400&h=400&fit=crop&crop=center", 
            "Smartphones", 4.7f, 1834, 75, "Samsung");
        phone2.setColors(Arrays.asList("#000000", "#8E24AA", "#FFD700", "#B0BEC5"));
        phone2.setSizes(Arrays.asList("128GB", "256GB", "512GB"));
        products.add(phone2);
          // Sample laptops
        Product laptop1 = new Product(3, "MacBook Pro 16\"", 
            "Powerful laptop with M3 Pro chip, stunning Liquid Retina XDR display", 
            2499.0, 2899.0, "https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=400&h=400&fit=crop&crop=center", 
            "Laptops", 4.9f, 892, 25, "Apple");
        laptop1.setColors(Arrays.asList("#C0C0C0", "#2C2C2C"));
        laptop1.setSizes(Arrays.asList("512GB", "1TB", "2TB"));
        products.add(laptop1);
        
        Product laptop2 = new Product(4, "Dell XPS 13", 
            "Ultra-portable laptop with Intel Core i7, beautiful InfinityEdge display", 
            1299.0, 1499.0, "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400&h=400&fit=crop&crop=center", 
            "Laptops", 4.6f, 1256, 40, "Dell");
        laptop2.setColors(Arrays.asList("#C0C0C0", "#1C1C1C"));
        laptop2.setSizes(Arrays.asList("256GB", "512GB", "1TB"));
        products.add(laptop2);
          // Sample headphones
        Product headphones1 = new Product(5, "AirPods Pro 2", 
            "Active Noise Cancellation, Spatial Audio, and up to 30 hours of listening time", 
            249.0, 299.0, "https://images.unsplash.com/photo-1606220945770-b5b6c2c55bf1?w=400&h=400&fit=crop&crop=center", 
            "Audio", 4.8f, 3421, 100, "Apple");
        headphones1.setColors(Arrays.asList("#FFFFFF"));
        products.add(headphones1);
        
        Product headphones2 = new Product(6, "Sony WH-1000XM5", 
            "Industry-leading noise cancellation with crystal clear hands-free calling", 
            349.0, 399.0, "https://images.unsplash.com/photo-1583394838336-acd977736f90?w=400&h=400&fit=crop&crop=center", 
            "Audio", 4.7f, 2187, 60, "Sony");
        headphones2.setColors(Arrays.asList("#000000", "#C0C0C0"));
        products.add(headphones2);
          // Sample accessories
        Product watch1 = new Product(7, "Apple Watch Series 9", 
            "Advanced health monitoring, ECG app, and cellular connectivity", 
            429.0, 499.0, "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=400&h=400&fit=crop&crop=center", 
            "Accessories", 4.8f, 1876, 80, "Apple");
        watch1.setColors(Arrays.asList("#000000", "#C0C0C0", "#FFD700", "#FF6B6B"));
        watch1.setSizes(Arrays.asList("41mm", "45mm"));
        products.add(watch1);
        
        Product tablet1 = new Product(8, "iPad Air", 
            "Powerful, colorful, and ultra-portable iPad with M1 chip", 
            599.0, 699.0, "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400&h=400&fit=crop&crop=center", 
            "Tablets", 4.7f, 1432, 55, "Apple");
        tablet1.setColors(Arrays.asList("#C0C0C0", "#8E24AA", "#FF6B6B", "#4FC3F7", "#81C784"));
        tablet1.setSizes(Arrays.asList("64GB", "256GB"));
        products.add(tablet1);
        
        // Add more trendy products
        Product shoes1 = new Product(9, "Nike Air Max 270", 
            "Revolutionary cushioning with large Air unit for maximum comfort", 
            129.0, 159.0, "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400&h=400&fit=crop&crop=center", 
            "Fashion", 4.6f, 890, 35, "Nike");
        shoes1.setColors(Arrays.asList("#000000", "#FFFFFF", "#FF0000", "#0070F3"));
        shoes1.setSizes(Arrays.asList("7", "8", "9", "10", "11", "12"));
        products.add(shoes1);
        
        Product camera1 = new Product(10, "Canon EOS R5", 
            "Professional mirrorless camera with 45MP sensor and 8K video recording", 
            3899.0, 4299.0, "https://images.unsplash.com/photo-1502920917128-1aa500764cbd?w=400&h=400&fit=crop&crop=center", 
            "Photography", 4.9f, 567, 15, "Canon");
        camera1.setColors(Arrays.asList("#000000"));
        products.add(camera1);
        
        Product gaming1 = new Product(11, "PlayStation 5", 
            "Next-gen gaming console with ultra-fast SSD and ray tracing", 
            499.0, 599.0, "https://images.unsplash.com/photo-1606813907291-d86efa9b94db?w=400&h=400&fit=crop&crop=center", 
            "Gaming", 4.8f, 1234, 20, "Sony");
        gaming1.setColors(Arrays.asList("#FFFFFF", "#000000"));
        products.add(gaming1);
        
        Product backpack1 = new Product(12, "Peak Design Everyday", 
            "Premium camera backpack with weatherproof design", 
            279.0, 329.0, "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400&h=400&fit=crop&crop=center", 
            "Accessories", 4.7f, 445, 25, "Peak Design");
        backpack1.setColors(Arrays.asList("#000000", "#8B4513", "#2F4F4F"));
        products.add(backpack1);
        
        return products;
    }
      public static List<Category> getSampleCategories() {
        List<Category> categories = new ArrayList<>();
        
        categories.add(new Category(1, "Smartphones", R.drawable.ic_smartphone));
        categories.add(new Category(2, "Laptops", R.drawable.ic_laptop));
        categories.add(new Category(3, "Audio", R.drawable.ic_headphones));
        categories.add(new Category(4, "Accessories", R.drawable.ic_watch));
        categories.add(new Category(5, "Tablets", R.drawable.ic_tablet));
        categories.add(new Category(6, "Gaming", R.drawable.ic_gamepad));
        categories.add(new Category(7, "Fashion", R.drawable.ic_fashion));
        categories.add(new Category(8, "Photography", R.drawable.ic_camera));
        
        return categories;
    }
    
    public static List<Product> getProductsByCategory(String category) {
        List<Product> allProducts = getSampleProducts();
        List<Product> filteredProducts = new ArrayList<>();
        
        for (Product product : allProducts) {
            if (product.getCategory().equals(category)) {
                filteredProducts.add(product);
            }
        }
        
        return filteredProducts;
    }
    
    public static List<Product> getFeaturedProducts() {
        List<Product> allProducts = getSampleProducts();
        List<Product> featured = new ArrayList<>();
        
        // Get first 4 products as featured
        for (int i = 0; i < Math.min(4, allProducts.size()); i++) {
            featured.add(allProducts.get(i));
        }
        
        return featured;
    }
    
    public static List<Product> getDiscountedProducts() {
        List<Product> allProducts = getSampleProducts();
        List<Product> discounted = new ArrayList<>();
        
        for (Product product : allProducts) {
            if (product.hasDiscount()) {
                discounted.add(product);
            }
        }
        
        return discounted;
    }
}
