package com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ftg2021.effo2021V1.Apps.Job.Job.Adapter.AppliedJobsAdapter;
import com.ftg2021.effo2021V1.Apps.News.Model.AppliedJobsModel;
import com.ftg2021.effo2021V1.R;
import java.util.ArrayList;
import java.util.List;

public class AppliedJobsActivity extends AppCompatActivity implements AppliedJobsAdapter.AdapterCallBack {
    SearchView searchView;
    RecyclerView recyclerView;
    AppliedJobsModel model;
    List<AppliedJobsModel> list;
    AppliedJobsAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_applied_jobs);
        recyclerView=findViewById(R.id.recyclerViewAllAppliedJobs);
        searchView=findViewById(R.id.search);

        progressDialog = new ProgressDialog(AppliedJobsActivity.this);

        loadAllAppliedJobs();
    }

    private void loadAllAppliedJobs() {
        list = new ArrayList<>();
        model = new AppliedJobsModel();
        model.setCompanyName("Biyani Technologies");
        model.setJobTitle("Android Developer");
        model.setJobDescription("We are hiring android developer with minimum 2 years of experience");
        model.setQualification("BE");
        model.setExperience("2");
        model.setLocation("Pune");
        model.setSalary("20000");
        model.setJobPlace("Work from home");
        list.add(model);

        model = new AppliedJobsModel();
        model.setCompanyName("Reapmind Innovation");
        model.setJobTitle("Android Developer");
        model.setJobDescription("We are hiring android developer with minimum 2 years of experience");
        model.setQualification("BE");
        model.setExperience("2");
        model.setLocation("Pune");
        model.setSalary("25000");
        model.setJobPlace("Work from home");
        list.add(model);

        model = new AppliedJobsModel();
        model.setCompanyName("Softcell");
        model.setJobTitle("Web developer");
        model.setJobDescription("We are hiring Web developer with minimum 2 years of experience");
        model.setQualification("BE");
        model.setExperience("2");
        model.setLocation("Pune");
        model.setSalary("25000");
        model.setJobPlace("Work from Office");
        list.add(model);

        model = new AppliedJobsModel();
        model.setCompanyName("5 TechG");
        model.setJobTitle("Android developer");
        model.setJobDescription("We are hiring Android developer with minimum 2 years of experience");
        model.setQualification("BE");
        model.setExperience("2");
        model.setLocation("Pune");
        model.setSalary("25000");
        model.setJobPlace("Work from Home");
        list.add(model);

        model = new AppliedJobsModel();
        model.setCompanyName("Infosys");
        model.setJobTitle("React developer");
        model.setJobDescription("We are hiring React developer with minimum 2 years of experience");
        model.setQualification("BE");
        model.setExperience("2");
        model.setLocation("Pune");
        model.setSalary("50000");
        model.setJobPlace("Work from Home");
        list.add(model);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        adapter = new AppliedJobsAdapter(list,AppliedJobsActivity.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void getRevokeData(AppliedJobsModel data) {
        list.remove(data);

        progressDialog.setMessage("Refreshing..please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        adapter = new AppliedJobsAdapter(list,AppliedJobsActivity.this);
        recyclerView.setAdapter(adapter);
        progressDialog.dismiss();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(AppliedJobsActivity.this,AllJobs.class);
//        startActivity(intent);
//    }
}
