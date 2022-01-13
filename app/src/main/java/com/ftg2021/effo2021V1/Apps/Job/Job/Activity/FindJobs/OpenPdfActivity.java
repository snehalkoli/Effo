package com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;

public class OpenPdfActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_pdf);
        webView = findViewById(R.id.webview);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("https://api.effoapp.com/uploads/311202112326163.pdf");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setBuiltInZoomControls(true);

        try {
            String pdfLink = getIntent().getStringExtra("PdfLink");
            String pdf = "https://api.effoapp.com/"+ pdfLink;
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdf);
        }catch (Exception e){

        }
        //String pdf = "https://api.effoapp.com/uploads/311202112326163.pdf";
//        String pdf = "https://api.effoapp.com/"+AppConstraints.currentEmployeeData.getResumeLink();


        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
            }
        });
    }
}
