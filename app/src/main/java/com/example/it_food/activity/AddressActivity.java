package com.example.it_food.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it_food.Adapter.AddressItemAdapter;
import com.example.it_food.Adapter.ProductItemAdapter;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.Address;
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

public class AddressActivity extends AppCompatActivity implements AddressItemAdapter.OnAddressRemovedListener, AddressItemAdapter.OnButtonClickListener {

    private RecyclerView recyclerView;
    private List<Address> mListAddress;
    private ImageView imageAdd;
    private AddressItemAdapter addressItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_address);

        imageAdd = findViewById(R.id.imageAdd);
        recyclerView = findViewById(R.id.recyclerAddress);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        mListAddress = new ArrayList<>();
        callApiGetAddress();
        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onAddressRemoved(int position) {
        mListAddress.remove(position);
        addressItemAdapter.notifyDataSetChanged();
    }


    private void callApiGetAddress() {
        User user = SharedPreferences.getInstance(this).getUser();
        APIService.apiService.getAddress(user.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray jsonArray = jsonObject.getJSONArray("addresses");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject itemObject = jsonArray.getJSONObject(i);
                            String id = itemObject.getString("_id");
                            String itemAddress = itemObject.getString("address");
                            // Trích xuất các thông tin khác của sản phẩm tại đây
                            Address address = new Address(id, itemAddress);
                            mListAddress.add(address);
                        }
                        addressItemAdapter = new AddressItemAdapter(mListAddress, AddressActivity.this, AddressActivity.this, AddressActivity.this);
                        recyclerView.setAdapter(addressItemAdapter);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(AddressActivity.this, "response not success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AddressActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onButtonClicked(int position) {
        Intent intent = new Intent(this, ConfirmOrderActivity.class);
        intent.putExtra("nameAddress", mListAddress.get(position).getAddress());
        startActivity(intent);
        finish();
    }
}