package com.example.it_food.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it_food.activity.OrderHistoryActivity;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>{

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {

        return 0;
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder{

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
