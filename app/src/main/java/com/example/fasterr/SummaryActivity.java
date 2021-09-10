package com.example.fasterr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummaryActivity extends AppCompatActivity {

    ArrayList<SummaryDataObject> source;
    private String bookName;
    private ArrayList<List<String>> mp;
    private ImageView wishlist;
    private boolean wi=false;
    private MaterialRatingBar m;
    private TextView ratingNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_summary);
        final TextView book_name,author_name,desc,aboutBook,who,aboutA,ti,pages;
        final AppBarLayout img=findViewById(R.id.appBarLayout);
        CollapsingToolbarLayout collapsingToolbar=findViewById(R.id.collapsingToolbar);
        book_name=findViewById(R.id.book_name);
        author_name=findViewById(R.id.author_name);
        aboutBook=findViewById(R.id.about);
        desc=findViewById(R.id.description);
        who=findViewById(R.id.who_for);
        aboutA=findViewById(R.id.about_author);
        wishlist=findViewById(R.id.imgWishlist);
        ti=findViewById(R.id.time);
        pages=findViewById(R.id.noP);
        Intent intent=getIntent();
        bookName=intent.getStringExtra("bookName");
        collapsingToolbar.setTitle(bookName);
        bookName=bookName.replaceAll(" ","");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("books/"+bookName.replace(" ",""));
        GlideApp.with(SummaryActivity.this).asBitmap()
                .load(storageReference)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady( Bitmap resource, Transition<? super Bitmap> transition)  {
                        Drawable d = new BitmapDrawable(getResources(), resource);
                        img.setBackground(d);
                    }
                });

        final RecyclerView summaryRecycler = findViewById(R.id.summary_recycler);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("books");
        ref.child(bookName.replace(" ","")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                    book_name.setText(dataSnapshot.child("bookName").getValue().toString());
                    author_name.setText(dataSnapshot.child("author").getValue().toString());
                    aboutBook.setText(dataSnapshot.child("about").getValue().toString());
                    desc.setText(dataSnapshot.child("description").getValue().toString());
                    ti.setText(dataSnapshot.child("time").getValue().toString());
                    pages.setText(dataSnapshot.child("pages").getValue().toString()+" pages");
                    who.setText(dataSnapshot.child("whoFor").getValue().toString());
                    aboutA.setText(dataSnapshot.child("aboutAuthor").getValue().toString());
                    mp=new ArrayList<>();
                    mp = (ArrayList<List<String>>)dataSnapshot.child("mp").getValue();
                    AddItemsToRecyclerViewArrayList();
                    SummaryAdapter adapter = new SummaryAdapter(source,bookName);
                    summaryRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference wishlists = FirebaseDatabase.getInstance().getReference("users");
        wishlists.child(FirebaseAuth.getInstance().getUid()).child("wishlist").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.hasChild(bookName))
                {
                    wishlist.setImageResource(R.drawable.ic_bookmark_select);
                    wi=true;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(SummaryActivity.this, LinearLayoutManager.VERTICAL, false);
        summaryRecycler.setLayoutManager(layoutManager);


        LinearLayout intro=findViewById(R.id.introduction);
        intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SummaryActivity.this, SummaryReader.class);
                intent.putExtra("bookName",bookName);
                startActivity(intent);
            }
        });
        LinearLayout finalSumm=findViewById(R.id.finalSumm);
        finalSumm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SummaryActivity.this, SummaryReader.class);
                intent.putExtra("pgno",mp.size()-2);
                intent.putExtra("bookName",bookName);
                startActivity(intent);
            }
        });

        LinearLayout read=findViewById(R.id.read_summary);
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SummaryActivity.this, SummaryReader.class);
                intent.putExtra("bookName",bookName);
                startActivity(intent);            }
        });

        LinearLayout audio=findViewById(R.id.play_audio);
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SummaryActivity.this, ActivityAudiobook.class);
                intent.putExtra("bookName",bookName);
                startActivity(intent);
            }
        });

        m=findViewById(R.id.summary_rating);
        ratingNo=findViewById(R.id.ratings_no);
        ref.child(bookName.replace(" ","")).child("ratings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                int avg=0,count=0;
                for(DataSnapshot rs:dataSnapshot.getChildren())
                {
                    avg+=Integer.parseInt(rs.child("rating").getValue(String.class));
                    count++;
                }
                if(count!=0)
                m.setProgress(avg/count);
                else
                    m.setProgress(0);
                ratingNo.setText(count+" ratings >");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        LinearLayout ratings=findViewById(R.id.ratings);
        ratings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SummaryActivity.this, RatingsActivity.class);
                intent.putExtra("bookName",bookName);
                startActivity(intent);
            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!wi) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                    ref.child(FirebaseAuth.getInstance().getUid()).child("wishlist").child(bookName).setValue("Yes").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Book Wishlisted", Toast.LENGTH_SHORT).show();
                            wishlist.setImageResource(R.drawable.ic_bookmark_select);
                        }
                    });
                }
                else
                {
                    DatabaseReference rem = FirebaseDatabase.getInstance().getReference("users");
                    rem.child(FirebaseAuth.getInstance().getUid()).child("wishlist").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            if(dataSnapshot.hasChild(bookName))
                            {
                                dataSnapshot.child(bookName).getRef().removeValue();
                                wishlist.setImageResource(R.drawable.ic_bookmark);
                                wi=false;
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }
    public void AddItemsToRecyclerViewArrayList() {
        source=new ArrayList<>();
        for(int i=1;i<mp.size()-2;i++){
            SummaryDataObject obj = new SummaryDataObject();
            obj.setSummaryHeading(mp.get(i+1).get(0));
            obj.setSummaryNo(""+(i));
            source.add(obj);
        }
    }
}
