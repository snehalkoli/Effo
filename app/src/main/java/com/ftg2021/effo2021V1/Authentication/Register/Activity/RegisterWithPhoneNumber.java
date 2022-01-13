package com.ftg2021.effo2021V1.Authentication.Register.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ftg2021.effo2021V1.Authentication.DataValidation.DataValidator;
import com.ftg2021.effo2021V1.Authentication.UserData.FirebaseUserData;
import com.ftg2021.effo2021V1.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegisterWithPhoneNumber extends AppCompatActivity {

    EditText ediTextMobile;
    Button submit;
    String mobile;
    boolean doubleBackToExitPressedOnce = false;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String actualText;
    String visibleText;
    TextView textView;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (doubleBackToExitPressedOnce) {
            super.finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to EXIT APP", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_with_phone_number);

        ediTextMobile = findViewById(R.id.editTextPhoneNo);
        textView = findViewById(R.id.tv);


        submit = findViewById(R.id.buttonGetOTP);
        mAuth = FirebaseAuth.getInstance();

        String text2 = "Via sms OTP send to your number";
        textView.setText(Html.fromHtml("Via sms " + "<font color=red>" + "OTP send" + "</font>" + " " + "to your number"));


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mobile = ediTextMobile.getText().toString().trim();

                submit.setEnabled(false);

                if (DataValidator.isValidMobile(mobile))
                    requestOtp(mobile);
                else {
                    ediTextMobile.setError("Invalid Mobile Number");
                    submit.setEnabled(true);
                }
            }
        });

    }

    private String myVisibleText(String actualText) {
        String ret = "";
        if (actualText.length() == 5) {
            ret = actualText.substring(0, 2) + " " + actualText.substring(2);
        } else if (actualText.length() == 6) {
            ret = actualText.substring(0, 3) + " " + actualText.substring(3);
        } else if (actualText.length() >= 7) {
            ret = actualText.substring(0, 4) + " " + actualText.substring(4);
        } else if (actualText.length() >= 10) {
            ret = actualText.substring(0, 2) + " " + actualText.substring(2);
        } else {
            ret = actualText;
        }
        return ret;
    }

    public void requestOtp(String mobile) {
        try {
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(getApplicationContext(), "Failed To Continue with Phone ! Try Again", Toast.LENGTH_SHORT).show();
                    Log.e("Error PHONE Register", e.getMessage());
                    submit.setEnabled(true);
                }

                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    FirebaseUserData.userPhone = mobile;
                    Log.e("MOBILE ", FirebaseUserData.userPhone);

                    Intent intent = new Intent(RegisterWithPhoneNumber.this, OtpVerification.class);
                    intent.putExtra("mobile", mobile);
                    intent.putExtra("verificationId", verificationId);
                    startActivity(intent);
                }
            };

            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber("+91" + mobile)
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)
                            .setCallbacks(mCallbacks)
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        } catch (Exception exception) {
            Log.e("Exception",""+exception.toString());
        }
    }
}