package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserHomeActivity extends AppCompatActivity {

    private Button btnUserProfile;
    private Button btnCategory;
    private Button btnCart;
    private Button btnOrderHistory;
    private Button btnVendors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home_activity);  // Ensure this layout name is correct.

        // Find views by ID
        btnUserProfile = findViewById(R.id.btn_user_profile);
        btnCategory = findViewById(R.id.btn_category);
        btnOrderHistory = findViewById(R.id.btn_orders);
        btnVendors = findViewById(R.id.btn_vendors);

//        btnOrderHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UserHomeActivity.this, OrderActivity.class);
//                startActivity(intent);
//            }
//
//        });
        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomeActivity.this, CategoryActivity.class);
                startActivity(intent);
            }

        });
        btnUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CartActivity
                Intent intent = new Intent(UserHomeActivity.this,UserProfileActivity.class);
                startActivity(intent);
            }

        });
        btnVendors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CartActivity
                Intent intent = new Intent(UserHomeActivity.this,VendorActivity.class);
                startActivity(intent);
            }

        });
        btnOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to CartActivity
                Intent intent = new Intent(UserHomeActivity.this,UserOrderActivity.class);
                startActivity(intent);
            }

        });
    }
}