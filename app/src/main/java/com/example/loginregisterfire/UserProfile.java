package com.example.loginregisterfire;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;

import javax.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class UserProfile extends Fragment {
    TextView usernameText, fullNameText, email;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    ImageView profilePicture;
    Button editProfile_btn,logoutBtn;
    StorageReference storageReference;

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile,container,false);
        return view;

    }

    public UserProfile() {
    }

    @Override
    public void onActivityCreated(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        usernameText = getActivity().findViewById(R.id.usernameText);
        fullNameText = getActivity().findViewById(R.id.fullNameText);
        email = getActivity().findViewById(R.id.emailText);
        profilePicture = getActivity().findViewById(R.id.profilePicture);
        logoutBtn = getActivity().findViewById(R.id.logout_btn);
        editProfile_btn = getActivity().findViewById(R.id.editProfile_btn);
    }

    @Override
    public void onStart() {
        super.onStart();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        String userID;

        userID = fAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = fStore.collection("users").document(userID);

//        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                //usernameText.setText(documentSnapshot.getString("Username"));
//                fullNameText.setText(documentSnapshot.getString("FullName"));
//                email.setText(documentSnapshot.getString("Email"));
//
//                if(documentSnapshot.get("isAdmin") != null){
//                    usernameText.setText("Admin");
//                }
//
//                if(documentSnapshot.get("isDonor") != null){
//                    usernameText.setText("Donor");
//                }
//            }
//        });

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){
                    Log.d("TAG", "onComplete: "+ task.getResult().getString("Username"));
                    fullNameText.setText(task.getResult().getString("FullName"));
                    email.setText(task.getResult().getString("Email"));

                    if(task.getResult().getString("isAdmin") != null){
                        usernameText.setText("Admin");
                    }
                    if(task.getResult().getString("isDonor") != null){
                        usernameText.setText("Donor");
                    }
                }
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
                getActivity().finish();
            }
        });

        editProfile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EditProfile.class);
                i.putExtra("FullName", fullNameText.getText().toString());
                i.putExtra("Email",email.getText().toString());
                startActivity(i);
            }
        });
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
        getActivity().finish();
    }

}

//       startActivity(new Intent(getActivity().getApplicationContext(), Login.class));