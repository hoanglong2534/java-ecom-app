package com.longg.gky.utils;

import com.longg.gky.models.Product;
import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    public static List<Product> getSampleProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "iPhone 15 Pro", "Màn hình Super Retina XDR 6.1 inch, chip A17 Pro, camera chính 48MP.", 28990000, 31990000, "https://cdn.hoanghamobile.com/i/productlist/ts/1715333555.webp", "Điện thoại", 0, 0, 100, "Apple"));
        products.add(new Product(2, "Galaxy S24 Ultra", "Màn hình Dynamic AMOLED 2X 6.8 inch, S Pen tích hợp, camera 200MP.", 33990000, 0, "https://cdn.hoanghamobile.com/i/productlist/ts/1705544719.webp", "Điện thoại", 0, 0, 100, "Samsung"));
        products.add(new Product(3, "MacBook Air M3", "Chip Apple M3, màn hình Liquid Retina 13.6 inch, thiết kế mỏng nhẹ.", 27990000, 29990000, "https://cdn.hoanghamobile.com/i/productlist/ts/1709608980.webp", "Laptop", 0, 0, 100, "Apple"));
        products.add(new Product(4, "Dell XPS 15", "Màn hình InfinityEdge 15.6 inch, Intel Core i9, card đồ họa NVIDIA GeForce RTX.", 45990000, 0, "https://cdn.hoanghamobile.com/i/productlist/ts/1687232238.webp", "Laptop", 0, 0, 100, "Dell"));
        products.add(new Product(5, "iPad Pro M4", "Chip Apple M4, màn hình Ultra Retina XDR, hỗ trợ Apple Pencil Pro.", 28990000, 0, "https://cdn.hoanghamobile.com/i/productlist/ts/1715072049.webp", "Tablet", 0, 0, 100, "Apple"));
        return products;
    }
}
