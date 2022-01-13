package com.ftg2021.effo2021V1;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.ftg2021.effo2021V1.AppList.AppList;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.AllJobs;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.CreateEmployeeProfileActivity;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.EmployeeProfile;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs.CompanyProfile;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs.PostJobDetailStep1;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs.PostJobDetailStep2;
import com.ftg2021.effo2021V1.Apps.Job.Job.Fragment.ProfessionalDetailsFragment;
import com.ftg2021.effo2021V1.Apps.maintainance.Activity.ChooseMaintananceUser;
import com.ftg2021.effo2021V1.Apps.maintainance.Activity.Vendor_SelectProfession;
import com.ftg2021.effo2021V1.Authentication.UserData.FirebaseUserData;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    public static int width,height;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                // Initialize Firebase Auth
                mAuth = FirebaseAuth.getInstance();

                FirebaseUserData.firebaseUser=mAuth.getCurrentUser();

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                height = displayMetrics.heightPixels;
                width = displayMetrics.widthPixels;

//                Intent i = new Intent(SplashScreen.this, Vendor_SelectProfession.class);
                Intent i = new Intent(SplashScreen.this, AppList.class);
//                Intent i = new Intent(SplashScreen.this, CompanyProfile.class);
//                Intent i = new Intent(SplashScreen.this, AllJobs.class);
                startActivity(i);
                finish();
            }
        }, 0);
    }
}