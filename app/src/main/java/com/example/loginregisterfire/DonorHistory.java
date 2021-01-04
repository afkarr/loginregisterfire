package com.example.loginregisterfire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.example.loginregisterfire.Adapter.HistoryAdapter;
import com.example.loginregisterfire.Model.HistoryModel;
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

public class DonorHistory extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    StorageReference storageReference;
    CircleImageView historysDonorImage;
    TextView historysName;

    private RecyclerView historysRecyclerList;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_history);

        historysDonorImage = findViewById(R.id.history_userImage);
        historysName = findViewById(R.id.history_userName);

        historysRecyclerList = findViewById(R.id.historys_recycler);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        String UID = fAuth.getCurrentUser().getUid();
        //Query
        Query query = fStore.collectionGroup("Booking")
                .whereEqualTo("donorUID", UID)
                //if error it the orderBy()
                .orderBy("done", Query.Direction.ASCENDING);

        //RecyclerOptions
        FirestoreRecyclerOptions<HistoryModel> options = new FirestoreRecyclerOptions.Builder<HistoryModel>()
                .setQuery(query, HistoryModel.class)
                .build();

        //Setting up adapter
        adapter = new HistoryAdapter(options);

        //Set recycler to view
        historysRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        historysRecyclerList.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //adapter start
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
                    historysName.setText(task.getResult().getString("FullName"));
                }
            }
        });
        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(historysDonorImage);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        //adapter stop
        if (adapter != null)
            adapter.stopListening();
    }
}