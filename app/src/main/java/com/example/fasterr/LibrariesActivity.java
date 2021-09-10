package com.example.fasterr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class LibrariesActivity extends AppCompatActivity {

    ArrayList<LibraryDataObject> source;
    LibraryAdapter adapter;
    RecyclerView libraryRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        libraryRecycler = findViewById(R.id.libraryRecycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(LibrariesActivity.this, LinearLayoutManager.VERTICAL, false);
        libraryRecycler.setLayoutManager(layoutManager);
        AddItemsToRecyclerViewArrayList();

    }

    public void AddItemsToRecyclerViewArrayList() {

        source=new ArrayList<>();
        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("users");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    int x=1;
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        if(npsnapshot.child("usertype").getValue().equals("library")) {
                            LibraryDataObject l = npsnapshot.getValue(LibraryDataObject.class);
                            int dist=(int)((Math.random() * (20 - 2)) + 2);
                            l.setDistance(dist + " km");
                            l.setNum(x);
                            l.setUid(npsnapshot.getKey());
                            x++;
                            source.add(l);
                        }
                    }
                    LibraryAdapter adapter = new LibraryAdapter(source);
                    libraryRecycler.setAdapter(adapter);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
