package com.example.loginregisterfire.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.loginregisterfire.Model.UserModel;
import com.example.loginregisterfire.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends FirestoreRecyclerAdapter<UserModel, UserAdapter.UserViewHolder> {

    public UserAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position, @NonNull UserModel model) {

        holder.username.setText(model.getFullName());
        holder.email.setText(model.getEmail());
        holder.score.setText(model.getScore()+"");

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

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.list_image);
            username = itemView.findViewById(R.id.list_username);
            email = itemView.findViewById(R.id.list_email);
            score = itemView.findViewById(R.id.list_score);
        }
    }
}
