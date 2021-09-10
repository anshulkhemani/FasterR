package com.example.fasterr;
import android.content.Intent;
import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class BooksTabFragment extends Fragment {
    private ViewGroup root;
    private String cat;
    private RecyclerView recyclerView;
    private List<BookDataObject> bookList;
    private BookAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root= (ViewGroup) inflater.inflate(R.layout.books_tab_fragment, container, false);
        Intent intent=getActivity().getIntent();
        cat=intent.getStringExtra("category");
        recyclerView = root.findViewById(R.id.wishlistRecycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        AddItemsToRecyclerViewArrayList();
        return root;
    }
    public void AddItemsToRecyclerViewArrayList() {
        bookList=new ArrayList<>();
        adapter=new BookAdapter(bookList,"booklistWishlist");
        final DatabaseReference book= FirebaseDatabase.getInstance().getReference("books");
        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("wishlist");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        String l=npsnapshot.getKey();
                        book.child(l).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dd) {
                                        BookDataObject l= dd.getValue(BookDataObject.class);
                                        bookList.add(l);
                                        recyclerView.setAdapter(adapter);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}