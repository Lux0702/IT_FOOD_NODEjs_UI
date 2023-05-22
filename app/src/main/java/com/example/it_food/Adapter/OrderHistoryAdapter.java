package com.example.it_food.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it_food.R;
import com.example.it_food.activity.OrderHistoryActivity;
import com.example.it_food.model.OrderHistory;
import com.example.it_food.model.ProductItem;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>{

    private List<OrderHistory> mListOrder;
    private OnButtonClickListener btnButtonListener;

    public OrderHistoryAdapter(List<OrderHistory> mListOrder, Context mContext, OnButtonClickListener btnButtonListener) {
        this.mListOrder = mListOrder;
        this.mContext = mContext;
        this.btnButtonListener = btnButtonListener;
    }

    private Context mContext;

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        OrderHistory orderHistory = mListOrder.get(position);
        if (orderHistory == null) {
            return;
        }
        holder.txtIdOrder.setText(orderHistory.get_id());
        holder.txtAddressOrder.setText(orderHistory.getAddress());
        holder.txtPhoneOrder.setText(orderHistory.getPhoneNumber());
        holder.txtDeliveryOrder.setText(orderHistory.getDelivery());
        holder.txtStatusOrder.setText(orderHistory.getStatus());
        holder.txtTotalPriceOrder.setText(orderHistory.getTotalPrice());
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                btnButtonListener.onButtonClicked(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListOrder != null) {
            return mListOrder.size();
        }
        return 0;
    }

    public interface OnButtonClickListener {
        void onButtonClicked(int position);
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder{

        TextView txtIdOrder, txtAddressOrder, txtPhoneOrder, txtDeliveryOrder,  txtStatusOrder, txtTotalPriceOrder;
        AppCompatButton btnDetail;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIdOrder = itemView.findViewById(R.id.txtIdOrder);
            txtAddressOrder = itemView.findViewById(R.id.txtAddressOrder);
            txtPhoneOrder = itemView.findViewById(R.id.txtPhoneOrder);
            txtDeliveryOrder = itemView.findViewById(R.id.txtDeliveryOrder);
            txtStatusOrder = itemView.findViewById(R.id.txtStatusOrder);
            txtTotalPriceOrder = itemView.findViewById(R.id.txtTotalPriceOrder);
            btnDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
}
