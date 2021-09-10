package com.example.fasterr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class NewsSummary extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private RecyclerView newsRecycler;
    private ArrayList<AddNewsObject> source;
    private String newstype="Top Stories";
    private DatabaseReference myRef;
    private TextView newsType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_summary);

        dl = findViewById(R.id.news_activity);
        newsType=findViewById(R.id.news_type);
        t = new ActionBarDrawerToggle(NewsSummary.this, dl,R.string.category_picker_headline, R.string.category_picker_headline);
        source=new ArrayList<AddNewsObject>();
        dl.addDrawerListener(t);
        t.syncState();
        final Toolbar mToolbar = findViewById(R.id.newsToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = findViewById(R.id.news_navigation_view);
        newsRecycler = findViewById(R.id.newsRecycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(NewsSummary.this, LinearLayoutManager.VERTICAL, false);
        newsRecycler.setLayoutManager(layoutManager);
        myRef = FirebaseDatabase.getInstance().getReference("news");
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(newsRecycler);
        news();
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.india:
                        newstype="India";
                        break;
                    case R.id.topStories:
                        newstype="Top Stories";
                        break;
                    case R.id.global:
                        newstype="Global";
                        break;
                    case R.id.business:
                        newstype="Business";
                        break;
                    case R.id.politics:
                        newstype="Politics";
                    case R.id.sports:
                        newstype="Sports";
                        break;
                    case R.id.technology:
                        newstype="Technology";
                        break;
                    case R.id.entertainment:
                        newstype="Entertainment";
                        break;
                    default:
                        return true;
                }
                news();
                dl.closeDrawer(GravityCompat.START);
                newsType.setText(newstype);
                return true;
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
    private void news()
    {
        source.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    AddNewsObject obj = ds.getValue(AddNewsObject.class);
                    if(obj.getType().equals(newstype))
                        source.add(obj);
                }
                NewsAdapter adapter = new NewsAdapter(source);
                newsRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("yolo", "Failed to read value.", error.toException());
            }
        });
    }
}
