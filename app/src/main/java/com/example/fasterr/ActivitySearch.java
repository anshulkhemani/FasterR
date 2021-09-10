package com.example.fasterr;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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


public class ActivitySearch extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<BookDataObject> bookList;
    private BookAdapter adapter;
    private TextView suggestion,ex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_search_layout);
        recyclerView = findViewById(R.id.searchRecycler);
        suggestion=findViewById(R.id.suggestions);
        ex=findViewById(R.id.suggestions_example);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
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
        AddItemsToRecyclerViewArrayList();
    }

    public void AddItemsToRecyclerViewArrayList() {
        bookList = new ArrayList<>();
        adapter = new BookAdapter(bookList, "booklistWishlist");
        final DatabaseReference books = FirebaseDatabase.getInstance().getReference("books");

                        books.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dd) {
                                for(DataSnapshot book:dd.getChildren())
                                {
                                    BookDataObject l = book.getValue(BookDataObject.class);
                                    bookList.add(l);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
    }
    void filter(String text){
        List<BookDataObject> temp = new ArrayList<>();
        for(BookDataObject d: bookList){
            if(text.equals(""))
                break;
            if(d.getBookName().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
            else if(d.getAuthor().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
            else if(d.getCategory().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        suggestion.setVisibility(View.GONE);
        ex.setVisibility(View.GONE);
        adapter = new BookAdapter(temp, "booklistWishlist");
        recyclerView.setAdapter(adapter);
    }
}