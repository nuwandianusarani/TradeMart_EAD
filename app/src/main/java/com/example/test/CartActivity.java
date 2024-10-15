package com.example.test;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.model.CartManager;
import com.example.test.model.Product;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private ArrayList<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_list_activity);  // Ensure the layout file name matches
        init();

        ArrayList<Product> cartItems = CartManager.getInstance().getCartItems();

//        if (getIntent().hasExtra("product")) {
//            Product product = (Product) getIntent().getSerializableExtra("product");
//            products.add(product);  // Add the received product to the cart list
//        }

        // Initialize RecyclerView adapter to display cart products
        CartActivityAdapter cartAdapter = new CartActivityAdapter(context, cartItems);
        recyclerView.setAdapter(cartAdapter);
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView); // Ensure this ID matches your layout
        context = CartActivity.this;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Adjust to your layout preference
    }
}
