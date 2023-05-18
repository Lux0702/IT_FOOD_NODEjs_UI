package com.example.it_food.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.activity.CartActivity;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.ProductItem;
import com.example.it_food.model.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.ProductItemViewHolder>{
    private final List<ProductItem> mListProduct;

    private OnCartItemRemovedListener listener;
    private Context mContext;

    public ProductItemAdapter(List<ProductItem> mListProduct, Context mContext, OnCartItemRemovedListener listener) {
        this.mListProduct = mListProduct;
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_listellipsetwenty, parent, false);
        return new ProductItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductItemViewHolder holder, final int position) {
        final ProductItem productItem = mListProduct.get(position);
        if (productItem == null){
            return;
        }
        User user = SharedPreferences.getInstance(mContext).getUser();
        holder.bindData(productItem.getImage());
        holder.name.setText(productItem.getName());
        holder.price.setText(String.valueOf(productItem.getPrice()*productItem.getQuantity()));
        holder.quantity.setText(String.valueOf(productItem.getQuantity()));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> body = new HashMap<>();
                body.put("userId", user.getId());
                body.put("productId", productItem.get_id());
                APIService.apiService.deleteCart(body).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.d("", "success");
                            int pos = holder.getAdapterPosition();
                            listener.onCartItemRemoved(pos);
                        }else {
                            Log.d("", "response error");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("", "error");
                    }
                });
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> body = new HashMap<>();
                body.put("userId", user.getId());
                body.put("productId", productItem.get_id());
                body.put("quantity",productItem.getQuantity()+1);
                APIService.apiService.updateProductInCart(body).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.d("", "success");
                            productItem.setQuantity(productItem.getQuantity()+1);
                            holder.quantity.setText(String.valueOf(productItem.getQuantity()));
                            holder.price.setText(String.valueOf(productItem.getPrice()*productItem.getQuantity()));
                        }else {
                            Log.d("", "response error");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("", "error");
                    }
                });
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> body = new HashMap<>();
                body.put("userId", user.getId());
                body.put("productId", productItem.get_id());
                body.put("quantity",productItem.getQuantity()-1);
                APIService.apiService.updateProductInCart(body).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.d("", "success");
                            productItem.setQuantity(productItem.getQuantity()-1);
                            holder.quantity.setText(String.valueOf(productItem.getQuantity()));
                            holder.price.setText(String.valueOf(productItem.getPrice()*productItem.getQuantity()));
                        }else {
                            Log.d("", "response error");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("", "error");
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mListProduct != null){
            return mListProduct.size();
        }
        return 0;
    }


    public interface OnCartItemRemovedListener {
        void onCartItemRemoved(int position);
    }

    public class ProductItemViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageProduct;
        private ImageView plus;
        private final TextView name, price, quantity;
        private LinearLayout minus, delete;


        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            name = itemView.findViewById(R.id.txtNameProduct);
            price = itemView.findViewById(R.id.txtPrice);
            quantity = itemView.findViewById(R.id.txtAmountTwo);
            plus = itemView.findViewById(R.id.btnGridTwo);
            minus = itemView.findViewById(R.id.linearIconMinus);
            delete = itemView.findViewById(R.id.btnDeleteCart);
        }
        public void bindData(String imageUrl) {
            Picasso.get().load(imageUrl).into(imageProduct);
        }
    }
}
