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

public class DonorAdapter extends FirestoreRecyclerAdapter<DonorModel, DonorAdapter.UserViewHolder> {

    public DonorAdapter(@NonNull FirestoreRecyclerOptions<DonorModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DonorAdapter.UserViewHolder holder, int position, @NonNull DonorModel model) {
        holder.FullName.setText(model.getDonorName());
        holder.Email.setText(model.getDonorEmail());
        holder.HospitalName.setText(model.getHospitalName());
        holder.Time.setText(model.getTime());
        holder.Status.setText(model.isDone() + "");
        holder.rank.setText(String.valueOf(position + 1));

        holder.done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set done status to done
            }
        });
    }

    @NonNull
    @Override
    public DonorAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_adminview_single, parent, false);
        return new UserViewHolder(view);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView FullName, Email, Time, HospitalName, Status, rank;
        Button done_btn;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            FullName = itemView.findViewById(R.id.list_username);
            Email = itemView.findViewById(R.id.list_email);
            Time = itemView.findViewById(R.id.list_booking_time);
            HospitalName = itemView.findViewById(R.id.list_hospital_name);
            done_btn = itemView.findViewById(R.id.done_btn);
            Status = itemView.findViewById(R.id.status_booking);
            rank = itemView.findViewById(R.id.adminview_position);
        }
    }
}
