package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test.model.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AdminHomeActivity extends AppCompatActivity {

    private TextView userEmail1, userEmail2, userEmail3, userEmail4, userEmail5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_actvity);

        // Initialize TextViews for emails
        userEmail1 = findViewById(R.id.username1);
        userEmail2 = findViewById(R.id.username2);
        userEmail3 = findViewById(R.id.username3);
        userEmail4 = findViewById(R.id.username4);
        userEmail5 = findViewById(R.id.username5);

        fetchUserEmails();
    }

    private void fetchUserEmails() {
        String result = "";
//        try {
//            URL url = new URL("http://10.0.2.2:3000/api/Auth/users");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9lbWFpbGFkZHJlc3MiOiJhZG1pbkBnbWFpbC5jb20iLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJBZG1pbiIsIlVzZXJJZCI6IjY3MGE2NjhlNGUwYmNiZjE5YjFhNTMzZCIsImV4cCI6MTcyODc0ODk1NywiaXNzIjoiRUFEV2ViQXBwbGljYXRpb24iLCJhdWQiOiJFQURXZWJBcHBsaWNhdGlvblVzZXJzIn0.DvTBdHVsvjqf_Jgi3gOeUD7Bv6GnQ6wmxd573JfkWiE");
//
//            Log.i("--------------------------------------------------------------------------------------", "fetchUserEmails: ");
//            conn.setDoOutput(true);
//
////            JSONObject loginData = new JSONObject();
////            loginData.put("email", email);
////            loginData.put("password", password);
//
//            OutputStream os = conn.getOutputStream();
////            os.write(loginData.toString().getBytes());
//            os.flush();
//            os.close();
//
//
//            int responseCode = conn.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                String inputLine;
//                StringBuilder response = new StringBuilder();
//
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//                Log.i("fetchUserEmails", "Response: ");
//
//                try {
//                    JSONObject jsonResponse = new JSONObject(response.toString());
////                    String token = jsonResponse.getString("token");
////                    String role = jsonResponse.getString("role");
//
//
//                    Log.i("jsonResponse", "Token: " + jsonResponse);
////                    Log.i("LoginResponse", "Role: " + role);
//
//
//                    result = jsonResponse.toString();
//                } catch (Exception jsonException) {
//
//                    Log.e("LoginResponse", "Failed to parse JSON: " + jsonException.getMessage());
//                    result = "Error: Failed to parse response JSON.";
//                }
//
//            } else {
//                BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//                StringBuilder errorResponse = new StringBuilder();
//                String errorLine;
//
//                while ((errorLine = errorReader.readLine()) != null) {
//                    errorResponse.append(errorLine);
//                }
//                errorReader.close();
//
//                result = "Error: " + responseCode + " - " + errorResponse.toString();
//                Log.e("LoginError", result);
//            }
//            Log.i("LoginResponse", "Result: " + result);
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//            result = "Error: " + e.getMessage();
//            Log.e("LoginException", result);
//        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:3000/api/Auth/users");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                    String savedToken = sharedPref.getString("token", null);

                    conn.setRequestProperty("Authorization", "Bearer " + savedToken);

                    // Get the response code
                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) { // Success
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        // Handle the response (Update the UI on the main thread)
                        String userEmails = response.toString();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Update UI elements such as a TextView with the user email list
                                Log.i("API_RESPONSE", userEmails);
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


