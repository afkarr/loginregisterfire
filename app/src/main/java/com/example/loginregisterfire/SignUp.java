package com.example.loginregisterfire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity  {

    public static final String TAG = "TAG";
    TextInputEditText textInputEditTextFullName,  textInputEditTextUsername, textInputEditTextPassword, textInputEditTextEmail;
    Button buttonSignUp;
    CheckBox isAdmin, isDonor;
    TextView textViewLogin;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    long points = 0;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        isAdmin = findViewById(R.id.admincheckBox);
        isDonor = findViewById(R.id.donorcheckBox);
        textInputEditTextFullName =findViewById(R.id.fullname);
        textInputEditTextUsername = findViewById(R.id.username);
        textInputEditTextEmail = findViewById(R.id.email);
        textInputEditTextPassword = findViewById(R.id.password);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewLogin = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progress);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // test error
        // if user is already logged in, no need to re-enter credential the second time.
        //if(fAuth.getCurrentUser() != null){
        //    finish();
        //    startActivity(new Intent(getApplicationContext(), MainActivity.class));
      //  }

        isAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isDonor.setChecked(false);
                }
            }
        });

        isDonor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isAdmin.setChecked(false);
                }
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = textInputEditTextEmail.getText().toString().trim();
                String password = textInputEditTextPassword.getText().toString().trim();
                final String fullName = textInputEditTextFullName.getText().toString();
               final String Username =  textInputEditTextUsername.getText().toString();


                if(TextUtils.isEmpty(email)){
                    textInputEditTextEmail.setError("Email is Required.");
                }

                if(TextUtils.isEmpty(password)){
                    textInputEditTextPassword.setError("Password is Required.");
                }

                if(password.length () < 6){
                    textInputEditTextPassword.setError("Password Must be More Than 6 Characters");
                }

                //checkbox validation

                if(!(isAdmin.isChecked() || isDonor.isChecked())){
                    Toast.makeText(SignUp.this, "Select The Account Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignUp.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReferenceUser = fStore.collection("users").document(userID);
                            DocumentReference documentReferenceAdmin = fStore.collection("admins").document(userID);

                            if(isDonor.isChecked()) {
                                Map<String, Object> user = new HashMap<>();
                                user.put("FullName", fullName);
                                user.put("Email", email);
                                user.put("Username", Username);
                                user.put("Score", points);
                                user.put("isAdmin", "0");

                                documentReferenceUser.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(TAG, "onSuccess: user Profile is created for "+userID);
                                    }
                                });
                            }


                            if(isAdmin.isChecked()){
                                Map<String, Object> user1 = new HashMap<>();
                                user1.put("FullName", fullName);
                                user1.put("Email", email);
                                user1.put("Username", Username);
                                user1.put("isAdmin", "1");

                                documentReferenceAdmin.set(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(TAG, "onSuccess: user Profile is created for "+userID);
                                    }
                                });
                            }

                            if(isDonor.isChecked()) {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }

                            if(isAdmin.isChecked()){
                                startActivity(new Intent(getApplicationContext(), AdminView.class));
                            }

                        }else {
                            Toast.makeText(SignUp.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}