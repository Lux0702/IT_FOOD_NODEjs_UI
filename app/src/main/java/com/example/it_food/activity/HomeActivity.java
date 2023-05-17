package com.example.it_food.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;

import com.example.it_food.R;
import com.example.it_food.Adapter.BannerAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private BannerAdapter bannerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewPager = findViewById(R.id.viewPager);

        bannerAdapter = new BannerAdapter(this);
        viewPager.setAdapter(bannerAdapter);
        bannerAdapter.startAutoScroll(3000);
    }
}