package com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;

public class JobPostHere extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post_here);
        button=findViewById(R.id.PostJobHere);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstraints.jobPostFlow=0;
                Intent intent=new Intent(JobPostHere.this, PostJobDetailStep1.class);
                startActivity(intent);
            }
        });

    }
}