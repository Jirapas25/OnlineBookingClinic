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
import com.example.brainwashing.onlinebookingclinic.Models.ClinicDataModel;

public class ClinicDetailsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ShowClinicHorizonAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView name;
    private TextView addr;
    private TextView open;
    private TextView dist;
    private TextView hour;
    private Button callBtn;

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
        open = (TextView) findViewById(R.id.d_open);
        dist = (TextView) findViewById(R.id.d_distance);
        hour = (TextView) findViewById(R.id.d_hours);
        callBtn = (Button) findViewById(R.id.btn_call);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_time);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager  = new LinearLayoutManager(con, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ImageView smap = (ImageView) findViewById(R.id.staticMap);
        ImageView cover = (ImageView) findViewById(R.id.cover_photo);
        final ClinicDataModel clinic= (ClinicDataModel) getIntent().getSerializableExtra("clinicDetails");
        double lat,lng;
        lat = clinic.getLocation().getLatitude();
        lng = clinic.getLocation().getLongitude();
        String url = "https://maps.googleapis.com/maps/api/staticmap?center="+lat+","+lng+"&zoom=13&scale=false&size=500x200&maptype=roadmap&format=png&visual_refresh=true&markers=size:mid%7Ccolor:0xff0000%7Clabel:%7C"+lat+","+lng;
        Glide.with(this)
                .load(url)
                .into(smap);

        Glide.with(this)
                .load(clinic.getClinic_image())
                .into(cover);

        showDetails(clinic);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(clinic.getClinic_phone());
            }
        });

    }

    public void showDetails( ClinicDataModel clinic ) {
        float dis = clinic.distance/1000;
        String show_dist = String.format("%.01f", dis);
        mAdapter = new ShowClinicHorizonAdapter(con, clinic);
        mRecyclerView.setAdapter(mAdapter);
        name.setText(clinic.getClinic_name());
        addr.setText(clinic.getClinic_address());
        hour.setText("Today Open Time : " + clinic.getOpen_hours().getSat()); //TODO: check this day
        if(clinic.is_open){
            open.setText("Open Now");
            open.setTextColor(Color.parseColor("#7CFC00"));
        }else {
            open.setText("Closed");
            open.setTextColor(Color.parseColor("#DC143C"));
        }
        dist.setText("distance : " + show_dist+ " km");

    }

    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

}
