package com.example.fasterr;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

// The adapter class which
// extends RecyclerView Adapter
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyView> {

    private List<AddNewsObject> list;
    View context;

    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        TextView heading,desc;
        ImageView img;


        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view)
        {
            super(view);
            context=view;
            // initialise TextView with id
            heading = view.findViewById(R.id.news_headline);
            desc = view.findViewById(R.id.news_content);
            img=view.findViewById(R.id.news_image);
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public NewsAdapter(List<AddNewsObject> list)
    {
        this.list = list;
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent,
                                     int viewType)
    {

        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.news_tab,
                        parent,
                        false);

        // return itemView
        return new MyView(itemView);
    }

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @Override
    public void onBindViewHolder(final MyView holder,
                                 final int position)
    {

        // Set the text of each item of
        // Recycler view with the list items
        holder.heading.setText(list.get(position).getHeading());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("news/"+list.get(position).getImg());
        GlideApp.with(context)
                .load(storageReference)
                .into(holder.img);
        holder.desc.setText(list.get(position).getContent());
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return list.size();
    }
}