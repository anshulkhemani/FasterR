package com.example.fasterr;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView,latestRecyclerView;
    private TextView freeBookName,freeBookDesc,freeBookAuthor,freeBookTime;
    private List<BookDataObject> bookList;
    private ImageView freeBookImg;
    private ViewGroup root;
    ArrayList<BookDataObject> source;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2,freeBook;
    public HomeFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root=(ViewGroup) inflater.inflate(R.layout.activity_home_screen, container, false);
        AppBarLayout appBar = root.findViewById(R.id.appBarLayout);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                View v=root.findViewById(R.id.divider);
                ImageView settings=root.findViewById(R.id.imgSettings);
                settings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getContext(),SettingsActivity.class);
                        startActivity(intent);
                    }
                });
                v.setAlpha(1.0f - Math.abs(verticalOffset / (float)
                        appBarLayout.getTotalScrollRange()));
            }
        });
        freeBookName=root.findViewById(R.id.free_book_name);
        freeBookAuthor=root.findViewById(R.id.free_book_author);
        freeBookDesc=root.findViewById(R.id.free_book_desc);
        freeBookTime=root.findViewById(R.id.free_book_time);
        freeBookImg=root.findViewById(R.id.free_book_image);

        recyclerView = root.findViewById(R.id.trendingRecycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        AddItemsToRecyclerViewArrayList();
        LinearLayout freeBook=root.findViewById(R.id.freeBook);
        freeBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),SummaryActivity.class);
                intent.putExtra("bookName",freeBookName.getText().toString());
                startActivity(intent);
            }
        });

        ImageView newsSummary=root.findViewById(R.id.newsSummary);
        newsSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),NewsSummary.class);
                startActivity(intent);
            }
        });
        return root;

    }
    public void AddItemsToRecyclerViewArrayList() {
        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference("daybook");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    freeBook=dataSnapshot.child("book").getValue().toString();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bookList=new ArrayList<>();
        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("books");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        BookDataObject l=npsnapshot.getValue(BookDataObject.class);
                        if(freeBook!=null && npsnapshot.getKey().equals(freeBook))
                        {
                            freeBookName.setText(l.getBookName());
                            freeBookAuthor.setText(l.getAuthor());
                            freeBookDesc.setText(l.getDescription());
                            freeBookTime.setText(l.getTime());
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("books/"+l.getBookName().replace(" ",""));
                            GlideApp.with(root)
                                    .load(storageReference)
                                    .into(freeBookImg);
                        }
                        bookList.add(l);
                    }
                    BookAdapter adapter=new BookAdapter(bookList);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}