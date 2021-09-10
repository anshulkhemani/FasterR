package com.example.fasterr;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

// The adapter class which
// extends RecyclerView Adapter
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyView> {

    // List with String type
    private List<BookDataObject> list;
    String layout="";
    View context;
    TextView donBookNo;

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        TextView name,author,time,desc;
        ImageView img;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(final View view)
        {
            super(view);
            context=view;
            // initialise TextView with id
            name = view.findViewById(R.id.book_tab_name);
            name.setEllipsize(TextUtils.TruncateAt.END);
            name.setMaxLines(2);
            time = view.findViewById(R.id.book_tab_time);
            author = view.findViewById(R.id.book_tab_author);
            author.setEllipsize(TextUtils.TruncateAt.END);
            author.setMaxLines(2);
            desc = view.findViewById(R.id.book_tab_desc);
            desc.setEllipsize(TextUtils.TruncateAt.END);
            desc.setMaxLines(2);
            img=view.findViewById(R.id.book_tab_image);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!layout.equals("donation")) {
                        Intent intent = new Intent(view.getContext(), SummaryActivity.class);
                        intent.putExtra("bookName", name.getText().toString());
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public BookAdapter(List<BookDataObject> horizontalList)
    {
        this.list = horizontalList;
    }
    public BookAdapter(List<BookDataObject> horizontalList, String layout)
    {
        this.list = horizontalList;
        this.layout=layout;
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent,
                                     int viewType)
    {
        View itemView;
        if(layout.equals("")) {

                 itemView   = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.book_tab,
                            parent,
                            false);
        }
        else if(layout.equals("booklist"))
        {
            itemView = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.book_tab_wide,
                            parent,
                            false);
        }
        else if(layout.equals("donation"))
        {
            itemView = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.donation_book_tab,
                            parent,
                            false);
            donBookNo = itemView.findViewById(R.id.don_book_no);
        }
        else
        {
            itemView = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.book_tab_wishlist,
                            parent,
                            false);
        }
            return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder,
                                 final int position)
    {

        // Set the text of each item of
        // Recycler view with the list items
        holder.name.setText(list.get(position).getBookName());
        if(!layout.equals("donation")) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("books/" + list.get(position).getBookName().replace(" ", ""));
            GlideApp.with(context)
                    .load(storageReference)
                    .into(holder.img);
        }
        else
        {
            donBookNo.setText(""+(position+1));
        }
        holder.author.setText(list.get(position).getAuthor());
        holder.time.setText(list.get(position).getTime());
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