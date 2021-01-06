package com.example.loginregisterfire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginregisterfire.Adapter.DonorAdapter;
import com.example.loginregisterfire.Interface.OnListAdminClick;
import com.example.loginregisterfire.Model.DonorModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class AdminView extends AppCompatActivity {

    FirebaseFirestore fStore;
    private FirestoreRecyclerAdapter adapter;
    private RecyclerView adminRecyclerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
        
        //RecyclerView
        adminRecyclerList = findViewById(R.id.admin_view_recycler);

        setupRecyclerView();

        //logout function
        ImageView logoutBtn;
        logoutBtn = findViewById(R.id.logout_admin_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminView.this, Login.class));
                finish();
            }
        });

        ImageView adminReward;
        adminReward = findViewById(R.id.reward_setup);
        adminReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminView.this, RewardAdmin.class));
            }
        });
    }

    private void setupRecyclerView() {
        fStore = FirebaseFirestore.getInstance();

        //Query
        Query query = fStore.collectionGroup("Booking")
                .whereEqualTo("done", false)
                .whereNotEqualTo("donorName", null);

        //RecyclerOptions
        FirestoreRecyclerOptions<DonorModel> options = new FirestoreRecyclerOptions.Builder<DonorModel>()
                .setQuery(query, DonorModel.class)
                .build();

        //Setting up adapter
        adapter = new DonorAdapter(options, new OnListAdminClick() {
            @Override
            public void onItemClick(String donorUID) {
                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                CollectionReference bookingRef = rootRef.collection("UserBooking")
                        .document(donorUID)
                        .collection("Booking");
                Query donorUidQuery = bookingRef.whereEqualTo("donorUID", donorUID);
                donorUidQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot document : task.getResult()){
                                addScore(donorUID);
                                document.getReference().update("done", true);
                                adminRecyclerList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(AdminView.this, donorUID + " donation is successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        //Set recycler to view
        adminRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        adminRecyclerList.setAdapter(adapter);
        adminRecyclerList.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //adapter start
        if (adapter != null)
            adapter.startListening();

        //Firebase user Instance
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(AdminView.this, Login.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //adapter stop
        if (adapter != null)
            adapter.stopListening();
    }

    private void addScore(String UID) {
        fStore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = fStore
                .collection("users")
                .document(UID);

        documentReference.update("Score", FieldValue.increment(10));
        documentReference.update("BloodDonated", FieldValue.increment(1));
    }

}