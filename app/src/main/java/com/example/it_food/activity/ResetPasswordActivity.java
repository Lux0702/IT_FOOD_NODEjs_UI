package com.example.it_food.activity;

import static com.example.it_food.InterFace.APIService.apiService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.it_food.Adapter.ProductItemAdapter;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.model.GetUserResponse;
import com.example.it_food.model.ProductItem;
import com.example.it_food.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    private  String  mphoneNumber,confirmPass,name,email;
    EditText etNewPassword,etConfirmPassword;
    private static final  String TAG=ResetPasswordActivity.class.getName();

    Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reset_password);
        mphoneNumber=getIntent().getStringExtra("phone_Number");
        init();
        findViewById(R.id.imageArrowleft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResetPasswordActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, android.R.anim.fade_out);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                String password = etNewPassword.getText().toString();
                confirmPass = etConfirmPassword.getText().toString();

                if (password.equals(confirmPass)) {
                    // Hai trường password và confirm password giống nhau
                    // Thực hiện các hành động tiếp
                    getDataUser();
                    gotoFinishResetActivity();

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

    private void getDataUser() {
        APIService.apiService.getUserByPhoneNumber(mphoneNumber).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        JSONObject jsonObject = new JSONObject(json);
                        JSONObject itemObject = jsonObject.getJSONObject("result");
                        name = itemObject.getString("name");
                        email = itemObject.getString("email");

                        Toast.makeText(ResetPasswordActivity.this, "Get data success", Toast.LENGTH_SHORT).show();
                        ResetPassword();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(ResetPasswordActivity.this, "Get data not success", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ResetPasswordActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void ResetPassword() {
        User user = new User("0979569098", confirmPass,name, email);

        APIService.apiService.resetPasswordForgot(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this, "Reset Password succcess", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Error connect", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ResetPasswordActivity.this, "Reset Fail", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void init() {
        etConfirmPassword = findViewById(R.id.etConfirmPasswor);
        etNewPassword = findViewById(R.id.etNewPasswordFo);
        btnNext = findViewById(R.id.btnNext);

    }

    private void gotoFinishResetActivity() {
        Intent intent = new Intent(ResetPasswordActivity.this, FinishResetActivity.class);
        //intent.putExtra("phone_Number", phoneNumber);
        startActivity(intent);
        finish();
    }
}
