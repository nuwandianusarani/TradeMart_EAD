package com.example.test;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
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

public class UserProfileActivity extends AppCompatActivity {
    private TextView tvUsername;
    private EditText etOldPassword, etNewPassword;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        // Initialize views
        tvUsername = findViewById(R.id.tv_username);
        etOldPassword = findViewById(R.id.et_old_password);
        etNewPassword = findViewById(R.id.et_new_password);
        btnSave = findViewById(R.id.btn_save);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", null);
        // Setting a hardcoded username for demonstration purposes
        tvUsername.setText(email);

        // Handle Save button click
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
    }

    // Method to handle password change
    private void saveChanges() {
        String oldPassword = etOldPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();

        // Simple validation
        if (TextUtils.isEmpty(oldPassword)) {
            Toast.makeText(UserProfileActivity.this, "Please enter your old password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            Toast.makeText(UserProfileActivity.this, "Please enter a new password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if old and new passwords are different
        if (oldPassword.equals(newPassword)) {
            Toast.makeText(UserProfileActivity.this, "New password must be different from the old password", Toast.LENGTH_SHORT).show();
            return;
        }

        new UserProfileActivity.ProfileTask().execute(oldPassword, newPassword);

        // Simulate saving changes (this is where you'd typically send the data to your backend)
        Toast.makeText(UserProfileActivity.this, "Password changed successfully!", Toast.LENGTH_SHORT).show();

        // Clear the input fields
        etOldPassword.setText("");
        etNewPassword.setText("");
    }
    private class ProfileTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String oldPassword = params[0];
            String newPassword = params[1];
            String result = "";

            try {
                // API URL for registration
                URL url = new URL("http://10.0.2.2:3000/api/Auth/change-password");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                String savedToken = sharedPref.getString("token", null);

                conn.setRequestProperty("Authorization", "Bearer " + savedToken);
                conn.setDoOutput(true);

                // Create JSON payload for registration
                JSONObject registerData = new JSONObject();
                registerData.put("oldPassword", oldPassword);
                registerData.put("newPassword", newPassword);

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