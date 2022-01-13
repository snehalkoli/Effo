package com.ftg2021.effo2021V1.AppList;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.Choose_FindOrPostJob;
import com.ftg2021.effo2021V1.Apps.News.NewsMainActivity;
import com.ftg2021.effo2021V1.Authentication.DataModels.UserDataModel;
import com.ftg2021.effo2021V1.Authentication.Register.Activity.DisplayMyProfileActivity;
import com.ftg2021.effo2021V1.Authentication.Register.Activity.RegisterUserToDatabase;
import com.ftg2021.effo2021V1.Authentication.Register.Activity.RegisterWithPhoneNumber;
import com.ftg2021.effo2021V1.Authentication.UserData.DatabaseUserData;
import com.ftg2021.effo2021V1.Authentication.UserData.FirebaseUserData;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class AppList extends AppCompatActivity {

    CardView news, services, shopping;
    CardView job;
    boolean result;
    private FirebaseAuth mAuth;
    ImageView ivMyprofile,ivLogOut;
    ImageView llMyProfile,llLogOut;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        news = findViewById(R.id.news);
        services = findViewById(R.id.services);
        job = findViewById(R.id.job);
        shopping = findViewById(R.id.shopping);
        llMyProfile = findViewById(R.id.myProfile);
        dialog = new ProgressDialog(AppList.this);


        mAuth=FirebaseAuth.getInstance();

        llMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppList.this, DisplayMyProfileActivity.class);
                startActivity(intent);
            }
        });


        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppList.this, NewsMainActivity.class);
                startActivity(intent);
            }
        });
        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AppList.this, "Coming Soon...", Toast.LENGTH_SHORT).show();
            }
        });
        job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Loading...please wait");
                dialog.show();
                Intent intent = new Intent(AppList.this, Choose_FindOrPostJob.class);

                FirebaseUserData.firebaseUser=mAuth.getCurrentUser();
                FirebaseUser currentUser = FirebaseUserData.firebaseUser;

                if (currentUser != null)
                    updateUI(currentUser, intent);
                else {
                    dialog.dismiss();
                    startActivity(new Intent(AppList.this, RegisterWithPhoneNumber.class));
                }


            }
        });
        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AppList.this, "Coming Soon...", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showPopUp() {
            AlertDialog.Builder alert = new AlertDialog.Builder(AppList.this);
            alert.setMessage("Are you sure?")
            .setPositiveButton("Logout", new DialogInterface.OnClickListener()                 {
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.signOut();
                            Toast.makeText(AppList.this,"Logged out succesfully",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AppList.this, AppList.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }).setNegativeButton("Cancel", null);

            AlertDialog alert1 = alert.create();
            alert1.show();
      }

    private void updateUI(FirebaseUser user, Intent i) {

        FirebaseUserData.firebaseUser = user;


        if (user != null) {

            String userMobile = user.getPhoneNumber();

            Log.e("status result", "" + userMobile);


            if (!userMobile.isEmpty() || !userMobile.equals(""))
                try {

                    userMobile = userMobile.substring(3);

                    isUserAlreadyHaveAccount(userMobile, i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    private void isUserAlreadyHaveAccount(String phone, Intent intentToRedirect) {

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
                            JSONObject jsonObjectUser = jsonArray.getJSONObject(0);

                            int id = jsonObjectUser.getInt("id");
                            String name = jsonObjectUser.getString("user_name");
                            String emailId = jsonObjectUser.getString("emailId");
                            String phoneNumber = jsonObjectUser.getString("phoneNumber");
                            String address = jsonObjectUser.getString("address");
                            String firebase_notifcation_token = jsonObjectUser.getString("firebase_notifcation_token");

                            DatabaseUserData.currentUserData = new UserDataModel(id, name, phoneNumber, emailId, address, firebase_notifcation_token);


                        } else
                            result = false;

                        //redirect to if result true= homepage | false = PersonalDetailsActivity


                        if (result) {
                            dialog.dismiss();
                            startActivity(intentToRedirect);
                        } else {
                            dialog.dismiss();
                            Intent intent = new Intent(AppList.this, RegisterUserToDatabase.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("LoginType", "phone");
                            startActivity(intent);
                            Toast.makeText(AppList.this, "You don't have account create New", Toast.LENGTH_LONG).show();

                            startActivity(intent);
                        }

                        Log.e("status data", "Found" + jsonArray.length() + result);


                    } catch (Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
                        Log.d("status 1", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
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
            dialog.dismiss();
            Log.d("ERROR", e.getMessage());
            Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
        }


    }
}