package com.example.brainwashing.onlinebookingclinic.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.brainwashing.onlinebookingclinic.LoginActivity;
import com.example.brainwashing.onlinebookingclinic.MainActivity;
import com.example.brainwashing.onlinebookingclinic.Manifest;
import com.example.brainwashing.onlinebookingclinic.Models.Location;
import com.example.brainwashing.onlinebookingclinic.R;
import com.example.brainwashing.onlinebookingclinic.SearchActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener  {
    private GoogleApiClient googleApiClient;
    private Double current_lat,current_lng;
    ProgressDialog pd;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Finding your location");
        if(current_lat == null){
            pd.show();
        }
        Button btn_search = (Button)view.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                if(current_lat != null && current_lng != null){
                    intent.putExtra("lat",current_lat);
                    intent.putExtra("lng",current_lng);
                    startActivity(intent);
                }

            }
        });
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("SEARCH");

        googleApiClient = new GoogleApiClient.Builder(this.getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
            if(locationAvailability.isLocationAvailable()) {
                LocationRequest locationRequest = new LocationRequest()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(5000);
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

            } else {
                Toast.makeText(getActivity(), "Location not availability", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(TAG, "no permission");
            Toast.makeText(getActivity(), "Request gps permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        pd.dismiss();
        current_lat = location.getLatitude();
        current_lng = location.getLongitude();
        //Log.i(TAG,current_lat + " " + current_lng);
    }
}
