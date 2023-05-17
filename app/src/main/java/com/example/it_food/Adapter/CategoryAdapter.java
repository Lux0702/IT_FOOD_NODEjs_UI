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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.activity.CartActivity;
import com.example.it_food.activity.ShowProductActivity;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.Category;
import com.example.it_food.model.ProductItem;
import com.example.it_food.model.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> mListCategory;
    private Context mContext;

    public CategoryAdapter(List<Category> mListCategory, Context mContext) {
        this.mListCategory = mListCategory;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = mListCategory.get(position);
        if (category == null) {
            return;
        }
        holder.categoryName.setText(category.getName());
        holder.bindData(category.getImage());

        holder.categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShowProductActivity.class);
                intent.putExtra("title", category.getName());
                intent.putExtra("idcategory", category.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListCategory != null) {
            return mListCategory.size();
        }
        return 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryImg;
        RelativeLayout categoryLayout;
        Target imageTarget;

        public CategoryViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryImg = itemView.findViewById(R.id.categoryImg);
            categoryLayout = itemView.findViewById(R.id.categoryLayout);

            imageTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                    categoryImg.setImageDrawable(drawable);
                    categoryImg.setBackground(drawable);
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
