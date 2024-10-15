package com.example.test.model;

import java.io.Serializable;

public class Product implements Serializable {

    private String productName;
    private String price;
    private String availableQuantity;
    private String category;
    private String description;
    private String imageUrl;

    // No-argument constructor (for flexibility, especially with serialization/deserialization frameworks)
    public Product() {
    }

    // Full constructor with all fields
    public Product(String productName, String price, String availableQuantity, String category, String description, String imageUrl) {
        this.productName = productName;
        this.price = price;
        this.availableQuantity = availableQuantity;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;  // Updated to 'imageUrl'
    }

    // Getters
    public String getProductName() {
        return productName;
    }

    public String getPrice() {
        return price;
    }

    public String getAvailableQuantity() {
        return availableQuantity;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;  // Updated to 'imageUrl'
    }

    // Setters
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAvailableQuantity(String availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {  // Updated to 'imageUrl'
        this.imageUrl = imageUrl;
    }
}
