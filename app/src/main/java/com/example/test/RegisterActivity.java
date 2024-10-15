package com.example.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        // Initialize the email and password input fields and register button
        emailInput = findViewById(R.id.register_email_input);
        passwordInput = findViewById(R.id.register_password_input);
        registerButton = findViewById(R.id.register_button);

        // Set up the "Login" link to navigate back to LoginActivity
        TextView loginLink = findViewById(R.id.login_link);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the LoginActivity when "Login" is clicked
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Set up register button click listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                // Validate that email and password are not empty
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Call the register API in the background
                   new RegisterTask().execute(email, password);
                }
            }
        });
    }

    // AsyncTask to handle the API call for registration
    private class RegisterTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            String result = "";

            try {
                // API URL for registration
                URL url = new URL("http://10.0.2.2:3000/api/Auth/register");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Create JSON payload for registration
                JSONObject registerData = new JSONObject();
                registerData.put("email", email);
                registerData.put("password", password);
                Log.i("password=========", password);

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

                    // Parse the response
                    try {
                        result = response.toString();

                        // Navigate to the LoginActivity after successful registration
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } catch (Exception jsonException) {
                        Log.e("RegisterResponse", "Failed to parse JSON: " + jsonException.getMessage());
                        result = "Error: Failed to parse response JSON.";
                    }

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
