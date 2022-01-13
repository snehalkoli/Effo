package com.ftg2021.effo2021V1.Apps.Job.Job.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.AllJobs;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.CreateEmployeeProfileActivity;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs.ActivityCompanyMainScreen_PostJobs;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs.CompanyProfile;
import com.ftg2021.effo2021V1.Authentication.UserData.DatabaseUserData;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.ftg2021.effo2021V1.Util.DataModels.SingleCompanyDataModel;
import com.ftg2021.effo2021V1.Util.DataModels.SingleEmployeeDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class Choose_FindOrPostJob extends AppCompatActivity {

    Button findJob, postJob;
    boolean result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_find_or_post_job);

        findJob = findViewById(R.id.findJob);
        postJob = findViewById(R.id.PostJobHere);


        findJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = null;
                try {
                    intent = new Intent(Choose_FindOrPostJob.this, CreateEmployeeProfileActivity.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                checkIfUserAlreadyRegisteredCompanyOrJob(1, intent);
            }
        });

        postJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(Choose_FindOrPostJob.this, CompanyProfile.class);

                    checkIfUserAlreadyRegisteredCompanyOrJob(2, intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        // getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,new JobPostFragment()).commit();

        if (AppConstraints.companyEditFlow == 1)
        {
            AppConstraints.companyEditFlow=0;
            Intent intent = new Intent(Choose_FindOrPostJob.this, CompanyProfile.class);
            checkIfUserAlreadyRegisteredCompanyOrJob(2,intent);
        }


    }

    private void checkIfUserAlreadyRegisteredCompanyOrJob(int type, Intent intent) {

        result = false;

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        String data = null;

        if (type == 2)
            data = "{\"query\":\"select * from job_company where userId=" + DatabaseUserData.currentUserData.getUserId() + "\"}";
        else if (type == 1)
            data = "{\"query\":\"select * from job_employees where userId=" + DatabaseUserData.currentUserData.getUserId() + "\"}";

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

                            if (type == 1) {

                                int id = jsonObjectUser.getInt("id");
                                String name = jsonObjectUser.getString("name");
                                String education = jsonObjectUser.getString("education");
                                String dob = jsonObjectUser.getString("dob");
                                String educationField = jsonObjectUser.getString("educationField");
                                String university = jsonObjectUser.getString("university");
                                int isExperienced = jsonObjectUser.getInt("isExperienced");
                                String companyName = jsonObjectUser.getString("email");
                                String experience = jsonObjectUser.getString("experience");
                                String jobTitle = jsonObjectUser.getString("jobTitle");
                                String email = jsonObjectUser.getString("email");
                                String resumeLink = jsonObjectUser.getString("resumeLink");
                                int gender = jsonObjectUser.getInt("gender");

                                boolean res=(isExperienced==1)?true:false;

                                AppConstraints.currentEmployeeData=new SingleEmployeeDataModel(id,gender,name,dob,email,education,educationField,university,res,jobTitle,companyName,experience,resumeLink);

                            } else if (type == 2) {
                                int id = jsonObjectUser.getInt("id");
                                String name = jsonObjectUser.getString("name");
                                String type = jsonObjectUser.getString("type");
                                String date_established = jsonObjectUser.getString("date_established");
                                String website = jsonObjectUser.getString("website");
                                String company_document = jsonObjectUser.getString("company_document");
                                String companyImage = jsonObjectUser.getString("companyImage");
                                String email = jsonObjectUser.getString("email");

                                AppConstraints.currentCompanyData = new SingleCompanyDataModel(id, name, type, date_established, website, company_document, companyImage, email);
                            }
                        } else
                            result = false;

                        //redirect to if result true= homepage | false = PersonalDetailsActivity


                        if (result) {
                            Intent intent = null;
                            if (type == 1) {
                                intent = new Intent(Choose_FindOrPostJob.this, AllJobs.class);
                            } else if (type == 2) {
                                intent = new Intent(Choose_FindOrPostJob.this, ActivityCompanyMainScreen_PostJobs.class);
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("LoginType", "phone");
                            startActivity(intent);
                        } else {

                            Toast.makeText(Choose_FindOrPostJob.this, "You don't have account create New", Toast.LENGTH_LONG).show();

                            AppConstraints.employeeEditFlow=0;
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