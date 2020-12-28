package com.example.loginregisterfire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Login extends AppCompatActivity {

    TextInputEditText textInputEditTextEmail, textInputEditTextPassword;
    Button buttonLogin;
    TextView textviewSignup;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputEditTextEmail = findViewById(R.id.email);
        textInputEditTextPassword = findViewById(R.id.password);
        buttonLogin= findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progress);
        textviewSignup = findViewById(R.id.signUpText);

        fAuth = FirebaseAuth.getInstance();

        // if user is already logged in, no need to re-enter credential the second time.
        if(fAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = textInputEditTextEmail.getText().toString().trim();
                String password = textInputEditTextPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    textInputEditTextPassword.setError("Email is Required.");
                }

                if(TextUtils.isEmpty(password)){
                    textInputEditTextPassword.setError("Password is Required.");
                }

                if(password.length () < 6){
                    textInputEditTextPassword.setError("Password Must be More Than 6 Characters");
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate the user
                FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                CollectionReference userRef = fStore.collection("users");
                CollectionReference adminRef = fStore.collection("admins");

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            userRef.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot document = task.getResult();
                                        if(document.exists()){
                                            String isAdmin = document.getString("isAdmin");
                                            if(isAdmin.equals("1")) {
                                                Toast.makeText(Login.this, "Welcome Back, Admin", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), AdminView.class));
                                            }else if(isAdmin.equals("0")){
                                                Toast.makeText(Login.this, "Welcome, Donor!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            }
                                        }
                                    }
                                }
                            });
                            adminRef.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String isAdmin = document.getString("isAdmin");
                                        if (isAdmin.equals("1")) {
                                            Toast.makeText(Login.this, "Welcome Back, Admin", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), AdminView.class));
                                        } else if (isAdmin.equals("0")) {
                                            Toast.makeText(Login.this, "Welcome, Donor!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        }
                                    }
                                }
                            });
                            //Toast.makeText(Login.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else{
                            Toast.makeText(Login.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        textviewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });
    }
}