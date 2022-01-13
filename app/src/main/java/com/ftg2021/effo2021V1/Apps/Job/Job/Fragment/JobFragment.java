package com.ftg2021.effo2021V1.Apps.Job.Job.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.ftg2021.effo2021V1.Util.DataModels.SingleJobDataModel;


public class JobFragment extends Fragment {


    TextView role,description,location,salary,workFrom,qualification,experience,jobDetails;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_job, container, false);


        //role=view.findViewById(R.id.txtJobRole);
        description=view.findViewById(R.id.txtJobDescription);
        location=view.findViewById(R.id.txtJobLocation);
        salary=view.findViewById(R.id.txtJobSalary);
        workFrom=view.findViewById(R.id.txtJobWorkFrom);
        qualification=view.findViewById(R.id.txtJobQualification);
        experience=view.findViewById(R.id.txtJobExperience);
        jobDetails = view.findViewById(R.id.txtJobDetails);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            SingleJobDataModel data= AppConstraints.viewJobData;

           // role.setText(data.getJobTitle());
            jobDetails.setText(data.getJobTitle());
            description.setText(data.getJobDescription());
            location.setText(data.getLocation());
            salary.setText(data.getSalary());
            qualification.setText(data.getQualification());
            experience.setText(data.getExperience());

            String workFromTxt="";
            if(data.getJobPlace().equals("10"))
                workFromTxt="Home";
            else
            if(data.getJobPlace().equals("01"))
                workFromTxt="Office";

            workFrom.setText(workFromTxt);
            progressDialog.dismiss();
        }catch (Exception e){
            Log.e("JobDetailException",""+e.toString());
        }
        return view;
    }
}