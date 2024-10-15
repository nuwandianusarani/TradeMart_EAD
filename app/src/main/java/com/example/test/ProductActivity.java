package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test.model.Category;
import com.example.test.model.Product;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity implements ProductAdapter.onProductClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private Context context;
    private ArrayList<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category); // Change to your layout file
        init();

        // Get the filtered products string from the Intent
        String filteredProductsString = getIntent().getStringExtra("filteredProducts");

        // Convert the string back to a JSONArray
        try {
            JSONArray filteredProductsJsonArray = new JSONArray(filteredProductsString);
            processProductData(filteredProductsJsonArray);
        } catch (Exception e) {
            Log.e("Error", "Error parsing product data", e);
        }
    }


    private void init() {
        recyclerView = findViewById(R.id.recyclerView); // Ensure this ID matches your layout
        context = ProductActivity.this;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Adjust to your layout preference
    }

    @Override
    public void onProductClick(int position) {
        String productName = products.get(position).getProductName();
        String categoryName = products.get(position).getCategory();
        String price = products.get(position).getPrice();
        String availableQuantity = products.get(position).getAvailableQuantity();
        String description = products.get(position).getDescription();
        String image = products.get(position).getImageUrl();
//        Intent intent = new Intent(this, ProductActivity.class);
        Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
        // Pass data using putExtra
        intent.putExtra("productName", productName);
        intent.putExtra("categoryName", categoryName);
        intent.putExtra("price", price);
        intent.putExtra("availableQuantity", availableQuantity);
        intent.putExtra("description", description);
        intent.putExtra("image", image);
        startActivity(intent);
    }

    public void processProductData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject productObject = jsonArray.getJSONObject(i);
                String productName = productObject.getString("productName");
                String price = productObject.getString("price");
                String availableQuantity = productObject.getString("availableQuantity");
                String category = productObject.getString("category");
                String description = productObject.getString("description");
                String image = "http://10.0.2.2:3000/images/" + productObject.getString("image");

                // Create a Category object and add it to the ArrayList
                Product product = new Product(productName, price, availableQuantity, category, description, image);
                products.add(product);
            }

            ProductAdapter productAdapter = new ProductAdapter (products,context,ProductActivity.this);
            recyclerView.setAdapter(productAdapter);

        } catch (Exception e) {
            Log.e("Error", "Error in API request", e);
        }

    }
}









