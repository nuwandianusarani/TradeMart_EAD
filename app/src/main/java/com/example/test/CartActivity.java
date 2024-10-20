package com.example.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.model.CartManager;
import com.example.test.model.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private ArrayList<Product> products = new ArrayList<>();
    private Button checkoutButton;


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

        checkoutButton = findViewById(R.id.checkoutButton);

        // Initialize RecyclerView adapter to display cart products
        CartActivityAdapter cartAdapter = new CartActivityAdapter(context, cartItems);
        recyclerView.setAdapter(cartAdapter);

        // Handle the Checkout Button Click
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceedToCheckout();

            }
        });
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView); // Ensure this ID matches your layout
        context = CartActivity.this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL); // Set the orientation to vertical
        recyclerView.setLayoutManager(layoutManager);
    }

    private void proceedToCheckout() {
        new CartActivity.CheckoutTask().execute();
        Toast.makeText(context, "Order Successful", Toast.LENGTH_SHORT).show();


    }

    private class CheckoutTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";

            try {
                // API URL for registration
//                URL url = new URL("http://10.0.2.2:3000/api/Order/create");
                URL url = new URL("http://192.168.138.71:3000/api/Order/create");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                String savedToken = sharedPref.getString("token", null);

                conn.setRequestProperty("Authorization", "Bearer " + savedToken);
                conn.setDoOutput(true);

                // Create JSON payload for registration
                JSONObject registerData = new JSONObject();
                registerData.put( "note", "Please deliver in the door step.");
                registerData.put("deliveryAddress", "123 Main St, Cityville");

                ArrayList<Product> cartItems = CartManager.getInstance().getCartItems();

                // Create JSONArray for order items
                JSONArray orderItems = new JSONArray();

                // Iterate over cart items and add to order items
                for (Product product : cartItems) {
                    JSONObject item = new JSONObject();
                    item.put("productId", "66fd830ce40ce14f30fe8dba"); // Assuming you have a productId field
                    item.put("productName", product.getProductName());
                    item.put("quantity", Integer.parseInt(product.getOrderQuantity())); // Convert quantity to int
                    item.put("unitPrice", Double.parseDouble(product.getPrice())); // Convert price to double
                    item.put("vendorId", "66f992a7f92d1a5ac2583834"); // Assuming you have a vendorId field
                    item.put("vendorEmail", "vendorc@example.com"); // Assuming you have a vendorEmail field

                    orderItems.put(item);
                }

                // Add order items to the order data
                registerData.put("orderItems", orderItems);

                // Write the JSON data to the output stream
                OutputStream os = conn.getOutputStream();
                os.write(registerData.toString().getBytes());
                os.flush();
                os.close();

                // Get response from the server
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // Success
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    result = response.toString();
                    // Parse the response

                } else {
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;

                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResponse.append(errorLine);
                    }
                    errorReader.close();

                    result = "Error: " + responseCode + " - " + errorResponse.toString();
                    String finalResult = errorResponse.toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(), finalResult, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                    Log.e("RegisterError", result);
                }

            } catch (Exception e) {
                e.printStackTrace();
                result = "Error: " + e.getMessage();
                Log.e("RegisterException", result);
            }

            return result;
        }
    }
}

