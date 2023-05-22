package com.example.it_food.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.activity.ResetPasswordActivity;
import com.example.it_food.activity.manager.EditCategoryActivity;
import com.example.it_food.activity.manager.ManageCategoryActivity;
import com.example.it_food.activity.manager.ManageProductActivity;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.Category;
import com.example.it_food.model.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;

public class EditCategoryAdapter extends RecyclerView.Adapter<EditCategoryAdapter.CategoryViewHolder>{
    private List<Category> mListCategory;
    private Context mContext;
    User user;
    public EditCategoryAdapter(List<Category> mListCategory, Context mContext) {
        this.mListCategory = mListCategory;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_edit, parent, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new EditCategoryAdapter.CategoryViewHolder(view, mContext);
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
                Intent intent = new Intent(mContext, ManageProductActivity.class);
                intent.putExtra("title", category.getName());
                intent.putExtra("idcategory", category.getId());
                mContext.startActivity(intent);
            }
        });
        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditCategoryActivity.class);
                intent.putExtra("categoryId", category.getId());
                intent.putExtra("name", category.getName());
                intent.putExtra("image", category.getImage());
                mContext.startActivity(intent);
            }
        });
        holder.buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có chắc chắn muốn clear không?");

                // Xử lý khi người dùng chọn "Có"
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý sự kiện khi người dùng chọn "Có"
                        clearCategory(category.getId()); // Gọi phương thức để xóa dữ liệu
                    }
                });

                // Xử lý khi người dùng chọn "Không"
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Không làm gì khi người dùng chọn "Không"
                    }
                });

                // Hiển thị dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListCategory != null) {
            return mListCategory.size();
        }
        return 0;    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryImg;
        RelativeLayout categoryLayout;
        Target imageTarget;
        Button buttonEdit, buttonClear;

        public CategoryViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.textProduct);
            categoryImg = itemView.findViewById(R.id.imageProduct);
            categoryLayout = itemView.findViewById(R.id.editCategoryLayout);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonClear = itemView.findViewById(R.id.buttonClear);
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
    private void clearCategory(String categoryId){
        String userId = "6454bd3cf82eec776eb7d842";
        APIService.apiService.deleteCategory(userId,categoryId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý khi phản hồi không thành công
                    Log.e("TAG","response not success" +categoryId);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mContext, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
