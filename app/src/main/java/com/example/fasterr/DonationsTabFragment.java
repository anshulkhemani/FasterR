package com.example.fasterr;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.google.android.gms.common.internal.Preconditions.checkArgument;

public class DonationsTabFragment extends Fragment {
    private RecyclerView recyclerView;
    private View root;
    private List<BookDataObject> source;
    private Button donate;
    private BookAdapter adapter;
    private EditText bookName,authorName,description;
    private Button alertAdd;
    private String date="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root= (ViewGroup)inflater.inflate(R.layout.donations_tab_fragment, container, false);
        recyclerView = root.findViewById(R.id.donation_recycler);
        donate=root.findViewById(R.id.donate);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        AddItemsToRecyclerViewArrayList();
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(root.getContext());
                LayoutInflater inflater = DonationsTabFragment.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_library_book, null);
                dialogBuilder.setView(dialogView);
                bookName=dialogView.findViewById(R.id.alertBookName);
                authorName=dialogView.findViewById(R.id.alertBookAuthor);
                description=dialogView.findViewById(R.id.alertBookDescription);
                LocalDate cur=LocalDate.now();
                int dt=cur.getDayOfMonth();
                date+=dt;
                date+=getDayOfMonthSuffix(dt);
                String mon  = cur.getMonth().toString();
                mon = mon.substring(0,1).toUpperCase() + mon.substring(1).toLowerCase();
                date+=" "+mon+", "+cur.getYear();
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                alertAdd=dialogView.findViewById(R.id.alertAdd);
                alertAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("donated");
                        myRef.child(bookName.getText().toString().replace(" ","")).setValue(new BookDataObject(bookName.getText().toString(),authorName.getText().toString(),description.getText().toString(),date)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(root.getContext(), "Book donated!", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        });
                    }
                });
            }
        });
        return root;
    }
    String getDayOfMonthSuffix(final int n) {
        checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }
    public void AddItemsToRecyclerViewArrayList() {
        source=new ArrayList<>();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("donated");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int x=1;
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren())
                    {
                        BookDataObject l = npsnapshot.getValue(BookDataObject.class);
                        l.setNum(x);
                        source.add(l);
                        x++;
                    }
                    adapter = new BookAdapter(source,"donation");
                    recyclerView.setAdapter(adapter);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}