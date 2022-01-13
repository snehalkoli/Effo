package com.ftg2021.effo2021V1.Apps.Job.Job.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.CreateEmployeeProfileActivity;
import com.ftg2021.effo2021V1.R;


public class CreateProfile extends AppCompatActivity {

    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        button=findViewById(R.id.createProfile);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateProfile.this, CreateEmployeeProfileActivity.class);
                startActivity(intent);

            }
        });

        //getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,new JobPostFragment()).commit();


    }
}