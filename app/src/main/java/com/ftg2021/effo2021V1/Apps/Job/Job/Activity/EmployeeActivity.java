package com.ftg2021.effo2021V1.Apps.Job.Job.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs.CompanyProfile;
import com.ftg2021.effo2021V1.R;


public class EmployeeActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);


        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeActivity.this, CompanyProfile.class);
                startActivity(intent);
            }
        });



    }



}