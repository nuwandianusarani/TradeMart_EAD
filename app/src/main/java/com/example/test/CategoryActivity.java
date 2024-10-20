package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test.model.Category;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity implements CategoryAdapter.onCategoryListener {

    private RecyclerView recyclerView;
    private Context context;
    private ArrayList<Category> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category); // Change to your layout file
        init();
        requestJsonData();
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView); // Ensure this ID matches your layout
        context = CategoryActivity.this;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Adjust to your layout preference
    }

    public void requestJsonData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    URL url = new URL("http://10.0.2.2:3000/api/Category/list");
                    URL url = new URL("http://192.168.138.71:3000/api/Category/list");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    // Get the response code
                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Handle the response
                        String categoryData = response.toString();
                        Log.i("CategoryData====", categoryData);

                        JSONArray jsonArray = new JSONArray(categoryData);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject categoryObject = jsonArray.getJSONObject(i);
                            String categoryName = categoryObject.getString("categoryName");

                            // Create a Category object and add it to the ArrayList
                            Category category = new Category();
                            category.setCategoryName(categoryName);
                            categories.add(category);
                        }

                        // Update the RecyclerView on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CategoryAdapter categoryAdapter = new CategoryAdapter(context, categories, CategoryActivity.this);
                                recyclerView.setAdapter(categoryAdapter);
                            }
                        });

                    } else {
                        Log.e("API_ERROR", "GET request failed with response code: " + responseCode);
                    }

                    conn.disconnect();

                } catch (Exception e) {
                    Log.e("API_EXCEPTION", "Error in API request", e);
                }
            }
        }).start();
    }
    // Implement category click listener to fetch products
    @Override
    public void onCategoryClick(int position) {
        String categoryName = categories.get(position).getCategoryName();
        fetchProductsByCategory(categoryName);
    }

    // Fetch products by category
    private void fetchProductsByCategory(String category) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    URL url = new URL("http://10.0.2.2:3000/api/Product/listwithEmail?category=" + category);
                    URL url = new URL("http://192.168.138.71:3000/api/Product/listwithEmail?category=" + category);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    Log.i("CategoryName====", category);

                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                    String savedToken = sharedPref.getString("token", null);

                    conn.setRequestProperty("Authorization", "Bearer " + savedToken);

                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String inputLine;

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        String productData = response.toString();


                        // Convert JSON response to a JSONArray
                        JSONArray jsonArray = new JSONArray(productData);


                        ArrayList<JSONObject> filteredProducts = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject product = jsonArray.getJSONObject(i);
                            if (product.getString("category").equals(category)) {
                                filteredProducts.add(product);
                            }
                        }
                        // Convert the ArrayList<JSONObject> to a JSONArray
                        JSONArray filteredProductsJsonArray = new JSONArray(filteredProducts);


                        String filteredProductsString = filteredProductsJsonArray.toString();


                        Intent intent = new Intent(CategoryActivity.this, ProductActivity.class);
                        intent.putExtra("filteredProducts", filteredProductsString);
                        startActivity(intent);
                        Log.i("-------------------filteredProducts-----------------", String.valueOf(filteredProducts));

                        // Process product data here and update the UI accordingly
                        // For example, you can parse the product data and display it in another RecyclerView or a new activity

                    } else {
                        Log.e("API_ERROR", "GET request failed with response code: " + responseCode);
                    }
                    conn.disconnect();

                } catch (Exception e) {
                    Log.e("API_EXCEPTION", "Error in API request", e);
                }
            }
        }).start();
    }
}
