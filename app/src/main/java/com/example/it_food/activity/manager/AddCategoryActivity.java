package com.example.it_food.activity.manager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.it_food.InterFace.APIService;
import com.example.it_food.R;
import com.example.it_food.activity.EditProfileActivity;
import com.example.it_food.helper.RealPathUtil;
import com.example.it_food.helper.SharedPreferences;
import com.example.it_food.model.User;

import org.checkerframework.checker.units.qual.C;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCategoryActivity extends AppCompatActivity {
    EditText edtNameCategory;
    AppCompatButton btnAdd;
    User user;
    ImageView imgCategoryEdit;
    ProgressDialog progressDialog;
    public static final String TAG = EditProfileActivity.class.getName();
    Uri mUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        edtNameCategory = findViewById(R.id.edtNameCategory);
        imgCategoryEdit = findViewById(R.id.imgCategoryEdit);
        user = SharedPreferences.getInstance(this).getUser();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApiAddCategory();
            }
        });
        imgCategoryEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCLickRequestPermission();
            }
        });
    }
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e(TAG, "onActivityResult");
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        Uri uri = data.getData();
                        mUri=uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imgCategoryEdit.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    }
                }
            });
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Image"));
    }
    private void callApiAddCategory() {
        progressDialog.show();
        String id = user.getId();
        String name = edtNameCategory.getText().toString();
        RequestBody requestBodyId = RequestBody.create(MediaType.parse("multipart/form-data"),id);
        RequestBody requestBodyName = RequestBody.create(MediaType.parse("multipart/form-data"),name);
        String strRealpath= RealPathUtil.getRealPath(this,mUri);
        Log.e("IT.FOOD",strRealpath);
        File file=new File(strRealpath);
        RequestBody requestBodyImage = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part multipartBodyImage = MultipartBody.Part.createFormData("image", file.getName(), requestBodyImage);
        User user = SharedPreferences.getInstance(this).getUser();
        APIService.apiService.addCategory(requestBodyName,multipartBodyImage, requestBodyId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(AddCategoryActivity.this, "Add success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddCategoryActivity.this, ManageCategoryActivity.class);
                    startActivity(intent);
                } else {
                    // Xử lý khi phản hồi không thành công
                    Toast.makeText(AddCategoryActivity.this, "response not success" +response.errorBody(), Toast.LENGTH_SHORT).show();
                    Log.e("TAG","response not success" +response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AddCategoryActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void onCLickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, 10);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            openGallery();
        }

    }

}