package com.ftg2021.effo2021V1.Apps.Job.Job.Fragment;

import static com.ftg2021.effo2021V1.Util.AppConstraints.categories;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ftg2021.effo2021V1.AppList.AppList;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.Choose_FindOrPostJob;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.AllJobs;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.EmployeeProfile;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.OpenPdfActivity;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.TouchImageView;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.ZoomableImageView;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs.ActivityCompanyMainScreen_PostJobs;
//import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs.Choose_FindOrPostJob;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs.CompanyProfile;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs.VolleyMultipartRequest;
import com.ftg2021.effo2021V1.Authentication.UserData.DatabaseUserData;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.ftg2021.effo2021V1.Util.DataModels.SingleCompanyDataModel;
import com.ftg2021.effo2021V1.Util.DataModels.SingleEmployeeDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CompanyProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int RESULT_CANCELED = 0;
    private static final int RESULT_OK = -1;
    EditText etCompanyName,etCompanyType,etWebsite,etEmail,etContactNo,editTextDate;
    ImageView ivCompanyImage;
    Button btnViewDocument,btnUpdateCompanyProof,btnUpdateCompanyImage,btnSaveCompanyData;
    String company_document ="", companyImage="";
    private String filePath;
    ProgressDialog progressDialog;
    int checkFlag = 0;
    String cameraPath = "", gallaryPath = "";
    TextView chooseDate;
    final Calendar myCalendar = Calendar.getInstance();
    int userId;
    Button btnLogout;
    private FirebaseAuth mAuth;
    ImageView imageView;
    Spinner companyTypeSpinner;
    String typeOfCompany="";
    int selectedPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_company_profile, container, false);
        etCompanyName = view.findViewById(R.id.etCompanyName);
//        etCompanyType = view.findViewById(R.id.etCompanyType);
        companyTypeSpinner = view.findViewById(R.id.companyTypeSpinner);
//        etDateOfEstablishment = view.findViewById(R.id.etDateOfEstablishment);
       // chooseDate = view.findViewById(R.id.chooseDate);
        editTextDate = view.findViewById(R.id.editTextDate);
        etWebsite = view.findViewById(R.id.etWebsite);
        etEmail = view.findViewById(R.id.etEmail);
        etContactNo = view.findViewById(R.id.etContactNo);
        ivCompanyImage = view.findViewById(R.id.ivCompanyImage);
        btnViewDocument = view.findViewById(R.id.btnViewDocument);
        btnUpdateCompanyProof = view.findViewById(R.id.btnUpdateCompanyProof);
        btnUpdateCompanyImage = view.findViewById(R.id.btnUpdateCompanyImage);
        btnSaveCompanyData = view.findViewById(R.id.btnSaveData);
        progressDialog = new ProgressDialog(getContext());
        imageView = view.findViewById(R.id.ivShowDate);
       // btnLogout = view.findViewById(R.id.btnLogOut);

        mAuth=FirebaseAuth.getInstance();

        companyTypeSpinner.setOnItemSelectedListener(this);
        getCompanyTypeList();
        getCompanyDetailsData();



        //Log Out functionality here
//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopup();
//            }
//        });


        btnUpdateCompanyProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    checkFlag = 1;
                    chooseImage(getContext());
                }
            }
        });

        btnUpdateCompanyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    checkFlag = 2;
                    chooseImage(getContext());
                }
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }

        };

//        chooseDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
//                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
//                datePickerDialog.show();
//            }
//        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }
        });


        btnSaveCompanyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()){
                    saveCompanyDetails();
                }
            }
        });


        btnViewDocument.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (!company_document.equalsIgnoreCase(null)&&
                        !company_document.equalsIgnoreCase("null" ) &&
                        !company_document.equalsIgnoreCase("") && company_document!=null){
                    String fileType = identifyFileType(company_document);
                    if (fileType.equals("application/pdf") || fileType.contains("application/pdf")){
                        Intent intent = new Intent(getContext(), OpenPdfActivity.class);
                        intent.putExtra("PdfLink",company_document);
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder alertadd = new AlertDialog.Builder(getContext());
                        LayoutInflater factory = LayoutInflater.from(getContext());
                        view = factory.inflate(R.layout.image_layout, null);
//                        ImageView imageView = view.findViewById(R.id.ivCompanyDocumentImage);
                        TouchImageView imageView = view.findViewById(R.id.ivCompanyDocumentImage);
                        Picasso.with(getContext()).load("https://api.effoapp.com/"+company_document).placeholder(R.drawable.waiting_img).into(imageView);
                        alertadd.setView(view);
                        alertadd.show();
                    }
                } else {
                    Toast.makeText(getContext(),"Please upload file first",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private boolean checkValidation() {
        if (etCompanyName.getText().toString().isEmpty()){
            etCompanyName.setError("please enter company name");
            return false;
        }else  if (typeOfCompany.equalsIgnoreCase("Select Company Type")){
            Toast.makeText(getContext(),"please select company type",Toast.LENGTH_SHORT).show();
            return false;
        } else if (editTextDate.getText().toString().isEmpty()){
            editTextDate.setError("please select date");
            return false;
        }else if (etWebsite.getText().toString().isEmpty()){
            etWebsite.setError("please enter website");
            return false;
        }else if (etEmail.getText().toString().isEmpty()){
            etEmail.setError("please enter email");
            return false;
        }else if (etContactNo.getText().toString().length()<10){
            etContactNo.setError("please enter correct mobile no");
            return false;
        }
        return true;
    }

    private void getCompanyTypeList() {
        categories=new ArrayList<>();
        categories.add(" Select Company Type");
        categories.add("Software Company");
        categories.add("Hardware Company");
        categories.add("Networking Company");
        categories.add("IT Company");
        categories.add("Private Company");
        categories.add("One Person Company");
        categories.add("Public Company");
        categories.add("Company Limited by Shares");
        categories.add("Unlimited Company");
        categories.add("Foreign Company");
        categories.add("Section 8 Company");
        categories.add("Producer Company");
        categories.add("Small Company");
        categories.add("Subsidiary Company");
        categories.add("Holding Company");
        Log.e("Categories",""+categories);
    }

    private void showPopup() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage("Are you sure?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener()                 {

                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Toast.makeText(getContext(),"Logged out succesfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), AppList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancel", null);

        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    private void updateDateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        editTextDate.setText(dateFormat.format(myCalendar.getTime()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
        private String identifyFileType(String path) {
            String fileType = "undermined";
            File file = new File(path);
            try {
                fileType = Files.probeContentType(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
            }
            return fileType;
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
                    chooseImage(getContext());
                }
                break;
        }
    }

    private void chooseImage(Context context) {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Choose from Files", "Exit"}; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (optionsMenu[i].equals("Take Photo")) {
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (optionsMenu[i].equals("Choose from Gallery")) {
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                } else if (optionsMenu[i].equals("Choose from Files")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    intent.setType("image/*");
                    intent.setType("application/pdf");
                    startActivityForResult(intent,2);
                } else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private void choosePDF(Context context) {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Choose from Files", "Exit"}; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (optionsMenu[i].equals("Take Photo")) {
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (optionsMenu[i].equals("Choose from Gallery")) {
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                } else if (optionsMenu[i].equals("Choose from Files")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/pdf");
                    startActivityForResult(intent,2);
                } else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            progressDialog.setMessage("loading..please wait");
            progressDialog.show();
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        {
                            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                            bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
                            //sizeOf(bitmap);
                            uploadPhoto(bitmap);
                            if (checkFlag==1){
 //                               imgProof.setImageBitmap(bitmap);
                            } else  if (checkFlag==2){
                               // ivCompanyImage.setImageBitmap(bitmap);
                            }
                        }
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        {
                            Uri picUri = data.getData();
                            filePath = getPath(picUri);
                            if (filePath != null) {
                                try {
                                    Log.i("filePath", String.valueOf(filePath));
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), picUri);
                                    bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
                                    //sizeOf(bitmap);
                                    uploadPhoto(bitmap);
                                    if (checkFlag==1){
//                                        imgProof.setImageBitmap(bitmap);
                                    } else  if (checkFlag==2){
                                        ivCompanyImage.setImageBitmap(bitmap);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getContext(), "no image selected", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    break;

                case 2:
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

    private String getPath(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        if (uri != null) {
            Cursor cursor = getContext().getContentResolver().query(uri, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                gallaryPath = cursor.getString(columnIndex);
                cursor.close();
            }
        }
        return gallaryPath;
    }

    private void uploadPhoto(final Bitmap bitmap) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, AppConstraints.uploadFileUrl,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);
                        try {
                            JSONObject jsonObject = new JSONObject(resultResponse);
                            String filePath = jsonObject.getString("filePath");
                            if (checkFlag == 1) {
                                company_document = filePath;
                            } else if (checkFlag == 2) {
                                companyImage = filePath;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("UploadResponse", "" + resultResponse);
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Don't forgot to click on save Button to reflect changes", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError", "" + error.getMessage());
                    }
                }) {


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart("", getFileDataFromDrawable(bitmap)));
                Log.i("Parameters", "" + params.toString());
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(getContext()).add(volleyMultipartRequest);
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
                                String filePath = jsonObject.getString("filePath");
                                if (checkFlag == 1) {
                                    company_document = filePath;
                                } else if (checkFlag == 2) {
                                    companyImage = filePath;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("UploadResponse", "" + resultResponse);
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Don't forgot to click on save Button to reflect changes", Toast.LENGTH_LONG).show();
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

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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

    private void saveCompanyDetails() {
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String companyNameTxt = etCompanyName.getText().toString().trim();
//        String companyTypeTxt = etCompanyType.getText().toString().trim();
        String companyTypeTxt = typeOfCompany;
        String dateOfEstablishmentTxt = editTextDate.getText().toString().trim();
        String websiteTxt = etWebsite.getText().toString().trim();
        String emailTxt = etEmail.getText().toString().trim();
        String contactNo = etContactNo.getText().toString().trim();
//        String contactNumberTxt = etContactNo.getText().toString().trim();

        SingleCompanyDataModel data = AppConstraints.currentCompanyData;
        data.setName(companyNameTxt);
        data.setType(companyTypeTxt);
        data.setDate(dateOfEstablishmentTxt);
        data.setWebsite(websiteTxt);
        data.setEmail(emailTxt);
        Log.e("ContactNo",""+data.getContactNo());
        data.setContactNo(contactNo);

        AppConstraints.currentCompanyData = data;
        String queryData = "";



        queryData="{\"query\":\"UPDATE `job_company` SET " +
                "`name`='"+data.getName()+"'," +
                "`type`='"+data.getType()+"'," +
                "`date_established`='"+data.getDate()+"'," +
                "`website`='"+data.getWebsite()+"'," +
                "`company_document`='"+company_document+"'," +
                "`companyImage`='"+companyImage+"'," +
                "`mobileNo`='"+data.getContactNo()+"'," +
                "`email`='"+data.getEmail()+"' WHERE " +
                "id="+AppConstraints.currentCompanyData.getId()+"\"}";

        Log.e("EditCompanyQuery",""+queryData);
        EditData(queryData);
    }

    private void EditData(String data) {
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
                        Toast.makeText(getContext(),"Data Saved",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();


                        AppConstraints.companyEditFlow=1;
//                        Intent intent = new Intent(getContext(), Choose_FindOrPostJob.class);
//                        startActivity(intent);
                        getCompanyDetailsData();

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            Log.e("status 5", e.getMessage());
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

    private void getCompanyDetailsData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        String data = null;
        data = "{\"query\":\"select * from job_company where userId='" + DatabaseUserData.currentUserData.getUserId() + "'\"}";
        Log.e("MukeshMack", " " + data);

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
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                        } catch (JSONException e) {
                            Log.e("status 5", e.getMessage());
                        }

                        if (jsonArray.length() > 0) {
                            JSONObject jsonObjectUser = jsonArray.getJSONObject(0);

                            int id = jsonObjectUser.getInt("id");
                            userId = jsonObjectUser.getInt("userId");
                            String name = jsonObjectUser.getString("name");
                            String type = jsonObjectUser.getString("type");
                            String date_established = jsonObjectUser.getString("date_established");
                            String website = jsonObjectUser.getString("website");
                            company_document = jsonObjectUser.getString("company_document");
                            companyImage = jsonObjectUser.getString("companyImage");
                            String email = jsonObjectUser.getString("email");
                            String mobileNo = jsonObjectUser.getString("mobileNo");

                            AppConstraints.currentCompanyData.setId(id);
                            etCompanyName.setText(name);
//                            etCompanyType.setText(type);
                            for (int i=0;i<AppConstraints.categories.size();i++){
                                if (AppConstraints.categories.get(i).equalsIgnoreCase(type)){
                                    companyTypeSpinner.setSelection(i);
                                } else {
                                    //companyTypeSpinner.setSelection(0);
                                }
                            }
                            editTextDate.setText(date_established);
                            etWebsite.setText(website);
                            etEmail.setText(email);
                            if (!mobileNo.equalsIgnoreCase(null) && !mobileNo.equalsIgnoreCase("null")){
                                etContactNo.setText(mobileNo);
                            }
                            Picasso.with(getContext()).load("https://api.effoapp.com/"+companyImage).placeholder(R.drawable.waiting_img).into(ivCompanyImage);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
                        Log.d("status 1", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String item = adapterView.getItemAtPosition(position).toString().trim();
        typeOfCompany = item;
        selectedPosition = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}