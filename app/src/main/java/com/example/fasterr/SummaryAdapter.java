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
public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.MyView> {

    private List<SummaryDataObject> list;
    String bookName;

    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        TextView summary_heading,summary_number;


        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(final View view)
        {
            super(view);

            // initialise TextView with id
            summary_heading = view.findViewById(R.id.summary_heading);
            summary_number = view.findViewById(R.id.summary_no);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(view.getContext(), SummaryReader.class);
                    intent.putExtra("bookName",bookName);
                    intent.putExtra("pgno",Integer.parseInt(summary_number.getText().toString()));
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public SummaryAdapter(List<SummaryDataObject> list,String bookName)
    {
        this.list = list;
        this.bookName=bookName;
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
                .inflate(R.layout.summary_tab,
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
        holder.summary_heading.setText(list.get(position).getSummaryHeading());
        holder.summary_number.setText(list.get(position).getSummaryNo());
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return list.size();
    }
}