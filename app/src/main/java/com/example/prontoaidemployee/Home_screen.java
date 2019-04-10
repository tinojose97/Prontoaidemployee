package com.example.prontoaidemployee;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home_screen extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    String wname,uname,noty_msg,cusname,cusnum,cusloc;
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            myRef = database.getReference("Assigned");


            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        wname = postSnapshot.child("Worker_User").getValue(String.class);
                        uname=getIntent().getStringExtra("for_user");
                        if (uname.equals(wname)) {
                            cusname = postSnapshot.child("Customer_Name").getValue(String.class);
                            cusnum = postSnapshot.child("Customer_Contact").getValue(String.class);
                            cusloc = postSnapshot.child("Customer_Location").getValue(String.class);
                            noty_msg = "You have been assigned to "+cusname+" at "+cusloc+"\n"+"Contact: "+cusnum;
                        }

                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText("Home");
                    return true;
                case R.id.navigation_notification:
                    mTextMessage.setText(noty_msg);
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText("Profile");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



    }

}
