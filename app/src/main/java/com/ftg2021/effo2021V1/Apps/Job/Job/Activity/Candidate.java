package com.ftg2021.effo2021V1.Apps.Job.Job.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ftg2021.effo2021V1.R;


public class Candidate extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate);

        button=findViewById(R.id.apply);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Candidate.this, ApplicationSentMessage.class);
                startActivity(intent);
            }
        });
    }
}