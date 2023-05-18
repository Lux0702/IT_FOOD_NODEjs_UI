package com.example.it_food.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
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

public class EditProductAdapter extends RecyclerView.Adapter<EditProductAdapter.ProductItemViewHolder>{


    private final List<ProductItem> mListProduct;
    private Context mContext;

    public EditProductAdapter(List<ProductItem> mListProduct, Context mContext) {
        this.mListProduct = mListProduct;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public EditProductAdapter.ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_edit, parent, false);
        return new EditProductAdapter.ProductItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditProductAdapter.ProductItemViewHolder holder, int position) {
        ProductItem productItem = mListProduct.get(position);
        if (productItem == null){
            return;
        }
        holder.bindData(productItem.getImage());
        holder.name.setText(productItem.getName());

        User user = SharedPreferences.getInstance(mContext).getUser();

    }

    @Override
    public int getItemCount() {
        if (mListProduct != null){
            return mListProduct.size();
        }
        return 0;
    }
    public class ProductItemViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageProduct;
        private ImageView plus;
        private final TextView name;
        private LinearLayout minus;


        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            name = itemView.findViewById(R.id.textProduct);


        }
        public void bindData(String imageUrl) {
            Picasso.get().load(imageUrl).into(imageProduct);
        }
    }

}
