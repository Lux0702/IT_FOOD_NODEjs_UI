package com.example.it_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it_food.Adapter.ProductAdapter;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.model.ProductItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    EditText edtSearch;
    private TextView txtTitle;
    private ArrayList<ProductItem> mListProduct;
    private RecyclerView recyclerViewShowProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // Áp dụng animation cho chuyển đổi vào SearchActivity
        String title = getIntent().getStringExtra("title");
        String idcategory = getIntent().getStringExtra("idcategory");
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        edtSearch=findViewById(R.id.edtSearch);
        Drawable[] drawables = edtSearch.getCompoundDrawablesRelative();
        Drawable drawableEnd = getResources().getDrawable(R.drawable.ic_cancel);
        mListProduct = new ArrayList<>();
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần thực hiện hành động trước khi thay đổi nội dung
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Thực hiện tìm kiếm ngay khi có sự thay đổi trong nội dung
                String searchQuery = s.toString();
                performSearch(searchQuery);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần thực hiện hành động sau khi thay đổi nội dung
            }
        });



/**/
// Ẩn DrawableEnd (index 2)
        drawables[2] = null;
        edtSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3]);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần thực hiện gì trước khi thay đổi văn bản
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Kiểm tra nếu EditText đang có văn bản
                if (s.length() > 0) {
                    // Ẩn DrawableEnd
                    edtSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
                } else {
                    // Hiện lại DrawableEnd
                    edtSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawableEnd , null);

                }
                String searchQuery = s.toString();
                performSearch(searchQuery);

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần thực hiện gì sau khi thay đổi văn bản
            }
        });
        recyclerViewShowProduct = findViewById(R.id.recyclerViewShowProduct);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewShowProduct.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerViewShowProduct.addItemDecoration(itemDecoration);
    }

    private void performSearch(String searchQuery) {
        APIService.apiService.searchString(searchQuery).enqueue(new Callback<ResponseBody>() {
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
                            String desc = itemObject.getString("description");
                            double price = itemObject.getDouble("price");
                            ProductItem productItem = new ProductItem(id, name, desc, price, image);
                            mListProduct.add(productItem);
                        }
                        ProductAdapter productAdapter = new ProductAdapter(mListProduct, SearchActivity.this);
                        recyclerViewShowProduct.setAdapter(productAdapter);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(SearchActivity.this, "Get data not success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


