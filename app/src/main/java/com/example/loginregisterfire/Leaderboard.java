package com.example.loginregisterfire;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.loginregisterfire.Adapter.UserAdapter;
import com.example.loginregisterfire.Interface.LeaderboardListener;
import com.example.loginregisterfire.Model.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.StorageReference;

public class Leaderboard extends Fragment{

    private RecyclerView leaderboard_recycler;
    private FirestoreRecyclerAdapter adapter;
    FirebaseFirestore fStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        leaderboard_recycler = view.findViewById(R.id.leaderboard_list);
        setUpRecyclerView();

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null)
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null)
        adapter.startListening();
    }


    private void setUpRecyclerView() {
        fStore = FirebaseFirestore.getInstance();

        //Query
        Query query = fStore.collection("users")
                .orderBy("Score", Query.Direction.DESCENDING);

        //RecyclerOptions
        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class)
                .build();

        adapter = new UserAdapter(options);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        leaderboard_recycler.setLayoutManager(linearLayoutManager);
        leaderboard_recycler.setAdapter(adapter);
        leaderboard_recycler.invalidate();
    }

}