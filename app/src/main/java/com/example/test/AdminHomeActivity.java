package com.example.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

    public class AdminHomeActivity extends AppCompatActivity {

        private Button approveButtonUser1, rejectButtonUser1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.admin_home_actvity);

            // Initialize approve/reject buttons for User 1
            approveButtonUser1 = findViewById(R.id.approveButton1);
            rejectButtonUser1 = findViewById(R.id.rejectButton1);

            // Handle approve action for User 1
            approveButtonUser1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AdminHomeActivity.this, "User 1 Approved", Toast.LENGTH_SHORT).show();
                    // Add your approve logic here
                }
            });

            // Handle reject action for User 1
            rejectButtonUser1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(AdminHomeActivity.this, "User 1 Rejected", Toast.LENGTH_SHORT).show();
                    // Add your reject logic here
                }
            });

            // You can similarly add more buttons and handle other users as needed
        }
    }
