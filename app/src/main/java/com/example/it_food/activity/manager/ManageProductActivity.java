package com.example.it_food.activity.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it_food.Adapter.EditProductAdapter;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.ProductItem;
import com.example.it_food.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageProductActivity extends AppCompatActivity {

    Button btnAdd;
    TextView textViewCategory;
    private ArrayList<ProductItem> mListProduct;
    private TextView txtTitle;
    private RecyclerView recyclerViewShowProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String idcategory = intent.getStringExtra("idcategory");
        btnAdd = findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageProductActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        recyclerViewShowProduct = findViewById(R.id.recyclerViewShowProduct);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewShowProduct.setLayoutManager(linearLayoutManager);
        mListProduct = new ArrayList<>();
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(title);
        callApiGetProductList(idcategory);
    }

    private void callApiGetProductList(String idcategogy) {
        User user = SharedPreferences.getInstance(this).getUser();
        APIService.apiService.getProductList(idcategogy).enqueue(new Callback<ResponseBody>() {
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
                        EditProductAdapter editProductAdapter = new EditProductAdapter(mListProduct, ManageProductActivity.this);
                        recyclerViewShowProduct.setAdapter(editProductAdapter);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(ManageProductActivity.this, "response not success", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ManageProductActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}