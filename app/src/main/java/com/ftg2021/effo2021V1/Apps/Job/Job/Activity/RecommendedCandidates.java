package com.ftg2021.effo2021V1.Apps.Job.Job.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;


import com.ftg2021.effo2021V1.Apps.Job.Job.Adapter.RecommendedCandidatesAdapter;
import com.ftg2021.effo2021V1.R;

import java.util.ArrayList;
import java.util.List;

public class RecommendedCandidates extends AppCompatActivity {

    RecyclerView recyclerView;

    List<String> name;
    List<String>designation;
    List<String>experience;
    List<String>salary;

    RecommendedCandidatesAdapter adapter;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_candidates);

        recyclerView=findViewById(R.id.recyclerView);


        name=new ArrayList<>();
        designation=new ArrayList<>();
        experience=new ArrayList<>();
        salary=new ArrayList<>();

        name.add("Akshay Pawar");
        designation.add("Web Developer");
        experience.add("2 Years");
        salary.add("25,000");

        name.add("Akshay Pawar");
        designation.add("Web Developer");
        experience.add("2 Years");
        salary.add("25,000");

        name.add("Akshay Pawar");
        designation.add("Web Developer");
        experience.add("2 Years");
        salary.add("25,000");

        name.add("Akshay Pawar");
        designation.add("Web Developer");
        experience.add("2 Years");
        salary.add("25,000");


        adapter=new RecommendedCandidatesAdapter(this,name,designation,experience,salary);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}