package com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ftg2021.effo2021V1.Apps.Job.Job.Fragment.PersonalDetailFragment;
import com.ftg2021.effo2021V1.R;


public class CreateEmployeeProfileActivity extends AppCompatActivity {

   // Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile1);


       getSupportFragmentManager().beginTransaction().add(R.id.framelayout,new PersonalDetailFragment()).commit();


    }
}