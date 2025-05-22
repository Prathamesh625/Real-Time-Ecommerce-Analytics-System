package org.example.factory;

import java.util.Random;
import java.util.List;
import java.util.Arrays;
import org.example.entity.Product;

public class ProductFactory {
    private static final List<String> CATEGORIES = Arrays.asList(
            "Electronics", "Clothing", "Books", "Home & Garden", "Sports", "Beauty", "Automotive", "Toys"
    );

    private static final String[] ELECTRONICS = {"Laptop", "Smartphone", "Tablet", "Headphones", "Camera"};
    private static final String[] CLOTHING = {"T-Shirt", "Jeans", "Jacket", "Sneakers", "Dress"};
    private static final String[] BOOKS = {"Fiction Novel", "Programming Guide", "Biography", "Cookbook", "Travel Guide"};
    private static final String[] HOME_GARDEN = {"Plant Pot", "Garden Tool", "Home Decor", "Kitchen Appliance", "Furniture"};

    private static final Random random = new Random();

    public static Product createRandomProduct() {
        String category = CATEGORIES.get(random.nextInt(CATEGORIES.size()));
        String[] productNames = getProductNamesByCategory(category);
        String name = productNames[random.nextInt(productNames.length)];

        String productId = "PROD" + System.currentTimeMillis() + random.nextInt(1000);
        double price = 10 + (random.nextDouble() * 990); // Price between $10-$1000
        int stock = random.nextInt(100) + 1; // Stock between 1-100

        return new Product(productId, name, category, Math.round(price * 100.0) / 100.0, stock);
    }

    private static String[] getProductNamesByCategory(String category) {
        switch (category) {
            case "Electronics": return ELECTRONICS;
            case "Clothing": return CLOTHING;
            case "Books": return BOOKS;
            case "Home & Garden": return HOME_GARDEN;
            default: return new String[]{"Generic Product"};
        }
    }
}
