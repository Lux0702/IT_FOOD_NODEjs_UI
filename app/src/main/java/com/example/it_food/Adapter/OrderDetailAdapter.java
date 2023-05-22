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
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.ProductItem;
import com.example.it_food.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder>{

    private OnButtonClickListener btnButtonListener;
    private List<ProductItem> mListProdduct ;
    private Context mContext;

    public OrderDetailAdapter(List<ProductItem> mListProduct, Context mContext, OnButtonClickListener btnButtonListener) {
        this.mListProdduct = mListProduct;
        this.mContext = mContext;
        this.btnButtonListener = btnButtonListener;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_detail_item, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        ProductItem productItem = mListProdduct.get(position);
        if(productItem == null){
            return;
        }
        holder.bindData(productItem.getImage());
        holder.txtNameProduct.setText(productItem.getName());
        holder.txtDescriptionProduct.setText(productItem.getDescription());
        holder.txtQuantity.setText(String.valueOf("x"+productItem.getQuantity()));
        holder.txtPrice.setText(String.valueOf(productItem.getPrice()));
        holder.btnFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                btnButtonListener.onButtonClicked(pos);
            }
        });
    }

    public interface OnButtonClickListener {
        void onButtonClicked(int position);
    }

    @Override
    public int getItemCount() {
        if (mListProdduct != null) {
            return mListProdduct.size();
        }
        return 0;
    }

    public class OrderDetailViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imageProduct;
        TextView txtNameProduct, txtDescriptionProduct, txtQuantity, txtPrice;
        AppCompatButton btnFeedBack;
        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProduct = itemView.findViewById(R.id.imageProduct);
            txtNameProduct = itemView.findViewById(R.id.txtNameProduct);
            txtDescriptionProduct = itemView.findViewById(R.id.txtDescriptionProduct);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            btnFeedBack = itemView.findViewById(R.id.btnFeedBack);
        }

        public void bindData(String imageUrl) {
            Picasso.get().load(imageUrl).into(imageProduct);
        }
    }

}
