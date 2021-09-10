package com.example.fasterr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

public class ActivityBookList extends AppCompatActivity {
    private String cat;
    private RecyclerView recyclerView;
    private List<BookDataObject> bookList;
    private CollapsingToolbarLayout collapsingToolbar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_list);
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
        cat=intent.getStringExtra("category");
        collapsingToolbar=findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitle(cat);
        recyclerView = findViewById(R.id.libraryRecycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        AddItemsToRecyclerViewArrayList();
    }
    public void AddItemsToRecyclerViewArrayList() {
        bookList=new ArrayList<>();
        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("books");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        BookDataObject l=npsnapshot.getValue(BookDataObject.class);
                        if(l.getCategory().equals(cat))
                            bookList.add(l);
                    }
                    BookAdapter adapter=new BookAdapter(bookList,"booklist");
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
