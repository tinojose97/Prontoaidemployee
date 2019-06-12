package com.example.prontoaidemployee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pending extends AppCompatActivity {

    String job,verifier,uname,date,time,loc;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    ArrayList activeRequest;
    String[] NAMES={"NIG B","ASDOASD","ASDASDASD","VHKSKPE","HFUIEJ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        job=getIntent().getStringExtra("for_job");
        verifier=getIntent().getStringExtra("for_verifier");
        uname=getIntent().getStringExtra("for_user");
        setContentView(R.layout.activity_pending);
        activeRequest = new ArrayList<Request>();
        myRef=database.getReference("Requesting");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    //Log.d("Assigning","1");
                    activeRequest.clear();

                    //String details = "";
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        Map reqd = (Map) postSnapshot.getValue();
                        //Log.d("Post Test",emp.toString());
                        Request e = new Request(reqd.get("DateBook").toString(), reqd.get("TimeBook").toString(), reqd.get("LocBook").toString(),reqd.get("Job").toString());
                        activeRequest.add(e);
                        Log.d("Requests",e.DateBook+" "+e.TimeBook);



                        }

                    Log.d("activereqs",activeRequest.size()+"");
                    ListView listView=(ListView)findViewById(R.id.listView);
                    CustomAdapter customAdapter=new CustomAdapter();
                    listView.setAdapter(customAdapter);
                    Log.d("List View size",activeRequest.size()+"");
                    }
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(Pending.this,Home_screen.class);
        intent.putExtra("for_user",uname);
        intent.putExtra("for_job",job);
        intent.putExtra("for_verifier",verifier);
        startActivity(intent);

    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return activeRequest.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Log.d("Testing","Basic");
            view=getLayoutInflater().inflate(R.layout.customlayout,null);
            TextView textviewdate=(TextView)view.findViewById(R.id.textviewdate);
            TextView textviewtime=(TextView)view.findViewById(R.id.textviewtime);
            TextView textviewloc=(TextView)view.findViewById(R.id.textviewloc);
            Log.d("Testing","Basic22");
            date=((Request)activeRequest.get(i)).getDate();
            time=((Request)activeRequest.get(i)).getTime();
            loc=((Request)activeRequest.get(i)).getLocation();
            Log.d("Testing","Basic22");
            textviewdate.setText("Date: "+date);
            //Log.d("Date ",date);
            textviewtime.setText("Time: "+time);
            //Log.d("Time  ",time);
            textviewloc.setText("Address: "+loc);
            //Log.d("Location ",loc);

            return view;
        }
    }
}

//The list view works with other data types like String array
//It is not working with the newly created test class Request
//Initially the arraylist is created but at some point the arraylist becomes empty