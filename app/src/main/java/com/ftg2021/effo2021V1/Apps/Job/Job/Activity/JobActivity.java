package com.ftg2021.effo2021V1.Apps.Job.Job.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.JobDetails;
import com.ftg2021.effo2021V1.R;


public class JobActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        imageView = findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobActivity.this, JobDetails.class);
                startActivity(intent);
            }
        });


//       getSupportFragmentManager().beginTransaction().add(R.id.framelayout, new JobFindAndPostFragment()).commit();
    }
}