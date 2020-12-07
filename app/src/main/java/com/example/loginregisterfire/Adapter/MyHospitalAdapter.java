package com.example.loginregisterfire.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginregisterfire.Model.Hospital;
import com.example.loginregisterfire.R;

import java.util.List;

public class MyHospitalAdapter extends RecyclerView.Adapter<MyHospitalAdapter.MyViewHolder> {

    Context context;
    List<Hospital> hospitalList;

    public MyHospitalAdapter(Context context, List<Hospital> hospitalList) {
        this.context = context;
        this.hospitalList = hospitalList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_hospital, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_hospital_name.setText(hospitalList.get(i).getName());
        myViewHolder.txt_hospital_address.setText(hospitalList.get(i).getAddress());
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_hospital_name, txt_hospital_address;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_hospital_name = (TextView)itemView.findViewById(R.id.txt_hospital_name);
            txt_hospital_address = (TextView)itemView.findViewById(R.id.txt_hospital_address);
        }
    }
}
