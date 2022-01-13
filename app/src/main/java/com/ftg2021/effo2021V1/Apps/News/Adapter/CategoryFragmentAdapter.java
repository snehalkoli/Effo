package com.ftg2021.effo2021V1.Apps.News.Adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ftg2021.effo2021V1.Apps.News.Model.NewsCategoryModel;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Apps.News.fragments.Discover;

import java.util.List;


public class CategoryFragmentAdapter extends RecyclerView.Adapter<CategoryFragmentAdapter.CategoryDataHolder>
{
    private List<NewsCategoryModel> categoryData;
    private Context context;
    private Discover fragment;

    public CategoryFragmentAdapter(Context c, List<NewsCategoryModel> newsList)
    {
        this.context=c;
        this.categoryData = newsList;
        Log.i("data_setting",""+categoryData.size());
    }

    @NonNull
    @Override
    public CategoryDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_list_single_item_in_fragment,parent,false);
        CategoryDataHolder siteListHolder = new CategoryDataHolder(view);
        return  siteListHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull CategoryDataHolder holder, int position)
    {
        final NewsCategoryModel myListData = categoryData.get(position);
        holder.category_name.setText(categoryData.get(position).getCategory_name());

        int catId = categoryData.get(position).getId();

        setFadeAnimation(holder.category_name);

        Drawable drawable = context.getResources().getDrawable(R.drawable.cat_news);


        switch (holder.category_name.getText().toString().trim()) {
            case "बातमी":
                drawable = context.getResources().getDrawable(R.drawable.cat_news);
                break;
            case "खेळ":
                drawable = context.getResources().getDrawable(R.drawable.cat_sports);
                break;
            case "मनोरंजन":
                drawable = context.getResources().getDrawable(R.drawable.cat_entertainment);
                break;
            case "कृषी":
                drawable = context.getResources().getDrawable(R.drawable.cat_farmer);
                break;
            case "व्यापार":
                drawable = context.getResources().getDrawable(R.drawable.cat_business);
                break;
            case "लक्षवेधी":
                drawable = context.getResources().getDrawable(R.drawable.cat_lakshvedhi);
                break;
            case "पॉलिटिक्स":
                drawable = context.getResources().getDrawable(R.drawable.cat_politics);
                break;
            case "जॉब्स":
                drawable = context.getResources().getDrawable(R.drawable.cat_jobs);
                break;
        }

        try {
            holder.layout.setBackground(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    Discover.catListLayout.setVisibility(View.GONE);
                    Discover.newsByCatIdLayout.setVisibility(View.VISIBLE);
                    Discover.fetchByCatId(""+catId);

                }catch (Exception e){
                    Log.i("ERROR1234",e.getMessage());
                }

            }
        });
    }



    @Override
    public int getItemCount()
    {
        return categoryData.size();
    }

    public static class CategoryDataHolder extends  RecyclerView.ViewHolder
    {

        public TextView category_name;
        public LinearLayout layout;

        public CategoryDataHolder(@NonNull View itemView)
        {
            super(itemView);

            category_name  = itemView.findViewById(R.id.categoryNameInMenu);
            layout = itemView.findViewById(R.id.categoryBackLinearLayout);



        }
    }

    public void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1500);
        view.startAnimation(anim);
    }
}
