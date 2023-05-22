package com.example.it_food.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.activity.manager.DashBoardActivity;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    EditText etPhone, etPassword;
    private String mpassword;
    private static final  String TAG=SignInActivity.class.getName();
    RelativeLayout signupLayout;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);
        etPhone = findViewById(R.id.txtPhone);
        etPassword = findViewById(R.id.txtPassword);

        findViewById(R.id.txtSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, android.R.anim.fade_out);            }
        });

        findViewById(R.id.btnSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
        findViewById(R.id.txtForgotpassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetPassword();
            }
        });
    }

    private void ResetPassword() {
        Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        finish();
    }

    private void userLogin() {
        final String username = etPhone.getText().toString();
        final String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            etPhone.setError("Please enter your username");
            etPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
            return;
        }

        User user = new User(username, password);

        APIService.apiService.login(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User.Data userData = response.body().getData();
                    User user=response.body();
                    mpassword=etPassword.getText().toString();
                    String id = userData.getId();
                    String name = userData.getName();
                    String phone = userData.getPhoneNumber();
                    String email = userData.getEmail();
                    String address = userData.getAddress();
                    String gender = userData.getGender();
                    String avatar = userData.getAvatar();
                    String role=userData.getRole();
                    SharedPreferences.getInstance(getApplicationContext()).userLogin(new User(id, phone, name, email, gender, avatar, address));
                    // Đăng nhập thành công
                    Toast.makeText(SignInActivity.this, "Login user successfully", Toast.LENGTH_SHORT).show();
                    // Chuyển đến activity tiếp theo sau khi đăng nhập thành công

                    if(role.equalsIgnoreCase("MANAGER"))
                    {
                        Intent intent = new Intent(SignInActivity.this, DashBoardActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    else{
                        goToActivity();
                    }
                } else {
                    // Xử lý thất bại, hiển thị thông báo lỗi
                    Toast.makeText(SignInActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SignInActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void goToActivity() {
        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);

        startActivity(intent);
        finish();
    }


}
