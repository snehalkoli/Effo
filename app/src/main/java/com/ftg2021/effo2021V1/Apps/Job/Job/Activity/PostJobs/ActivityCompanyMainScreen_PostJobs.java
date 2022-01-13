package com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ftg2021.effo2021V1.AppList.AppList;
import com.ftg2021.effo2021V1.Apps.Job.Job.Fragment.ApplicationReceivedFragment;
import com.ftg2021.effo2021V1.Apps.Job.Job.Fragment.CompanyProfileFragment;
import com.ftg2021.effo2021V1.Apps.Job.Job.Fragment.JobPostedFragment;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class ActivityCompanyMainScreen_PostJobs extends AppCompatActivity {

    Button button;

   BottomNavigationView bottomNavigationView;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(ActivityCompanyMainScreen_PostJobs.this, AppList.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post);

        button=findViewById(R.id.PostJobHere);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstraints.jobPostFlow=0;
                Intent intent=new Intent(ActivityCompanyMainScreen_PostJobs.this, JobPostHere.class);
                startActivity(intent);
            }
        });

        bottomNavigationView=findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.Framelayout,new JobPostedFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.JobPosted);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment=null;
                switch (item.getItemId()){
                    case R.id.JobPosted:

                        fragment=new JobPostedFragment();
                        break;
                    case R.id.ApplicationReceived:
                        fragment=new ApplicationReceivedFragment();
                        break;
                    case R.id.Company:
                        fragment=new CompanyProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.Framelayout,fragment).commit();
                return true;

            }
        });

    }
}