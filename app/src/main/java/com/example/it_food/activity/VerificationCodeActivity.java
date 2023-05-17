package com.example.it_food.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it_food.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationCodeActivity extends AppCompatActivity {

    EditText etCode1,etCode2,etCode3,etCode4,etCode5,etCode6;
    Button btnVerify;
    String mphone,mVerification;
    private  Boolean mresetPassword;
    String OTP_CODE;
    TextView txtSendOTPAgain;
    private FirebaseAuth mAuth;
    private static final  String TAG=VerificationCodeActivity.class.getName();
    private PhoneAuthProvider.ForceResendingToken mforceResendingToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verification_code);
        mphone=getIntent().getStringExtra("phone_Number");
        mphone="0"+ mphone.substring(3);
        mresetPassword= getIntent().getBooleanExtra("reset_password", false);
        mVerification=getIntent().getStringExtra("verification_Id");
        init();
        findViewById(R.id.imageArrowleft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerificationCodeActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, android.R.anim.fade_out);
            }
        });
        mAuth=FirebaseAuth.getInstance();
        FocusText();
        txtSendOTPAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etCode1.setText("");
                etCode2.setText("");
                etCode3.setText("");
                etCode4.setText("");
                etCode5.setText("");
                etCode6.setText("");
                onClickSendOtpAgain();
            }
        });
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etCode1.getText().toString())) {
                    etCode1.setError("Please enter code OTP");
                    etCode1.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(etCode2.getText().toString())) {
                    etCode2.setError("Please enter code OTP");
                    etCode2.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(etCode3.getText().toString())) {
                    etCode3.setError("Please enter code OTP");
                    etCode3.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(etCode4.getText().toString())) {
                    etCode4.setError("Please enter code OTP");
                    etCode4.requestFocus();
                    return;
                }
                else{
                    OTP_CODE=etCode1.getText().toString().trim()+
                             etCode2.getText().toString().trim()+
                             etCode3.getText().toString().trim()+
                             etCode4.getText().toString().trim()+
                            etCode5.getText().toString().trim()+
                            etCode6.getText().toString().trim();
                    onClickSendOtpCode(OTP_CODE);
                }

            }
        });
    }



    private void init(){
        etCode1=findViewById(R.id.edtCode1);
        etCode2=findViewById(R.id.edtCode2);
        etCode3=findViewById(R.id.edtCode3);
        etCode4=findViewById(R.id.edtCode4);
        etCode5=findViewById(R.id.edtCode5);
        etCode6=findViewById(R.id.edtCode6);
        btnVerify=findViewById(R.id.btnVerify);
        txtSendOTPAgain=findViewById(R.id.txtSendOTPAgain);
    }

    private void onClickSendOtpCode(String otp_code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerification, otp_code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            Toast.makeText(VerificationCodeActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            if(mresetPassword==true)
                            {
                                gotoResetPassActivity(user.getPhoneNumber());
                            }
                            else{
                                // chuyển qua activity khi register success
                            }
                        }
                        else {

                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerificationCodeActivity.this, "The verification code entered was invalid ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void gotoResetPassActivity(String phoneNumber) {
        Intent intent = new Intent(VerificationCodeActivity.this, ResetPasswordActivity.class);
        intent.putExtra("phone_Number", phoneNumber);
        startActivity(intent);
        finish();
    }
    private void goToEnterActivity(String phoneNumber, String verificationId) {
        Intent intent = new Intent(VerificationCodeActivity.this, VerificationCodeActivity.class);
        intent.putExtra("phone_Number", phoneNumber);
        intent.putExtra("verification_Id", verificationId);
        startActivity(intent);
        finish();
    }
    private void onClickSendOtpAgain(){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mphone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)
                        .setForceResendingToken(mforceResendingToken)// (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(VerificationCodeActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                mVerification=verificationId;
                                mforceResendingToken=forceResendingToken;
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void FocusText(){
        etCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Kiểm tra nếu đã nhập ký tự
                if (s.length() > 0) {
                    // Di chuyển trỏ tới EditText tiếp theo
                    etCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý
            }
        });
        etCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Kiểm tra nếu đã nhập ký tự
                if (s.length() > 0) {
                    // Di chuyển trỏ tới EditText tiếp theo
                    etCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý
            }
        });
        etCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Kiểm tra nếu đã nhập ký tự
                if (s.length() > 0) {
                    // Di chuyển trỏ tới EditText tiếp theo
                    etCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý
            }
        });
        etCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Kiểm tra nếu đã nhập ký tự
                if (s.length() > 0) {
                    // Di chuyển trỏ tới EditText tiếp theo
                    etCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý
            }
        });
        etCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Kiểm tra nếu đã nhập ký tự
                if (s.length() > 0) {
                    // Di chuyển trỏ tới EditText tiếp theo
                    etCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý
            }
        });
    }
}