package com.example.loginregisterfire;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.loginregisterfire.Adapter.ClaimedRewardAdapter;
import com.example.loginregisterfire.Model.RewardModel;
import com.example.loginregisterfire.Model.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ClaimedRewardDonor extends AppCompatActivity {

    FirebaseFirestore fStore;

    private FirestoreRecyclerAdapter adapter;
    private RecyclerView claimedRewardRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claimed_reward_donor);

        claimedRewardRecycler = findViewById(R.id.claimed_recycler);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        fStore = FirebaseFirestore.getInstance();

        //Query
        Query query = fStore.collection("users")
                .whereNotEqualTo("awardReceived", null);

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class)
                .build();

        adapter = new ClaimedRewardAdapter(options);

        claimedRewardRecycler.setLayoutManager(new LinearLayoutManager(this));
        claimedRewardRecycler.setAdapter(adapter);
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