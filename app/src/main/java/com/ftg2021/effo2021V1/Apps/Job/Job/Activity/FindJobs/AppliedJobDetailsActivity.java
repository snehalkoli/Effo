package com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.ftg2021.effo2021V1.Apps.News.Model.AppliedJobsModel;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;

public class AppliedJobDetailsActivity extends AppCompatActivity {
    TextView role,description,location,salary,workFrom,qualification,experience,companyName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_job_details);

        role=findViewById(R.id.txtJobRole);
        description=findViewById(R.id.txtJobDescription);
        location=findViewById(R.id.txtJobLocation);
        salary=findViewById(R.id.txtJobSalary);
        workFrom=findViewById(R.id.txtJobWorkFrom);
        qualification=findViewById(R.id.txtJobQualification);
        experience=findViewById(R.id.txtJobExperience);
        companyName = findViewById(R.id.txtCompanyName);

        AppliedJobsModel data= AppConstraints.viewAppliedJobsData;

        role.setText(data.getJobTitle());
        description.setText(data.getJobDescription());
        location.setText(data.getLocation());
        salary.setText(data.getSalary());
        qualification.setText(data.getQualification());
        experience.setText(data.getExperience());
        workFrom.setText(data.getJobPlace());
        companyName.setText(data.getCompanyName());
    }
}
