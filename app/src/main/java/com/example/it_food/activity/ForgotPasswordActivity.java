package com.example.it_food.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ForgotPasswordActivity extends AppCompatActivity {
    Button btnNext;
    private  Boolean resetpass=false;
    EditText  editTextphoneNumber;
    private String phoneNumber;
    private FirebaseAuth mAuth;
    private static final  String TAG=ForgotPasswordActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        btnNext=findViewById(R.id.btnNext);
        editTextphoneNumber=findViewById(R.id.etPhoneNumber);

        mAuth=FirebaseAuth.getInstance();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editTextphoneNumber.getText().toString())) {
                    editTextphoneNumber.setError("Please enter code OTP");
                    editTextphoneNumber.requestFocus();
                    return;
                }
                else{
                    phoneNumber=editTextphoneNumber.getText().toString().trim();
                    onClickVerifyPhoneNumber(phoneNumber);

                }


            }

        });
    }

    private void onClickVerifyPhoneNumber(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(ForgotPasswordActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                resetpass=true;
                                goToEnterActivity(phoneNumber,verificationId,resetpass);
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            goToActivity(user.getPhoneNumber());
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(ForgotPasswordActivity.this, "The verification code entered was invalid ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void goToActivity(String phoneNumber) {
        Intent intent = new Intent(ForgotPasswordActivity.this, SignUpActivity.class);
        intent.putExtra("phone_Number", phoneNumber);
        startActivity(intent);
        finish();
    }
    private void goToEnterActivity(String phoneNumber, String verificationId,Boolean reset) {
        Intent intent = new Intent(ForgotPasswordActivity.this, VerificationCodeActivity.class);
        intent.putExtra("phone_Number", phoneNumber);
        intent.putExtra("verification_Id", verificationId);
        intent.putExtra("reset_password", reset);
        startActivity(intent);
        finish();
    }
}