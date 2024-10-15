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

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        TextView registerLink = findViewById(R.id.register_link);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                Log.i("message","Calling on click");

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Call login API in the background
                    new LoginTask().execute(email, password);
                }
            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            String result = "";

            try {
                URL url = new URL("http://10.0.2.2:3000/api/Auth/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject loginData = new JSONObject();
                loginData.put("email", email);
                loginData.put("password", password);

                OutputStream os = conn.getOutputStream();
                os.write(loginData.toString().getBytes());
                os.flush();
                os.close();


                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    try {
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        String token = jsonResponse.getString("token");
                        String role = jsonResponse.getString("role");


                        Log.i("LoginResponse", "Token: " + token);
                        Log.i("LoginResponse", "Role: " + role);

                        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();

                        // Save the token and role to SharedPreferences
                        editor.putString("token", token);
                        editor.putString("role", role);

                        // Commit the changes
                        editor.apply();  // Use apply() for async saving or commit() for synchronous saving

                        Log.i("SharedPreferences", "Token and Role saved successfully");

                        if ("User".equals(role)) {

                            Intent intent = new Intent(LoginActivity.this, CategoryActivity.class);
                            startActivity(intent);
                        } else if ("Customer Service Representative".equals(role)) {

                            Intent intent = new Intent(LoginActivity.this, TestActivity.class);
                            intent.putExtra("token", token);
                            startActivity(intent);
                        } else {

                            Toast.makeText(LoginActivity.this, "Unknown role: " + role, Toast.LENGTH_SHORT).show();
                        }

                        result = jsonResponse.toString();
                    } catch (Exception jsonException) {

                        Log.e("LoginResponse", "Failed to parse JSON: " + jsonException.getMessage());
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
                    Log.e("LoginError", result);
                }
                Log.i("LoginResponse", "Result: " + result);

            } catch (Exception e) {

                e.printStackTrace();
                result = "Error: " + e.getMessage();
                Log.e("LoginException", result);
            }
            return result;
        }

    }
}
