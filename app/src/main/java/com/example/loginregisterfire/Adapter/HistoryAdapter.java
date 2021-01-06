package com.example.loginregisterfire.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginregisterfire.Model.HistoryModel;
import com.example.loginregisterfire.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class HistoryAdapter extends FirestoreRecyclerAdapter<HistoryModel, HistoryAdapter.HistoryViewHolder> {

    String pending;

    public HistoryAdapter(@NonNull FirestoreRecyclerOptions<HistoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position, @NonNull HistoryModel model) {
        holder.rank.setText(String.valueOf(position + 1));
        holder.historyUserName.setText(model.getDonorName());
        holder.historyUserEmail.setText(model.getDonorEmail());
        holder.historyHospitalName.setText(model.getHospitalName());
        holder.historyBookedTime.setText(model.getTime());

        if(model.isDone() == false)
        {
            pending = "Pending";
        }
        else
            pending = "Done";

        holder.historyStatus.setText(pending);
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history_single, parent, false);
        return new HistoryViewHolder(view);
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView rank;
        TextView historyUserName;
        TextView historyUserEmail;
        TextView historyHospitalName;
        TextView historyBookedTime;
        TextView historyStatus;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            rank = itemView.findViewById(R.id.history_position);
            historyUserName = itemView.findViewById(R.id.history_username);
            historyUserEmail = itemView.findViewById(R.id.history_email);
            historyHospitalName = itemView.findViewById(R.id.history_hospital_name);
            historyBookedTime = itemView.findViewById(R.id.history_booking_time);
            historyStatus = itemView.findViewById(R.id.status_booking_history);

        }
    }
}
