package com.ftg2021.effo2021V1.Apps.Job.Job.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.ftg2021.effo2021V1.Util.DataModels.SingleCompanyDataModel;
import com.ftg2021.effo2021V1.Util.DataModels.SingleJobDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class CompanyFragment extends Fragment {


    TextView name,type,date,website,email;

    boolean result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_company, container, false);

        name=view.findViewById(R.id.comName);
        type=view.findViewById(R.id.comType);
        date=view.findViewById(R.id.comDateFounded);
        website=view.findViewById(R.id.comWebsite);
        email=view.findViewById(R.id.comEmail);

        getCompanyInformation();

        return view;
    }


    private void getCompanyInformation() {

        result = false;

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());


        String data = "{\"query\":\"select * from job_company where id=" + AppConstraints.viewJobData.getCompanyId() + "\"}";

        Log.e("data",data);

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
                            String type = jsonObjectUser.getString("type");
                            String date_established = jsonObjectUser.getString("date_established");
                            String website = jsonObjectUser.getString("website");
                            String company_document = jsonObjectUser.getString("company_document");
                            String companyImage = jsonObjectUser.getString("companyImage");
                            String email = jsonObjectUser.getString("email");

                            AppConstraints.viewCompanyData= new SingleCompanyDataModel(id, name, type, date_established, website, company_document, companyImage, email);




                        } else
                            result = false;

                        if (result)
                        {
                            SingleCompanyDataModel cdata=AppConstraints.viewCompanyData;

                            name.setText(cdata.getName());
                            type.setText(cdata.getType());
                            date.setText(cdata.getDate());
                            website.setText(cdata.getWebsite());
                            email.setText(cdata.getEmail());
                        }

                        //redirect to if result true= homepage | false = PersonalDetailsActivity



                        Log.e("status data", "Found" + jsonArray.length() + result);


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