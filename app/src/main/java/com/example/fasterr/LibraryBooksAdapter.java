package com.example.fasterr;


import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

// The adapter class which
// extends RecyclerView Adapter
public class LibraryBooksAdapter extends RecyclerView.Adapter<LibraryBooksAdapter.MyView> {

    private List<BookDataObject> list;
    private String books="";
    private View itemView;
    public class MyView
            extends RecyclerView.ViewHolder {

        TextView name,author,desc,id;


        public MyView(final View view)
        {
            super(view);

            // initialise TextView with id
            name = view.findViewById(R.id.lib_book_name);
            author = view.findViewById(R.id.lib_author_name);
            desc=view.findViewById(R.id.lib_book_desc);
            id=view.findViewById(R.id.lib_book_no);
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public LibraryBooksAdapter(List<BookDataObject> list)
    {
        this.list = list;
    }
    @Override
    public MyView onCreateViewHolder(ViewGroup parent,
                                     int viewType)
    {
        itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.library_books_tab,
                        parent,
                        false);

        // return itemView
        return new MyView(itemView);
    }
    @Override
    public void onBindViewHolder(final MyView holder,
                                 final int position)
    {

        // Set the text of each item of
        // Recycler view with the list items
        holder.name.setText(list.get(position).getBookName());
        holder.id.setText(""+list.get(position).getNum());
        holder.author.setText(list.get(position).getAuthor());
        holder.desc.setText(list.get(position).getDescription());
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return list.size();
    }
}