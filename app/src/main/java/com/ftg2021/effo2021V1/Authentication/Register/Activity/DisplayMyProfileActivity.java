package com.ftg2021.effo2021V1.Authentication.Register.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

public class DisplayMyProfileActivity extends AppCompatActivity {
    TextView name,phoneNo,Email,Address;
    boolean result;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    Button btnLogOut,btnUpdateData;
    String emailId,address,user_id ;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_my_profile);
        name = findViewById(R.id.tvName);
        phoneNo = findViewById(R.id.tvUserContactNo);
        Email = findViewById(R.id.etUserEmail);
        Address = findViewById(R.id.etUserAddress);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnUpdateData = findViewById(R.id.btnUpdateData);
        progressDialog = new ProgressDialog(DisplayMyProfileActivity.this);
        mAuth=FirebaseAuth.getInstance();

        getUserData();

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUserData.firebaseUser=mAuth.getCurrentUser();
                FirebaseUser currentUser = FirebaseUserData.firebaseUser;

                if (currentUser != null){
                    showPopUp();
                }
                else {
                    startActivity(new Intent(DisplayMyProfileActivity.this, RegisterWithPhoneNumber.class));
                }
            }
        });

        btnUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidEmailId(Email.getText().toString().trim())){
                    updateData();
//                    Email.setError("Valid Email Address.");
//                    Toast.makeText(getApplicationContext(), "Valid Email Address.", Toast.LENGTH_SHORT).show();
                }else{
                    Email.setError("Valid Email Address.");
//                    Toast.makeText(getApplicationContext(), "InValid Email Address.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() > 0) {
//                    btnUpdateData.setVisibility(View.VISIBLE);
//                } else {
//                    btnUpdateData.setVisibility(View.GONE);
//                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() > 0) {
                    btnUpdateData.setVisibility(View.VISIBLE);
                } else {
                    btnUpdateData.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equalsIgnoreCase(emailId)){
                    btnUpdateData.setVisibility(View.GONE);
                } else {
//                    hideKeyboard(DisplayMyProfileActivity.this);
                    btnUpdateData.setVisibility(View.VISIBLE);
                }
            }
        });

        Address.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() > 0) {
//                    btnUpdateData.setVisibility(View.VISIBLE);
//                } else {
//                    btnUpdateData.setVisibility(View.GONE);
//                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() > 0) {
                    btnUpdateData.setVisibility(View.VISIBLE);
                } else {
                    btnUpdateData.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equalsIgnoreCase(address)){
                    btnUpdateData.setVisibility(View.GONE);
                } else {
//                    hideKeyboard(DisplayMyProfileActivity.this);
                    btnUpdateData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean isValidEmailId(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }


    private void updateData() {
        String queryData="";
        queryData = "{\"query\":\"UPDATE `users` SET " +
                "`user_name`='"+name.getText().toString().trim()+"'," +
                "`emailId`='"+Email.getText().toString().trim()+"'," +
                "`phoneNumber`='"+phoneNo.getText().toString().trim()+"'," +
                "`address`='"+Address.getText().toString().trim()+"' WHERE  id="+user_id+"\"}";

        updateProfile(queryData);
    }

    private void updateProfile(String data) {
        progressDialog.setMessage("Updating your profile....please wait");
        progressDialog.show();
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
                        progressDialog.dismiss();

                        if (affectedRows == 1) {
                            Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DisplayMyProfileActivity.this, AppList.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error To Update Data! Try Again", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
                        Log.d("status 1", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
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
                        progressDialog.dismiss();
                        //Log.v("Unsupported Encoding while trying to get the bytes", data);
                        return null;
                    }
                }
            };
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            progressDialog.dismiss();
            Log.d("ERROR", e.getMessage());
            Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showPopUp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(DisplayMyProfileActivity.this);
        alert.setMessage("Are you sure?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener()                 {
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Toast.makeText(DisplayMyProfileActivity.this,"Logged out succesfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DisplayMyProfileActivity.this, AppList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancel", null);

        AlertDialog alert1 = alert.create();
        alert1.show();
    }


    private void getUserData() {
        FirebaseUserData.firebaseUser=mAuth.getCurrentUser();
        FirebaseUser currentUser = FirebaseUserData.firebaseUser;

        if (currentUser != null){
            updateUI(currentUser);
        }
        else {
            Intent intent = new Intent(DisplayMyProfileActivity.this, RegisterWithPhoneNumber.class);
            startActivity(intent);
            finish();
        }
    }

    private void updateUI(FirebaseUser user) {
        progressDialog.setMessage("Loading..please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        FirebaseUserData.firebaseUser = user;
        if (user != null) {

            String userMobile = user.getPhoneNumber();
            Log.e("status result", "" + userMobile);

            if (!userMobile.isEmpty() || !userMobile.equals(""))
            {
                try {
                    userMobile = userMobile.substring(3);
                    isUserAlreadyHaveAccount(userMobile);
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }else {
              progressDialog.dismiss();
            }
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
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                user_id = jsonObject.getString("id");
                                String user_name = jsonObject.getString("user_name");
                                emailId = jsonObject.getString("emailId");
                                String phoneNumber = jsonObject.getString("phoneNumber");
                                address = jsonObject.getString("address");

                                name.setText(user_name);
                                Email.setText(emailId);
                                phoneNo.setText(phoneNumber);
                                Address.setText(address);
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            Log.e("status 5", e.getMessage());
                            progressDialog.dismiss();
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
