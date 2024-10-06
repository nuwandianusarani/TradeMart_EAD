package com.example.test;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Set up the Register link to navigate to RegisterActivity
        TextView registerLink = findViewById(R.id.register_link);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the RegisterActivity when "Register" is clicked
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // Initialize input fields and login button
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        // Set up login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                Log.i("message", "Login button clicked with email: " + email);
                Log.i("message", "Login button clicked with email: " + password);

                // Call login API
                new LoginTask().execute(email, password);
            }
        });
    }

    // AsyncTask to handle login API call in the background
    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            String result = "";

            Log.i("message", "----------------------------------------------------------------------------------------------------");

            try {
                URL url = new URL("http://10.0.2.2:3000/api/Auth/login"); // Replace with your API endpoint
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Create JSON object with login details
                JSONObject loginData = new JSONObject();
                loginData.put("email", email);
                loginData.put("password", password);

                // Send the request
                OutputStream os = conn.getOutputStream();
                os.write(loginData.toString().getBytes());
                os.flush();
                os.close();

                // Get the response
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    result = response.toString();
                } else {
                    result = "Error: " + responseCode;
                }
                Log.i("message", "result: " + result);

            } catch (Exception e) {
                e.printStackTrace();
                result = e.getMessage();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // Handle the API response here
            Toast.makeText(LoginActivity.this, "Response: " + result, Toast.LENGTH_LONG).show();

            // You can navigate to a different activity based on successful login if needed
            // For example:
            // if (result.contains("success")) {
            //     Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            //     startActivity(intent);
            // }
        }
    }
}
