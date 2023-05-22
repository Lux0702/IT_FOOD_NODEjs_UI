package com.example.it_food.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.it_food.R;

public class OrderCompletedActivity extends AppCompatActivity {

    AppCompatButton btnPlaceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_completed);

        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderCompletedActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}