package com.example.prontoaidemployee;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Home_screen extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef,myRef1;
    Button m,map;
    Double Latitude,Longitude;
    String username,email,TAG="ggfhgfh";
    String wname,uname,noty_msg,cusname,cusnum,job;
    double cuslat,cuslong;

    ProgressDialog progressDialog;
    private TextView mTextMessage;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            myRef = database.getReference("Assigned");


            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int notyflag=0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        wname = postSnapshot.child("Worker_User").getValue(String.class);
                        uname=getIntent().getStringExtra("for_user");
                        if (uname.equals(wname)) {
                            cusname = postSnapshot.child("Customer_Name").getValue(String.class);
                            cusnum = postSnapshot.child("Customer_Contact").getValue(String.class);
                            cuslat =Double.parseDouble(postSnapshot.child("Customer_Latitude").getValue(String.class));
                            cuslong=Double.parseDouble(postSnapshot.child("Customer_Longitude").getValue(String.class));
                            noty_msg = "You're service has been requested by"+cusname+" \nContact: "+cusnum;
                            notyflag=1;
                        }

                    }
                    if (notyflag==0)
                        noty_msg="No active jobs";

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    TextView e4 = (TextView) findViewById(R.id.message);
                    e4.setVisibility(View.VISIBLE);
                    mTextMessage = (TextView) findViewById(R.id.message);
                    mTextMessage.setText("Welcome "+username);
                    Button m = (Button) findViewById(R.id.docupload);
                    Button map = (Button) findViewById(R.id.map);
                    map.setVisibility(View.GONE);
                    ImageView i = (ImageView) findViewById(R.id.imageView2);
                    i.setVisibility(View.GONE);
                    m.setVisibility(View.GONE);
                    TextView n = (TextView) findViewById(R.id.textView5);
                    TextView e = (TextView) findViewById(R.id.textView6);
                    n.setVisibility(View.GONE);
                    e.setVisibility(View.GONE);



                    return true;
                case R.id.navigation_notification:
                    //mTextMessage.setVisibility(View.VISIBLE);
                    //TextView e5 = (TextView) findViewById(R.id.message);
                    //mTextMessage = (TextView) findViewById(R.id.message);
                    mTextMessage.setText(noty_msg);
                    mTextMessage.setVisibility(View.VISIBLE);
                    //mTextMessage = (TextView) findViewById(R.id.message);

                    Button m1 = (Button) findViewById(R.id.docupload);
                    m1.setVisibility(View.GONE);
                    Button map1 = (Button) findViewById(R.id.map);
                    map1.setVisibility(View.VISIBLE);
                    ImageView i2 = (ImageView) findViewById(R.id.imageView2);
                    i2.setVisibility(View.GONE);
                    TextView n1 = (TextView) findViewById(R.id.textView5);
                    TextView e1 = (TextView) findViewById(R.id.textView6);
                    n1.setVisibility(View.GONE);
                    e1.setVisibility(View.GONE);

                    return true;
                case R.id.navigation_profile:

                    Button m2 = (Button) findViewById(R.id.docupload);
                    m2.setVisibility(View.VISIBLE);
                    ImageView i3 = (ImageView) findViewById(R.id.imageView2);
                    i3.setVisibility(View.VISIBLE);
                    TextView n2 = (TextView) findViewById(R.id.textView5);
                    n2.setText(username);
                    TextView e2 = (TextView) findViewById(R.id.textView6);
                    e2.setText(email);
                    n2.setVisibility(View.VISIBLE);
                    e2.setVisibility(View.VISIBLE);



                    TextView e3 = (TextView) findViewById(R.id.message);
                    e3.setVisibility(View.GONE);
                    Button map2 = (Button) findViewById(R.id.map);
                    map2.setVisibility(View.GONE);


                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        job=getIntent().getStringExtra("for_job");
        final SharedPreferences user=getSharedPreferences("picdtata" , MODE_PRIVATE);
        username=user.getString("name","null");
        email=user.getString("username","null");
        //Log.i("Latitude",user.getString("Latitude","null"));
        Latitude=Double.parseDouble(user.getString("Latitude","null"));
        Longitude=Double.parseDouble(user.getString("Longitude", "null"));

        setContentView(R.layout.activity_home_screen);
        mTextMessage = (TextView) findViewById(R.id.message);
        mTextMessage.setText("Welcome "+username);


        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        addListenerOnButton();

}
    public void addListenerOnButton() {
        m = (Button) findViewById(R.id.docupload);
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_screen.this, Doc_Upload.class);
                startActivity(intent);
            }
        });
        map = (Button) findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGoogleMap(Latitude, Longitude,cuslat, cuslong);
            }
        });




    }
    void goToGoogleMap(double userLat, double userLng, double venueLat, double venueLng)
    {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+userLat+","+userLng+"&daddr="+venueLat+","+venueLng));
        startActivity(intent);
    }


}

