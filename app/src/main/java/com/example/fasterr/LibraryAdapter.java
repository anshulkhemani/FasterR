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
public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.MyView> {

    private List<LibraryDataObject> list;
    private String books="";
    private View itemView;
    public class MyView
            extends RecyclerView.ViewHolder {

        TextView library_name,library_number,library_area,library_dist,library_id;


        public MyView(final View view)
        {
            super(view);

            // initialise TextView with id
            library_name = view.findViewById(R.id.library_name);
            library_number = view.findViewById(R.id.library_no);
            library_area=view.findViewById(R.id.library_area);
            library_dist=view.findViewById(R.id.library_dist);
            library_id=view.findViewById(R.id.uid);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(view.getContext(),ActivityLibraryBooks.class);
                    intent.putExtra("uid",library_id.getText().toString());
                    intent.putExtra("dist",library_dist.getText().toString());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public LibraryAdapter(List<LibraryDataObject> list)
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
                    .inflate(R.layout.libraries_tab,
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
        holder.library_name.setText(list.get(position).getName());
        holder.library_number.setText(""+list.get(position).getNum());
        holder.library_area.setText(list.get(position).getAddress());
        holder.library_dist.setText(list.get(position).getDistance());
        holder.library_id.setText(list.get(position).getUid());
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return list.size();
    }
}