package com.example.loginregisterfire.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginregisterfire.Model.RewardModel;
import com.example.loginregisterfire.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RewardAdapter extends FirestoreRecyclerAdapter<RewardModel, RewardAdapter.RewardViewHolder> {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference documentReference;

    public RewardAdapter(@NonNull FirestoreRecyclerOptions<RewardModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RewardAdapter.RewardViewHolder holder, int position, @NonNull RewardModel model) {
        holder.rank.setText(String.valueOf(position + 1));
        Double dq = model.getDonationReq();
        holder.donationRequired.setText(String.format("%.0f", dq));
        holder.rewardDescription.setText(model.getRewardDesc());

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        String UID = fAuth.getCurrentUser().getUid();

        documentReference = fStore.collection("users").document(UID);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                     Double donated = task.getResult().getDouble("BloodDonated");
                     holder.claim_btn.setVisibility(dq <= donated ? View.VISIBLE : View.GONE);
                }
            }
        });

        holder.claim_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });
    }

    @NonNull
    @Override
    public RewardAdapter.RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.example.loginregisterfire.R.layout.list_rewards_single, parent, false);
        return new RewardViewHolder(view);
    }

    public class RewardViewHolder extends RecyclerView.ViewHolder {

        TextView rank;
        TextView donationRequired;
        TextView rewardDescription;
        Button claim_btn;

        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);

            rank = itemView.findViewById(R.id.rewards_rank);
            donationRequired = itemView.findViewById(R.id.single_reward_amount);
            rewardDescription = itemView.findViewById(R.id.single_reward_desc);
            claim_btn = itemView.findViewById(R.id.claim_btn);
        }
    }
}
