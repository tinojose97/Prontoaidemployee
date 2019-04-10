package com.example.prontoaidemployee;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import javax.security.auth.Subject;

public class FirstPage extends AppCompatActivity {
    Button r, l;
    EditText iemail, ipassword;
    FirebaseAuth Auth;
    private TextView ForgotPassword;
    int flag = 0, number;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Customer");


        FirebaseApp.initializeApp(this);
        Auth = FirebaseAuth.getInstance();
        setContentView(R.layout.firstpage);
        iemail = (EditText) findViewById(R.id.uname);
        ipassword = (EditText) findViewById(R.id.signinPwd);
        Auth = FirebaseAuth.getInstance();
        ForgotPassword = (TextView) findViewById(R.id.textView2);

        addListenerOnButton();
    }

    public void addListenerOnButton() {
        flag = 0;
        final Context context = this;
        progressDialog = new ProgressDialog(this);
        r = (Button) findViewById(R.id.signinbtn);
        l = (Button) findViewById(R.id.joinus);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, signup_page.class);
                startActivity(intent);
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = iemail.getText().toString();
                final String password = ipassword.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Loading");
                progressDialog.show();
                Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(FirstPage.this, new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull final Task<AuthResult> task) {

                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference myRef = database.getReference("Workers");

                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Log.i("Test2",email);
                                number = (int) dataSnapshot.getChildrenCount();
                                //Log.i("Number of Children",number+"");
                                //number=Integer.parseInt(dataSnapshot.child("number_customer").getValue().toString());
                                int n = 1;
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                    String uname = postSnapshot.child("Username").getValue(String.class);
                                    //Log.i("Email",email);
                                    //Log.i("uname",uname);
                                    if (uname.equals(email)) {
                                        //Log.i("Test4",flag+"");
                                        if (task.isSuccessful()) {
                                            flag = 1;
                                            progressDialog.dismiss();

                                            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                                            final String job=postSnapshot.child("Occupation").getValue(String.class);
                                            final String location=postSnapshot.child("Location").getValue(String.class);
                                            final String name=postSnapshot.child("Name").getValue(String.class);
                                            final String phone=postSnapshot.child("Phone_Number").getValue(String.class);
                                            final DatabaseReference myRef1 = database.getReference("Jobs");


                                            myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    int number1 = (int)dataSnapshot.child(job).getChildrenCount();
                                                    number1++;
                                                    //myRef=myRef.getParent();

                                                    myRef1.child(job).child(number1+"").child("User").setValue(email);

                                                    myRef1.child(job).child(number1+"").child("Loc").setValue(location);
                                                    //myRef1.child(job).child(number1+"").child("AvailableNow").setValue("Online");
                                                    myRef1.child(job).child(number1+"").child("Emp_Name").setValue(name);
                                                    myRef1.child(job).child(number1+"").child("Phone_Number").setValue(phone);
                                                    myRef1.child(job).child(number1+"").onDisconnect().removeValue();

                                                    //if(myRef1.child())

                                                    //int number2=(int)dataSnapshot.getChildrenCount();
                                                    //int number2=(int)dataSnapshot.getParent().child("Jobs").getChildre
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });

                                            Intent intent = new Intent(FirstPage.this, Home_screen.class);

                                            intent.putExtra("for_user",uname);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                    if (n == number)
                                        break;
                                    n++;

                                }
                                if (flag == 0) {
                                    progressDialog.dismiss();
                                    Toast.makeText(FirstPage.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }

                });


            }
        });

        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FirstPage.this, PasswordActivity.class));
            }
        });


    }
}