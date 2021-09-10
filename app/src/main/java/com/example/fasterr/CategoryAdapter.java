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
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyView> {

    // List with String type
    private List<CategoryDataObject> list;

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        TextView textView;
        ImageView img;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter

        public MyView(final View view)
        {
            super(view);

            // initialise TextView with id
            textView = view.findViewById(R.id.categoryText);
            img=view.findViewById(R.id.categoryImage);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(view.getContext(), ActivityBookList.class);
                    intent.putExtra("category",textView.getText().toString());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public CategoryAdapter(List<CategoryDataObject> horizontalList)
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

        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_search_category,
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
        holder.textView.setText(list.get(position).getCategory_name());
        holder.img.setImageResource(list.get(position).getDraw());
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return list.size();
    }
}
