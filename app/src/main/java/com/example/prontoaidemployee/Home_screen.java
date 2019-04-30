package com.example.prontoaidemployee;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
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

public class Home_screen extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef,myRef1;
    Button m,map;
    String username,email,TAG="ggfhgfh";
    String wname,uname,noty_msg,cusname,cusnum,cusloc,job;
    LocationManager mLocationManager;
    Double latitude,longitude;
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
                            cusloc = postSnapshot.child("Customer_Location").getValue(String.class);
                            noty_msg = "You have been assigned to "+cusname+" at "+cusloc+"\n"+"Contact: "+cusnum;
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
                    mTextMessage.setText("Welcome ");
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
                    mTextMessage = (TextView) findViewById(R.id.message);
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage = (TextView) findViewById(R.id.message);
                    mTextMessage.setText(noty_msg);
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
        myRef=database.getReference("UpdateLocation");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i("Listening for",dataSnapshot.getValue().toString());
                if (dataSnapshot.getValue().toString().equals("1")){
                    progressDialog.setMessage("Obtaining location");
                    progressDialog.show();
                    locationListenSet();
                    myRef1=database.getReference("Workers");
                    //myRef1=myRef1.child(job);
                    myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren() ){
                                Log.i("Username Test",email);
                                if (postSnapshot.child("Username").getValue().toString().equals(email)){
                                    myRef1.child(postSnapshot.getKey()).child("Latitude").setValue(latitude+"");
                                    myRef1.child(postSnapshot.getKey()).child("Longitude").setValue(longitude+"");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        setContentView(R.layout.activity_home_screen);

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
                Intent intent = new Intent(Home_screen.this, Doc_Upload.class);
                startActivity(intent);
            }
        });



    }
    void locationListenSet()
    {
        initializeLocationManager();
        LocationListener[] mLocationListeners = new Home_screen.LocationListener[]{

                new Home_screen.LocationListener(LocationManager.GPS_PROVIDER),
                new LocationListener(LocationManager.NETWORK_PROVIDER)
        };
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 100, 10f,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.e(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,100, 10f,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.e(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, "gps provider does not exist " + ex.getMessage());
        }

    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
    }
    public class LocationListener implements android.location.LocationListener {
        public Location mLastLocation;
        int i = 0;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);

        }

        @Override
        public void onLocationChanged(Location location)
        {


            latitude=location.getLatitude();
            longitude=location.getLongitude();
            progressDialog.dismiss();
            Log.i("Latitude",latitude+"");
            Log.i("Longitude",longitude+"");

        }



        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }


    }


}

