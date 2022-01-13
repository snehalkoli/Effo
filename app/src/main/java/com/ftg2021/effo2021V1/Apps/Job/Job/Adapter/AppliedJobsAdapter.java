package com.ftg2021.effo2021V1.Apps.Job.Job.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ftg2021.effo2021V1.Apps.Job.Job.Activity.FindJobs.AppliedJobDetailsActivity;
import com.ftg2021.effo2021V1.Apps.News.Model.AppliedJobsModel;
import com.ftg2021.effo2021V1.R;
import com.ftg2021.effo2021V1.Util.AppConstraints;
import java.util.ArrayList;
import java.util.List;

public class AppliedJobsAdapter extends RecyclerView.Adapter<AppliedJobsAdapter.ViewHolder> {
    List<AppliedJobsModel> list;
    List<AppliedJobsModel> backup;
    AdapterCallBack adapterCallBack;

    public AppliedJobsAdapter(List<AppliedJobsModel> dataholder,AdapterCallBack adapterCallBack) {
        this.list = dataholder;
        this.backup = new ArrayList<>(dataholder);
        this.adapterCallBack = adapterCallBack;
    }

    @NonNull
    @Override
    public AppliedJobsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applied_jobs, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AppliedJobsAdapter.ViewHolder holder, int position) {
            AppliedJobsModel data = list.get(position);

            holder.jobType.setText(data.getJobTitle());
            holder.jobSchedule.setText(data.getJobPlace());
            holder.companyName.setText(""+data.getCompanyName());
            holder.location.setText(data.getLocation());

        holder.parentLinearLayoutAllJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), AppliedJobDetailsActivity.class);
                AppConstraints.viewAppliedJobsData=data;
                v.getContext().startActivity(intent);
            }
        });

        holder.tvRevoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallBack.getRevokeData(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView jobType, jobSchedule, companyName, location;
        LinearLayout parentLinearLayoutAllJobs;
        TextView tvApplyNow,tvApplied,tvRevoke;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jobType = itemView.findViewById(R.id.jobType);
            jobSchedule = itemView.findViewById(R.id.jobSchedule);
            companyName = itemView.findViewById(R.id.companyName);
            location = itemView.findViewById(R.id.location);
            tvApplied = itemView.findViewById(R.id.tvApplied);
            tvRevoke = itemView.findViewById(R.id.tvRevoke);

            parentLinearLayoutAllJobs=itemView.findViewById(R.id.parentLinearLayoutAllJobs);
        }
    }

    public interface AdapterCallBack {
        void getRevokeData(AppliedJobsModel data);
    }
}
