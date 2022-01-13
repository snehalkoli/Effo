package com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs;

import static com.ftg2021.effo2021V1.Util.AppConstraints.categories;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.Choose_FindOrPostJob;
import com.ftg2021.effo2021V1.Authentication.UserData.DatabaseUserData;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.ftg2021.effo2021V1.Util.DataModels.SingleCompanyDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CompanyProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    final Calendar myCalendar = Calendar.getInstance();
    Button buttonContinue;
    EditText editTextDate, companyName, companyType, companyWebsite, companyEmail,etcontactNo;
    TextView chooseDate, companyDetailsText;
    String companyDocumentFile = "", companyImageFile = "";
    String cameraPath = "", gallaryPath = "";
    Button btnUploadCompanyProof, btnUploadCompanyProfile;
    ProgressDialog progressDialog;
    int checkFlag = 0;
    ImageView imgProof, imgCompanyImage;
    private String filePath;
    Spinner companyTypeSpinner;
    String typeOfCompany="";
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);

        buttonContinue = findViewById(R.id.Continue);

        editTextDate = findViewById(R.id.editTextDate);
        chooseDate = findViewById(R.id.chooseDate);
        companyName = findViewById(R.id.companyName);
//        companyType = findViewById(R.id.companyType);
        companyTypeSpinner = findViewById(R.id.companyTypeSpinner);
        companyWebsite = findViewById(R.id.companyWebsite);
        companyEmail = findViewById(R.id.companyEmailMain);

        companyDetailsText = findViewById(R.id.companyDetailsText);
        btnUploadCompanyProof = findViewById(R.id.btnUploadProof);
        btnUploadCompanyProfile = findViewById(R.id.btnUploadProfile);
        imgProof = findViewById(R.id.imgUploadProof);
        imgCompanyImage = findViewById(R.id.imgUploadCompanyImage);
        etcontactNo = findViewById(R.id.contactNumberText);

        companyTypeSpinner.setOnItemSelectedListener(this);
        getCompanyTypeList();

        companyEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                flag = false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                flag = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Call API
                String queryData;
                queryData = "{\"query\":\"Select email from job_company"+"\"}";
                checkEmailAlreadyExistOrNot(companyEmail.getText().toString().trim(),queryData);
            }
        });

        btnUploadCompanyProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()) {
                    checkFlag = 1;
                    chooseImage(CompanyProfile.this);
                }
            }
        });

        btnUploadCompanyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()) {
                    checkFlag = 2;
                    chooseImage(CompanyProfile.this);
                }
            }
        });


        if (AppConstraints.companyEditFlow == 1) {
            companyDetailsText.setText("Edit Company Details");

            SingleCompanyDataModel data = AppConstraints.editCompanyData;

            editTextDate.setText(data.getDate());
            companyName.setText(data.getName());
            companyEmail.setText(data.getEmail());
            companyWebsite.setText(data.getWebsite());
           // companyType.setText(data.getType());

            companyDocumentFile = data.getCompanyDocument();
            companyImageFile = data.getCompanyImage();
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(CompanyProfile.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }
        });


        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(CompanyProfile.this,ActivityCompanyMainScreen_PostJobs.class);
//                startActivity(intent);

                String comName = companyName.getText().toString().trim();
//                String comType = companyType.getText().toString().trim();
                String comType = typeOfCompany;
                String comDate = editTextDate.getText().toString().trim();
                String comWebsite = companyWebsite.getText().toString().trim();
                String comEmail = companyEmail.getText().toString().trim();
                String contactNo = etcontactNo.getText().toString().trim();

                int userId = DatabaseUserData.currentUserData.getUserId();

                if (validateData(comName, comType, comDate, comWebsite, comEmail,flag,companyDocumentFile,companyImageFile,contactNo)) {

                    AppConstraints.currentCompanyData = new SingleCompanyDataModel();

                    AppConstraints.currentCompanyData.setName(comName);
                    AppConstraints.currentCompanyData.setType(comType);
                    AppConstraints.currentCompanyData.setDate(comDate);
                    AppConstraints.currentCompanyData.setWebsite(comWebsite);
                    AppConstraints.currentCompanyData.setEmail(comEmail);
                    AppConstraints.currentCompanyData.setCompanyDocument(companyDocumentFile);
                    AppConstraints.currentCompanyData.setCompanyImage(companyImageFile);
                    AppConstraints.currentCompanyData.setContactNo(contactNo);


                    String data;

                    if (AppConstraints.companyEditFlow == 1){
                        AppConstraints.currentCompanyData.setId(AppConstraints.editCompanyData.getId());

                        data = "{\"query\":\"UPDATE `job_company` SET " +
                                "`name`='" + comName + "'," +
                                "`type`='" + comType + "'," +
                                "`date_established`='" + comDate + "'," +
                                "`website`='" + comWebsite + "'," +
                                "`company_document`='" + companyDocumentFile + "'," +
                                "`companyImage`='" + companyImageFile + "'," +
                                "`mobileNo`='" + contactNo + "'," +
                                "`email`='" + comEmail + "' WHERE " +
                                "id=" + AppConstraints.editCompanyData.getId() + "\"}";
                    }
                    else
                        data = "{\"query\":\"INSERT INTO `job_company`(`userId`, `name`, `type`, `date_established`, `website`, `company_document`, `companyImage`,`mobileNo`,`email`) " +
                                "VALUES (" + userId + ",'" + comName + "','" + comType + "','" + comDate + "','" + comWebsite + "','" + companyDocumentFile + "','" + companyImageFile + "','" + contactNo + "','" + comEmail + "')\"}";

                    Log.e("DATA", data);

                    registerCompany(data);
                }
            }
        });
    }

    private void checkEmailAlreadyExistOrNot(String editText,String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                        JSONArray jsonArray = new JSONArray(response.toString());
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String email = jsonObject.getString("email");
                            if (email.equalsIgnoreCase(editText)){
                                flag = false;
                                companyEmail.setError("already you are the user");
                            } else{
                                flag = true;
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
                        Log.d("status 1", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

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
            Log.d("ERROR", e.getMessage());
            Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
        }
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
        categories.add("Section 8  Company");
        categories.add("Producer  Company");
        categories.add("Small  Company");
        categories.add("Subsidiary  Company");
        categories.add("Holding  Company");
        Log.e("Categories",""+categories);
    }

    private boolean checkAndRequestPermissions() {
        int WExtstorePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(this,
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
            ActivityCompat.requestPermissions(this, listPermissionsNeeded
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
                if (ContextCompat.checkSelfPermission(CompanyProfile.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(CompanyProfile.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(CompanyProfile.this);
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
                    intent.setType("application/pdf");
                    startActivityForResult(intent, 2);
                } else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            progressDialog = new ProgressDialog(CompanyProfile.this);
            progressDialog.setMessage("loading..please wait");
            progressDialog.show();
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
//                        String filePath = identifyFileType(data.getData().getPath());
//                        if (filePath.equals("video/mp4")) {
//                            Toast.makeText(CompanyProfile.this,"Video file can not be uploaded",Toast.LENGTH_SHORT).show();
//                        }else  if (filePath.equals("audio/mp3")){
//                            Toast.makeText(CompanyProfile.this,"Audio file can not be uploaded",Toast.LENGTH_SHORT).show();
//                        }
//                        else
                        {
                            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                            bitmap = Bitmap.createScaledBitmap(bitmap, 50, 100, true);
                            //sizeOf(bitmap);
                            uploadPhoto(bitmap);
                            imgProof.setVisibility(View.VISIBLE);
                            imgCompanyImage.setVisibility(View.VISIBLE);
                            if (checkFlag == 1) {
                                imgProof.setImageBitmap(bitmap);
                            } else if (checkFlag == 2) {
                                imgCompanyImage.setImageBitmap(bitmap);
                            }
                        }

//                        Uri picUri = data.getData();
//                        filePath = getPath(picUri);
//                        if (filePath != null) {
//                            try {
//                                Log.i("filePath", String.valueOf(filePath));
//                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),picUri);
//                                uploadPhoto(bitmap);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
                        //Upload camera or gallary images here,call api here to upload
//                        UploadFileToServer(cameraPath);
//                        imageView.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
//                        String filePath = identifyFileType(data.getData().getPath());
//                        if (filePath.equals("video/mp4")) {
//                            Toast.makeText(CompanyProfile.this,"Video file can not be uploaded",Toast.LENGTH_SHORT).show();
//                        } else  if (filePath.equals("audio/mp3")){
//                            Toast.makeText(CompanyProfile.this,"Audio file can not be uploaded",Toast.LENGTH_SHORT).show();
//                        }
//                        else
                        {
                            Uri picUri = data.getData();
                            filePath = getPath(picUri);
                            if (filePath != null) {
                                try {
                                    Log.i("filePath", String.valueOf(filePath));
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                                    bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
                                   // int size = sizeOf(bitmap);
                                    uploadPhoto(bitmap);
                                    imgProof.setVisibility(View.VISIBLE);
                                    imgCompanyImage.setVisibility(View.VISIBLE);
                                    if (checkFlag == 1) {
                                        imgProof.setImageBitmap(bitmap);
                                    } else if (checkFlag == 2) {
                                        imgCompanyImage.setImageBitmap(bitmap);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(CompanyProfile.this, "no image selected", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

//                        Uri selectedImage = data.getData();
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        if (selectedImage != null) {
//                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                            if (cursor != null) {
//                                cursor.moveToFirst();
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                gallaryPath = cursor.getString(columnIndex);
////                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                                cursor.close();
//                                //Upload camera or gallary images here,call api here to upload
//                                UploadFileToServer(gallaryPath);
//                                uploadPhoto(BitmapFactory.decodeFile(gallaryPath));
//                            }
//                        }
//                    }
                    break;

                case 2:
                    if (resultCode == RESULT_OK && data != null) {
                        try {
                            String filePath = data.getData().getPath();
//                        InputStream is = null;
//                        try {
//                            is = getContentResolver().openInputStream(data.getData());
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                        Bitmap bitmap = BitmapFactory.decodeStream(is);
//                        File sd = Environment.getExternalStorageDirectory();
//                        File image = new File(sd+filePath, filePath);
//                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                        Bitmap bitmap = BitmapFactory.decodeFile(filePath,bmOptions);File mFolder = new File(getFilesDir() + "/sample");

                            Uri uri = data.getData();
                            String uriString = uri.toString();
                            File myFile = new File(uriString);
                            String path = myFile.getAbsolutePath();
                            String displayName = null;

                            if (uriString.startsWith("content://")) {
                                Cursor cursor = null;
                                try {
                                    cursor = this.getContentResolver().query(uri, null, null, null, null);
                                    if (cursor != null && cursor.moveToFirst()) {
                                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                        Log.d("nameeeee>>>>  ", displayName);

                                        uploadPDF(displayName, uri);
                                    }
                                } finally {
                                    cursor.close();
                                }
                            } else if (uriString.startsWith("file://")) {
                                displayName = myFile.getName();
                                Log.d("nameeeee>>>>  ", displayName);
                            }
                        } catch (Exception e) {

                        }
                    }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private String identifyFileType(String path) {
        String fileType = "undermined";
        File file = new File(path);
        try {
            fileType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        return fileType;
    }

    private void uploadPDF(final String pdfname, Uri pdffile) {
        InputStream iStream = null;
        try {
            iStream = getContentResolver().openInputStream(pdffile);
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
                                    companyDocumentFile = filePath;
                                } else if (checkFlag == 2) {
                                    companyImageFile = filePath;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("UploadResponse", "" + resultResponse);
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Pdf uploaded", Toast.LENGTH_SHORT).show();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    params.put("file", new DataPart(pdfname, inputData));
                    return params;
                }
            };

            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(this).add(volleyMultipartRequest);
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


    private Bitmap renderToBitmap(Context context, String filePath) {
        Bitmap bi = null;
        InputStream inStream = null;

        try {
            AssetManager assetManager = getAssets();
            InputStream in = null;
            OutputStream out = null;
            File file = new File(getFilesDir(), filePath);

            in = assetManager.open(String.valueOf(file));
            out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;

            bi = renderToBitmap(context, String.valueOf(in));
        } catch (Exception e) {

        }


//        try {
//            AssetManager assetManager = context.getAssets();
//            Log.d("TAG", "Attempting to copy this file: " + filePath);
//            inStream = assetManager.open(filePath);
//            bi = renderToBitmap(context, String.valueOf(inStream));
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                inStream.close();
//            } catch (IOException e) {
//                // do nothing because the stream has already been closed
//            }
//        }
        return bi;
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    protected int sizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else {
            return data.getByteCount();
        }
    }

    private String getPath(Uri uri) {
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        String document_id = cursor.getString(0);
//        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
//        cursor.close();
//
//        cursor = getContentResolver().query(
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
//        cursor.moveToFirst();
//        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//        cursor.close();
//
//        return path;

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
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
                                companyDocumentFile = filePath;
                            } else if (checkFlag == 2) {
                                companyImageFile = filePath;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("UploadResponse", "" + resultResponse);
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void UploadFileToServer(String path) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = AppConstraints.uploadFileUrl;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("file", path);
            final String requestBody = jsonBody.toString();
            Log.e("RequestBody", "" + requestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private boolean validateData(String comName, String comType, String comDate, String comWebsite, String comEmail, boolean flag,String companyDocumentFile,String companyImageFile,String contactNo) {
        boolean result = true;

        if (comName.isEmpty()) {
            result = false;
            companyName.setError("Enter Company Name");
        }

//        if (comType.isEmpty()) {
//            result = false;
//            companyType.setError("Enter Company Type");
//        }
        else  if(comType.equalsIgnoreCase("Select Company Type")){
            Toast.makeText(getApplicationContext(),"Please select company type",Toast.LENGTH_SHORT).show();
        }
        else if (comWebsite.isEmpty()){
            if (!Patterns.WEB_URL.matcher(comWebsite).find()) {
                companyWebsite.setError("Invalid Website");
                result = false;
            }
        }

       else if (comDate.isEmpty()) {
            result = false;
            editTextDate.setError("Enter Company Name");
        }

        else if (comEmail.isEmpty()) {
            result = false;
            companyEmail.setError("Enter Company email");
        }

//        else if (flag == true){
//            result = false;
//            Toast.makeText(getApplicationContext(),"Please enter another email",Toast.LENGTH_SHORT).show();
//            flag=false;
//        }
        else  if (companyEmail.getError()!=null){
            if (companyEmail.getError().equals("already you are the user")){
                result = false;
                Toast.makeText(getApplicationContext(),"Please enter another email",Toast.LENGTH_SHORT).show();
            }
        }

        else if (companyDocumentFile.equalsIgnoreCase("")){
            result = false;
            Toast.makeText(getApplicationContext(),"Please add company documents",Toast.LENGTH_SHORT).show();
        }

        else if (companyImageFile.equalsIgnoreCase("")){
            result = false;
            Toast.makeText(getApplicationContext(),"Please add company images",Toast.LENGTH_SHORT).show();
        }

        else if (contactNo.length()<10){
            result = false;
            etcontactNo.setError("please enter correct mobile no");
        }

        return result;
    }

    private void updateDateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        editTextDate.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void registerCompany(String data) {

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
                            if (AppConstraints.companyEditFlow == 1) {
                                startActivity(new Intent(CompanyProfile.this, ActivityCompanyMainScreen_PostJobs.class));
                                finish();
                            } else
                                startActivity(new Intent(CompanyProfile.this, Choose_FindOrPostJob.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error Creating Company Account! Try Again", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
                        Log.d("status 1", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

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
            Log.d("ERROR", e.getMessage());
            Toast.makeText(getApplicationContext(), "No internet connection ..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String item = adapterView.getItemAtPosition(position).toString();
        typeOfCompany = item;

//        if (!item.equalsIgnoreCase("Select Company Type")){
//            Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}