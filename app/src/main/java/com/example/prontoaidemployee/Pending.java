package com.example.prontoaidemployee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pending extends AppCompatActivity {

    String job,verifier,uname,date,time,loc,jobid,name,phone;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef,myRef2,myRef3;
    ArrayList activeRequest;
    TextView textviewdate,textviewtime,textviewloc;
    SharedPreferences sp;

    int deluser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp=getSharedPreferences("picdtata",MODE_PRIVATE);
        name=sp.getString("name","null");
        phone=sp.getString("number","null");
        Log.d("NameBro",name);
        job=getIntent().getStringExtra("for_job");
        verifier=getIntent().getStringExtra("for_verifier");
        uname=getIntent().getStringExtra("for_user");
        //Log.d("Usernaaame",uname);
        setContentView(R.layout.activity_pending);
        activeRequest = new ArrayList<Request>();
        myRef=database.getReference("Requesting");
        myRef2=database.getReference("Requesting");
        myRef3 = database.getReference("Schedule_Assigned");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    //Log.d("Assigning","1");
                    activeRequest.clear();

                    //String details = "";
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        Map reqd = (Map) postSnapshot.getValue();
                        jobid=postSnapshot.getKey();
                        Log.d("JobKey",jobid);
                        //Log.d("Post Test",emp.toString());
                        if (reqd.get("Job").equals(job)) {
                            Request e = new Request(reqd.get("DateBook").toString(), reqd.get("TimeBook").toString(), reqd.get("LocBook").toString(), reqd.get("Job").toString(),jobid,reqd.get("CustomerUser").toString());
                            activeRequest.add(e);
                            Log.d("Requests", e.DateBook + " " + e.TimeBook);
                        }

                        }

                    Log.d("activereqs",activeRequest.size()+"");
                    ListView listView=(ListView)findViewById(R.id.listView);
                    CustomAdapter customAdapter=new CustomAdapter();
                    listView.setAdapter(customAdapter);
                    /*
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //
                            date=((Request)activeRequest.get(i)).getDate();
                            time=((Request)activeRequest.get(i)).getTime();
                            loc=((Request)activeRequest.get(i)).getLocation();
                            Toast.makeText(Pending.this, date+", "+time+", "+loc, Toast.LENGTH_SHORT).show();



                        }
                    });*/
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
            if (activeRequest.size()!=0)
                return activeRequest.size();
            else
                return 1;
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

            //Log.d("Testing","Basic");

            view=getLayoutInflater().inflate(R.layout.customlayout,null);

            textviewdate=(TextView)view.findViewById(R.id.textviewdate);
            textviewtime=(TextView)view.findViewById(R.id.textviewtime);
            textviewloc=(TextView)view.findViewById(R.id.textviewloc);
            final TextView textviewjobid=view.findViewById(R.id.jobid);
            textviewjobid.setVisibility(View.GONE);
            Button buttonyes=(Button)view.findViewById(R.id.buttonyes);
            Button buttonno=(Button)view.findViewById(R.id.buttonno);
            if (activeRequest.size()!=0){
                buttonyes.setVisibility(View.VISIBLE);
                buttonno.setVisibility(View.VISIBLE);

                //Log.d("Testing","Basic22");
                date=((Request)activeRequest.get(i)).getDate();
                time=((Request)activeRequest.get(i)).getTime();
                loc=((Request)activeRequest.get(i)).getLocation();
                jobid=((Request)activeRequest.get(i)).getJobId();
                Log.d("Testing","Basic22");
                textviewdate.setText("Date: "+date);
                //Log.d("Date ",date);
                textviewtime.setText("Time: "+time);
                //Log.d("Time  ",time);
                textviewloc.setText("Address: "+loc);
                textviewjobid.setText(jobid);
            }
             else {
                buttonyes.setVisibility(View.GONE);
                buttonno.setVisibility(View.GONE);
                textviewdate.setText("");
                textviewtime.setText("");
                textviewloc.setText("No Requests available");
            }

            buttonyes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(Pending.this, "Job Accepted", Toast.LENGTH_SHORT).show();


                    for(int j=0;j<activeRequest.size();j++){

                        if (((Request)activeRequest.get(j)).getJobId().equals(textviewjobid.getText().toString())){
                            Map schedule=new HashMap();
                            schedule.put("DateBook",((Request)activeRequest.get(j)).getDate());
                            schedule.put("TimeBook",((Request)activeRequest.get(j)).getTime());
                            schedule.put("LocBook",((Request)activeRequest.get(j)).getLocation());
                            schedule.put("WorkerUser",uname);
                            schedule.put("CustomerUser",((Request)activeRequest.get(j)).getCustomerUser());
                            schedule.put("WorkerName",name);
                            schedule.put("WorkerContact",phone);
                            schedule.put("Job",job);

                            deluser=j;
                            myRef3.child(textviewjobid.getText().toString()).setValue(schedule);
                            notifyDataSetChanged();

                        }
                    }
                    activeRequest.remove(deluser);

                    myRef2.child(textviewjobid.getText().toString()).removeValue();
                    //finish();
                    //startActivity(getIntent());
                }
            });
            buttonno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(Pending.this, "Job Rejected", Toast.LENGTH_SHORT).show();
                    for(int j=0;j<activeRequest.size();j++){

                        if (((Request)activeRequest.get(j)).getJobId().equals(textviewjobid.getText().toString())){
                            deluser=j;
                        }

                    }
                    activeRequest.remove(deluser);
                    notifyDataSetChanged();
                    //myRef2.child(textviewjobid.getText().toString()).removeValue();
                    //finish();
                    //startActivity(getIntent());
                }
            });

            return view;
        }
    }
}
//The list view works with other data types like String array
//It is not working with the newly created test class Request
//Initially the arraylist is created but at some point the arraylist becomes empty