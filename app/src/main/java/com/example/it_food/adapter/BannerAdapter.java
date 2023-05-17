package com.example.it_food.Adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.it_food.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BannerAdapter extends PagerAdapter {
    private Context context;
    private List<Integer> listImg;
    private ViewPager viewPager;

    public BannerAdapter(Context context) {
        this.context = context;
        listImg = new ArrayList<>();
        listImg.add(R.drawable.img_food_1);
        listImg.add(R.drawable.img_food_2);
        listImg.add(R.drawable.img_food_3);
        listImg.add(R.drawable.img_food_4);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item, container, false);
        ImageView imageView = view.findViewById(R.id.imgBanner);
        imageView.setImageResource(listImg.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return listImg.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void startAutoScroll(final int interval) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                if (currentItem < getCount() - 1) {
                    viewPager.setCurrentItem(currentItem + 1);
                } else {
                    viewPager.setCurrentItem(0);
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, interval, interval);
    }
}