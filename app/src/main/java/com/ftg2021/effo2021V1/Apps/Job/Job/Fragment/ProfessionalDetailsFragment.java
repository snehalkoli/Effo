package com.ftg2021.effo2021V1.Apps.Job.Job.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.AllJobs;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.OpenPdfActivity;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs.CompanyProfile;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs.VolleyMultipartRequest;
import com.ftg2021.effo2021V1.Authentication.UserData.DatabaseUserData;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.ftg2021.effo2021V1.Util.DataModels.SingleEmployeeDataModel;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs.VolleyMultipartRequest.DataPart;


public class ProfessionalDetailsFragment extends Fragment {


    private static final int RESULT_CANCELED = 0;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static final int RESULT_OK = -1;
    RadioButton radioFresh, radioExp;
    EditText experience, jobTitle, companyName;
    LinearLayout toggleLayout, parentLayout;
    boolean validData;
    int isExperienced = -1;
    private Button button,btnUploadResume;
    TextView tvResumePath;
    String resumePath="";
    String filePath = "";
    String freshersFilePath = "";
    ProgressDialog progressDialog;
//    Button btnViewResume;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_professional_details, container, false);
        button = view.findViewById(R.id.professional);
        btnUploadResume = view.findViewById(R.id.btnUploadResume);
//        btnViewResume = view.findViewById(R.id.btnViewResume);
        tvResumePath = view.findViewById(R.id.tvResumePath);

        experience = view.findViewById(R.id.empExperience);
        jobTitle = view.findViewById(R.id.empJobTitle);
        companyName = view.findViewById(R.id.empCompanyName);

        toggleLayout = view.findViewById(R.id.toggleFresherOrExperiencedLayout);
        toggleLayout.setVisibility(View.GONE);

        parentLayout = view.findViewById(R.id.parentLayoutProfessionalData);

        radioExp = view.findViewById(R.id.radioExperienced);
        radioFresh = view.findViewById(R.id.radioFresher);
        radioExp.setChecked(false);
        radioFresh.setChecked(false);

//        if (AppConstraints.editEmployeeData.getResumeLink()!=null &&
//                !AppConstraints.editEmployeeData.getResumeLink().equalsIgnoreCase("null")
//                && !AppConstraints.editEmployeeData.getResumeLink().equalsIgnoreCase(null)
//                && !AppConstraints.editEmployeeData.getResumeLink().equalsIgnoreCase("")){
//            filePath = AppConstraints.editEmployeeData.getResumeLink();
//        }

        btnUploadResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(intent,1);
                }
            }
        });

//        btnViewResume.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SingleEmployeeDataModel data = AppConstraints.editEmployeeData;
//                if (AppConstraints.editEmployeeData.getResumeLink()!=null &&
//                        !AppConstraints.editEmployeeData.getResumeLink().equalsIgnoreCase("null")
//                        && !AppConstraints.editEmployeeData.getResumeLink().equalsIgnoreCase(null)
//                        && !AppConstraints.editEmployeeData.getResumeLink().equalsIgnoreCase("")){
//                    Intent intent = new Intent(getContext(), OpenPdfActivity.class);
//                    intent.putExtra("PdfLink",data.getResumeLink());
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(getContext(),"Please upload resume first",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        if (AppConstraints.employeeEditFlow == 1) {
            SingleEmployeeDataModel data = AppConstraints.editEmployeeData;

            if (data.isExperienced()) {

                radioExp.setChecked(true);
                radioExp.setSelected(true);

                toggleLayout.setVisibility(View.VISIBLE);

                experience.setText(data.getExperience());
                jobTitle.setText(data.getWorkedJobTitle());
                companyName.setText(data.getWorkedCompanyName());
                filePath = data.getResumeLink();

                isExperienced=1;


            } else {
                toggleLayout.setVisibility(View.GONE);
                radioFresh.setChecked(true);
                radioFresh.setSelected(true);
                freshersFilePath = data.getResumeLink();
                isExperienced=0;
            }
        }


        radioExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioFresh.setChecked(false);
                toggleLayout.setVisibility(View.VISIBLE);
                isExperienced = 1;
            }
        });

        radioFresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioExp.setChecked(false);
                toggleLayout.setVisibility(View.GONE);
                isExperienced = 0;
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleEmployeeDataModel dataNew = AppConstraints.editEmployeeData;
                if (radioExp.isChecked()){
                    String expTxt = experience.getText().toString().trim();
                    String jobTxt = jobTitle.getText().toString().trim();
                    String comTxt = companyName.getText().toString().trim();

                    if (validData(expTxt, jobTxt, comTxt, filePath)) {
                        try {
                            SingleEmployeeDataModel data = AppConstraints.currentEmployeeData;

                            boolean result = false;
                            if (isExperienced == 1)
                                result = true;
                            if (isExperienced == 0)
                                result = false;

                            data.setExperienced(result);
                            data.setExperience(expTxt);
                            data.setWorkedJobTitle(jobTxt);
                            data.setWorkedCompanyName(comTxt);
//                            data.setResumeLink(filePath);

                            if (data.isExperienced()){
                                data.setResumeLink(filePath);
                            } else {
                                data.setResumeLink(freshersFilePath);
                            }

                            AppConstraints.currentEmployeeData = data;


                            String queryData = "";

                            if (AppConstraints.employeeEditFlow == 1)
                                queryData = "{\"query\":\"UPDATE `job_employees` SET " +
                                        "`name`='" + data.getName() + "'," +
                                        "`dob`='" + data.getDateOfBirth() + "'," +
                                        "`email`='" + data.getEmail() + "'," +
                                        "`education`='" + data.getEducation() + "'," +
                                        "`educationField`='" + data.getEduField() + "'," +
                                        "`university`='" + data.getUniversity() + "'," +
                                        "`isExperienced`=" + isExperienced + "," +
                                        "`jobTitle`='" + data.getWorkedJobTitle() + "'," +
                                        "`companyName`='" + data.getWorkedCompanyName() + "'," +
                                        "`experience`='" + data.getExperience() + "'," +
                                        "`resumeLink`='" + data.getResumeLink() + "'," +
                                        "`gender`=" + data.getGender() + " WHERE  id=" + AppConstraints.editEmployeeData.getId() + "\"}";
                            else

                                queryData = "{\"query\":\"INSERT INTO `job_employees`(`userId`, `name`, `dob`, `email`, `education`, `educationField`, `isExperienced`, `jobTitle`, `companyName`, `experience`,`university`,`resumeLink`,`gender`) " +
                                        "VALUES (" + DatabaseUserData.currentUserData.getUserId() + ",'" + data.getName() + "','" + data.getDateOfBirth() + "','" + data.getEmail() + "','" + data.getEducation() + "','"
                                        + data.getEduField() + "','" + isExperienced + "','" + data.getWorkedJobTitle() + "','" + data.getWorkedCompanyName() + "'," +
                                        "'" + data.getExperience() + "','" + data.getUniversity() + "'," + data.getResumeLink() + "'," + data.getGender() + ")\"}";

                            Log.e("DATA", queryData);
                            registerEmployee(queryData);
                        } catch (Exception e) {
                            Log.e("ProfessionalException", "" + e.toString());
                        }
                    }
                } else {
                    //For freshers
                    if (validDataForFresher(freshersFilePath)) {
                        SingleEmployeeDataModel data = AppConstraints.currentEmployeeData;

                        boolean result = false;
                        if (isExperienced == 1)
                            result = true;
                        if (isExperienced == 0)
                            result = false;

                        data.setExperienced(result);

                        if (data.isExperienced()){
                            data.setResumeLink(filePath);
                        } else {
                            data.setResumeLink(freshersFilePath);
                        }
                        AppConstraints.currentEmployeeData = data;

                        String queryData = "";

                        if (AppConstraints.employeeEditFlow == 1)
                            queryData = "{\"query\":\"UPDATE `job_employees` SET " +
                                    "`name`='" + data.getName() + "'," +
                                    "`dob`='" + data.getDateOfBirth() + "'," +
                                    "`email`='" + data.getEmail() + "'," +
                                    "`education`='" + data.getEducation() + "'," +
                                    "`educationField`='" + data.getEduField() + "'," +
                                    "`university`='" + data.getUniversity() + "'," +
                                    "`isExperienced`=" + isExperienced + "," +
                                    "`resumeLink`='" + data.getResumeLink() + "'," +
                                    "`gender`=" + data.getGender() + " WHERE  id=" + AppConstraints.editEmployeeData.getId() + "\"}";
                        else

                            queryData = "{\"query\":\"INSERT INTO `job_employees`(`userId`, `name`, `dob`, `email`, `education`, `educationField`, `isExperienced`,`university`,`resumeLink`,`gender`) " +
                                    "VALUES (" + DatabaseUserData.currentUserData.getUserId() + ",'" + data.getName() + "','" + data.getDateOfBirth() + "','" + data.getEmail() + "','" + data.getEducation() + "','"
                                    + data.getEduField() + "','" + isExperienced + "','" + "','" + data.getUniversity() + "'," + data.getResumeLink() + "'," + data.getGender() + ")\"}";

                        Log.e("DATA", queryData);
                        registerEmployee(queryData);
                    }
                }
            }
        });
        return view;
    }

    private boolean validDataForFresher(String filePath) {
        if (filePath.equals("") || filePath.equalsIgnoreCase("null")
                || filePath.equalsIgnoreCase(null)){
           // tvResumePath.setError("Please upload resume");
            Toast.makeText(getContext(),"Please upload resume",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkAndRequestPermissions() {
        int WExtstorePermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private boolean validData(String expTxt, String jobTxt, String comTxt,String filePath) {
        validData = true;
        if (isExperienced == -1) {
            Snackbar snackbar = Snackbar
                    .make(parentLayout, "Please choose Data", Snackbar.LENGTH_SHORT);

            snackbar.show();

            validData = false;
        } else if (isExperienced == 1) {
            if (expTxt.isEmpty()) {
                experience.setError("Please enter experience");
                validData = false;
            }

            if (jobTxt.isEmpty()) {
                jobTitle.setError("Please enter Job");
                validData = false;
            }

            if (comTxt.isEmpty()) {
                companyName.setError("Please enter company name");
                validData = false;
            }

            if (filePath.equals("") || filePath.equalsIgnoreCase("null")
                    || filePath.equalsIgnoreCase(null)){
              //  tvResumePath.setError("Please upload resume");
                Toast.makeText(getContext(),"please upload resume",Toast.LENGTH_SHORT).show();
                validData = false;
            }
//            if (AppConstraints.editEmployeeData.getResumeLink().equalsIgnoreCase(null) ||
//                    AppConstraints.editEmployeeData.getResumeLink().equalsIgnoreCase("null") ||
//                    AppConstraints.editEmployeeData.getResumeLink().equalsIgnoreCase("")){
//                tvResumePath.setError("Please upload resume");
//                validData = false;
//            }
        }

        return validData;
    }

    public void replaceFragment() {

    }

    private void registerEmployee(String data) {


        Log.e("data", data);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());


        Log.e("Effo", " " + data);

        try {

            final String savedata = data;

            String URL = AppConstraints.dynamicApiUrl;

            //Toast.makeText(getContext(),"Fetching cats"+URL, Toast.LENGTH_SHORT).show();

            requestQueue = Volley.newRequestQueue(getContext());
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


                            try {
                                AppConstraints.currentEmployeeData.setId(AppConstraints.editEmployeeData.getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            startActivity(new Intent(getContext(), AllJobs.class));

                        } else {
                            Toast.makeText(getContext(), "Error ! Try Again", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error ..", Toast.LENGTH_SHORT).show();
                        Log.d("status 1", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getContext(), "Error ..", Toast.LENGTH_SHORT).show();
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
            Log.d("ERROR", e.getMessage());
            Toast.makeText(getContext(), "Error ..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("loading..please wait");
            progressDialog.show();

            switch (requestCode) {
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        try {
                            String filePath = data.getData().getPath();
                            Uri uri = data.getData();
                            String uriString = uri.toString();
                            File myFile = new File(uriString);
                            String path = myFile.getAbsolutePath();
                            String displayName = null;

                            if (uriString.startsWith("content://")) {
                                Cursor cursor = null;
                                try {
                                    cursor = getContext().getContentResolver().query(uri, null, null, null, null);
                                    if (cursor != null && cursor.moveToFirst()) {
                                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                        Log.d("nameeeee>>>>  ",displayName);
                                        uploadPDF(displayName,uri);
                                    }
                                } finally {
                                    cursor.close();
                                }
                            } else if (uriString.startsWith("file://")) {
                                displayName = myFile.getName();
                                Log.d("nameeeee>>>>  ",displayName);
                            }
                        } catch (Exception e) {
                        }
                    }
            }
        }
    }

    private void uploadPDF(final String pdfname, Uri pdffile){
        InputStream iStream = null;
        try {
            iStream = getContext().getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConstraints.uploadPdfUrl,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            String resultResponse = new String(response.data);
                            try {
                                JSONObject jsonObject = new JSONObject(resultResponse);
                                SingleEmployeeDataModel data = AppConstraints.editEmployeeData;
                                if (radioExp.isChecked()){
                                    filePath = jsonObject.getString("filePath");
                                    tvResumePath.setText(filePath);
//                                    AppConstraints.editEmployeeData.setResumeLink(filePath);
//                                    AppConstraints.currentEmployeeData.setResumeLink(filePath);
                                } else {
                                    freshersFilePath = jsonObject.getString("filePath");
                                    tvResumePath.setText(freshersFilePath);
//                                    AppConstraints.editEmployeeData.setResumeLink(freshersFilePath);
//                                    AppConstraints.currentEmployeeData.setResumeLink(filePath);
                                }
//                                AppConstraints.editEmployeeData.setResumeLink(filePath);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("UploadResponse", "" + resultResponse);
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Pdf uploaded", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    // params.put("tags", "ccccc");  add string parameters
                    return params;
                }
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("file", new DataPart(pdfname ,inputData));
                    return params;
                }
            };

            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getContext()).add(volleyMultipartRequest);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {

                }
                break;
        }
    }
}