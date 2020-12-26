package com.example.loginregisterfire.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loginregisterfire.Interface.LeaderboardListener;
import com.example.loginregisterfire.Model.UserModel;
import com.example.loginregisterfire.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends FirestoreRecyclerAdapter<UserModel, UserAdapter.UserViewHolder> {

    LeaderboardListener mLeaderboardListener;

    public UserAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, LeaderboardListener listener) {
        super(options);
        mLeaderboardListener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position, @NonNull UserModel model) {

        holder.username.setText(model.getFullName());
        holder.email.setText(model.getEmail());
        holder.score.setText(model.getScore() + "");
        holder.rank.setText(String.valueOf(position + 1));

        if (position == 0 || position == 1 || position == 2 && mLeaderboardListener != null)
            mLeaderboardListener.topLeaderboardListener(position, model.getUrl());

        Glide.with(holder.userImage.getContext())
                .load(model.getUrl())
                .into(holder.userImage);


    }


    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_leaderboard_single, parent, false);
        return new UserViewHolder(view);
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userImage;
        TextView username;
        TextView email;
        TextView score;
        TextView rank;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.list_image);
            username = itemView.findViewById(R.id.list_username);
            email = itemView.findViewById(R.id.list_email);
            score = itemView.findViewById(R.id.list_score);
            rank = itemView.findViewById(R.id.leaderboard_position);

        }
    }
}
