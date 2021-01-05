package com.example.loginregisterfire.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginregisterfire.Model.RewardModel;
import com.example.loginregisterfire.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminRewardAdapter extends FirestoreRecyclerAdapter<RewardModel, AdminRewardAdapter.RewardViewHolder> {

    FirebaseFirestore fStore;

    public AdminRewardAdapter(@NonNull FirestoreRecyclerOptions<RewardModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AdminRewardAdapter.RewardViewHolder holder, int position, @NonNull RewardModel model) {

        fStore = FirebaseFirestore.getInstance();

        holder.rank.setText(String.valueOf(position + 1));
        Double dq = model.getDonationReq();
        holder.donationRequired.setText(String.format("%.0f", dq));
        holder.rewardDescription.setText(model.getRewardDesc());

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CollectionReference collectionReference = fStore.collection("Rewards");

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete " + (position + 1) + " reward?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Query query = collectionReference.whereEqualTo("rewardDesc", model.getRewardDesc());
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(DocumentSnapshot document : task.getResult()){
                                        document.getReference().delete();
                                        Log.d("DELETETAB", "test :" + document.getId());
                                    }
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });

    }

    @NonNull
    @Override
    public AdminRewardAdapter.RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lists_adminrewards_single, parent, false);
        return new RewardViewHolder(view);
    }

    public class RewardViewHolder extends RecyclerView.ViewHolder {

        TextView rank;
        TextView donationRequired;
        TextView rewardDescription;
        ImageView delete_btn;

        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);

            rank = itemView.findViewById(R.id.admin_rewards_rank);
            donationRequired = itemView.findViewById(R.id.admin_single_reward_amount);
            rewardDescription = itemView.findViewById(R.id.admin_single_reward_desc);
            delete_btn = itemView.findViewById(R.id.remove_btn);
        }
    }
}
