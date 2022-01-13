package com.ftg2021.effo2021V1.Apps.Job.Job.Fragment;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.ftg2021.effo2021V1.Util.DataModels.SingleEmployeeDataModel;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class PersonalDetailFragment extends Fragment {

    final Calendar myCalendar = Calendar.getInstance();
    EditText name, dob, email;
    RadioButton radioM, radioF, radioO;
    TextView chooseDate;
    boolean validData;
    int gender = -1;//0=M 1=F
    LinearLayout parentLayout;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_detail, container, false);
        button = view.findViewById(R.id.empContinueButton);


        name = view.findViewById(R.id.empName);
        dob = view.findViewById(R.id.empDOB);
        email = view.findViewById(R.id.empEmail);

        radioM = view.findViewById(R.id.genderM);
        radioF = view.findViewById(R.id.genderF);
        radioO = view.findViewById(R.id.genderO);

        chooseDate = view.findViewById(R.id.chooseDate);
        parentLayout = view.findViewById(R.id.layoutPD1);


        radioM.setChecked(false);
        radioF.setChecked(false);
        radioO.setChecked(false);




        if(AppConstraints.employeeEditFlow==1)
        {
            SingleEmployeeDataModel data = AppConstraints.editEmployeeData;

            name.setText(data.getName());
            dob.setText(data.getDateOfBirth());

            email.setText(data.getEmail());

            if(data.getGender()==1) {
                radioF.setChecked(true);
                gender=1;
            } else if (data.getGender()==2){
                radioO.setChecked(true);
                gender=2;
            }
            else {
                radioM.setChecked(true);
                gender=0;
            }
        }



        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }

        };

        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }
        });


        radioM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioF.setChecked(false);
                radioO.setChecked(false);
                gender = 0;
            }
        });

        radioF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioM.setChecked(false);
                radioO.setChecked(false);
                gender = 1;
            }
        });

        radioO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioM.setChecked(false);
                radioF.setChecked(false);
                gender = 2;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameTxt = name.getText().toString().trim();
                String dobTxt = dob.getText().toString().trim();
                String emailTxt = email.getText().toString().trim();


                if (validData(nameTxt, dobTxt, emailTxt)) {

                    SingleEmployeeDataModel data=new SingleEmployeeDataModel();

                    data.setName(nameTxt);
                    data.setDateOfBirth(dobTxt);
                    data.setEmail(emailTxt);
                    data.setGender(gender);

                    AppConstraints.currentEmployeeData=data;

                    replaceFragment();
                }
//                replaceFragment();
            }
        });
        return view;
    }

    private boolean validData(String nameTxt, String dobTxt, String emailTxt) {
        validData = true;

        if (nameTxt.isEmpty()) {
            name.setError("Please enter name");
            validData = false;
        }
        if (dobTxt.isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(parentLayout, "Please choose Date of Birth", Snackbar.LENGTH_SHORT);

            snackbar.show();
            validData = false;
        }

        if (emailTxt.isEmpty()) {
            email.setError("Please enter email");
            validData = false;
        }

        if (gender == -1) {

            Snackbar snackbar = Snackbar
                    .make(parentLayout, "Please choose Gender", Snackbar.LENGTH_SHORT);

            snackbar.show();

            validData=false;
        }

        return validData;
    }


    public void replaceFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        EducationalDetailFragment llf = new EducationalDetailFragment();
        ft.replace(R.id.framelayout, llf, TAG);
        ft.addToBackStack(TAG);
        ft.commit();
    }

    private void updateDateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        dob.setText(dateFormat.format(myCalendar.getTime()));
    }


}