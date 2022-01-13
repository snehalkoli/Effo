package com.ftg2021.effo2021V1.Apps.News.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ftg2021.effo2021V1.Apps.News.Adapter.CategoryFragmentAdapter;
import com.ftg2021.effo2021V1.Apps.News.Adapter.NewsDataAdapterNew;
import com.ftg2021.effo2021V1.Apps.News.Model.NewsCategoryModel;
import com.ftg2021.effo2021V1.Apps.News.Model.NewsDataModel;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Apps.News.Scroll.SnapHelperOneByOne;
import com.ftg2021.effo2021V1.Apps.News.api.AccessData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Discover extends Fragment {

    private  static RecyclerView recyclerViewCategoryFragment,recyclerViewCatById ;



    public static View root;

    public static Context context;

    public static RelativeLayout rlDiscover,rlCatById;

    public static RelativeLayout catListLayout,newsByCatIdLayout;

    LinearLayout loadingBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_discover, container, false);


        loadingBar = root.findViewById(R.id.loadingProgressBar);

        //adding categories to side menu
        recyclerViewCategoryFragment = root.findViewById(R.id.rvCategoryFragment);
        recyclerViewCatById = root.findViewById(R.id.rvCatById);


        rlDiscover = root.findViewById(R.id.discoverLayout);
        rlCatById = root.findViewById(R.id.catByIdLayout);

        catListLayout = root.findViewById(R.id.discoverLayout);
        newsByCatIdLayout = root.findViewById(R.id.catByIdLayout);

        context=getContext();

        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
        linearSnapHelper.attachToRecyclerView(recyclerViewCatById);



        fetchCategories();



        return root;
    }




    //fetching all categories

    private void fetchCategories()
    {
        String data="";

        loadingBar.setVisibility(View.VISIBLE);

        try {

            final String savedata = data;

            String URL = AccessData.getApiUrl()+"listCategories";
            Log.d("RAYAT", URL+" "+data);

           // Toast.makeText(getContext(),"Fetching cats"+URL, Toast.LENGTH_SHORT).show();


            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try
                    {
                     //   Toast.makeText(getContext(),"Fetching cats data", Toast.LENGTH_SHORT).show();

                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONArray(0);


                        //JSONArray jsonArray =new  JSONArray(response["data"]);

                        Log.i("CATS","NO DATA FOUND");



                        List<NewsCategoryModel> categoryList=new ArrayList<>();



                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int catId      = object.getInt("categoryId");
                            String catName = object.getString("categoryName");

                            categoryList.add(new NewsCategoryModel(catId,catName));
                        }



                        loadingBar.setVisibility(View.GONE);

                        CategoryFragmentAdapter adapter = new CategoryFragmentAdapter(getContext(), categoryList);
                        recyclerViewCategoryFragment.setHasFixedSize(true);
                        recyclerViewCategoryFragment.setLayoutManager(new GridLayoutManager(getContext(),2));
                        recyclerViewCategoryFragment.setAdapter(adapter);




                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Some Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                         Log.d("status", e.getMessage());
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

        }catch (Exception e){

          //  Log.d("ERROR",e.getMessage());
          //  Toast.makeText(getContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    //fetching all news by categories

    public static void fetchByCatId(String catId)
    {
        String data="";


        try {

            final String savedata = data;

            String URL = AccessData.getApiUrl()+catId;
            // Log.d("RAYAT", URL+" "+data);

           //  Toast.makeText(context,"Fetching cats"+URL, Toast.LENGTH_SHORT).show();
            Log.i("status",URL);

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try
                    {
//                          Toast.makeText(context,"Fetching cats data", Toast.LENGTH_SHORT).show();

                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("data");


                        //JSONArray jsonArray =new  JSONArray(response["data"]);

                  //      Log.i("CATS","NO DATA FOUND");



                        List<NewsDataModel> newsList=new ArrayList<>();

                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int newsId      = object.getInt("newsId");
                            String imageUrl = AccessData.getBaseUrl() +"/"+ object.getString("imageUrl");
                            String heading = object.getString("title");
                            String description = object.getString("description");
                            String publisher = object.getString("publisher");
                            String sourceUrl = object.getString("source");
                            String sourceName = object.getString("sourceName");
                            String date = object.getString("date");

//                            newsList.add(new NewsDataModel(""+newsId,heading,description,imageUrl,date,sourceName));


                            if(i%5==0 && i>0)
                                newsList.add(new NewsDataModel(0,"","","","","","","",true));

                            newsList.add(new NewsDataModel(newsId,heading,description,imageUrl,date,sourceName,publisher,sourceUrl,false));


                        }


                        NewsDataAdapterNew adapter = new NewsDataAdapterNew(root.getContext(), newsList);
                        recyclerViewCatById.setHasFixedSize(true);
                        recyclerViewCatById.setLayoutManager(new LinearLayoutManager(root.getContext()));
                        recyclerViewCatById.setAdapter(adapter);



                    } catch (Exception e) {
                      //     Toast.makeText(context, "Some Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                           Log.d("status1", e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                 //      Toast.makeText(context, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                       Log.i("status-->",error.toString());

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
        }catch (Exception e){
              Log.d("ERROR",e.getMessage());
           //   Toast.makeText(context, "status end: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}