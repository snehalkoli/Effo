package com.ftg2021.effo2021V1.Authentication.Register.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ftg2021.effo2021V1.AppList.AppList;
import com.ftg2021.effo2021V1.Authentication.DataValidation.DataValidator;
import com.ftg2021.effo2021V1.Authentication.UserData.DatabaseUserData;
import com.ftg2021.effo2021V1.Authentication.UserData.FirebaseUserData;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RegisterUserToDatabase extends AppCompatActivity {

    EditText name, email, address;

    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_to_database);

        name = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail);
        address = findViewById(R.id.editTextAddress);

        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameText = name.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                String addressText = address.getText().toString().trim();

                String phoneText = FirebaseUserData.userPhone;

                boolean valid = true;

                if (!emailText.isEmpty())
                    if (!DataValidator.isValidEmail(emailText)) {
                        email.setError("Invalid");
                        valid = false;
                    }

                if (nameText.isEmpty()) {
                    name.setError("Invalid");
                    valid = false;
                }


                if (addressText.isEmpty()) {
                    address.setError("Invalid");
                    valid = false;
                }


                if (valid) {
                    FirebaseMessaging.getInstance().getToken()
                            .addOnCompleteListener(task -> {
                                if (!task.isSuccessful()) {
                                    //Could not get FirebaseMessagingToken
                                    return;
                                }
                                if (null != task.getResult()) {
                                    //Got FirebaseMessagingToken
                                    String firebaseMessagingToken = task.getResult();
                                    //Use firebaseMessagingToken further
                                    String data = "{\"query\":\"INSERT INTO `users` (`user_name`, `password`, `emailId`, `phoneNumber`, `address`,  `login_type`, `firebase_notifcation_token`) " +
                                            "VALUES ('" + nameText + "', '', '" + emailText + "', '" + phoneText + "', '" + addressText + "', '',  '" + firebaseMessagingToken + "')\"}";

                                    saveProfile(data);

                                }
                            });
                } else {
                    Snackbar snackbar = null;


                    snackbar = Snackbar
                            .make(v, "Invalid Data", Snackbar.LENGTH_SHORT);

                    snackbar.show();


                }

            }
        });


    }

    public void saveProfile(String data) {

        Log.e("data", data);
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        Log.e("Effo", " " + data);

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

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            Log.e("status 5", e.getMessage());
                        }


                        int affectedRows = jsonObject.getInt("affectedRows");

                        if (affectedRows == 1) {
                            startActivity(new Intent(RegisterUserToDatabase.this, AppList.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Error Creating Account! Try Again", Toast.LENGTH_SHORT).show();
                        }


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