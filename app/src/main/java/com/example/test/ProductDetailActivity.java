package com.example.test;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test.model.CartManager;
import com.example.test.model.Product;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImageView, plusImageView;
    private TextView brandTextView, nameTextView, priceTextView, oldPriceTextView;
    private EditText quantityEditText;
    private Button addToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);  // Make sure your layout is named activity_product_detail.xml

        // Initialize UI components
        productImageView = findViewById(R.id.pImg);
        plusImageView = findViewById(R.id.plusImg);
        brandTextView = findViewById(R.id.pBrand);
        nameTextView = findViewById(R.id.pName);
        priceTextView = findViewById(R.id.pPrice);
        oldPriceTextView = findViewById(R.id.prodOldPrice);
        quantityEditText = findViewById(R.id.qty);
        addToCartButton = findViewById(R.id.addCartBtn);

        String productName = getIntent().getStringExtra("productName");
        String categoryName = getIntent().getStringExtra("categoryName");
        String price = getIntent().getStringExtra("price");
        String availableQuantity = getIntent().getStringExtra("availableQuantity");
        String description = getIntent().getStringExtra("description");
        String image = getIntent().getStringExtra("image");

        // Set product details (example data, these can be fetched from an API or passed via intent)
        brandTextView.setText(categoryName);
        nameTextView.setText(productName);
        priceTextView.setText(price);// Example old price
        quantityEditText.setText("1");
        Picasso.get()
                .load(image) // Load product image URL dynamically
//                .placeholder(R.drawable.placeholder_image) // Placeholder image
//                .error(R.drawable.error_image) // Error image if URL fails
                .into(productImageView);


        // Handling click on the plus image to increase quantity
        plusImageView.setOnClickListener(v -> {
            int currentQty = Integer.parseInt(quantityEditText.getText().toString());
            currentQty++;
            quantityEditText.setText(String.valueOf(currentQty));
        });

        // Handling click on the Add to Cart button
        addToCartButton.setOnClickListener(v -> {

            // Create a product object (assuming you have a Product model)
             Product product = new Product(productName, price, availableQuantity, categoryName,description,image);

            // Add the product to the singleton CartManager
            CartManager.getInstance().addProduct(product);

            // Send the product to the CartActivity via Intent
            Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
//            intent.putExtra("product", product);  // Pass the product object via intent
            startActivity(intent);
        });
    }
}