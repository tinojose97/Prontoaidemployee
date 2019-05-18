package com.example.prontoaidemployee;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class signup_page extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

FirebaseAuth auth;
    String location, job;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);


        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();


        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        final List<String> categories = new ArrayList<String>();
        categories.add("Kakkanad");
        categories.add("Palarivattom");
        categories.add("Thripunithura");
        categories.add("Vytila");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location=categories.get(position);
                //Log.i("Selected",location);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //final String location=spinner.getSelectedItem().toString();
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        //spinner.setOnItemSelectedListener(this);
        final List<String> categories2 = new ArrayList<String>();
        categories2.add("Carpenter");
        categories2.add("House Cleaner");
        categories2.add("Plumber");
        categories2.add("Electrician");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);
        spinner2.setAdapter(dataAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                job=categories2.get(position);
                //Log.i("Selected",location);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //final String job=spinner2.getSelectedItem().toString();



        Button signupButton = findViewById(R.id.signupbtn);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText name=(EditText)findViewById(R.id.name);

                EditText paswrd= (EditText)findViewById(R.id.password);
                EditText cpaswrd= (EditText)findViewById(R.id.confirm_pswrd);
                EditText phnno= (EditText)findViewById(R.id.phone_number);
                //EditText occpt=(EditText)findViewById(R.id.occupation);
                EditText emaid=(EditText)findViewById(R.id.email);
                final String nme = name.getText().toString().trim();
                final String email=emaid.getText().toString().trim();
                final  String password=paswrd.getText().toString().trim();
                final String phoneno=phnno.getText().toString().trim();
                //final  String job=occpt.getText().toString().trim();
                if(name.getText().toString().equals(""))
                    Toast.makeText(signup_page.this,"Enter the  name",Toast.LENGTH_LONG).show();
                else if(emaid.getText().toString().equals(""))
                    Toast.makeText(signup_page.this,"enter the EmailID",Toast.LENGTH_LONG).show();
                else if(phnno.getText().toString().equals(""))
                    Toast.makeText(signup_page.this,"Enter a valid phone number",Toast.LENGTH_LONG).show();
                //else if(occpt.getText().toString().equals(""))
                    //Toast.makeText(signup_page.this,"Enter an Occupation",Toast.LENGTH_LONG).show();
                else if(paswrd.getText().toString().equals(""))
                    Toast.makeText(signup_page.this,"enter a password",Toast.LENGTH_LONG).show();
                else if(cpaswrd.getText().toString().equals(""))
                    Toast.makeText(signup_page.this,"confirm password",Toast.LENGTH_LONG).show();
                else if(!paswrd.getText().toString().equals(cpaswrd.getText().toString()))
                    Toast.makeText(signup_page.this,"Password Does not match",Toast.LENGTH_LONG).show();
                else
                { auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(signup_page.this, new OnCompleteListener<AuthResult>() {

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(signup_page.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        if (!task.isSuccessful()) {
                            Toast.makeText(signup_page.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference myRef = database.getReference("Workers");
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int number1 = (int)dataSnapshot.getChildrenCount();
                                    number1++;
                                    //myRef=myRef.getParent();
                                    myRef.child(number1+"").child("Name").setValue(nme);
                                    myRef.child(number1+"").child("Phone_Number").setValue(phoneno);
                                    myRef.child(number1+"").child("Username").setValue(email);
                                    myRef.child(number1+"").child("Occupation").setValue(job);
                                    myRef.child(number1+"").child("Location").setValue(location);
                                    myRef.child(number1+"").child("AvgReview").setValue("5");
                                    myRef.child(number1+"").child("NoReviews").setValue("0");
                                    myRef.child(number1+"").child("Verifier").setValue("0");
                                    //myRef.child(number1+"").child("Available").setValue("Disconnected");
                                    //int number2=(int)dataSnapshot.getChildrenCount();
                                    //int number2=(int)dataSnapshot.getParent().child("Jobs").getChildre
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });



                            startActivity(new Intent(signup_page.this, FirstPage.class));
                            finish();
                        }
                       // progressDialog.dismiss();
                    }
                });
            }}
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
