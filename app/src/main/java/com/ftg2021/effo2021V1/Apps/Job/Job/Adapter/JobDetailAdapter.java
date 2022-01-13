package com.ftg2021.effo2021V1.Apps.Job.Job.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.Choose_FindOrPostJob;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.AllJobs;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs.ActivityCompanyMainScreen_PostJobs;
import com.ftg2021.effo2021V1.Apps.Job.Job.Fragment.CompanyFragment;
import com.ftg2021.effo2021V1.Apps.Job.Job.Fragment.JobFragment;
import com.ftg2021.effo2021V1.Authentication.UserData.DatabaseUserData;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.ftg2021.effo2021V1.Util.DataModels.SingleCompanyDataModel;
import com.ftg2021.effo2021V1.Util.DataModels.SingleEmployeeDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class JobDetailAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;

    boolean result;

    public JobDetailAdapter(@NonNull Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;



    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                JobFragment jobFragment = new JobFragment();
                return jobFragment;
            case 1:
                CompanyFragment companyFragment = new CompanyFragment();
                return companyFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

}
