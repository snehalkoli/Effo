package com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ftg2021.effo2021V1.AppList.AppList;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.Candidate;
import com.ftg2021.effo2021V1.Apps.Job.Job.Adapter.AllJobsAdapter;
import com.ftg2021.effo2021V1.Apps.Job.Job.Fragment.ApplicationReceivedFragment;
import com.ftg2021.effo2021V1.Apps.Job.Job.Fragment.CompanyProfileFragment;
import com.ftg2021.effo2021V1.Apps.Job.Job.Fragment.JobPostedFragment;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.ftg2021.effo2021V1.Util.DataModels.SingleJobDataModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class AllJobs extends AppCompatActivity {

    RecyclerView recyclerView;

    ImageView employeeProfile;

    SearchView searchView;

    List<SingleJobDataModel> jobList = new ArrayList<>();

    boolean result;
    AllJobsAdapter adapter;
    ImageView ivAppliedJobs;
    BottomNavigationView bottomNavigationView;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AllJobs.this, AppList.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_jobs);

        recyclerView=findViewById(R.id.recyclerViewAllJobs);
        searchView=findViewById(R.id.search);

        employeeProfile=findViewById(R.id.imageEmployeeProfile);
//        ivAppliedJobs = findViewById(R.id.ivAppliedJobs);
        bottomNavigationView=findViewById(R.id.bottom_navigation_jobs);



//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AllJobs.this, Candidate.class);
//                startActivity(intent);
//            }
//        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment=null;
                switch (item.getItemId()){
                    case R.id.AppliedJobs:
                        Intent intent = new Intent(AllJobs.this,AppliedJobsActivity.class);
                        startActivity(intent);
                        break;
                    }
                return true;
            }
        });

//        ivAppliedJobs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(AllJobs.this,AppliedJobsActivity.class);
//                startActivity(intent);
//            }
//        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        employeeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstraints.editEmployeeData=AppConstraints.currentEmployeeData;
                Intent intent = new Intent(AllJobs.this, EmployeeProfile.class);
                startActivity(intent);
            }
        });



        loadAllJobs();


    }

    private void loadAllJobs() {

        result = false;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


        String data = "{\"query\":\"select j.*,c.name as companyName from job_jobs_posted j,job_company c where j.status=1 and c.id=j.companyId\"}";
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


                        if (jsonArray.length() == 0) {
                    //        noJobsFound.setVisibility(View.VISIBLE);
                    //        noJobsFound.setText("No Job Posted Yet.\nStart Posting Jobs by clicking on Post Jobs");
                        } else
                     //       noJobsFound.setVisibility(View.GONE);

                        jobList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectUser = jsonArray.getJSONObject(i);

                            int id = jsonObjectUser.getInt("id");
                            int companyId = jsonObjectUser.getInt("companyId");
                            String title = jsonObjectUser.getString("title");
                            String description = jsonObjectUser.getString("description");
                            String type = jsonObjectUser.getString("type");
                            String place = jsonObjectUser.getString("place");
                            int employeeCount = jsonObjectUser.getInt("employeeCount");
                            String qualification = jsonObjectUser.getString("qualification");
                            String experience = jsonObjectUser.getString("experience");
                            String salary = jsonObjectUser.getString("salary");
                            int status = jsonObjectUser.getInt("status");
                            String datePosted = jsonObjectUser.getString("datePosted");
                            String location = jsonObjectUser.getString("location");
                            String communicationSkill = jsonObjectUser.getString("communicationSkill");


                            String companyName = jsonObjectUser.getString("companyName");


                            SingleJobDataModel data=new SingleJobDataModel(id, title, description, type, place, employeeCount, qualification, experience, salary, status, datePosted, location,communicationSkill);

                            data.setCompanyName(""+companyName);
                            data.setCompanyId(companyId);

                            jobList.add(data);
                        }



                        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
                        adapter = new AllJobsAdapter(jobList);
                        recyclerView.setAdapter(adapter);
//                        recyclerView.setAdapter(new AllJobsAdapter(jobList));

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
            Toast.makeText(this, "No internet connection ..", Toast.LENGTH_SHORT).show();
        }

    }

}

