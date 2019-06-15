package com.example.prontoaidemployee;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Home_screen extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef,myRef1,myRef2;
    Button m,map;
    Double Latitude,Longitude;
    String username,email,TAG="ggfhgfh";
    String wname,uname,noty_msg="No active jobs",cusname,cusnum,job,address,verifier;
    double cuslat,cuslong;
    int notyflag=0;
    ProgressDialog progressDialog;
    private TextView mTextMessage;
    public LocationManager mLocationManager = null;
    SharedPreferences user;




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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
                    TextView t7 = (TextView) findViewById(R.id.textView7);
                    t7.setVisibility(View.GONE);
                    TextView t8 = (TextView) findViewById(R.id.textView8);
                    t8.setVisibility(View.GONE);
                    Button b4 = (Button) findViewById(R.id.signout);
                    b4.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_pending:

                    /*
                    //change to the new view, change the whole block
                    Button bloc1=(Button) findViewById(R.id.gpssetter);
                    //mTextMessage.setVisibility(View.VISIBLE);
                    //TextView e5 = (TextView) findViewById(R.id.message);
                    //mTextMessage = (TextView) findViewById(R.id.message);
                    mTextMessage.setText(noty_msg);
                    mTextMessage.setVisibility(View.VISIBLE);
                    //mTextMessage = (TextView) findViewById(R.id.message);

                    Button m11 = (Button) findViewById(R.id.docupload);
                    m11.setVisibility(View.GONE);
                    Button map11 = (Button) findViewById(R.id.map);
                    if (notyflag==1) {
                        map11.setVisibility(View.VISIBLE);
                        bloc1.setVisibility(View.GONE);
                    }
                    else {
                        map11.setVisibility(View.GONE);
                        bloc1.setVisibility(View.VISIBLE);
                    }
                    ImageView i21 = (ImageView) findViewById(R.id.imageView2);
                    i21.setVisibility(View.GONE);
                    TextView n11 = (TextView) findViewById(R.id.textView5);
                    TextView e11 = (TextView) findViewById(R.id.textView6);
                    n11.setVisibility(View.GONE);
                    e11.setVisibility(View.GONE);
                    */
                    Intent intent = new Intent(Home_screen.this, Pending.class);
                    //intent.putExtra("for_user",uname);
                    //Log.d("USernaame",uname);
                    //intent.putExtra("for_job",job);
                    //intent.putExtra("for_verifier",verifier);
                    startActivity(intent);
                    finish();

                    return true;

                case R.id.navigation_notification:
                    Button bloc=(Button) findViewById(R.id.gpssetter);
                    //mTextMessage.setVisibility(View.VISIBLE);
                    //TextView e5 = (TextView) findViewById(R.id.message);
                    //mTextMessage = (TextView) findViewById(R.id.message);
                    mTextMessage.setText(noty_msg);
                    mTextMessage.setVisibility(View.VISIBLE);
                    //mTextMessage = (TextView) findViewById(R.id.message);

                    Button m1 = (Button) findViewById(R.id.docupload);
                    m1.setVisibility(View.GONE);
                    Button map1 = (Button) findViewById(R.id.map);
                    if (notyflag==1) {
                        map1.setVisibility(View.VISIBLE);
                        bloc.setVisibility(View.GONE);
                        }
                    else {
                        map1.setVisibility(View.GONE);
                        bloc.setVisibility(View.VISIBLE);
                        }
                    ImageView i2 = (ImageView) findViewById(R.id.imageView2);
                    i2.setVisibility(View.GONE);
                    TextView n1 = (TextView) findViewById(R.id.textView5);
                    TextView e1 = (TextView) findViewById(R.id.textView6);
                    n1.setVisibility(View.GONE);
                    e1.setVisibility(View.GONE);
                    TextView t5 = (TextView) findViewById(R.id.textView7);
                    t5.setVisibility(View.GONE);
                    TextView t4 = (TextView) findViewById(R.id.textView8);
                    t4.setVisibility(View.GONE);
                    Button b3 = (Button) findViewById(R.id.signout);
                    b3.setVisibility(View.GONE);

                    return true;
                case R.id.navigation_profile:

                    Button m2 = (Button) findViewById(R.id.docupload);
                    m2.setVisibility(View.VISIBLE);
                    ImageView i3 = (ImageView) findViewById(R.id.imageView2);
                    i3.setVisibility(View.VISIBLE);
                    TextView n2 = (TextView) findViewById(R.id.textView5);
                    n2.setText(username+"\n"+email);
                    TextView e2 = (TextView) findViewById(R.id.textView6);
                    e2.setText(verifier);
                    TextView t3 = (TextView) findViewById(R.id.textView7);
                    t3.setVisibility(View.VISIBLE);
                    TextView t9 = (TextView) findViewById(R.id.textView8);
                    t9 .setVisibility(View.VISIBLE);

                    n2.setVisibility(View.VISIBLE);
                    e2.setVisibility(View.VISIBLE);
                    TextView e3 = (TextView) findViewById(R.id.message);
                    e3.setVisibility(View.GONE);
                    Button map2 = (Button) findViewById(R.id.map);
                    map2.setVisibility(View.GONE);
                    Button b2 = (Button) findViewById(R.id.signout);
                    b2.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user=getSharedPreferences("picdtata" , MODE_PRIVATE);

        //Log.d("Usernaaame",uname);
        progressDialog = new ProgressDialog(this);
        uname=user.getString("for_user","null");
        job=user.getString("for_job","null");
        verifier=user.getString("for_verifier","null");



        username=user.getString("name","null");
        email=user.getString("username","null");

        //Log.i("Latitude",user.getString("Latitude","null"));
        Latitude=Double.parseDouble(user.getString("Latitude","null"));
        Longitude=Double.parseDouble(user.getString("Longitude", "null"));

        setContentView(R.layout.activity_home_screen);
        mTextMessage = (TextView) findViewById(R.id.message);
        mTextMessage.setText("Welcome "+username);

        if (verifier.equals("0"))
            verifier="Employee documents not verified";
        else
            verifier="";

        myRef = database.getReference("Assigned");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notyflag=0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    wname = postSnapshot.child("Worker_User").getValue(String.class);


                    if (uname.equals(wname)) {
                        cusname = postSnapshot.child("Customer_Name").getValue(String.class);
                        cusnum = postSnapshot.child("Customer_Contact").getValue(String.class);
                        cuslat =Double.parseDouble(postSnapshot.child("Customer_Latitude").getValue(String.class));
                        cuslong=Double.parseDouble(postSnapshot.child("Customer_Longitude").getValue(String.class));
                        address=getAddress(cuslat,cuslong);
                        noty_msg = "You're service has been requested by "+cusname+" at "+address+" \nContact: "+cusnum;
                        //tid=postSnapshot.getKey();
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
        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        addListenerOnButton();

    }
    public void locationsetter(View view){
        Log.d("Noty flag value",notyflag+"");
        Log.d("Email id",email+"");
        Log.d("Job value",job+"");
        if (notyflag==0) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Setting Your Location");
            progressDialog.show();
            locationListenSet();

            myRef1 = database.getReference("Jobs");
            myRef1=myRef1.child(job);
            myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        if (email.equals(postSnapshot.child("User").getValue(String.class))){
                            Log.d("Found user",postSnapshot.getKey());
                            //progressDialog.dismiss();
                            myRef1.child(postSnapshot.getKey()).child("Loclatitude").setValue(Latitude+"");
                            myRef1.child(postSnapshot.getKey()).child("Loclongitude").setValue(Longitude+"");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
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

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(Home_screen.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            /*add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();*/

            //Log.v("IGA", "Address" + add);
            return add;
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    void locationListenSet()
    {
        initializeLocationManager();
        Home_screen.LocationListener[] mLocationListeners = new Home_screen.LocationListener[]{

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


            Latitude=location.getLatitude();
            Longitude=location.getLongitude();
            progressDialog.dismiss();
            //progressDialog.show();


            //Log.i("Latitude",latitude+"");
            //Log.i("Longitude",longitude+"");
            //progressDialog.dismiss();
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

    public void logOut(View view){
        myRef2=database.getReference("Jobs");
        myRef2=myRef2.child(job);
        myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    if ((postSnapshot.child("User").getValue().toString()).equals(uname)){
                        myRef2.child(postSnapshot.getKey()).removeValue();
                        finish();
                        Intent intent = new Intent(Home_screen.this, FirstPage.class);
                        startActivity(intent);
                        finish();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

