package com.ftg2021.effo2021V1.Apps.Job.Job.Adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ftg2021.effo2021V1.AppList.AppList;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.EmployeeProfile;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.PostJobs.PostJobDetailStep1;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.ftg2021.effo2021V1.Util.DataModels.SingleJobDataModel;

import java.util.ArrayList;
import java.util.List;

public class JobPostedAdapter extends RecyclerView.Adapter<JobPostedAdapter.JobsDataViewHolder> implements Filterable {

    List<SingleJobDataModel> jobListDataholder;
    List<SingleJobDataModel> backup;
    JobFragmentCallBack jobFragmentCallBack;

    public JobPostedAdapter(List<SingleJobDataModel> dataholder,JobFragmentCallBack jobFragmentCallBack) {
        this.jobListDataholder = dataholder;
        this.backup = new ArrayList<>(dataholder);
        this.jobFragmentCallBack = jobFragmentCallBack;
    }


    @NonNull
    @Override
    public JobsDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_layout_job_list, parent, false);

        return new JobsDataViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull JobsDataViewHolder holder, int position) {

        SingleJobDataModel data = jobListDataholder.get(position);

        holder.title.setText(data.getJobTitle());
       // holder.applicationReceived.setText("3 Applied");
        holder.date.setText(data.getDate());
        holder.salary.setText(data.getSalary() + " \u20B9");
        holder.location.setText(data.getLocation());
        holder.experience.setText(data.getExperience());

        String statusText = (data.getStatus() == 1) ? ("open") : ("close");
        holder.status.setText(statusText);
        data.setSearchStatus(statusText);

        if (data.getStatus() == 1){
            holder.closeJob.setText("Close Job");
        }

        if (data.getStatus() == 0){
            holder.closeJob.setText("Open Job");
        }


        holder.jobEditClickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstraints.jobPostFlow = 1;
                AppConstraints.editJobData = data;

                v.getContext().startActivity(new Intent(v.getContext(), PostJobDetailStep1.class));
            }
        });

        holder.closeJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Are you sure you want to change the job status?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (holder.status.getText().equals("close")){
                                    holder.status.setTextColor(v.getContext().getColor(R.color.lightgreen));
                                    holder.status.setText("open");
                                    holder.closeJob.setText("Close Job");
                                    jobFragmentCallBack.getCallBack(data,1);
                                } else if (holder.status.getText().equals("open")){
                                    holder.status.setTextColor(v.getContext().getColor(R.color.gnt_red));
                                    holder.status.setText("close");
                                    holder.closeJob.setText("Open Job");
                                    jobFragmentCallBack.getCallBack(data,0);
                                }
                            }
                        }).setNegativeButton("Cancel", null);

                AlertDialog alert1 = alert.create();
                alert1.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return jobListDataholder.size();
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
                    if ((obj.getJobTitle().toString().toLowerCase().contains(keyword.toString().toLowerCase()))
                     || (obj.getExperience().toString().toLowerCase().contains(keyword.toString().toLowerCase()))
                     || (obj.getSalary().toString().toLowerCase().contains(keyword.toString().toLowerCase()))
                     ||(obj.getLocation().toString().toLowerCase().contains(keyword.toString().toLowerCase()))
                     ||(obj.getJobType().toString().toLowerCase().contains(keyword.toString().toLowerCase()))
                     ||(obj.getSearchStatus().toString().toLowerCase().contains(keyword.toString().toLowerCase()))
                     ||(obj.getJobPlace().toString().toLowerCase().contains(keyword.toString().toLowerCase()))
                    )
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
            jobListDataholder.clear();
            jobListDataholder.addAll((ArrayList<SingleJobDataModel>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    class JobsDataViewHolder extends RecyclerView.ViewHolder {

        LinearLayout jobEditClickLayout;
        TextView title, applicationReceived, date, salary, location, experience, status, closeJob;

        public JobsDataViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.jobTitlePosted);
            applicationReceived = itemView.findViewById(R.id.applicationReceived);
            date = itemView.findViewById(R.id.postedDate);
            salary = itemView.findViewById(R.id.salary);
            location = itemView.findViewById(R.id.location);
            experience = itemView.findViewById(R.id.experience);
            status = itemView.findViewById(R.id.status);
            closeJob = itemView.findViewById(R.id.closeJob);

            jobEditClickLayout = itemView.findViewById(R.id.jobEditClickLayout);
        }
    }

    public interface JobFragmentCallBack {
        void getCallBack(SingleJobDataModel singleJobDataModel,int status);
    }
}
