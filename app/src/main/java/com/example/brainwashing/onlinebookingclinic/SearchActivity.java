package com.example.brainwashing.onlinebookingclinic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.example.brainwashing.onlinebookingclinic.Adapters.RecycleAdapter;
import com.example.brainwashing.onlinebookingclinic.Models.ClinicDataModel;
import com.example.brainwashing.onlinebookingclinic.Models.FilterModel;
import com.example.brainwashing.onlinebookingclinic.Models.Location;
import com.example.brainwashing.onlinebookingclinic.Models.Open_hours;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private DatabaseReference mClinicRef;
    public List<ClinicDataModel> datatoview = new ArrayList<>();
    public List<ClinicDataModel> datatemp = new ArrayList<>();
    public List<ClinicDataModel> datafilter = new ArrayList<>();
    private ProgressBar loadingLayout;
    private Context con = this;
    private ViewSwitcher simpleViewSwitcher;
    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    private RecycleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GoogleMap mMap;
    private Button searchBtn;
    private Button filterBtn;
    private Button mapSwapBtn;
    private FilterModel filters;
    private ClinicDataModel data;

    private boolean DistanceFilter;
    private boolean AvailableFilter;
    private double currentLat;
    private double currentLng;
    private boolean is_map;

    private TextView name;
    private TextView addr;
    private TextView open;
    private TextView dist;
    private Button more;
    private LinearLayout layout_marker;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        currentLat = bundle.getDouble("lat");
        currentLng = bundle.getDouble("lng");

        mClinicRef = FirebaseDatabase.getInstance().getReference("clinics");
        mRecyclerView = (RecyclerView) findViewById(R.id.RecycleView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        loadingLayout = (ProgressBar) findViewById(R.id.loadingPanel);
        filters = new FilterModel(false, 5, "");
        simpleViewSwitcher = (ViewSwitcher) findViewById(R.id.resultViewSwitcher);
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        simpleViewSwitcher.setInAnimation(in);
        simpleViewSwitcher.setOutAnimation(out);

        name = (TextView) findViewById(R.id.name);
        addr = (TextView)findViewById(R.id.address);
        open = (TextView) findViewById(R.id.open);
        dist = (TextView) findViewById(R.id.distance);
        more = (Button) findViewById(R.id.btn_more);
        img = (ImageView) findViewById(R.id.imgDetail);
        layout_marker = (LinearLayout) findViewById(R.id.markerDetail);
        layout_marker.setVisibility(View.INVISIBLE);

        filterBtn = (Button) findViewById(R.id.filterBtn);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        mapSwapBtn = (Button) findViewById(R.id.mapSwapBtn);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapSwapBtn.setOnClickListener(mapSwapListener);
        filterBtn.setOnClickListener(filterListener);
        searchBtn.setOnClickListener(searchListener);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mClinicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    ClinicDataModel clinic = singleSnapshot.getValue(ClinicDataModel.class);
                    clinic.clinic_id = singleSnapshot.getKey();
                    clinic.distance = calculateDistance(clinic.getLocation().getLatitude(), clinic.getLocation().getLongitude());
                    clinic.is_open = isOpenNow(clinic.getOpen_hours());

                    System.out.println((clinic.getClinic_name()));
                    System.out.println((clinic.clinic_id));
                    System.out.println((clinic.distance));
                    datatoview.add(clinic);
                    datatemp.add(clinic);
                }

                mAdapter = new RecycleAdapter(con, datatoview);
                mRecyclerView.setAdapter(mAdapter);
                loadingLayout.setVisibility(View.GONE);

                //add my location flag
                for (int i = 0; i < datatoview.size(); i++) {
                    final double lat = datatoview.get(i).getLocation().getLatitude();
                    final double lng = datatoview.get(i).getLocation().getLongitude();
                    LatLng pos = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(pos).title(datatoview.get(i).getClinic_name()));
                }
                LatLng camfocus = new LatLng(currentLat, currentLng);
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(camfocus));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(camfocus, 13.0f));
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        for(ClinicDataModel c : datatoview){
                            Glide.with(con)
                                    .load(c.getClinic_image())
                                    .into(img);
                            Log.i("log",c.getClinic_name());
                            Log.i("log",marker.getTitle());
                            if(c.getClinic_name().equals(marker.getTitle())) {
                                Log.i("log","marker yes");

                                name.setText(c.getClinic_name());
                                addr.setText(c.getClinic_address());
                                if(c.is_open){
                                    open.setText("Open Now");
                                    open.setTextColor(Color.parseColor("#7CFC00"));
                                }else {
                                    open.setText("Closed");
                                    open.setTextColor(Color.parseColor("#DC143C"));
                                }
                                float distF = c.distance/1000;
                                String show_dist = String.format("%.01f", distF);
                                dist.setText(show_dist);
                                data = c;

                                layout_marker.setVisibility(View.VISIBLE);
                            }
                        }
                        more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(data != null){
                                    Intent in = new Intent(SearchActivity.this, ClinicDetailsActivity.class);
                                    in.putExtra("clinicDetails",data);
                                    startActivity(in);
                                }
                            }
                        });
                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("err", "error get db");
            }
        });

    }

    public Boolean isOpenNow(Open_hours work_time) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String time = "";

        switch (day) {
            case Calendar.SUNDAY:
                time = work_time.getSun();
                break;
            case Calendar.MONDAY:
                time = work_time.getMon();
                break;
            case Calendar.TUESDAY:
                time = work_time.getTue();
                break;
            case Calendar.WEDNESDAY:
                time = work_time.getWed();
                break;
            case Calendar.THURSDAY:
                time = work_time.getThu();
                break;
            case Calendar.FRIDAY:
                time = work_time.getFri();
                break;
            case Calendar.SATURDAY:
                time = work_time.getSat();
                break;
            default:
                break;
        }
        if (time.equals("close")) {
            String[] timeSplit = time.split("-");
            Log.i("time", timeSplit[0] + " " + timeSplit[1]);
            SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
            DateFormat df = new SimpleDateFormat("HH:mm");
            String time_now = df.format(Calendar.getInstance().getTime());
            try {
                Date open = parser.parse(timeSplit[0]);
                Date close = parser.parse(timeSplit[1]);
                Date check_time = parser.parse(time_now);
                Log.i("dateopen", open.toString());
                Log.i("dateclose", close.toString());
                if (check_time.after(open) && check_time.before(close)) {
                    return true;
                }
            } catch (ParseException e) {
                // Invalid date was entered
            }
        }
        return false;
    }

    public float calculateDistance(double lat, double lng) {
        final float distance;
        android.location.Location location_current = new android.location.Location("l_current");
        android.location.Location location_clinic = new android.location.Location("l_clinic");
        location_clinic.setLatitude(lat);
        location_clinic.setLongitude(lng);
        location_current.setLatitude(currentLat);
        location_current.setLongitude(currentLng);
        distance = location_current.distanceTo(location_clinic);
        return distance;
    }

    public void updateFilter() {
        dataToViewInit();
        /*if(filters.openNow) {
            for (ClinicDataModel c : datatoview) {
                if(c.is_open == false){
                    datatoview.remove(c);
                    Log.i("remove",c.getClinic_name());
                }
            }
        }*/

        for (ClinicDataModel c : datatoview) {
            if(c.distance/1000 > filters.distance){
                datatoview.remove(c);
                Log.i("remove",c.getClinic_name());
            }
        }


        /*for(int i=0;i<datatoview.size();i++){
            if(datatoview.get(i).distance/1000 > filters.distance){
                datatoview.remove(i);
                Log.i("dt", String.valueOf(datatoview.get(i).distance/1000));
                Log.i("dt",datatoview.get(i).getClinic_name());
            }
        }*/
        
        mAdapter.updateData(datatoview);
    }

    public View.OnClickListener filterListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            View bottomSheetView = getLayoutInflater().inflate(R.layout.buttom_sheet_filter_layout, null);
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SearchActivity.this);
            bottomSheetDialog.setContentView(bottomSheetView);
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
            bottomSheetDialog.show();
            final TextView ds = (TextView) bottomSheetView.findViewById(R.id.txt_show_distance);
            Button showResultBtn = (Button) bottomSheetView.findViewById(R.id.btn_filter_result);
            final SeekBar distanceSeekbar = (SeekBar) bottomSheetView.findViewById(R.id.seekBarDistance);
            final Switch openSwitch = (Switch) bottomSheetView.findViewById(R.id.simpleSwitch);
            openSwitch.setChecked(filters.openNow);
            distanceSeekbar.setProgress(Math.round(filters.distance));
            ds.setText("Distance : "+Math.round(filters.distance)+" km");

            distanceSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    i+=2; //start distance at 5
                    ds.setText("Distance : "+i+" km");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            showResultBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filters.openNow = openSwitch.isChecked();
                    filters.distance = (float) distanceSeekbar.getProgress()+2;
                    filters.specialist = "";
                    bottomSheetDialog.hide();
                    updateFilter();
                }
            });
        }
    };

    public View.OnClickListener searchListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent in = new Intent(SearchActivity.this, SearchByNameActivity.class);
            startActivity(in);
        }
    };

    public View.OnClickListener mapSwapListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            simpleViewSwitcher.showNext();

            if (is_map) {
                mapSwapBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_format_list_bulleted_black_24dp, 0, 0, 0);
                is_map = false;
            } else {
                mapSwapBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_map_black_24dp, 0, 0, 0);
                is_map = true;
            }

            loadingLayout.setVisibility(View.GONE);
        }
    };

    public void dataToViewInit() {
        datatoview.clear();
        for (ClinicDataModel c : datatemp) {
            datatoview.add(c);
        }
    }


}
