package com.example.it_food.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it_food.R;
import com.example.it_food.activity.ProductDetailActivity;
import com.example.it_food.model.ProductItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductItem> mListProduct;
    private Context mContext;

    public ProductAdapter(List<ProductItem> mListBestSellProduct, Context mContext) {
        this.mListProduct = mListBestSellProduct;
        this.mContext = mContext;
    }

    @androidx.annotation.NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ProductViewHolder holder, int position) {
        ProductItem productItem = mListProduct.get(position);
        if (productItem == null) {
            return;
        }
        holder.productName.setText(productItem.getName());
        holder.productPrice.setText(String.valueOf(productItem.getPrice()));
        holder.bindData(productItem.getImage());

        holder.productItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("productName", productItem.getName());
                intent.putExtra("productId", productItem.get_id());
                intent.putExtra("productPrice", productItem.getPrice());
                intent.putExtra("productDesc", productItem.getDescription());
                intent.putExtra("productImg", productItem.getImage());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListProduct != null) {
            return mListProduct.size();
        }
        return 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        ImageView productImg;
        RelativeLayout productItemLayout;
        Target imageTarget;

        public ProductViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productImg = itemView.findViewById(R.id.productImg);
            productPrice = itemView.findViewById(R.id.productPrice);
            productItemLayout = itemView.findViewById(R.id.productItemLayout);

            imageTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                    productImg.setImageDrawable(drawable);
                    productImg.setBackground(drawable);
                }
                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    // Xử lý khi không thể tải được ảnh
                }
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    // Chuẩn bị trước khi tải ảnh
                }
            };
        }
        public void bindData(String imageUrl) {
            Picasso.get().load(imageUrl).into(imageTarget);
        }
    }
}
