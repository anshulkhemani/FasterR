package com.example.fasterr;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityLibraryBooks extends AppCompatActivity {
    private String uid;
    private List<BookDataObject> source;
    private LibraryBooksAdapter  adapter;
    private RecyclerView libraryRecycler;
    private DatabaseReference ref;
    private TextView address,distance;
    private CollapsingToolbarLayout name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_books);
        AppBarLayout appBar = findViewById(R.id.appBarLayout);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                View v=findViewById(R.id.divider);
                v.setAlpha(1.0f - Math.abs(verticalOffset / (float)
                        appBarLayout.getTotalScrollRange()));
            }
        });
        Intent intent=getIntent();
        uid=intent.getStringExtra("uid");
        ref= FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        name=findViewById(R.id.collapsingToolbar);
        address=findViewById(R.id.lib_address);
        distance=findViewById(R.id.lib_dist);
        distance.setText(intent.getStringExtra("dist"));
        libraryRecycler=findViewById(R.id.categoryRecycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityLibraryBooks.this, LinearLayoutManager.VERTICAL, false);
        libraryRecycler.setLayoutManager(layoutManager);
        AddItemsToRecyclerViewArrayList();
        EditText searchField=findViewById(R.id.library_book_search);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }
    public void AddItemsToRecyclerViewArrayList() {

        source=new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int x=1;
                if (dataSnapshot.exists()){
                    name.setTitle(dataSnapshot.child("name").getValue().toString());
                    address.setText(dataSnapshot.child("address").getValue().toString());
                    for (DataSnapshot npsnapshot : dataSnapshot.child("books").getChildren())
                    {
                            BookDataObject l = npsnapshot.getValue(BookDataObject.class);
                            l.setNum(x);
                            source.add(l);
                            x++;
                    }
                    adapter = new LibraryBooksAdapter(source);
                    libraryRecycler.setAdapter(adapter);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    void filter(String text){
        List<BookDataObject> temp = new ArrayList<>();
        for(BookDataObject d: source){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getBookName().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
            else if(d.getAuthor().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        adapter = new LibraryBooksAdapter(temp);
        libraryRecycler.setAdapter(adapter);
    }
}
