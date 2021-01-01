package com.example.loginregisterfire.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginregisterfire.Model.DonorModel;
import com.example.loginregisterfire.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class DonorAdapter extends FirestoreRecyclerAdapter<DonorModel, DonorAdapter.DonorViewHolder> {

    public DonorAdapter(@NonNull FirestoreRecyclerOptions<DonorModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DonorAdapter.DonorViewHolder holder, int position, @NonNull DonorModel model) {
        holder.rank.setText(String.valueOf(position + 1));
        holder.donorName.setText(model.getDonorName());
        holder.donorEmail.setText(model.getDonorEmail());
        holder.hospitalName.setText(model.getHospitalName());
        holder.bookingTime.setText(model.getTime());
        holder.status.setText(model.isDone() + "");

        holder.done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set done status to done
            }
        });
    }

    @NonNull
    @Override
    public DonorAdapter.DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_adminview_single, parent, false);
        return new DonorViewHolder(view);
    }

    public class DonorViewHolder extends RecyclerView.ViewHolder {

        TextView rank;
        TextView donorName;
        TextView donorEmail;
        TextView hospitalName;
        TextView bookingTime;
        TextView status;
        Button done_btn;

        public DonorViewHolder(@NonNull View itemView) {
            super(itemView);

            rank = itemView.findViewById(R.id.adminview_position);
            donorName = itemView.findViewById(R.id.list_username);
            donorEmail = itemView.findViewById(R.id.list_email);
            hospitalName = itemView.findViewById(R.id.list_hospital_name);
            bookingTime = itemView.findViewById(R.id.list_booking_time);
            status = itemView.findViewById(R.id.status_booking);
            done_btn = itemView.findViewById(R.id.done_btn);
        }
    }
}
