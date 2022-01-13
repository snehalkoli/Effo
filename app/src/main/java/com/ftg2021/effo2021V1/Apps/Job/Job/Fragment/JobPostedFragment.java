package com.ftg2021.effo2021V1.Apps.Job.Job.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs.ActivityCompanyMainScreen_PostJobs;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs.PostJobDetailStep2;
import com.ftg2021.effo2021V1.Apps.Job.Job.Adapter.JobPostedAdapter;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.ftg2021.effo2021V1.Util.DataModels.SingleJobDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class JobPostedFragment extends Fragment implements JobPostedAdapter.JobFragmentCallBack {


    boolean result;
    TextView noJobsFound;

    RecyclerView recyclerViewJobPostedList;
    List<SingleJobDataModel> jobList = new ArrayList<>();
    SearchView searchView;
    JobPostedAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_job_posted, container, false);

        noJobsFound = v.findViewById(R.id.noJobsFound);
        searchView = v.findViewById(R.id.search);
        noJobsFound.setText("Fetching Jobs ! Please Wait ...");

        fetchAllJobs(v);

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

        return v;
    }

    private void fetchAllJobs(View view) {
        result = false;

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());


        String data = "{\"query\":\"select * from job_jobs_posted where companyId='" + AppConstraints.currentCompanyData.getId() + "'\"}";
        Log.e("MukeshMack", " " + data);

        try {

            final String savedata = data;

            String URL = AppConstraints.dynamicApiUrl;

            //Toast.makeText(getContext(),"Fetching cats"+URL, Toast.LENGTH_SHORT).show();

            requestQueue = Volley.newRequestQueue(view.getContext());
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
                            noJobsFound.setVisibility(View.VISIBLE);
                            noJobsFound.setText("No Job Posted Yet.\nStart Posting Jobs by clicking on Post Jobs");
                        } else
                            noJobsFound.setVisibility(View.GONE);


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectUser = jsonArray.getJSONObject(i);

                            int id = jsonObjectUser.getInt("id");
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


                            jobList.add(new SingleJobDataModel(id, title, description, type, place, employeeCount, qualification, experience, salary, status, datePosted, location,communicationSkill));

                        }


                        recyclerViewJobPostedList = view.findViewById(R.id.recyclerViewJobPosted);
                        recyclerViewJobPostedList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                        recyclerViewJobPostedList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                        adapter = new JobPostedAdapter(jobList,JobPostedFragment.this);
                        recyclerViewJobPostedList.setAdapter(adapter);
//                        recyclerViewJobPostedList.setAdapter(new JobPostedAdapter(jobList));

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
                        Log.d("status 1", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void getCallBack(SingleJobDataModel jobDataModel,int status) {
       String data = "{\"query\":\"UPDATE `job_jobs_posted` SET`title`='" + jobDataModel.getJobTitle() + "',`description`='"+jobDataModel.getJobDescription() +"'," +
                "`type`='" + jobDataModel.getJobType() + "',`status`='" + status+ "',`place`='" + jobDataModel.getJobPlace() + "'," +
                "`employeeCount`='"+ jobDataModel.getMaxEmployeeCount() + "',`qualification`='"+ jobDataModel.getQualification() +"'," +
                "`experience`='" + jobDataModel.getExperience() + "',`salary`='" + jobDataModel.getSalary() +"'," +
                "`location`='"+jobDataModel.getLocation()+"' WHERE id="+jobDataModel.getId()+"\"}";

        postJob(data);
    }

    private void postJob(String data) {
        Log.e("data", data);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        Log.e("Effo", " " + data);
        try {
            final String savedata = data;
            String URL = AppConstraints.dynamicApiUrl;
            //Toast.makeText(getContext(),"Fetching cats"+URL, Toast.LENGTH_SHORT).show();

            requestQueue = Volley.newRequestQueue(getContext());
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
                            Toast.makeText(getContext(), "Status Updated Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error Creating Account! Try Again", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        Toast.makeText(getContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
                        Log.d("status 1", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
        }
    }
}