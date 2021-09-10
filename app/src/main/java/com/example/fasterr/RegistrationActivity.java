package com.example.fasterr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.RenderProcessGoneDetail;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button register=findViewById(R.id.registerButton);
        final TextView userEmail=findViewById(R.id.registerEmail);
        final TextInputEditText userPassword=findViewById(R.id.registerPassword);
        final TextView name=findViewById(R.id.name);

        final Spinner dropdown = findViewById(R.id.userSpinner);
        String[] items = new String[]{"user", "library", "editor","admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        LinearLayout user=findViewById(R.id.usertype);
        Intent intent=getIntent();
        if(intent.hasExtra("usertype") && intent.getExtras().getBoolean("usertype"))
        {
            user.setVisibility(View.VISIBLE);
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseAuth firebaseAuth;
                firebaseAuth = FirebaseAuth.getInstance();
                Toast.makeText(getApplicationContext(),"email -" + userEmail.getText().toString() +" password - " +userPassword.getText().toString() ,Toast.LENGTH_LONG).show();
                if ((!userEmail.getText().toString().matches("") && (!userPassword.getText().toString().matches("")))) {
                    firebaseAuth.createUserWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString())
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Authentication failed. " + task.getException(), Toast.LENGTH_LONG).show();
                                    } else {
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference("users");
                                        String key=firebaseAuth.getCurrentUser().getUid();
                                        final String type=dropdown.getSelectedItem().toString();
                                        myRef.child(key).setValue(new UserDetailsObject(name.getText().toString(),userEmail.getText().toString(),type)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                if(type.equals("admin")) {
                                                    Intent intent = new Intent(RegistrationActivity.this, ActivityAdmin.class);
                                                    startActivity(intent);
                                                }
                                                else if(type.equals("editor")) {
                                                    Intent intent = new Intent(RegistrationActivity.this, EditorActivity.class);
                                                    startActivity(intent);
                                                }
                                                else if(type.equals("library")) {
                                                    Intent intent = new Intent(RegistrationActivity.this, LibraryActivity.class);
                                                    startActivity(intent);
                                                }
                                                else
                                                {
                                                    Intent intent = new Intent(RegistrationActivity.this, HomeScreenActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
