package com.example.it_food.activity.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it_food.Adapter.EditCategoryAdapter;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.activity.HomeActivity;
import com.example.it_food.model.Category;
import com.example.it_food.model.ProductItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadProductActivity extends AppCompatActivity {
    Button btnClear,buttonEdit;
    TextView textViewCategory;
    private List<Category> mListCategory;
    private List<ProductItem> mListProduct;
    RecyclerView recyclerViewCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        init();
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadProductActivity.this);
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có chắc chắn muốn clear không?");

                // Xử lý khi người dùng chọn "Có"
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý sự kiện khi người dùng chọn "Có"
                        clearData(); // Gọi phương thức để xóa dữ liệu
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
        buttonEdit.findViewById(R.id.buttonEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditData();
            }
        });
        findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCategories.setLayoutManager(linearLayoutManager);
        mListCategory = new ArrayList<>();
        callApiGetCategory();    }



    private void EditData() {

    }

    private void clearData() {
    }

    private void init(){

        LayoutInflater inflater = LayoutInflater.from(this);
        View itemView = inflater.inflate(R.layout.row_product_edit, null);
        buttonEdit = itemView.findViewById(R.id.buttonEdit);
        textViewCategory= itemView.findViewById(R.id.textProduct);
        btnClear=itemView.findViewById(R.id.buttonClear);
    }
    private void callApiGetCategory() {
        APIService.apiService.getCategories().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject itemObject = jsonArray.getJSONObject(i);
                            String id = itemObject.getString("_id");
                            String name = itemObject.getString("name");
                            String image = itemObject.getString("image");
                            // Trích xuất các thông tin khác của sản phẩm tại đây
                            Category category = new Category(id, name, image);
                            mListCategory.add(category);
                        }
                        EditCategoryAdapter editCategoryAdapter= new EditCategoryAdapter(mListCategory, UploadProductActivity.this);
                        recyclerViewCategories.setAdapter(editCategoryAdapter);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(UploadProductActivity.this, "response not success", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UploadProductActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}