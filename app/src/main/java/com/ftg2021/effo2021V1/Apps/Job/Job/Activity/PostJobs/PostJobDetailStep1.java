package com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs;

import static com.ftg2021.effo2021V1.Util.AppConstraints.categories;
import static com.ftg2021.effo2021V1.Util.AppConstraints.jobLocationList;
import static com.ftg2021.effo2021V1.Util.AppConstraints.jobTitleList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.ftg2021.effo2021V1.Util.DataModels.SingleJobDataModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import kotlinx.coroutines.Job;


public class PostJobDetailStep1 extends AppCompatActivity {

    Button continueButton;
    TextView jobDetailsTextView,tvCall;

    EditText jobTitle, jobLocation, employeeCount;

    CheckBox full, part, internship, office, home;

    boolean validData = false;

    View parentLayout;
    Snackbar snackbar = null;
    Spinner jobTitleSpinner,jobLocationSpinner;
    String JobTitle="",JobLocation="";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);


//        jobTitle = findViewById(R.id.jobTitle);
        jobTitleSpinner = findViewById(R.id.jobTitleSpinner);
//        jobLocation = findViewById(R.id.jobLocation);
        jobLocationSpinner = findViewById(R.id.jobLocationSpinner);
        employeeCount = findViewById(R.id.employeeCount);
        tvCall = findViewById(R.id.tvCall);


        full = findViewById(R.id.fulTime);
        part = findViewById(R.id.partTime);
        internship = findViewById(R.id.internship);
        office = findViewById(R.id.office);
        home = findViewById(R.id.homeCheck);

        jobDetailsTextView=findViewById(R.id.jobDetailsTextView);

        parentLayout = findViewById(R.id.mainLayoutRegisterJobStep1);

        continueButton = findViewById(R.id.continueButton);

        getJobTitleList();
        getJobLocationList();

        tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(tvCall.getText().toString()));
                startActivity(intent);
            }
        });

        jobTitleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString().trim();
                JobTitle = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        jobLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString().trim();
                JobLocation = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jobType = getJobType();
                String jobPlace = getJobPlace();

//                String jobTitleText = jobTitle.getText().toString();
                String jobTitleText = JobTitle;
//                String jobLocationText = jobLocation.getText().toString();
                String jobLocationText = JobLocation;
                int employeeCountNo = 0;

                try {
                    employeeCountNo = Integer.parseInt(employeeCount.getText().toString().trim());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (validateData(jobTitleText, jobLocationText, jobType, jobPlace, employeeCountNo)) {

                    SingleJobDataModel jobDataModel = new SingleJobDataModel();

                    jobDataModel.setJobTitle(jobTitleText);
                    jobDataModel.setLocation(jobLocationText);
                    jobDataModel.setJobType(jobType);
                    jobDataModel.setJobPlace(jobPlace);
                    jobDataModel.setMaxEmployeeCount(employeeCountNo);


                    AppConstraints.currentJobData = jobDataModel;

                    Intent intent = new Intent(PostJobDetailStep1.this, PostJobDetailStep2.class);
                    startActivity(intent);

                }


                Log.e("Data", jobType + " " + jobPlace);

//                Intent intent=new Intent(PostJobDetailStep1.this, PostJobDetailStep2.class);
//                startActivity(intent);
            }
        });


        if (AppConstraints.jobPostFlow == 1) {
            SingleJobDataModel data = AppConstraints.editJobData;

            jobDetailsTextView.setText("Editing Job Details");

//            jobTitle.setText(data.getJobTitle());
            for (int i = 0; i< AppConstraints.jobTitleList.size(); i++){
                if (AppConstraints.jobTitleList.get(i).equalsIgnoreCase(data.getJobTitle())){
                    jobTitleSpinner.setSelection(i);
                } else {

                }
            }
//            jobLocation.setText(data.getLocation());
            for (int i = 0; i< AppConstraints.jobLocationList.size(); i++){
                if (AppConstraints.jobLocationList.get(i).equalsIgnoreCase(data.getLocation())){
                    jobLocationSpinner.setSelection(i);
                } else {

                }
            }
            employeeCount.setText(""+data.getMaxEmployeeCount());

            String jobType = data.getJobType();

            int t1=0, t2=0, t3=0;

            try {
                t1 = Integer.parseInt("" + jobType.charAt(0));
                t2 = Integer.parseInt("" + jobType.charAt(1));
                t3 = Integer.parseInt("" + jobType.charAt(2));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            Log.e("type", "" + t1);

            if (t1 == 1)
                full.setChecked(true);
            if (t2 == 1)
                part.setChecked(true);
            if (t3 == 1)
                internship.setChecked(true);

            String jobPlace = data.getJobPlace();

            int p1=0, p2=0;

            try {
                p1 = Integer.parseInt("" + jobPlace.charAt(0));
                p2 = Integer.parseInt("" + jobPlace.charAt(1));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


            if (p1 == 1)
                home.setChecked(true);
            if (p2 == 1)
                office.setChecked(true);
        }
    }

    private void getJobLocationList() {
        jobLocationList = new ArrayList<>();
        jobLocationList.add("Select Job Location");
        jobLocationList.add("Mumbai");
        jobLocationList.add("Delhi");
        jobLocationList.add("Bangalore");
        jobLocationList.add("Hyderabad");
        jobLocationList.add("Ahmedabad");
        jobLocationList.add("Chennai");
        jobLocationList.add("Kolkata");
        jobLocationList.add("Surat");
        jobLocationList.add("Pune");
        jobLocationList.add("Jaipur");
        jobLocationList.add("Lucknow");
        jobLocationList.add("Kanpur");
        jobLocationList.add("Nagpur");
        jobLocationList.add("Indore");
        jobLocationList.add("Thane");
        jobLocationList.add("Bhopal");
        jobLocationList.add("Pimpri-Chinchwad");
        jobLocationList.add("Patna");
        jobLocationList.add("Vadodara");
        jobLocationList.add("Allahabad");

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,jobLocationList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobLocationSpinner.setAdapter(aa);
    }

    private void getJobTitleList() {
        jobTitleList = new ArrayList<>();
        jobTitleList.add("Select Job Title");
        jobTitleList.add("Account Manager");
        jobTitleList.add("Account Services Executive");
        jobTitleList.add("Accountant");
        jobTitleList.add("Accounts Assistant/ Book Keeper");
        jobTitleList.add("Accounts Head/ GM - Accounts");
        jobTitleList.add("Actuary");
        jobTitleList.add("Admin - Executive");
        jobTitleList.add("Admin - Head/ Manager");
        jobTitleList.add("Advertising - Executive");
        jobTitleList.add("Advertising - Manager");
        jobTitleList.add("Advisory");
        jobTitleList.add("Business Analyst");
        jobTitleList.add("Business Center Manager");
        jobTitleList.add("Business Editor");
        jobTitleList.add("Business Writer");
        jobTitleList.add("Cameraman");
        jobTitleList.add("Cash Management Operations");
        jobTitleList.add("Cash Officer/ Manager");
        jobTitleList.add("Cashier");
        jobTitleList.add("Data Management/ Statistics");
        jobTitleList.add("Database Administrator (DBA)");
        jobTitleList.add("Database Architect/ Designer");
        jobTitleList.add("Datawarehousing Consultants");
        jobTitleList.add("Depository Participant");
        jobTitleList.add("Derivatives Analyst");
    }

    private boolean validateData(String jobTitleText, String jobLocationText, String jobType, String jobPlace, int employeeCountNo) {
        validData = true;


//        if (jobTitleText.isEmpty()) {
//            validData = false;
//            jobTitle.setError("Please Enter Title");
//        }

        if (jobTitleText.equalsIgnoreCase("Select Job Title")){
            validData = false;
//            snackbar = Snackbar.make(parentLayout, "Please Select Job Title", Snackbar.LENGTH_SHORT);
            Toast.makeText(getApplicationContext(),"Please Select Job Title",Toast.LENGTH_SHORT).show();
        }

        if (employeeCountNo == 0) {
            validData = false;
            employeeCount.setError("Invalid count");
        }

//        if (jobLocationText.isEmpty()) {
//            validData = false;
//            jobLocation.setError("Please Enter Location");
//        }

        if (jobLocationText.equalsIgnoreCase("Select Job Location")){
            validData = false;
            Toast.makeText(getApplicationContext(),"Please Select Job Location",Toast.LENGTH_SHORT).show();
        }


        if (jobType.equals("000")) {
            snackbar = Snackbar
                    .make(parentLayout, "Please choose Job Type", Snackbar.LENGTH_SHORT);

            snackbar.show();

            validData = false;

        }

        if (jobPlace.equals("00")) {
            snackbar = Snackbar
                    .make(parentLayout, "Please choose Working Place", Snackbar.LENGTH_SHORT);

            snackbar.show();

            validData = false;

        }


        return validData;
    }

    private String getJobPlace() {

        String place = "";

        if (home.isChecked())
            place += "1";
        else
            place += "0";

        if (office.isChecked())
            place += "1";
        else
            place += "0";

        return place;
    }


    private String getJobType() {
        String type = "";

        if (full.isChecked())
            type += "1";
        else
            type += "0";

        if (part.isChecked())
            type += "1";
        else
            type += "0";

        if (internship.isChecked())
            type += "1";
        else
            type += "0";

        return type;
    }
}