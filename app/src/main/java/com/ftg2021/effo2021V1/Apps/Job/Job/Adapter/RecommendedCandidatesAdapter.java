package com.ftg2021.effo2021V1.Apps.Job.Job.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ftg2021.effo2021V1.R;

import java.util.List;

public class RecommendedCandidatesAdapter extends RecyclerView.Adapter<RecommendedCandidatesAdapter.holder> implements View.OnClickListener{
    List<String> name;
    List<String>designation;
    List<String>experience;
    List<String>salary;

    LayoutInflater inflater;

    public RecommendedCandidatesAdapter(Context context, List<String>name, List<String>designation, List<String>experience,List<String>salary){

        this.name=name;
        this.designation=designation;
        this.experience=experience;
        this.salary=salary;
        this.inflater=LayoutInflater.from(context);

    }



    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.recommended_candidate,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.name.setText(name.get(position));
        holder.designation.setText(designation.get(position));
        holder.experience.setText(experience.get(position));
        holder.salary.setText(salary.get(position));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class holder extends RecyclerView.ViewHolder{
        TextView name,designation,experience,salary;
        public holder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            designation=itemView.findViewById(R.id.designation);
            experience=itemView.findViewById(R.id.experience);
            salary=itemView.findViewById(R.id.salary);
        }
    }
}
