package com.example.fasterr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class SummaryReader extends AppCompatActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    List<SummaryDataObject> source;
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;
    private int dotscount;
    private ImageView[] dots;
    String bookName;
    int x=0;
    private ArrayList<List<String>> mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_reader);

        final Intent intent=getIntent();
        bookName=intent.getStringExtra("bookName");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("books");
        ref.child(bookName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                mp=new ArrayList<>();
                mp = (ArrayList<List<String>>)dataSnapshot.child("mp").getValue();
                AddItemsToRecyclerViewArrayList();
                mPager =  findViewById(R.id.view_pager);
                pagerAdapter = new SummaryReaderAdapter(source);
                mPager.setAdapter(pagerAdapter);
                PageIndicatorView pageIndicatorView = findViewById(R.id.pageIndicatorView);
                dotscount = pagerAdapter.getCount();
                pageIndicatorView.setCount(dotscount);
                dots = new ImageView[dotscount];
                if(intent.hasExtra("pgno")) {
                    mPager.setCurrentItem(intent.getIntExtra("pgno",0));
                    pageIndicatorView.setSelection(intent.getIntExtra("pgno",0));
                }
                else
                {
                    pageIndicatorView.setSelection(0);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ImageView audio=findViewById(R.id.audio);
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SummaryReader.this, ActivityAudiobook.class);
                intent.putExtra("bookName",bookName);
                startActivity(intent);            }
        });



    }
    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */


    public void AddItemsToRecyclerViewArrayList() {
        // Adding items to ArrayList

        source=new ArrayList<>();
        for(int i=0;i<mp.size()-1;i++){
            SummaryDataObject obj = new SummaryDataObject();
            obj.setSummaryHeading(mp.get(i+1).get(0));
            obj.setContent(mp.get(i+1).get(1));
            source.add(obj);
        }
    }
}
