package com.example.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class VendorCommentActivity extends AppCompatActivity {

    private EditText commentEditText;
    private RatingBar ratingBar;
    private Button submitButton;
    private ProgressBar progressBar; // Added ProgressBar for loading indication
    private String vendorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_comment);

        commentEditText = findViewById(R.id.commentEditText);
        ratingBar = findViewById(R.id.ratingBar);
        submitButton = findViewById(R.id.submitButton);

        // Get vendorId from the intent
        vendorId = getIntent().getStringExtra("vendorId");
        Log.i("--------------------------------------------------------------------------", "Vendor ID: " + vendorId);
        Log.i("VENDOR_ID", "Vendor ID: " + vendorId);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = commentEditText.getText().toString();
                int rank = (int) ratingBar.getRating(); // Get rank from RatingBar
                Log.i("--------------------------------------------------------------------------", "Vendor ID: " + vendorId);
                if (comment.isEmpty()) {
                    Toast.makeText(VendorCommentActivity.this, "Please enter a comment", Toast.LENGTH_SHORT).show();
                    return;
                }

                submitComment(vendorId, comment, rank);
            }
        });
    }

    private void submitComment(String vendorId, String comment, int rank) {
        // Show loading indicator and disable button
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                submitButton.setEnabled(false);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:3000/api/Vendor/comment/" + vendorId);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");

                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                    String savedToken = sharedPref.getString("token", null);

                    conn.setRequestProperty("Authorization", "Bearer " + savedToken);


                    Log.i("VENDOR_ID", "Vendor ID: " + vendorId);
                    Log.i("COMMENT", "Comment: " + comment);
                    Log.i("RANK", "Rank: " + rank);
                    // Prepare the JSON body
                    JSONObject requestBody = new JSONObject();
                    requestBody.put("comment", comment);
                    requestBody.put("rank", rank);

                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    os.write(requestBody.toString().getBytes());
                    os.flush();
                    os.close();

                    // Get the response code
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(VendorCommentActivity.this, "Comment submitted successfully", Toast.LENGTH_SHORT).show();
                                resetForm(); // Clear form after successful submission
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(VendorCommentActivity.this, "Failed to submit comment. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.e("API_ERROR", "Failed to submit comment. Response code: " + responseCode);
                    }

                    conn.disconnect();

                } catch (Exception e) {
                    Log.e("API_EXCEPTION", "Error in submitting comment", e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(VendorCommentActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } finally {
                    // Hide loading indicator and re-enable button
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            submitButton.setEnabled(true);
                        }
                    });
                }
            }
        }).start();
    }

    // Reset form fields after successful submission
    private void resetForm() {
        commentEditText.setText("");
        ratingBar.setRating(0);
    }
}
