package com.example.fasterr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddSummary extends AppCompatActivity {
    private Button image,add,alertNext;
    private EditText bookName,authorName,description,about,whoFor,aboutAuthor,time,alertBookHeading,alertBookSummary;
    private String pages;
    TextView pgNo;
    private Uri filePath;
    private Map<String, List<String>> mp;
    List<String> summ;
    FirebaseStorage storage;
    StorageReference storageReference;
    private int pg,page;
    private Spinner dropdown,bookCategory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_summary);
        dropdown = findViewById(R.id.pageSpinner);
        bookCategory=findViewById(R.id.category_spinner);
        String[] items = new String[]{"1", "2", "3","4","5","6","7","8","9","10"};
        String names[]= {
                "Psychology",
                "Productivity",
                "Communication Skills",
                "Mindfulness & Happiness",
                "Biography & Memoir",
                "Economics",
                "Parenting",
                "Marketing & Sales",
                "History",
                "Personal Development",
                "Philosophy",
                "Motivation & Inspiration",
                "Health & Nutrition",
                "Entrepreneurship",
                "Creativity",
                "Corporate Culture",
                "Education",
                "Religion & Spirituality",
                "Career & Success",
                "Management & Leadership",
                "Science",
                "Technology & the Future",
                "Society & Culture",
                "Nature & Environment",
                "Politics",
                "Money & Investments"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names);
        bookCategory.setAdapter(adapter2);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        bookName=findViewById(R.id.book_name);
        authorName=findViewById(R.id.author_name);
        description=findViewById(R.id.description);
        about=findViewById(R.id.whatAbout);
        whoFor=findViewById(R.id.whoFor);
        aboutAuthor=findViewById(R.id.abouAuthor);
        time=findViewById(R.id.timeToRead);
        image=findViewById(R.id.select_image);
        add=findViewById(R.id.add_summary);
        mp=new HashMap<String, List<String>>();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.clear();
                pages=dropdown.getSelectedItem().toString();
                page=Integer.parseInt(pages);
                pg=1;
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddSummary.this);
                LayoutInflater inflater = AddSummary.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_summary_page, null);
                dialogBuilder.setView(dialogView);
                pgNo = dialogView.findViewById(R.id.alertPageNo);
                alertBookHeading=dialogView.findViewById(R.id.alertBookHeading);
                alertBookSummary=dialogView.findViewById(R.id.alertBookSummary);
                alertNext=dialogView.findViewById(R.id.alertNext);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                alertNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(pg<page)
                        {
                            summ=new ArrayList<>();
                            summ.add(alertBookHeading.getText().toString());
                            summ.add(alertBookSummary.getText().toString());
                            mp.put(""+(pg),summ);
                            alertBookHeading.setText("");
                            alertBookSummary.setText("");
                            alertBookHeading.requestFocus();
                        }
                        else
                        {
                            summ=new ArrayList<>();
                            summ.add(alertBookHeading.getText().toString());
                            summ.add(alertBookSummary.getText().toString());
                            mp.put(""+(pg),summ);
                            alertDialog.cancel();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("books");
                            myRef.child(bookName.getText().toString().replace(" ","")).setValue(new AddBookDataObject(bookName.getText().toString(),authorName.getText().toString(),description.getText().toString(),about.getText().toString(),whoFor.getText().toString(),aboutAuthor.getText().toString(),time.getText().toString(),bookName.getText().toString(),pages,bookCategory.getSelectedItem().toString(),mp)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    uploadImage();
                                }
                            });
                        }
                        pg++;
                        pgNo.setText("Page " + pg);
                    }
                });

            }
        });
    }

    private void SelectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                1);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == 1
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
            }
            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage()
    {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("daybook");
        myRef.child("book").setValue(bookName.getText().toString().replace(" ","")).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddSummary.this,"Book of the day updated",Toast.LENGTH_LONG).show();
            }
        });

        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "books/"
                                    + bookName.getText().toString().replace(" ","") );

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(AddSummary.this, "Book added!!", Toast.LENGTH_SHORT).show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(AddSummary.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }
}
