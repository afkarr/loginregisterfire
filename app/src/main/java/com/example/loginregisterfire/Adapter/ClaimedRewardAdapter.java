package com.example.loginregisterfire.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginregisterfire.Model.UserModel;
import com.example.loginregisterfire.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ClaimedRewardAdapter extends FirestoreRecyclerAdapter<UserModel, ClaimedRewardAdapter.DonorViewHolder> {

    public ClaimedRewardAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ClaimedRewardAdapter.DonorViewHolder holder, int position, @NonNull UserModel model) {
        holder.rank.setText(String.valueOf(position + 1));
        holder.donorName.setText(model.getFullName());
        holder.donorEmail.setText(model.getEmail());
        holder.claimReward.setText(model.getAwardReceived());
    }

    @NonNull
    @Override
    public ClaimedRewardAdapter.DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_claimed_single, parent, false);
        return new DonorViewHolder(view);
    }

    public class DonorViewHolder extends RecyclerView.ViewHolder {

        TextView rank;
        TextView donorName;
        TextView donorEmail;
        TextView claimReward;

        public DonorViewHolder(@NonNull View itemView) {
            super(itemView);

            rank = itemView.findViewById(R.id.claim_position);
            donorName = itemView.findViewById(R.id.claim_username);
            donorEmail = itemView.findViewById(R.id.claim_email);
            claimReward = itemView.findViewById(R.id.claim_reward_description);

        }
    }
}
