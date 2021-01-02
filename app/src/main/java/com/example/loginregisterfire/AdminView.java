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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.loginregisterfire.Adapter.DonorAdapter;
import com.example.loginregisterfire.Model.DonorModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
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
        Button logoutBtn;
        logoutBtn = findViewById(R.id.logout_admin_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminView.this, Login.class));
                finish();
            }
        });
    }

    private void setupRecyclerView() {
        fStore = FirebaseFirestore.getInstance();

        //Query
        Query query = fStore.collectionGroup("Booking")
                .whereEqualTo("done", false)
                .whereNotEqualTo("donorName", null)
                //if error its because of line 69
                .orderBy("done", Query.Direction.DESCENDING);

        //RecyclerOptions
        FirestoreRecyclerOptions<DonorModel> options = new FirestoreRecyclerOptions.Builder<DonorModel>()
                .setQuery(query, DonorModel.class)
                .build();

        //Setting up adapter
        adapter = new DonorAdapter(options);

        //Set recycler to view
        adminRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        adminRecyclerList.setAdapter(adapter);
        adminRecyclerList.invalidate();
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

}