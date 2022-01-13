package com.ftg2021.effo2021V1.Authentication.Register.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ftg2021.effo2021V1.AppList.AppList;
import com.ftg2021.effo2021V1.Authentication.UserData.FirebaseUserData;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class OtpVerification extends AppCompatActivity {
    EditText otpEditText;
    Pinview pinview;
    Button submit;
    TextView mobileNum;
    String verificationId;

    boolean doubleBackToExitPressedOnce = false;
    boolean result;
    String otpText ="";
    TextView tvshowTimer;

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
        setContentView(R.layout.activity_otp_verification);


        mobileNum = findViewById(R.id.mobileNum);

        //otpEditText = findViewById(R.id.otpEditText);
        pinview = findViewById(R.id.otpEditText);


        submit = findViewById(R.id.submit2);
        tvshowTimer = findViewById(R.id.tvShowTimer);

        reverseTimer(5);


        verificationId = getIntent().getStringExtra("verificationId");
        mobileNum.setText(getIntent().getStringExtra("mobile"));

        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                otpText = pinview.getValue();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submit.setEnabled(false);

//                String otpText = otpEditText.getText().toString().trim();
                //otpText = otpEditText.toString().trim();

                if (!otpText.isEmpty() && otpText.length() == 6)
                    verifyOtp(otpText);
                else {
//                    otpEditText.se("Invalid OTP");
                    Toast.makeText(getApplicationContext(),"Please enter valid OTP",Toast.LENGTH_SHORT).show();
                    submit.setEnabled(true);
                }
            }
        });
    }

    public void reverseTimer(int Seconds){
        new CountDownTimer(Seconds* 1000+1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tvshowTimer.setText("TIME : " + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                tvshowTimer.setText("Completed");
                Intent intent = new Intent(OtpVerification.this,RegisterWithPhoneNumber.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    public void verifyOtp(String otp) {
        if (verificationId != null) {
            String code = otp.toString().trim();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            FirebaseAuth
                    .getInstance()
                    .signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {


                                isUserAlreadyHaveAccount(FirebaseUserData.userPhone);
                                Toast.makeText(OtpVerification.this, "verified successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(OtpVerification.this, "OTP is not verified ! Try Again", Toast.LENGTH_SHORT).show();
                                submit.setEnabled(true);
                            }
                        }
                    });
        }
    }

    private void isUserAlreadyHaveAccount(String phone) {

        result = false;

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        String data = "{\"query\":\"select * from users where phoneNumber='" + phone + "'\"}";
        Log.e("MukeshMack", " " + data);

        try {

            final String savedata = data;

            String URL = AppConstraints.dynamicApiUrl;

            //Toast.makeText(getContext(),"Fetching cats"+URL, Toast.LENGTH_SHORT).show();

            requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        //Toast.makeText(getContext(),"Fetching cats data", Toast.LENGTH_SHORT).show();

                        Log.e("status = Response", response);

                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                        } catch (JSONException e) {
                            Log.e("status 5", e.getMessage());
                        }




                        if (jsonArray.length() > 0) {
                            result = true;
                        }
                        else
                            result = false;




                        if (result) {
                            Intent intent = new Intent(OtpVerification.this, AppList.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("LoginType", "phone");
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(OtpVerification.this, RegisterUserToDatabase.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("LoginType", "phone");
                            startActivity(intent);
                            Toast.makeText(OtpVerification.this, "You don't have account create New", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        }



                        Log.e("status data", "Found" + jsonArray.length() + result);


                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
                        Log.d("status 1", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
                    Log.i("status 2", error.toString());

                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return savedata == null ? null : savedata.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        //Log.v("Unsupported Encoding while trying to get the bytes", data);
                        return null;
                    }
                }
            };
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Log.d("ERROR", e.getMessage());
            Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
        }


    }
}

