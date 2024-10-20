package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test.model.Vendor;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class VendorActivity extends AppCompatActivity implements VendorAdapter.onVendorListener {

    private RecyclerView recyclerView;
    private Context context;
    private ArrayList<Vendor> vendors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor); // Change to your layout file
        init();
        requestJsonData();
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView); // Ensure this ID matches your layout
        context = VendorActivity.this;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Adjust columns as needed
    }

    public void requestJsonData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    URL url = new URL("http://10.0.2.2:3000/api/Vendor/list");
                    URL url = new URL("http://192.168.138.71:3000/api/Vendor/list");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String inputLine;

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        String vendorData = response.toString();
                        Log.i("VendorData====", vendorData);

                        JSONArray jsonArray = new JSONArray(vendorData);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject vendorObject = jsonArray.getJSONObject(i);
                            String vendorName = vendorObject.getString("vendorName");

                            Vendor vendor = new Vendor();
                            vendor.setVendorName(vendorName);
                            vendor.setVendorId(vendorObject.getString("id"));
                            vendors.add(vendor);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                VendorAdapter vendorAdapter = new VendorAdapter(context, vendors, VendorActivity.this);
                                recyclerView.setAdapter(vendorAdapter);
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

    @Override
    public void onVendorClick(int position) {
        String vendorId = vendors.get(position).getVendorId();
        String vendorName = vendors.get(position).getVendorName();

        Log.i("VendorIdset", "Vendor IDSET: " + vendorId);
        Intent intent = new Intent(VendorActivity.this, VendorCommentActivity.class);
        intent.putExtra("vendorId", vendorId);   // Pass vendorId for comment functionality
        intent.putExtra("vendorName", vendorName); // Pass vendorName for display purposes
        startActivity(intent);
    }

}
