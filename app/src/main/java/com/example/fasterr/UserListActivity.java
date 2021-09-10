package com.example.fasterr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_list);
        CollapsingToolbarLayout tool=findViewById(R.id.collapsingToolbar);
        tool.setTitle("Manage Users");
        final RecyclerView libraryRecycler=findViewById(R.id.libraryRecycler);
        libraryRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        final List<UserDetailsObject> userList;
        userList=new ArrayList<>();
        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("users");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        UserDetailsObject l=npsnapshot.getValue(UserDetailsObject.class);
                        l.setUid(npsnapshot.getKey());
                        userList.add(l);
                    }
                    UserlistAdapter adapter=new UserlistAdapter(userList);
                    libraryRecycler.setAdapter(adapter);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
