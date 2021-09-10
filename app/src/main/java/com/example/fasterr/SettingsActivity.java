package com.example.fasterr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    String uid;
    TextView feedbackText;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        TextView profile=findViewById(R.id.accountSettings);
        TextView logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(getApplicationContext())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(SettingsActivity.this, WelcomeActivity.class));
                                finish();
                            }
                        });
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this,ProfileDetails.class);
                intent.putExtra("uid",FirebaseAuth.getInstance().getUid());
                startActivity(intent);
            }
        });
        TextView upgrade=findViewById(R.id.upgrade);
        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        TextView feedback=findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SettingsActivity.this);
                LayoutInflater inflater = SettingsActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_feedback, null);
                dialogBuilder.setView(dialogView);
                feedbackText = dialogView.findViewById(R.id.give_feedback);
                submit=dialogView.findViewById(R.id.alertFeedback);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date date = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String strDate= formatter.format(date);
                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("feedback");
                        Map<String,String> mp=new HashMap<>();
                        mp.put("review",feedbackText.getText().toString());
                        mp.put("date",strDate);
                        myRef.child(FirebaseAuth.getInstance().getUid()).setValue(mp).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(getApplicationContext(),"Feedback submitted.",Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        });
                    }
                });
            }
        });
    }
}
