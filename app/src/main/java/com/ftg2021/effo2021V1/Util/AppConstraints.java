package com.ftg2021.effo2021V1.Util;

import android.content.Context;

import com.ftg2021.effo2021V1.Apps.News.Model.AppliedJobsModel;
import com.ftg2021.effo2021V1.Util.DataModels.SingleApplicationReceived;
import com.ftg2021.effo2021V1.Util.DataModels.SingleCompanyDataModel;
import com.ftg2021.effo2021V1.Util.DataModels.SingleEmployeeDataModel;
import com.ftg2021.effo2021V1.Util.DataModels.SingleJobDataModel;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppConstraints {

    private final static String baseUrl="https://api.effoapp.com";
    private final static String port="5000";
    private final static String apiUrl="https://api.effoapp.com/news/";
    public final static String dynamicApiUrl="https://api.effoapp.com/news/executeQuery/";
    public final static String uploadFileUrl="https://api.effoapp.com/news/upload";
    public final static String uploadPdfUrl="https://api.effoapp.com/news/uploadPDF";


    public static FirebaseUser currentFirebaseUser;
    public static SingleJobDataModel currentJobData;
    public static SingleJobDataModel editJobData;

    public static SingleCompanyDataModel currentCompanyData;
    public static SingleCompanyDataModel editCompanyData;

    public static SingleEmployeeDataModel currentEmployeeData;
    public static SingleEmployeeDataModel editEmployeeData;

    public static SingleJobDataModel viewJobData;
    public static AppliedJobsModel viewAppliedJobsData;
    public static SingleCompanyDataModel viewCompanyData;

    public static SingleApplicationReceived currentApplicationData;

    public static GoogleSignInClient mGoogleSignInClient;


    public static Context context;

    public static List<String> categories = new ArrayList<String>();
    public static List<String> jobTitleList = new ArrayList<String>();
    public static List<String> jobLocationList = new ArrayList<String>();
    public static List<String> jobQualificationList = new ArrayList<String>();
    public static List<Integer> workExperienceList = new ArrayList<Integer>();

    public static int jobPostFlow; //0=post new job || 1=edit job
    public static int companyEditFlow; //0=create new company || 1=edit company
    public static int employeeEditFlow;//0=register new employee || 1=edit employee
    public static int viewEmployeeFlow;//0=user || 1= view as a company
    public static String otherEditTextData;

    public static String getCurrentDate()
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }
}
