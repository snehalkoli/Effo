package com.ftg2021.effo2021V1.Apps.Job.Job.Fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.ftg2021.effo2021V1.Util.DataModels.SingleEmployeeDataModel;
import com.google.android.material.snackbar.Snackbar;


public class EducationalDetailFragment extends Fragment {

    private Button button;

    EditText education,eduField,university;
    boolean validData;

    public EducationalDetailFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_educational_detail, container, false);
        button = view.findViewById(R.id.eduContinueButton);


        education = view.findViewById(R.id.empEducation);
        eduField = view.findViewById(R.id.empEducationField);
        university = view.findViewById(R.id.empUniversity);

        if(AppConstraints.employeeEditFlow==1)
        {
            SingleEmployeeDataModel _data = AppConstraints.editEmployeeData;

            education.setText(_data.getEducation());
            eduField.setText(_data.getEduField());

            university.setText(_data.getUniversity());

            Log.e("data","okay");

        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String educationTxt = education.getText().toString().trim();
                String educationFieldTxt = eduField.getText().toString().trim();
                String universityTxt = university.getText().toString().trim();

                if(validData(educationTxt,educationFieldTxt,universityTxt))
                {
                    SingleEmployeeDataModel data= AppConstraints.currentEmployeeData;

                    data.setEducation(educationTxt);
                    data.setEduField(educationFieldTxt);
                    data.setUniversity(universityTxt);

                    AppConstraints.currentEmployeeData=data;

                    replaceFragment();
                }
//                replaceFragment();
            }
        });


        return view;
    }

    private boolean validData(String educationTxt, String educationFieldTxt, String universityTxt) {
        validData=true;


        if (educationTxt.isEmpty()) {
            education.setError("Please enter education");
            validData = false;
        }
        if (educationFieldTxt.isEmpty()) {
            eduField.setError("Please enter data");
            validData = false;
        }

        if (universityTxt.isEmpty()) {
            university.setError("Please enter university");
            validData = false;
        }

        return validData;

    }

    public void replaceFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ProfessionalDetailsFragment llf = new ProfessionalDetailsFragment();
        ft.replace(R.id.framelayout, llf, TAG);
        ft.addToBackStack(TAG);
        ft.commit();
    }
}