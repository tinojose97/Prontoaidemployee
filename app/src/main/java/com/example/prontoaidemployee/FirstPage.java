package com.example.prontoaidemployee;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import static android.content.ContentValues.TAG;
import javax.security.auth.Subject;

public class FirstPage extends AppCompatActivity {
    Button r, l;
    EditText iemail, ipassword;
    FirebaseAuth Auth;
    private TextView ForgotPassword;
    int flag = 0, number;
    ProgressDialog progressDialog;
    Double latitude,longitude;
    String verifier,rating;
    SharedPreferences ref_pic;
    public LocationManager mLocationManager = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        askPermission();
        ref_pic=getSharedPreferences("picdtata" , MODE_PRIVATE);
        //progressDialog=new ProgressDialog(this);
        //locationListenSet();
        //progressDialog.setMessage("Setting Your Location");
        //progressDialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //final DatabaseReference myRef = database.getReference("Customer");


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
        //progressDialog = new ProgressDialog(this);
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

                progressDialog.setMessage("Loading");
                progressDialog.show();
                final String email = iemail.getText().toString();
                ref_pic.edit().putString("username",email).commit();
                final String password = ipassword.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //progressDialog.setMessage("Loading");
                //progressDialog.show();
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
                                            verifier=postSnapshot.child("Verifier").getValue(String.class);
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                                            final String job=postSnapshot.child("Occupation").getValue(String.class);
                                            //final String location=postSnapshot.child("Location").getValue(String.class);
                                            final String name=postSnapshot.child("Name").getValue(String.class);
                                            final String phone=postSnapshot.child("Phone_Number").getValue(String.class);
                                            final DatabaseReference myRef1 = database.getReference("Jobs");
                                            rating=postSnapshot.child("AvgReview").getValue().toString();
                                            ref_pic.edit().putString("uid",postSnapshot.getKey()).commit();
                                            ref_pic.edit().putString("name",name).commit();
                                            //Log.d("Namers",ref_pic.getString("name","null"));
                                            ref_pic.edit().putString("number",phone).commit();
                                            //Log.d("Verifier value",verifier);
                                            if (verifier.equals("1")){
                                                //Log.d("Verifier","verifieddddd");

                                                myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        //int number1 = (int)dataSnapshot.child(job).getChildrenCount();
                                                        //number1++;
                                                        //myRef=myRef.getParent();
                                                    /*
                                                    myRef1.child(job).child(number1+"").child("User").setValue(email);
                                                    myRef1.child(job).child(number1+"").child("Loc").setValue(location);
                                                    //myRef1.child(job).child(number1+"").child("AvailableNow").setValue("Online");
                                                    myRef1.child(job).child(number1+"").child("Emp_Name").setValue(name);
                                                    myRef1.child(job).child(number1+"").child("Phone_Number").setValue(phone);
                                                    myRef1.push().key;
                                                    */
                                                        String uid = myRef1.push().getKey();
                                                        Map data = new HashMap();
                                                        data.put("User", email);
                                                        data.put("Emp_Name", name);
                                                        data.put("Phone_Number", phone);
                                                        data.put("Loclatitude", latitude + "");
                                                        data.put("Loclongitude", longitude + "");
                                                        data.put("Rating",rating);
                                                        myRef1.child(job).child(uid).setValue(data);
                                                        //SharedPreferences.Editor refedit = ref_pic.edit();
                                                        //refedit.putString("WorkerName",name);
                                                        Log.d("Broma",name);
                                                        //refedit.putString("WorkerContact",phone);
                                                        ref_pic.edit().putString("Latitude", latitude + "").commit();
                                                        ref_pic.edit().putString("Longitude", longitude + "").commit();


                                                        //myRef1.child(job).push({"User": "Hello", "Name": "World" });
                                                        myRef1.child(job).child(uid).onDisconnect().removeValue();
                                                        //if(myRef1.child())
                                                        //int number2=(int)dataSnapshot.getChildrenCount();
                                                        //int number2=(int)dataSnapshot.getParent().child("Jobs").getChildre
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });
                                            }
                                            
                                            else{
                                                Toast.makeText(FirstPage.this, "Document verification incomplete", Toast.LENGTH_SHORT).show();
                                            }

                                            Intent intent = new Intent(FirstPage.this, Home_screen.class);
                                            ref_pic.edit().putString("for_user",uname).commit();
                                            ref_pic.edit().putString("for_job",job).commit();
                                            ref_pic.edit().putString("for_verifier",verifier).commit();
                                            /*intent.putExtra("for_user",uname);
                                            intent.putExtra("for_job",job);
                                            intent.putExtra("for_verifier",verifier);*/
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                    if (n == number)
                                        break;
                                    n++;

                                }
                                if (flag == 0) {
                                    //progressDialog.dismiss();
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

    void locationListenSet()
    {
        initializeLocationManager();
        LocationListener[] mLocationListeners = new FirstPage.LocationListener[]{

                new FirstPage.LocationListener(LocationManager.GPS_PROVIDER),
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


            ref_pic.edit().putString("Latitude",latitude+"").commit();
            ref_pic.edit().putString("Longitude",longitude+"").commit();


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
    void askPermission()
    {
        try {
            if (ActivityCompat.checkSelfPermission(FirstPage.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {//Checking permission
                progressDialog=new ProgressDialog(this);
                locationListenSet();
                progressDialog.setMessage("Setting Your Location");
                progressDialog.show();


            } else {

                ActivityCompat.requestPermissions(FirstPage.this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},99);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        progressDialog=new ProgressDialog(this);
        locationListenSet();
        progressDialog.setMessage("Setting Your Location");
        progressDialog.show();
        Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show();

    }

}