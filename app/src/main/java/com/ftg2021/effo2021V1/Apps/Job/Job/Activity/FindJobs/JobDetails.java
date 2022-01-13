package com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ftg2021.effo2021V1.Apps.Job.Job.Adapter.JobDetailAdapter;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class JobDetails extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    Button applyNow;

    LinearLayout parentJobDetailsLayout;

    boolean isApplied = false;
    int id,employee_id,job_id,status;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);


        parentJobDetailsLayout = findViewById(R.id.parentJobDetailsLayout);

        applyNow = findViewById(R.id.applyNow);

        applyNow.setClickable(false);


        applyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked

                                if (isApplied)
                                    applyOrCancelJob(1);//cancel Job
                                else
                                    applyOrCancelJob(2);//apply Job

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                String message = "", yes = "", no = "";

                if (isApplied) {
                    message = "Do you really want to cancel this application ?";
                    yes = "Yes ,I want to Cancel";
                    no = "No";
                } else {
                    message = "Make sure that you have already entered valid information in your Profile . Clicking Apply button you will declaring that all information is correct .  ";
                    yes = "Apply";
                    no = "Cancel";
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(JobDetails.this);
                builder.setMessage(message).setPositiveButton(yes, dialogClickListener)
                        .setNegativeButton(no, dialogClickListener).show();


            }
        });


        tabLayout.addTab(tabLayout.newTab().setText("Job Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Company Details"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final JobDetailAdapter adapter = new JobDetailAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        checkIfAlreadyApplied();
    }

    private void checkIfAlreadyApplied() {
//        String data = "{\"query\":\"SELECT id FROM `job_jobs_applied` WHERE job_id=" + AppConstraints.viewJobData.getId() + " AND employee_id=" + AppConstraints.currentEmployeeData.getId() + "\"}";
        String data = "{\"query\":\"SELECT * FROM `job_jobs_applied` WHERE job_id=" + AppConstraints.viewJobData.getId() + " AND employee_id=" + AppConstraints.currentEmployeeData.getId() + "\"}";

        Log.e("DATA", data);

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
                        //Toast.makeText(getContext(),"Fetching cats data", Toast.LENGTH_SHORT).show()
                        Log.e("status = Response", response);

                        JSONArray jsonArray = new JSONArray(response);

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = jsonArray.getJSONObject(0);
                        } catch (JSONException e) {
                            Log.e("status 5", e.getMessage());
                        }

                        if (jsonArray.length() > 0) {
                            applyNow.setText("Cancel Application");
                            isApplied = true;
                        } else {
                            applyNow.setText("Apply Now");
                            isApplied = false;
                        }
                        applyNow.setClickable(true);

                        id = jsonObject.getInt("id");
                        employee_id = jsonObject.getInt("employee_id");
                        job_id = jsonObject.getInt("job_id");
                        date = jsonObject.getString("date");
                        status = jsonObject.getInt("status");

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


    private void applyOrCancelJob(int type) {

        String data = "";

        if (type == 2)
            data = "{\"query\":\"INSERT INTO `job_jobs_applied`(`employee_id`, `job_id`, `date`) VALUES (" + AppConstraints.currentEmployeeData.getId() + "" +
                    "," + AppConstraints.viewJobData.getId() + ",'" + AppConstraints.getCurrentDate() + "')\"}";
        else if (type == 1)
            data = "{\"query\":\"DELETE FROM `job_jobs_applied` WHERE employee_id=" + AppConstraints.currentEmployeeData.getId() + " AND job_id=" + AppConstraints.viewJobData.getId() + "\"}";
//        data = "{\"query\":\"DELETE FROM `job_jobs_applied` WHERE employee_id=" + AppConstraints.currentEmployeeData.getId() + " AND job_id=" + AppConstraints.viewJobData.getId() + "\"}";

        Log.e("DATA", data);

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
                            if (type == 1) {
                                applyNow.setText("Apply Now");
                                isApplied = false;
                            } else if (type == 2) {
                                applyNow.setText("Cancel Application");
                                isApplied = true;
                            }
                        } else {

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