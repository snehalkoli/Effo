package com.ftg2021.effo2021V1.Apps.News;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ftg2021.effo2021V1.Apps.News.Model.NewsCategoryModel;
import com.ftg2021.effo2021V1.Apps.News.URL.OpenURLInBrowser;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Apps.News.api.AccessData;
import com.ftg2021.effo2021V1.Apps.News.fragments.AboutUs;
import com.ftg2021.effo2021V1.Apps.News.fragments.Discover;
import com.ftg2021.effo2021V1.Apps.News.fragments.MyFeed;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class NewsMainActivity extends AppCompatActivity {

    public static final String ADAPTERPOSITION = "POSITION";
    public static int position = 0;


    public static BottomNavigationView bottomNavigationView;
    public static Fragment frag;
    public static LinearLayout blackBoxBehindBottomNav;
    public static View appBar;
    public static RecyclerView myFeed;
    public static boolean visibility = true; //for visible or disable appbar and bottom nav
    public static WebView webView;
    public static FrameLayout fm;
    public static Window window;
    NavigationView sideNav;
    DrawerLayout drawerLayout;
    MaterialToolbar toolbar;
    ActionBarDrawerToggle toggle;
    boolean doubleBackToExitPressedOnce = false;
    private Fragment fragment = null;
    private RequestQueue requestQueue;

    ImageView backButton;

    public static boolean isRefreshclicked=false;


    public static Window getCallingWindow() {
        return window;
    }




    @Override
    public void onBackPressed() {
        webView.setVisibility(View.GONE);
        fm.setVisibility(View.GONE);

        if (doubleBackToExitPressedOnce) {
            super.finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }


    //fetching all categories

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        setFirebaseToken();

        NewsMainActivity.position = getIntent().getIntExtra(NewsMainActivity.ADAPTERPOSITION,0);



        backButton=findViewById(R.id.backToAppListButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        fm = findViewById(R.id.floatButton);//for floating action bottom frame layout
        fm.setVisibility(View.GONE);
        webView = findViewById(R.id.webView1);

        webView.setWebViewClient(new WebViewClient());

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


        window = getWindow();
        appBar = findViewById(R.id.toolbar_main);
        myFeed = findViewById(R.id.rvNews);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        blackBoxBehindBottomNav = findViewById(R.id.bottom_nav_back_black_shape);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //fragment management with bottom navigation
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_root, new MyFeed()).commit();
        transaction.addToBackStack(null);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.discover:
                        fragment = new Discover();
                        break;
                    case R.id.myfeed:
                        fragment = new MyFeed();
                    case R.id.refresh:
                        MyFeed.NotificationNewsPosition=0;
                        NewsMainActivity.isRefreshclicked=true;
                        fragment = new MyFeed();
                        break;
                    case R.id.aboutUsBottomNav:
                        fragment = new AboutUs();
                        break;
                    case R.id.bookmark:
                        //  fragment=new Bookmark();
                        Toast.makeText(getApplicationContext(), "This feature is coming soon..", Toast.LENGTH_SHORT).show();
                        break;
                }


                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                if (fragment != null)
                    transaction.replace(R.id.fragment_root, fragment).commit();
                transaction.addToBackStack(null);

                return true;
            }
        });


        //side menu working

        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close);


        ImageView drawerButton = findViewById(R.id.appbar_drawerButton);

        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DrawerLayout navDrawer = drawerLayout;
                if (!navDrawer.isDrawerOpen(Gravity.LEFT)) navDrawer.openDrawer(Gravity.LEFT);
                else navDrawer.closeDrawer(Gravity.RIGHT);

            }
        });

        toggle.setDrawerIndicatorEnabled(false);
        //      try{toggle.setHomeAsUpIndicator(getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_menu_open_24));}catch (Exception e){}

        /* ****** Listeners ****** */

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        sideNav = findViewById(R.id.nav_view);
        sideNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.aboutUs:
                        fragment = new AboutUs();
                        break;
                    case R.id.privacy:
                        try {
                            new OpenURLInBrowser(getApplicationContext(), "https://www.effoapp.com/terms.html");
                        } catch (Exception e) {
                        }
                        break;
//                    case R.id.setting:
//                        fragment = new Setting();
//                        break;

                }
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                if (fragment != null)
                    transaction.replace(R.id.fragment_root, fragment).commit();
                transaction.addToBackStack(null);
                return false;
            }
        });


        fetchCategories();

        //firebase push notifications
        // Intent intentBackgroundService = new Intent(this, FirebasePushNotification.class);
        //  startService(intentBackgroundService);

        FirebaseMessaging.getInstance().subscribeToTopic("notification");
    }

    private void setFirebaseToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        //Could not get FirebaseMessagingToken
                        return;
                    }
                    if (null != task.getResult()) {
                        //Got FirebaseMessagingToken
                        String firebaseMessagingToken = Objects.requireNonNull(task.getResult());
                        //Use firebaseMessagingToken further



                        String data="{\"query\":\"INSERT INTO notification_tokens(token) VALUES ('" + firebaseMessagingToken + "')\"}";

                        try {

                            final String savedata = data;

                            String URL = AccessData.getApiUrl() + "executeQuery";
                            Log.d("EFFO", URL+" "+data);

                            // Toast.makeText(getContext(),"Fetching cats"+URL, Toast.LENGTH_SHORT).show();

                            requestQueue = Volley.newRequestQueue(this);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    try {
                                        //   Toast.makeText(getContext(),"Fetching cats data", Toast.LENGTH_SHORT).show();

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);

                                            JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONArray(0);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        //JSONArray jsonArray =new  JSONArray(response["data"]);




                                    } catch (Exception e) {
                                        //   Toast.makeText(getContext(), "Some Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        //   Log.d("status", e.getMessage());
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    //   Toast.makeText(getContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                    //   Log.i("status",error.toString());

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
                            //  Log.d("ERROR",e.getMessage());
                            //  Toast.makeText(getContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void fetchCategories() {
        String data = "";


        try {

            final String savedata = data;

            String URL = AccessData.getApiUrl() + "listCategories";
            //  Log.d("RAYAT", URL+" "+data);

            // Toast.makeText(getContext(),"Fetching cats"+URL, Toast.LENGTH_SHORT).show();

            requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        //   Toast.makeText(getContext(),"Fetching cats data", Toast.LENGTH_SHORT).show();

                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONArray(0);


                        //JSONArray jsonArray =new  JSONArray(response["data"]);

                        Log.i("CATS", "NO DATA FOUND");


                        List<NewsCategoryModel> categoryList = new ArrayList<>();


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int catId = object.getInt("categoryId");
                            String catName = object.getString("categoryName");

                            categoryList.add(new NewsCategoryModel(catId, catName));


                        }


                    } catch (Exception e) {
                        //   Toast.makeText(getContext(), "Some Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        //   Log.d("status", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //   Toast.makeText(getContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    //   Log.i("status",error.toString());

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
            //  Log.d("ERROR",e.getMessage());
            //  Toast.makeText(getContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void goBack(View view) {


      onBackPressed();
    }


}