package com.example.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test.model.UserOrder;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class UserOrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private ArrayList<UserOrder> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order); // Use your layout file
        init();
        requestJsonData();
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        context = UserOrderActivity.this;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void requestJsonData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    URL url = new URL("http://10.0.2.2:3000/api/Order/user-orders");
                    URL url = new URL("http://192.168.138.71:3000/api/Order/user-orders");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

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

                        String orderData = response.toString();
                        Log.i("OrderData====", orderData);

                        JSONArray jsonArray = new JSONArray(orderData);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject orderObject = jsonArray.getJSONObject(i);
                            String orderId = orderObject.getString("id");
//                            String deliveryAddress = orderObject.getString("deliveryAddress");
//                            String note = orderObject.getString("note");
                            int orderStatus = orderObject.getInt("orderStatus");
                            double orderTotal = orderObject.getDouble("orderTotal");
                            String orderDate = orderObject.getString("orderDate");
                            String orderTime = orderObject.getString("orderTime");
//                            int paymentMethod = orderObject.getInt("paymentMethod");

                            // Fetch order items array
                            JSONArray orderItemsArray = orderObject.getJSONArray("orderItems");

                            for (int j = 0; j < orderItemsArray.length(); j++) {
                                JSONObject orderItemObject = orderItemsArray.getJSONObject(j);
                                String productId = orderItemObject.getString("productId");
                                String productName = orderItemObject.getString("productName");
                                int quantity = orderItemObject.getInt("quantity");
                                double unitPrice = orderItemObject.getDouble("unitPrice");
                                double totalPrice = orderItemObject.getDouble("totalPrice");
//                                String vendorId = orderItemObject.getString("vendorId");
//                                String vendorEmail = orderItemObject.getString("vendorEmail");
                                int orderItemStatus = orderItemObject.getInt("orderItemStatus");

                                // Create UserOrder object for each order item
                                UserOrder order = new UserOrder(orderId, productName, quantity, unitPrice, totalPrice, orderItemStatus, orderStatus, orderTotal, orderDate, orderTime);
                                orderList.add(order);
                            }
                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UserOrderAdapter orderAdapter = new UserOrderAdapter(context, orderList);
                                recyclerView.setAdapter(orderAdapter);
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
}
