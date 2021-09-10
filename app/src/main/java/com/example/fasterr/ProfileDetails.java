package com.example.fasterr;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileDetails extends AppCompatActivity {
    Calendar myCalendar;
    EditText dob_edit;
    String type;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_details);
        final TextView name,email,address_edit;
        name=findViewById(R.id.profile_name);
        email=findViewById(R.id.profile_email);
        address_edit=findViewById(R.id.profile_address);
        final LinearLayout dob=findViewById(R.id.dob_container), address=findViewById(R.id.address_container);
        Intent intent=getIntent();
        final String uid = intent.getStringExtra("uid");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                type = dataSnapshot.child("usertype").getValue().toString();
                name.setText(dataSnapshot.child("name").getValue().toString());
                email.setText(dataSnapshot.child("email").getValue().toString());
                Toast.makeText(getApplicationContext(),"usertype - " + type ,Toast.LENGTH_LONG).show();
                if(type.equals("user")) {
                    dob.setVisibility(View.VISIBLE);
                    address.setVisibility(View.GONE);
                    if(dataSnapshot.child("dob").exists())
                    {
                        dob_edit.setText(dataSnapshot.child("dob").getValue().toString());
                    }
                    else
                    {
                        dob_edit.setText("20/11/1997");
                    }
                }
                else if(type.equals("admin")) {
                    dob.setVisibility(View.GONE);
                    address.setVisibility(View.GONE);
                }
                else if(type.equals("editor")) {
                    dob.setVisibility(View.GONE);
                    address.setVisibility(View.GONE);
                }
                else if(type.equals("library"))
                {
                    dob.setVisibility(View.GONE);
                    address.setVisibility(View.VISIBLE);
                    if(dataSnapshot.child("address").exists())
                    {
                        address_edit.setText(dataSnapshot.child("address").getValue().toString());
                    }
                    else
                    {
                        address_edit.setText("Shahibaug");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myCalendar = Calendar.getInstance();

        dob_edit= (EditText) findViewById(R.id.profile_dob);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dob_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ProfileDetails.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button save=findViewById(R.id.save_details);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");
                myRef.child(uid).child("name").setValue(name.getText().toString());
                myRef.child(uid).child("email").setValue(email.getText().toString());
                if(type.equals("user")) {
                    myRef.child(uid).child("dob").setValue(dob_edit.getText().toString());
                }
                else if(type.equals("library"))
                {
                    myRef.child(uid).child("address").setValue(address_edit.getText().toString());
                }
                Toast.makeText(getApplicationContext(),"Success!" ,Toast.LENGTH_LONG).show();
            }
        });
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob_edit.setText(sdf.format(myCalendar.getTime()));
    }
}
