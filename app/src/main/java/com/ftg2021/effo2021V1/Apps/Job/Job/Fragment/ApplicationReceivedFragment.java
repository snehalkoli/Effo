package com.ftg2021.effo2021V1.Apps.Job.Job.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ftg2021.effo2021V1.Apps.Job.Job.Adapter.ApplicationReceivedAdapter;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.ftg2021.effo2021V1.Util.DataModels.SingleApplicationReceived;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class ApplicationReceivedFragment extends Fragment {


    RecyclerView recyclerViewApplications;

    List<String> name;
    List<String> designation;
    List<String> experience;
    List<String> salary;

    List<SingleApplicationReceived> applicationList;
    boolean result;


    ApplicationReceivedAdapter adapter;
    SearchView searchView;

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
        View view = inflater.inflate(R.layout.fragment_application_received, container, false);

        recyclerViewApplications = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.search);

        fetchApplicationsReceived(view);

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

        return view;
    }

    private void fetchApplicationsReceived(View view) {
        result = false;

        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());


        String data = "{\"query\":\"select e.name,j.experience ,j.title,a.* FROM job_employees e,job_jobs_posted j,job_jobs_applied a WHERE e.id=a.employee_id AND j.id=a.job_id AND j.companyId=" + AppConstraints.currentCompanyData.getId() + "\"}";
//        String data = "{\"query\":\"select e.name,j.experience ,j.title,a.* FROM job_employees e,job_jobs_posted j,job_jobs_applied a WHERE a.status=1 AND e.id=a.employee_id AND j.id=a.job_id AND j.companyId=" + AppConstraints.currentCompanyData.getId() + "\"}";

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


//                        if (jsonArray.length() == 0) {
//                            noJobsFound.setVisibility(View.VISIBLE);
//                            noJobsFound.setText("No Job Posted Yet.\nStart Posting Jobs by clicking on Post Jobs");
//                        } else
//                            noJobsFound.setVisibility(View.GONE);


                        applicationList = new ArrayList<>();
                        applicationList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectUser = jsonArray.getJSONObject(i);

                            int id = jsonObjectUser.getInt("id");
                            int employee_id = jsonObjectUser.getInt("employee_id");
                            int job_id = jsonObjectUser.getInt("job_id");
                            String empName = jsonObjectUser.getString("name");
                            String jobTitle = jsonObjectUser.getString("title");
                            String experience = jsonObjectUser.getString("experience");
                            String date = jsonObjectUser.getString("date");
                            int status = jsonObjectUser.getInt("status");

                            applicationList.add(new SingleApplicationReceived(id, employee_id, job_id, empName, jobTitle, experience, date, status));

                        }


                        recyclerViewApplications.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                        recyclerViewApplications.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                        adapter = new ApplicationReceivedAdapter(applicationList,getContext());
                        recyclerViewApplications.setAdapter(adapter);
//                        recyclerViewApplications.setAdapter(new ApplicationReceivedAdapter(applicationList));

                    } catch (Exception e) {
                        //Toast.makeText(getContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
                        Log.d("status 1", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                  //  Toast.makeText(getContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
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
          //  Toast.makeText(getContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
        }

    }
}