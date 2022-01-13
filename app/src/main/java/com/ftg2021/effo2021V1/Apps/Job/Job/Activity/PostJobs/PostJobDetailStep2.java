package com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs;

import static com.ftg2021.effo2021V1.Util.AppConstraints.jobLocationList;
import static com.ftg2021.effo2021V1.Util.AppConstraints.jobQualificationList;
import static com.ftg2021.effo2021V1.Util.AppConstraints.jobTitleList;
import static com.ftg2021.effo2021V1.Util.AppConstraints.workExperienceList;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ftg2021.effo2021V1.AppList.AppList;
import com.ftg2021.effo2021V1.Apps.Job.Job.Fragment.JobPostedFragment;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.ftg2021.effo2021V1.Util.DataModels.SingleJobDataModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class PostJobDetailStep2 extends AppCompatActivity {

    Button continueButton;
    EditText jobDescription, jobQualification, jobExperience, minSalary, maxSalary,etjobQualification;
    Spinner jobQualificationSpinner,workExperienceSpinner;
    String JobQualification="",workExperience="";
    boolean validData = false;
    ProgressDialog progressDialog;
    RadioButton rbExcellent,rbGood;
    String communicationSkill ="";
    RadioGroup radioGroup;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_description);


        jobDescription = findViewById(R.id.jobDescription);
//        jobQualification = findViewById(R.id.jobQualification);
        etjobQualification = findViewById(R.id.etjobQualification);
        jobQualificationSpinner = findViewById(R.id.jobQualificationSpinner);
        workExperienceSpinner = findViewById(R.id.workExperienceSpinner);
//        jobExperience = findViewById(R.id.jobExperience);
        minSalary = findViewById(R.id.salaryFrom);
        maxSalary = findViewById(R.id.salaryTo);
        rbExcellent = findViewById(R.id.radioButtonExcellent);
        rbGood = findViewById(R.id.radioButtonGood);
        radioGroup = findViewById(R.id.radioGroup);

        continueButton = findViewById(R.id.continueButton);
        progressDialog = new ProgressDialog(PostJobDetailStep2.this);

//        getJobQualificationList();

        if (jobQualificationList.size()==0){
            getJobQualificationList();
        } else {
//            List<String> listDistinct = jobQualificationList.stream().distinct().collect(Collectors.toList());
//            jobQualificationList = listDistinct;
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,jobQualificationList);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            jobQualificationSpinner.setAdapter(aa);
        }

        if (workExperienceList.size()==0){
            getWorkExperienceList();
        } else {
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,workExperienceList);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            workExperienceSpinner.setAdapter(aa);
        }


        jobQualificationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString().trim();
                if (item.equalsIgnoreCase("Other")){
                    etjobQualification.setVisibility(View.VISIBLE);
                    JobQualification = item;
                } else {
                    etjobQualification.setVisibility(View.GONE);
                    JobQualification = item;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        workExperienceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString().trim();
                workExperience = item;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rbExcellent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    communicationSkill = "Excellent";
                }
            }
        });

        rbGood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    communicationSkill = "Good";
                }
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jobDescriptionText = jobDescription.getText().toString().trim();
//                String jobQualificationText = jobQualification.getText().toString().trim();


                String jobQualificationText = JobQualification;
//                String jobExperienceText = jobExperience.getText().toString().trim();
                String jobExperienceText = workExperience;
                String minSalaryText = minSalary.getText().toString().trim();
                String maxSalaryText = maxSalary.getText().toString().trim();
//                String jobQualificationText="";
//
//                if (etjobQualification.getVisibility()==View.VISIBLE && !etjobQualification.getText().toString().isEmpty()){
//                    jobQualificationText = etjobQualification.getText().toString().trim();
//                } else {
//                    jobQualificationText = JobQualification;
//                }

                if (validateData(jobDescriptionText, jobQualificationText, jobExperienceText, minSalaryText, maxSalaryText,radioGroup)) {
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    SingleJobDataModel jobDataModel = AppConstraints.currentJobData;

                    jobDataModel.setJobDescription(jobDescriptionText);
                    jobDataModel.setQualification(jobQualificationText);
                    jobDataModel.setExperience(jobExperienceText);
                    jobDataModel.setSalary("" + minSalaryText + "-" + maxSalaryText);
                    jobDataModel.setCommunicationSkill(communicationSkill);

                    AppConstraints.currentJobData = jobDataModel;

                    int companyId = AppConstraints.currentCompanyData.getId();

                    String date = AppConstraints.getCurrentDate();


                    if (etjobQualification.getVisibility()==View.VISIBLE
                            && !etjobQualification.getText().toString().isEmpty())
                    {
//                       jobQualificationList.add(etjobQualification.getText().toString().trim());
//                        AppConstraints.otherEditTextData = etjobQualification.getText().toString().trim();
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString("name", etjobQualification.getText().toString().trim());
                        myEdit.commit();
                    }

                    String data ;

                    if(AppConstraints.jobPostFlow==1) {
                        data = "{\"query\":\"UPDATE `job_jobs_posted` SET`title`='" + jobDataModel.getJobTitle() + "',`description`='"+jobDataModel.getJobDescription() +"'," +
                                "`type`='" + jobDataModel.getJobType() + "',`place`='" + jobDataModel.getJobPlace() + "'," +
                                "`employeeCount`='"+ jobDataModel.getMaxEmployeeCount() + "',`qualification`='"+ jobDataModel.getQualification() +"'," +
                                "`experience`='" + jobDataModel.getExperience() + "',`communicationSkill`='" + jobDataModel.getCommunicationSkill() + "',`salary`='" + jobDataModel.getSalary() +"'," +
                                "`location`='"+jobDataModel.getLocation()+"' WHERE id="+AppConstraints.editJobData.getId()+"\"}";
                    }else
                        data="{\"query\":\"INSERT INTO `job_jobs_posted`" +
                            "(`companyId`, `title`, `description`, `type`, `place`, `employeeCount`, `qualification`, `experience`, `salary`, `communicationSkill`, `status`,`datePosted`,`location`) " +
                            "VALUES " +
                            "('" + companyId + "','" + jobDataModel.getJobTitle() + "','" + jobDataModel.getJobDescription() + "'," +
                            "'" + jobDataModel.getJobType() + "','" + jobDataModel.getJobPlace() + "'," +
                            "'" + jobDataModel.getMaxEmployeeCount() + "','" + jobDataModel.getQualification() + "','" + jobDataModel.getExperience() + "','" + jobDataModel.getSalary() + "','" + jobDataModel.getCommunicationSkill() + "'," +
                            "'1','" + date + "','" + jobDataModel.getLocation() + "')\"}";


                    postJob(data);

                }


            }
        });


        if (AppConstraints.jobPostFlow == 1) {
            SingleJobDataModel data = AppConstraints.editJobData;

            jobDescription.setText(data.getJobDescription());
//            jobQualification.setText(data.getQualification());

            try {
                for (int i = 0; i< AppConstraints.jobQualificationList.size(); i++){
                    if (jobQualificationList.get(i).equalsIgnoreCase(data.getQualification())){
                        jobQualificationSpinner.setSelection(i);

                        @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_APPEND);
                        String s1 = sh.getString("name", "");
                        if (s1!=null && s1!="" && jobQualificationSpinner.getSelectedItem().toString().equalsIgnoreCase("Other")){
                            etjobQualification.setVisibility(View.VISIBLE);
                            etjobQualification.setText(s1);
                        }
                    } else {

                    }
                }
            }catch (Exception e){
            }
//            jobExperience.setText(data.getExperience());
            for (int i=0;i< workExperienceList.size();i++){
                if (workExperienceList.get(i).toString().equalsIgnoreCase(data.getExperience())) {
                    workExperienceSpinner.setSelection(i);
                }
            }

            if (data.getCommunicationSkill().equalsIgnoreCase("Excellent")){
                rbExcellent.setChecked(true);
            } else  if (data.getCommunicationSkill().equalsIgnoreCase("Good")){
                rbGood.setChecked(true);
            }
            String salary[] = new String[2]; //0=min salary 1=max salary


            try {

                salary = data.getSalary().split("-");

                minSalary.setText("" + salary[0]);
                maxSalary.setText("" + salary[1]);
            } catch (Exception e) {
            }
            continueButton.setText("Update");
        }
    }

    private void getWorkExperienceList() {
        workExperienceList = new ArrayList<>();
        workExperienceList.add(0);
        workExperienceList.add(1);
        workExperienceList.add(2);
        workExperienceList.add(3);
        workExperienceList.add(4);
        workExperienceList.add(5);
        workExperienceList.add(6);
        workExperienceList.add(7);
        workExperienceList.add(8);
        workExperienceList.add(9);
        workExperienceList.add(10);
        workExperienceList.add(11);
        workExperienceList.add(12);
        workExperienceList.add(13);
        workExperienceList.add(14);
        workExperienceList.add(15);
        workExperienceList.add(16);
        workExperienceList.add(17);
        workExperienceList.add(18);
        workExperienceList.add(19);
        workExperienceList.add(20);
        workExperienceList.add(21);
        workExperienceList.add(22);
        workExperienceList.add(23);
        workExperienceList.add(24);
        workExperienceList.add(25);
        workExperienceList.add(26);
        workExperienceList.add(27);
        workExperienceList.add(28);
        workExperienceList.add(29);
        workExperienceList.add(30);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,workExperienceList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workExperienceSpinner.setAdapter(aa);
    }

    private void getJobQualificationList() {
        jobQualificationList = new ArrayList<>();
        jobQualificationList.add("Select Job Qualification");
        jobQualificationList.add("10th");
        jobQualificationList.add("12th");
        jobQualificationList.add("Graduation");
        jobQualificationList.add("Post Graduation");
        jobQualificationList.add("BA");
        jobQualificationList.add("BSC");
        jobQualificationList.add("B.Com");
        jobQualificationList.add("BE/BTech");
        jobQualificationList.add("BBA");
        jobQualificationList.add("BMS");
        jobQualificationList.add("BFA");
        jobQualificationList.add("BEM");
        jobQualificationList.add("BFD");
        jobQualificationList.add("Other");

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,jobQualificationList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobQualificationSpinner.setAdapter(aa);
    }

    private boolean validateData(String jobDescriptionText, String jobQualificationText, String jobExperienceText, String minSalaryText, String maxSalaryText, RadioGroup radioGroup) {

        validData = true;

        if (jobDescriptionText.isEmpty()) {
            validData = false;
            jobDescription.setError("Please Enter Description");
        }

//        if (jobQualificationText.isEmpty()) {
//            validData = false;
//            jobQualification.setError("Please Enter Qualification");
//        }

        if (jobQualificationText.equalsIgnoreCase("Select Job Qualification")) {
            validData = false;
            Toast.makeText(getApplicationContext(),"Select Job Qualification",Toast.LENGTH_SHORT).show();
        }

        if (jobExperienceText.isEmpty()) {
            validData = false;
            jobExperience.setError("Please Enter Experience Required");
        }


        if (minSalaryText.isEmpty()) {
            validData = false;
            minSalary.setError("Please enter minimum salary");
        }


        if (maxSalaryText.isEmpty()) {
            validData = false;
            maxSalary.setError("Please enter maximum salary");
        }

        if (!maxSalaryText.isEmpty() && !minSalaryText.isEmpty()) {
            float salaryFrom = 0.0F;
            float salaryTo = 0.0F;

            try {
                salaryFrom = Float.parseFloat(minSalaryText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            try {
                salaryTo = Float.parseFloat(maxSalaryText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (salaryFrom > salaryTo) {
                maxSalary.setError("Please enter greater or equal value from minimum salary");
            }
        }

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            validData = false;
            Toast.makeText(getApplicationContext(),"Please Select Communication Skill",Toast.LENGTH_SHORT).show();
        }

        return validData;
    }


    public void postJob(String data) {

        Log.e("data", data);
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        Log.e("Effo", " " + data);

        try {

            final String savedata = data;

            String URL = AppConstraints.dynamicApiUrl;

            //Toast.makeText(getContext(),"Fetching cats"+URL, Toast.LENGTH_SHORT).show();

            requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        //Toast.makeText(getContext(),"Fetching cats data", Toast.LENGTH_SHORT).show();

                        Log.e("status = Response", response);

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            Log.e("status 5", e.getMessage());
                        }


                        int affectedRows = jsonObject.getInt("affectedRows");

                        if (affectedRows == 1) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(PostJobDetailStep2.this,ActivityCompanyMainScreen_PostJobs.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
//                            startActivity(new Intent(PostJobDetailStep2.this, ActivityCompanyMainScreen_PostJobs.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Error Creating Account! Try Again", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
                        Log.d("status 1", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
                    Log.i("status 2", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return savedata == null ? null : savedata.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        //Log.v("Unsupported Encoding while trying to get the bytes", data);
                        return null;
                    }
                }
            };
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            progressDialog.dismiss();
            Log.d("ERROR", e.getMessage());
            Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
        }
    }
}