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
        fStore = FirebaseFirestore.getInstance();

        //Query
        //CollectionReference donorRef = fStore.collection("UserBooking");
        //Query query = donorRef.whereEqualTo("done", false);
        Query query = fStore.collectionGroup("Booking")
                .whereEqualTo("done", false)
                .whereNotEqualTo("donorName", null);

        //RecyclerOptions
        FirestoreRecyclerOptions<DonorModel> options = new FirestoreRecyclerOptions.Builder<DonorModel>()
                .setQuery(query, DonorModel.class)
                .build();

        //RecyclerAdapter
         adapter = new FirestoreRecyclerAdapter<DonorModel, DonorsViewModel>(options) {
            @NonNull
            @Override
            public DonorsViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_adminview_single, parent, false);
                return new DonorsViewModel(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DonorsViewModel holder, int position, @NonNull DonorModel model) {
                holder.rank.setText(String.valueOf(position + 1));
                holder.donorName.setText(model.getDonorName());
                holder.donorEmail.setText(model.getDonorEmail());
                holder.hospitalName.setText(model.getHospitalName());
                holder.bookingTime.setText(model.getTime());
                holder.status.setText(model.isDone() + "");

            }
        };

         //set recycler to view
        adminRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        adminRecyclerList.setAdapter(adapter);
        adminRecyclerList.invalidate();


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

    //ViewHolders

    private class DonorsViewModel extends RecyclerView.ViewHolder{

        private TextView rank;
        private TextView donorName;
        private TextView donorEmail;
        private TextView hospitalName;
        private TextView bookingTime;
        private TextView status;
        private Button done_btn;

        public DonorsViewModel(@NonNull View itemView) {
            super(itemView);

            rank = itemView.findViewById(R.id.adminview_position);
            donorName = itemView.findViewById(R.id.list_username);
            donorEmail = itemView.findViewById(R.id.list_email);
            hospitalName = itemView.findViewById(R.id.list_hospital_name);
            bookingTime = itemView.findViewById(R.id.list_booking_time);
            status = itemView.findViewById(R.id.status_booking);
            done_btn = itemView.findViewById(R.id.done_btn);

        }
    }
}