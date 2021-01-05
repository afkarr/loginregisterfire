package com.example.loginregisterfire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginregisterfire.Adapter.AdminRewardAdapter;
import com.example.loginregisterfire.Model.RewardModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class RewardAdmin extends AppCompatActivity {

    TextInputEditText rewardDescription, DonationRequired;
    Button rewardSubmit;

    FirebaseFirestore fStore;

    private FirestoreRecyclerAdapter adapter;
    private RecyclerView adminRewardRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_admin);

        rewardDescription = findViewById(R.id.rewardDesc);
        DonationRequired = findViewById(R.id.donationReq);
        rewardSubmit = findViewById(R.id.admin_reward_button);

        //RecyclerView
        adminRewardRecycler = findViewById(R.id.admin_reward_recycler);

        setupRecyclerView();


        //OnClick Submit Button
        rewardSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rewardD = rewardDescription.getText().toString().trim();
                Double donationR = Double.parseDouble(DonationRequired.getText().toString());

                fStore = FirebaseFirestore.getInstance();

                DocumentReference documentReference = fStore.collection("Rewards")
                        .document();

                Map<String, Object> rewards = new HashMap<>();
                rewards.put("donationReq", donationR);
                rewards.put("rewardDesc", rewardD);

                documentReference.set(rewards).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(RewardAdmin.this, "Reward Submitted!", Toast.LENGTH_SHORT).show();
                        Log.d("REWARD", "onSuccess: rewardDesc -> "+ rewardD);
                    }
                });
            }
        });
    }

    private void setupRecyclerView() {
        fStore = FirebaseFirestore.getInstance();

        //Query
        Query query = fStore.collection("Rewards")
                .orderBy("donationReq", Query.Direction.ASCENDING);;

        FirestoreRecyclerOptions<RewardModel> options = new FirestoreRecyclerOptions.Builder<RewardModel>()
                .setQuery(query, RewardModel.class)
                .build();

        adapter = new AdminRewardAdapter(options);

        //Set recycler to view
        adminRewardRecycler.setLayoutManager(new LinearLayoutManager(this));
        adminRewardRecycler.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //start recycler listening
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //stop recycler listening
        if (adapter != null)
            adapter.stopListening();
    }
}