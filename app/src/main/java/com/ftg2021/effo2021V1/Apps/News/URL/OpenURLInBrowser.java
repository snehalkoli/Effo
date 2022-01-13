package com.ftg2021.effo2021V1.Apps.News.URL;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class OpenURLInBrowser {

    public OpenURLInBrowser(Context context, String url)
    {
        try
        {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }catch (Exception e){
            Log.i("URL EXCEPTION",e.getMessage());
        }
    }


}
