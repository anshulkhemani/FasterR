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
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

// The adapter class which
// extends RecyclerView Adapter
public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.MyView> {

    // List with String type
    private List<RatingsDataObject> list;

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        TextView name,review,date;
        MaterialRatingBar ratingBar;
        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(final View view)
        {
            super(view);
            // initialise TextView with id
            name = view.findViewById(R.id.review_name);
            ratingBar=view.findViewById(R.id.rating_bar);
            review=view.findViewById(R.id.review_desc);
            date=view.findViewById(R.id.ratings_date);

        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public RatingsAdapter(List<RatingsDataObject> horizontalList)
    {
        this.list = horizontalList;
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent,
                                     int viewType)
    {
        View itemView = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.ratings_tab,
                            parent,
                            false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder,
                                 final int position)
    {
        holder.name.setText(list.get(position).getUname());
        holder.ratingBar.setProgress(Integer.parseInt(list.get(position).getRating()));
        holder.review.setText(list.get(position).getReview());
        holder.date.setText(list.get(position).getDate());
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return list.size();
    }
}