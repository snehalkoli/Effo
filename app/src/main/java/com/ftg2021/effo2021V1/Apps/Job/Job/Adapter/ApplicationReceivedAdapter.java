package com.ftg2021.effo2021V1.Apps.Job.Job.Adapter;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;


import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.EmployeeProfile;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import com.ftg2021.effo2021V1.Util.DataModels.SingleApplicationReceived;
import com.ftg2021.effo2021V1.Util.DataModels.SingleJobDataModel;

import java.util.ArrayList;
import java.util.List;

public class ApplicationReceivedAdapter extends RecyclerView.Adapter<ApplicationReceivedAdapter.ApplicationDataViewHolder> implements Filterable {

    List<SingleApplicationReceived> applicationsDataHolder;
    List<SingleApplicationReceived> backup;
    Context context;


    public ApplicationReceivedAdapter(List<SingleApplicationReceived> applicationList, Context context) {
        applicationsDataHolder = applicationList;
        this.backup = new ArrayList<>(applicationList);
        this.context = context;
    }


    @NonNull
    @Override
    public ApplicationDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_received, parent, false);

        return new ApplicationReceivedAdapter.ApplicationDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationDataViewHolder holder, int position) {

        SingleApplicationReceived data=applicationsDataHolder.get(position);

        holder.name.setText(data.getemployeeName());
        holder.designation.setText(data.getJobTitle());
        holder.experience.setText(data.getExperience());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstraints.currentApplicationData=data;
                AppConstraints.viewEmployeeFlow=1;

                Intent intent = new Intent(v.getContext(),EmployeeProfile.class);
                intent.putExtra("setData","true");
                intent.putExtra("id",data.getId());
                intent.putExtra("employee_id",data.getEmployeeId());
                intent.putExtra("job_id",data.getJobId());
                intent.putExtra("date",data.getDate());
                intent.putExtra("status",data.getStatus());
                v.getContext().startActivity(intent);
//                v.getContext().startActivity(new Intent(v.getContext(), EmployeeProfile.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return applicationsDataHolder.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            ArrayList<SingleApplicationReceived> filtereddata = new ArrayList<>();

            if (keyword.toString().isEmpty()){
                filtereddata.addAll(backup);
            } else {
                for (SingleApplicationReceived obj:backup){
                    if ((obj.getemployeeName().toString().toLowerCase().contains(keyword.toString().toLowerCase()))
                            || (obj.getJobTitle().toString().toLowerCase().contains(keyword.toString().toLowerCase()))
                            || (obj.getExperience().toString().toLowerCase().contains(keyword.toString().toLowerCase()))
                            ||(obj.getDate().toString().toLowerCase().contains(keyword.toString().toLowerCase()))
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
            applicationsDataHolder.clear();
            applicationsDataHolder.addAll((ArrayList<SingleApplicationReceived>) filterResults.values);
            if (((ArrayList<?>) filterResults.values).isEmpty()){
                Toast.makeText(context, "No match found", Toast.LENGTH_SHORT).show();
            }
            notifyDataSetChanged();
        }
    };

    public class ApplicationDataViewHolder extends RecyclerView.ViewHolder {
        TextView name,designation,experience,salary;
        LinearLayout parentLayout;
        public ApplicationDataViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            designation=itemView.findViewById(R.id.designation);
            experience=itemView.findViewById(R.id.experience);
            parentLayout=itemView.findViewById(R.id.linearLayoutApplicationReceived);
        }
    }
}
