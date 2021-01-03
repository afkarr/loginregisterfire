package com.example.loginregisterfire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.example.loginregisterfire.Adapter.RewardAdapter;
import com.example.loginregisterfire.Model.RewardModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonorRewards extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    StorageReference storageReference;
    CircleImageView rewardsDonorImage;
    TextView rewardsName, rewardsDonated;

    private FirestoreRecyclerAdapter adapter;
    private RecyclerView rewardsRecyclerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_rewards);

        rewardsDonorImage = findViewById(R.id.rewards_donor_image);
        rewardsName = findViewById(R.id.rewards_userName);
        rewardsDonated = findViewById(R.id.rewards_amount_donor);

        //RecyclerView
        rewardsRecyclerList = findViewById(R.id.rewards_recycler);
        
        setupRecyclerView();

    }

    private void setupRecyclerView() {
        fStore = FirebaseFirestore.getInstance();
        //Query
        Query query = fStore.collection("Rewards")
                .orderBy("donationReq", Query.Direction.ASCENDING);

        //RecyclerOptions
        FirestoreRecyclerOptions<RewardModel> options = new FirestoreRecyclerOptions.Builder<RewardModel>()
                .setQuery(query, RewardModel.class)
                .build();

        //Setting adapter
        adapter = new RewardAdapter(options);

        //Set recycler to view
        rewardsRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        rewardsRecyclerList.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //start recycler listening
        if (adapter != null)
            adapter.startListening();

        //Donor Info
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        String userID;
        userID = fAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    rewardsName.setText(task.getResult().getString("FullName"));
                    Double bd = task.getResult().getDouble("BloodDonated");
                    rewardsDonated.setText(String.format("%.0f", bd));
                }
            }
        });
        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(rewardsDonorImage);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        //stop recycler listening
        if (adapter != null)
            adapter.stopListening();
    }
}