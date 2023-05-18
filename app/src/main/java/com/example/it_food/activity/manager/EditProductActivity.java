package com.example.it_food.activity.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it_food.Adapter.BestSellerAdapter;
import com.example.it_food.Adapter.EditCategoryAdapter;
import com.example.it_food.Adapter.EditProductAdapter;
import com.example.it_food.Adapter.ProductAdapter;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.activity.HomeActivity;
import com.example.it_food.activity.ShowProductActivity;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.Category;
import com.example.it_food.model.ProductItem;
import com.example.it_food.model.User;

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

public class EditProductActivity extends AppCompatActivity {

    Button btnClear, buttonEdit;
    TextView textViewCategory;
    private ArrayList<ProductItem> mListProduct;
    private TextView txtTitle;
    private RecyclerView recyclerViewShowProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String idcategory = intent.getStringExtra("idcategory");
        //init();
        recyclerViewShowProduct = findViewById(R.id.recyclerViewShowProduct);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewShowProduct.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerViewShowProduct.addItemDecoration(itemDecoration);

        /*recyclerViewShowProduct.setLayoutManager(new GridLayoutManager(this, 3));
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewShowProduct.setLayoutManager(linearLayoutManager1);*/
        mListProduct = new ArrayList<>();
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(title);
        mListProduct = new ArrayList<>();
        callApiGetProductList(idcategory);    }

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
                        EditProductAdapter editProductAdapter = new EditProductAdapter(mListProduct, EditProductActivity.this);
                        recyclerViewShowProduct.setAdapter(editProductAdapter);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(EditProductActivity.this, "response not success", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditProductActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}