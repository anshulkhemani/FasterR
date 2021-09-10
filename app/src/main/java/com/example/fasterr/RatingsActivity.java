package com.example.fasterr;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RatingsActivity extends AppCompatActivity {
    private List<RatingsDataObject> ratingsList;
    private RecyclerView recyclerView;
    private String bookName;
    private Button rateBook,rate;
    private TextView name,review;
    private String nam;
    MaterialRatingBar ratingBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);
        Intent intent = getIntent();
        bookName = intent.getStringExtra("bookName");
        AppBarLayout appBar = findViewById(R.id.appBarLayout);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                View v=findViewById(R.id.divider);
                v.setAlpha(1.0f - Math.abs(verticalOffset / (float)
                        appBarLayout.getTotalScrollRange()));
            }
        });
        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid());
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nam=dataSnapshot.child("name").getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        recyclerView=findViewById(R.id.ratingsRecycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        AddItemsToRecyclerViewArrayList();
        rateBook=findViewById(R.id.rate_book);
        rateBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RatingsActivity.this);
                LayoutInflater inflater = RatingsActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_review, null);
                dialogBuilder.setView(dialogView);
                review = dialogView.findViewById(R.id.give_review);
                ratingBar=dialogView.findViewById(R.id.rating_bar);
                rate=dialogView.findViewById(R.id.alertRate);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                rate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date date = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String strDate= formatter.format(date);
                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("books").child(bookName).child("ratings");
                        myRef.child(FirebaseAuth.getInstance().getUid()).setValue(new RatingsDataObject((Integer.toString(ratingBar.getProgress())),review.getText().toString(),nam, strDate)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(getApplicationContext(),"Review submitted.",Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        });
                    }
                });
            }
        });

    }

    public void AddItemsToRecyclerViewArrayList() {
        ratingsList=new ArrayList<>();
        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("books").child(bookName).child("ratings");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        RatingsDataObject l=npsnapshot.getValue(RatingsDataObject.class);
                        ratingsList.add(l);
                    }
                    RatingsAdapter adapter=new RatingsAdapter(ratingsList);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
