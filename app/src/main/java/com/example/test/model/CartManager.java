package com.example.test.model;

import java.util.ArrayList;

public class CartManager {

        private static CartManager instance;
        private ArrayList<Product> cartItems;

        private CartManager() {
            cartItems = new ArrayList<>();
        }

        public static synchronized CartManager getInstance() {
            if (instance == null) {
                instance = new CartManager();
            }
            return instance;
        }

        public void addProduct(Product product) {
            cartItems.add(product);
        }

        public ArrayList<Product> getCartItems() {
            return cartItems;
        }
    }
