package com.example.it_food.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.User;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    Button btnEditPasswd;
    ProgressDialog progressDialog;
    EditText edtPhone, edtUserName, edtEmail, edtGender;
    ImageView imgProfileEdit;
    User user;
    private APIService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        btnEditPasswd = findViewById(R.id.btnEditPasswd);
        if(SharedPreferences.getInstance(this).isLoggedIn()) {
            edtUserName = findViewById(R.id.edtUserName);
            edtEmail = findViewById(R.id.edtEmail);
            edtPhone = findViewById(R.id.edtPhone);
            edtGender = findViewById(R.id.edtGender);
            imgProfileEdit = findViewById(R.id.imgProfileEdit);

            user = SharedPreferences.getInstance(this).getUser();
            edtEmail.setText(user.getEmail());
            edtPhone.setText(user.getPhoneNumber());
            edtUserName.setText(user.getName());
            edtGender.setText(user.getGender());

            Glide.with(getApplicationContext()).load(user.getAvatar()).into(imgProfileEdit);

        } else {
            Intent intent = new Intent(EditProfileActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }

        findViewById(R.id.avatarFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(EditProfileActivity.this);
                chooseImage();
            }
        });

        btnEditPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, android.R.anim.fade_out);
            }
        });
    }
    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }
    Uri imageUri;
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // create a file from the image URI
            File file = new File(getRealPathFromURI(imageUri));

            // create a request body with the file
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);

            // create a multipart request with the request body
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
            RequestBody idRequestBody = RequestBody.create(MediaType.parse("text/plain"), user.getId());
            // upload the image using the API service
            apiService.uploadImage(idRequestBody,imagePart).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, "Failed " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Failed " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
}