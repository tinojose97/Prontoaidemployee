package com.example.prontoaidemployee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Doc_Upload extends AppCompatActivity implements View.OnClickListener /*  implementing click listener */ {
    //a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;
    String username,name;
    //Buttons
    private Button buttonChoose;
    private Button buttonUpload;
    //private Button buttonChoose2;
    //private Button buttonUpload2;

    //ImageView
    private ImageView imageView;
    //private ImageView imageView2;

    //a Uri object to store file path
    private Uri filePath;
    //private Uri filePath2;

    //firebase storage reference
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc__upload);
        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
        //spinner.setOnItemSelectedListener(this);
        final List<String> categories3 = new ArrayList<String>();
        categories3.add("Aadhar");
        categories3.add("PAN");
        categories3.add("Driving License");
        //categories3.add("Palarivattom");
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories3);
        spinner3.setAdapter(dataAdapter3);



        //getting views from layout
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        //but = (Button) findViewById(R.id.but);

        imageView = (ImageView) findViewById(R.id.imageView);

        //attaching listener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        //but.setOnClickListener(this);

        //getting firebase storage reference
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final SharedPreferences ref_pic=getSharedPreferences("picdtata" , MODE_PRIVATE);
        username=ref_pic.getString("username","null");
        name=ref_pic.getString("name","null");
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //this method will upload the file
    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = storageReference.child("images/"+username+".jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

    @Override
    public void onClick(View view) {
        //if the clicked button is choose
        if (view == buttonChoose) {
            showFileChooser();
        }
        //if the clicked button is upload
        else if (view == buttonUpload) {
            uploadFile();
        }

    }
}
