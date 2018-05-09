package com.example.brainwashing.onlinebookingclinic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.brainwashing.onlinebookingclinic.Adapters.RecycleAdapter;
import com.example.brainwashing.onlinebookingclinic.Adapters.ShowClinicHorizonAdapter;
import com.example.brainwashing.onlinebookingclinic.Models.Appointment;
import com.example.brainwashing.onlinebookingclinic.Models.ClinicDataModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClinicDetailsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ShowClinicHorizonAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView name;
    private TextView addr;
    private TextView open;
    private TextView dist;
    private TextView hour;
    private TextView mon,tue,wed,thu,fri,sat,sun;
    private Button callBtn;
    private Button prevBtn;
    private Button nextBtn;
    private TextView showDate;
    private Calendar c;
    private String formattedDate;
    private SimpleDateFormat df,sdf;
    private Context con = this;
    String url = "https://maps.googleapis.com/maps/api/staticmap?center=13.730475,100.575624&zoom=10&scale=false&size=500x150&maptype=roadmap&format=png&visual_refresh=true&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C13.730475,100.575624";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        name = (TextView) findViewById(R.id.d_name);
        addr = (TextView)findViewById(R.id.d_address);
        //open = (TextView) findViewById(R.id.d_open);
        //dist = (TextView) findViewById(R.id.d_distance);
        //hour = (TextView) findViewById(R.id.d_hours);
        //callBtn = (Button) findViewById(R.id.btn_call);
        prevBtn = (Button) findViewById(R.id.date_prev);
        nextBtn = (Button) findViewById(R.id.date_next);
        showDate = (TextView) findViewById(R.id.date_txt);
        mon = (TextView) findViewById(R.id.table_time);
        tue = (TextView) findViewById(R.id.table_time2);
        wed = (TextView) findViewById(R.id.table_time3);
        thu = (TextView) findViewById(R.id.table_time4);
        fri = (TextView) findViewById(R.id.table_time5);
        sat = (TextView) findViewById(R.id.table_time6);
        sun = (TextView) findViewById(R.id.table_time7);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_time);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager  = new LinearLayoutManager(con, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ImageView smap = (ImageView) findViewById(R.id.staticMap);
        ImageView cover = (ImageView) findViewById(R.id.imageView_clinic_logo);
        final ClinicDataModel clinic= (ClinicDataModel) getIntent().getSerializableExtra("clinicDetails");
        double lat,lng;
        lat = clinic.getLocation().getLatitude();
        lng = clinic.getLocation().getLongitude();
        String url = "https://maps.googleapis.com/maps/api/staticmap?center="+lat+","+lng+"&zoom=16&scale=false&size=500x200&maptype=roadmap&format=png&visual_refresh=true&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C"+lat+","+lng;
        Glide.with(this)
                .load(url)
                .into(smap);

        Glide.with(this)
                .load(clinic.getClinic_image())
                .into(cover);

        showDetails(clinic);
        /*callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(clinic.getClinic_phone());
            }
        });*/

        c = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        clinic.date_pick = sdf.format(c.getTime());
        System.out.println("Current time => " + c.getTime());
        df = new SimpleDateFormat("EEE, d MMM");
        formattedDate = df.format(c.getTime());
        showDate.setText(formattedDate);

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.add(Calendar.DATE, -1);
                formattedDate = df.format(c.getTime());
                Log.v("PREVIOUS DATE : ", formattedDate);
                showDate.setText(formattedDate);
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                clinic.date_pick = sdf.format(c.getTime());
                updateSlot(clinic);

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.add(Calendar.DATE, 1);
                formattedDate = df.format(c.getTime());
                Log.v("NEXT DATE : ", formattedDate);
                showDate.setText(formattedDate);
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                clinic.date_pick = sdf.format(c.getTime());
                updateSlot(clinic);
            }
        });


    }

    public void showDetails( ClinicDataModel clinic ) {
        //float dis = clinic.distance/1000;
        //String show_dist = String.format("%.01f", dis);
        mAdapter = new ShowClinicHorizonAdapter(con, clinic);
        mRecyclerView.setAdapter(mAdapter);
        name.setText(clinic.getClinic_name());
        addr.setText(clinic.getClinic_address());
        mon.setText(clinic.getOpen_hours().getMon());
        tue.setText(clinic.getOpen_hours().getTue());
        wed.setText(clinic.getOpen_hours().getWed());
        thu.setText(clinic.getOpen_hours().getThu());
        fri.setText(clinic.getOpen_hours().getFri());
        sat.setText(clinic.getOpen_hours().getSat());
        sun.setText(clinic.getOpen_hours().getSun());
        /*hour.setText("Today Open Time : " + clinic.getOpen_hours().getSat()); //TODO: check this day
        if(clinic.is_open){
            open.setText("Open Now");
            open.setTextColor(Color.parseColor("#7CFC00"));
        }else {
            open.setText("Closed");
            open.setTextColor(Color.parseColor("#DC143C"));
        }
        dist.setText("distance : " + show_dist+ " km");*/

    }

    /*public void getAppointment(){
        mAppmRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    Appointment appm = singleSnapshot.getValue(Appointment.class);
                    appointmentData.add(appm);
                }

                //mAdapter = new RecycleAdapter(con, this.appointmentToView);
                //mRecyclerView.setAdapter(mAdapter);
                mAdapter.updateTime(appointmentData);
                mAdapter = new ShowClinicHorizonAdapter(con, clinic);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("err", "error get db");
            }
        });
    }*/

    public void updateSlot( ClinicDataModel clinic) {
        mAdapter.updateTime(clinic);
        mAdapter = new ShowClinicHorizonAdapter(con, clinic);
    }

    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

}
