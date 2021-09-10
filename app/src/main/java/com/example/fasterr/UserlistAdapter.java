package com.example.fasterr;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserlistAdapter extends RecyclerView.Adapter<UserlistAdapter.ViewHolder>{
    private List<UserDetailsObject>listData;

    public UserlistAdapter(List<UserDetailsObject> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_users_tab,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserDetailsObject ld=listData.get(position);

        holder.name.setText(ld.getName());
        holder.email.setText(ld.getEmail());
        holder.type.setText(ld.getUsertype());
        holder.uid.setText(ld.getUid());
        holder.no.setText(""+(position+1));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name,email,type,no,uid;
        public ViewHolder(final View itemView) {
            super(itemView);
            no=itemView.findViewById(R.id.user_no);
            name=(TextView)itemView.findViewById(R.id.user_name);
            email=(TextView)itemView.findViewById(R.id.user_email);
            type=(TextView)itemView.findViewById(R.id.usertype);
            uid=(TextView)itemView.findViewById(R.id.uid);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ProfileDetails.class);
                    intent.putExtra("uid",uid.getText().toString());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}