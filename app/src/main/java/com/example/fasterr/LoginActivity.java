package com.example.fasterr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = findViewById(R.id.submitButton);
        final EditText email = findViewById(R.id.inputEmail);
        final TextInputEditText password = findViewById(R.id.inputPassword);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog
                        = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Logging in");
                progressDialog.show();
                final FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                if ((!email.getText().toString().matches("") && (!password.getText().toString().matches("")))) {
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_LONG).show();
                                        String key=mAuth.getCurrentUser().getUid();
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                                        ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String type = dataSnapshot.child("usertype").getValue().toString();
                                                Toast.makeText(getApplicationContext(),"usertype - " + type ,Toast.LENGTH_LONG).show();
                                                progressDialog.dismiss();
                                                if(type.equals("admin")) {
                                                    Intent intent = new Intent(LoginActivity.this, ActivityAdmin.class);
                                                    startActivity(intent);
                                                }
                                                else if(type.equals("editor")) {
                                                    Intent intent = new Intent(LoginActivity.this, EditorActivity.class);
                                                    startActivity(intent);
                                                }
                                                else if(type.equals("library")) {
                                                    Intent intent = new Intent(LoginActivity.this, LibraryActivity.class);
                                                    startActivity(intent);
                                                }
                                                else
                                                {
                                                    Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
                                                    startActivity(intent);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                }
                            });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
