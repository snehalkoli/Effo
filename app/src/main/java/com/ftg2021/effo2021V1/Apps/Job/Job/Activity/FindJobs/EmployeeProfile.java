package com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.ftg2021.effo2021V1.Util.DataModels.SingleEmployeeDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class EmployeeProfile extends AppCompatActivity {

    TextView name, dob, email, education, university, experienced, empContactNo, empDesignation;
    ImageView editEmpProfile;
    boolean result;
    
    Button btnViewResume,btnLogOut,btnCloseApplication;
    TextView tvViewResume;
    private FirebaseAuth mAuth;
    int id,employee_id,job_id,status;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_info);


        name = findViewById(R.id.empName);
        dob = findViewById(R.id.empDob);
        email = findViewById(R.id.empEmail);
        education = findViewById(R.id.empEducation);
        university = findViewById(R.id.empUniversity);
        experienced = findViewById(R.id.empExperienced);
        editEmpProfile = findViewById(R.id.editEmpProfile);
        empContactNo = findViewById(R.id.empMobileNumber);
        empDesignation = findViewById(R.id.empDesignation);
//        btnViewResume = findViewById(R.id.btnViewResume);
        tvViewResume = findViewById(R.id.tvViewResume);
        btnCloseApplication = findViewById(R.id.btnCloseApplication);
        //btnLogOut = findViewById(R.id.btnLogOut);

        mAuth=FirebaseAuth.getInstance();

        SpannableString content = new SpannableString("Click here to view resume");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvViewResume.setText(content);

//        btnLogOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopup();
//            }
//        });

        try {
            String data = getIntent().getStringExtra("setData");
             id = getIntent().getIntExtra("id",0);
             employee_id = getIntent().getIntExtra("employee_id",0);
             job_id = getIntent().getIntExtra("job_id",0);
             date = getIntent().getStringExtra("date");
             status = getIntent().getIntExtra("status",0);

            if (data.equalsIgnoreCase("true")){
                btnCloseApplication.setVisibility(View.VISIBLE);
            }
        }catch (Exception exception){
                Log.e("Exception",""+exception.toString());
        }

        btnCloseApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EmployeeProfile.this);
                alert.setMessage("Do you want to close this job?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            closeJob(id,employee_id,job_id,date,status);
                        }
                    }).setNegativeButton("Cancel", null);

                AlertDialog alert1 = alert.create();
                alert1.show();
            }
        });

        if (AppConstraints.viewEmployeeFlow == 1) {
            getAllData();

            editEmpProfile.setVisibility(View.GONE);
            editEmpProfile.setClickable(false);
            AppConstraints.viewEmployeeFlow = 0;

        } else {
            editEmpProfile.setVisibility(View.VISIBLE);
            editEmpProfile.setClickable(true);
        }

        SingleEmployeeDataModel data = AppConstraints.currentEmployeeData;

        try{
            name.setText(data.getName());
            dob.setText(data.getDateOfBirth());
            email.setText(data.getEmail());
            education.setText(data.getEducation());
            university.setText(data.getUniversity());
            education.setText(data.getEducation());
            FirebaseUserData.firebaseUser=mAuth.getCurrentUser();
            FirebaseUser currentUser = FirebaseUserData.firebaseUser;
            empContactNo.setText(currentUser.getPhoneNumber());
            empDesignation.setText("Designation:"+ " "+data.getWorkedJobTitle());

            if (data.isExperienced())
                experienced.setText("Experienced");
            else
                experienced.setText("Fresher");
        }catch (Exception exception){

        }
        editEmpProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstraints.employeeEditFlow = 1;
                AppConstraints.editEmployeeData = AppConstraints.currentEmployeeData;
                startActivity(new Intent(EmployeeProfile.this, CreateEmployeeProfileActivity.class));
            }
        });

//        btnViewResume.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (AppConstraints.currentEmployeeData.getResumeLink()!=null &&
//                        !AppConstraints.currentEmployeeData.getResumeLink().equalsIgnoreCase("null")
//                        && !AppConstraints.currentEmployeeData.getResumeLink().equalsIgnoreCase(null)
//                         && !AppConstraints.currentEmployeeData.getResumeLink().equalsIgnoreCase("")){
//                    Intent intent = new Intent(EmployeeProfile.this,OpenPdfActivity.class);
//                    intent.putExtra("PdfLink",AppConstraints.currentEmployeeData.getResumeLink());
//                    startActivity(intent);
//                }else {
//                    Toast.makeText(EmployeeProfile.this,"Please upload resume first",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        tvViewResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppConstraints.currentEmployeeData.getResumeLink()!=null &&
                        !AppConstraints.currentEmployeeData.getResumeLink().equalsIgnoreCase("null")
                        && !AppConstraints.currentEmployeeData.getResumeLink().equalsIgnoreCase(null)
                        && !AppConstraints.currentEmployeeData.getResumeLink().equalsIgnoreCase("")){
                    Intent intent = new Intent(EmployeeProfile.this,OpenPdfActivity.class);
                    intent.putExtra("PdfLink",AppConstraints.currentEmployeeData.getResumeLink());
                    startActivity(intent);
                }else {
                    Toast.makeText(EmployeeProfile.this,"Please upload resume first",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void closeJob(int id, int employee_id, int job_id, String date, int status) {
        String queryData;
        queryData = "{\"query\":\"UPDATE `job_jobs_applied` SET " +
                "`employee_id`='" + employee_id + "'," +
                "`job_id`='" + job_id + "'," +
                "`date`='" + date + "'," +
                "`status`=" + 0 + " WHERE  id=" + id + "\"}";
        closeQuery(queryData);
    }

    private void closeQuery(String queryData) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        try {

            final String savedata = queryData;

            String URL = AppConstraints.dynamicApiUrl;

            requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        //Toast.makeText(getContext(),"Fetching cats data", Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            Log.e("status 5", e.getMessage());
                        }

                        int affectedRows = jsonObject.getInt("affectedRows");

                        if (affectedRows == 1) {
                            btnCloseApplication.setText("Closed Application");
                        }else {

                        }
                    } catch (Exception e) {
                        //   Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
                        Log.d("status 1", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //  Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
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
            //  Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPopup() {
        AlertDialog.Builder alert = new AlertDialog.Builder(EmployeeProfile.this);
        alert.setMessage("Are you sure?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener()                 {

                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Toast.makeText(getApplicationContext(),"Logged out succesfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EmployeeProfile.this, AppList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancel", null);

        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    private void getAllData() {

        result = false;

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        String data = "{\"query\":\"select * from job_employees where id=" + AppConstraints.currentApplicationData.getEmployeeId() + "\"}";

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

                            boolean res = (isExperienced == 1) ? true : false;

                            AppConstraints.currentEmployeeData = new SingleEmployeeDataModel(id, gender, name, dob, email, education, educationField, university, res, jobTitle, companyName, experience,resumeLink);

                        } else
                            result = false;

                        //redirect to if result true= homepage | false = PersonalDetailsActivity


                        if (result) {


                        } else {


                        }

                        Log.e("status data", "Found" + jsonArray.length() + result);


                    } catch (Exception e) {
                        //   Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
                        Log.d("status 1", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //  Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
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
            //  Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
        }
    }
}