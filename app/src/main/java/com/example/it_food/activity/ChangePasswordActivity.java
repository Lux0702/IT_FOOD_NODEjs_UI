package com.example.it_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.model.User;

import org.checkerframework.checker.units.qual.C;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {


    private  String  phone,currentPass,newPass,confirmPass,id;
    Button btnNext;
    ImageView imageArrowleft;
    EditText etNewPassword,etOldPassword,etConfirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_statistical);
        setContentView(R.layout.activity_change_password);
        phone=getIntent().getStringExtra("phone_Number");
        currentPass=getIntent().getStringExtra("current_Password");

        imageArrowleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePasswordActivity.this, EditProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, android.R.anim.fade_out);
            }
        });
        init();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etOldPassword.getText().toString())) {
                    etOldPassword.setError("Please enter code old Password");
                    etOldPassword.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(etNewPassword.getText().toString())) {
                    etNewPassword.setError("Please enter new password");
                    etNewPassword.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(etConfirmPassword.getText().toString())) {
                    etConfirmPassword.setError("Please enter exactly password");
                    etConfirmPassword.requestFocus();
                    return;
                }
                newPass = etNewPassword.getText().toString();
                confirmPass = etConfirmPassword.getText().toString();
                String oldPass=etOldPassword.getText().toString();

                if(!currentPass.equals(oldPass))
                {
                    etOldPassword.setError("Please enter exactly old password");
                    etOldPassword.requestFocus();
                    return;
                }
                if (newPass.equals(confirmPass)) {
                    // Hai trường password và confirm password giống nhau
                    // Thực hiện các hành động tiếp
                    getDataUser();

                } else {
                    // Hai trường password và confirm password không giống nhau
                    // Hiển thị thông báo lỗi cho người dùng
                    Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
                    etConfirmPassword.setError("Please enter correct password");
                    etConfirmPassword.requestFocus();
                }
            }
        });
    }
    private void init() {
        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPasswordFo);
        etConfirmPassword=findViewById(R.id.etConfirmPasswor);
        imageArrowleft=findViewById(R.id.imageArrowleft);
        btnNext = findViewById(R.id.btnNext);

    }

    private void getDataUser() {
        APIService.apiService.getUserByPhoneNumber(phone).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        JSONObject itemObject = jsonObject.getJSONObject("result");
                        id = itemObject.getString("id");

                        Toast.makeText(ChangePasswordActivity.this, "Get data success", Toast.LENGTH_SHORT).show();
                        ResetPassword();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(ChangePasswordActivity.this, "Get data not success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void ResetPassword() {
        User user = new User(id, currentPass,confirmPass);

        APIService.apiService.ChangePassword(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChangePasswordActivity.this, "Change Password succcess", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangePasswordActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Error connect", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Reset Fail", Toast.LENGTH_SHORT).show();

            }
        });

    }

}