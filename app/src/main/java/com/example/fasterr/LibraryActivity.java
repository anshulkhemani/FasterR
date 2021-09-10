package com.example.fasterr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LibraryActivity extends AppCompatActivity {
    private EditText bookName,authorName,description;
    private Button alertAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        TextView logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(getApplicationContext())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(LibraryActivity.this, WelcomeActivity.class));
                                finish();
                            }
                        });
            }
        });

        TextView add=findViewById(R.id.add_book);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LibraryActivity.this);
                LayoutInflater inflater = LibraryActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_library_book, null);
                dialogBuilder.setView(dialogView);
                bookName=dialogView.findViewById(R.id.alertBookName);
                authorName=dialogView.findViewById(R.id.alertBookAuthor);
                description=dialogView.findViewById(R.id.alertBookDescription);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                alertAdd=dialogView.findViewById(R.id.alertAdd);
                alertAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("books");
                        myRef.child(bookName.getText().toString().replace(" ","")).setValue(new BookDataObject(bookName.getText().toString(),authorName.getText().toString(),description.getText().toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(LibraryActivity.this, "Book added!", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        });
                    }
                });

            }
        });

        TextView update=findViewById(R.id.update_library);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LibraryActivity.this, ProfileDetails.class);
                intent.putExtra("uid", FirebaseAuth.getInstance().getUid());
                startActivity(intent);
            }
        });
    }
}
