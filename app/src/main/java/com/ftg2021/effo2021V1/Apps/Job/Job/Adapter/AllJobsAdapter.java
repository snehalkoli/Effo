package com.ftg2021.effo2021V1.Apps.Job.Job.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.JobDetails;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.ftg2021.effo2021V1.Util.DataModels.SingleJobDataModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class AllJobsAdapter extends RecyclerView.Adapter<AllJobsAdapter.JobsDataViewHolder> implements Filterable {

    List<SingleJobDataModel> jobListDataHolder;
    List<SingleJobDataModel> backup;

    public AllJobsAdapter(List<SingleJobDataModel> dataholder) {
        this.jobListDataHolder = dataholder;
        this.backup = new ArrayList<>(dataholder);
    }


    @NonNull
    @Override
    public JobsDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_jobs, parent, false);

        return new JobsDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobsDataViewHolder holder, int position) {

        SingleJobDataModel data = jobListDataHolder.get(position);

        holder.jobType.setText(data.getJobTitle());

        String workFrom="";
        if(data.getJobPlace().equals("10"))
            workFrom="Home";
        else
        if(data.getJobPlace().equals("01"))
            workFrom="Office";


        holder.jobSchedule.setText("work From "+workFrom);
        holder.companyName.setText(""+data.getCompanyName());
        holder.location.setText(data.getLocation());

        holder.parentLinearLayoutAllJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(v.getContext(), JobDetails.class);

                AppConstraints.viewJobData=data;

                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return jobListDataHolder.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            ArrayList<SingleJobDataModel> filtereddata = new ArrayList<>();

            if (keyword.toString().isEmpty()){
                filtereddata.addAll(backup);
            } else {
                for (SingleJobDataModel obj:backup){
//                    if (obj.getJobTitle().toString().toLowerCase().contains(keyword.toString().toLowerCase())){
//                        filtereddata.add(obj);
//                    }
//                    if (obj.getJobTitle().toString().toLowerCase().contains(keyword.toString().toLowerCase())){
//                        filtereddata.add(obj);
//                    } else if (obj.getJobDescription().toString().contains(keyword.toString().toLowerCase())){
//                        filtereddata.add(obj);
//                    }else if (obj.getQualification().toString().contains(keyword.toString().toLowerCase())){
//                        filtereddata.add(obj);
//                    }else if (obj.getExperience().toString().contains(keyword.toString().toLowerCase())){
//                        filtereddata.add(obj);
//                    }else if (obj.getLocation().toString().contains(keyword.toString().toLowerCase())){
//                        filtereddata.add(obj);
//                    }else if (obj.getSalary().toString().contains(keyword.toString().toLowerCase())){
//                        filtereddata.add(obj);
//                    }else if (obj.getJobPlace().toString().contains(keyword.toString().toLowerCase())){
//                        filtereddata.add(obj);
//                    }else if (obj.getCompanyName().toString().contains(keyword.toString().toLowerCase())){
//                        filtereddata.add(obj);
//                    }else if (obj.getJobType().toString().contains(keyword.toString().toLowerCase())){
//                        filtereddata.add(obj);
//                    }else if (obj.getJobType().toString().contains(keyword.toString().toLowerCase())){
//                        filtereddata.add(obj);
//                    }else if (obj.getSalary().toString().contains(keyword.toString().toLowerCase())){
//                        filtereddata.add(obj);
//                    }

                    if ((obj.getJobTitle().toString().toLowerCase().contains(keyword.toString().toLowerCase())
                            || (obj.getLocation().toString().toLowerCase().contains(keyword.toString().toLowerCase())
                            || (obj.getJobDescription().toString().toLowerCase().contains(keyword.toString().toLowerCase())
                            || (obj.getQualification().toString().toLowerCase().contains(keyword.toString().toLowerCase())
                            || (obj.getExperience().toString().toLowerCase().contains(keyword.toString().toLowerCase())
                            || (obj.getLocation().toString().toLowerCase().contains(keyword.toString().toLowerCase())
                            || (obj.getSalary().toString().toLowerCase().contains(keyword.toString().toLowerCase())
                            || (obj.getJobPlace().toString().toLowerCase().contains(keyword.toString().toLowerCase())
                            || (obj.getJobType().toString().toLowerCase().contains(keyword.toString().toLowerCase())
                            || (obj.getCompanyName().toString().toLowerCase().contains(keyword.toString().toLowerCase()))))))))))))
                    {
                        filtereddata.add(obj);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtereddata;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                jobListDataHolder.clear();
                jobListDataHolder.addAll((ArrayList<SingleJobDataModel>) filterResults.values);
                notifyDataSetChanged();
        }
    };


    public class JobsDataViewHolder extends RecyclerView.ViewHolder {

        TextView jobType, jobSchedule, companyName, location;
        LinearLayout parentLinearLayoutAllJobs;

        public JobsDataViewHolder(@NonNull View itemView) {
            super(itemView);
            jobType = itemView.findViewById(R.id.jobType);
            jobSchedule = itemView.findViewById(R.id.jobSchedule);
            companyName = itemView.findViewById(R.id.companyName);
            location = itemView.findViewById(R.id.location);

            parentLinearLayoutAllJobs=itemView.findViewById(R.id.parentLinearLayoutAllJobs);
        }
    }
}
