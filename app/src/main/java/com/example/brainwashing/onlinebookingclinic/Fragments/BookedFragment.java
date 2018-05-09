package com.example.brainwashing.onlinebookingclinic.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.brainwashing.onlinebookingclinic.Adapters.BookedAdapter;
import com.example.brainwashing.onlinebookingclinic.Models.Appointment;
import com.example.brainwashing.onlinebookingclinic.Models.ClinicDataModel;
import com.example.brainwashing.onlinebookingclinic.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookedFragment extends Fragment {
    private DatabaseReference mClinicRef;
    BookedAdapter mAdapter;
    Context con = this.getActivity();
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    public List<Appointment> datatoview = new ArrayList<>();
    public BookedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booked, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecycleViewBook);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(con);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        SharedPreferences sp = this.getActivity().getSharedPreferences("PREF_LOGIN", Context.MODE_PRIVATE);
        final String user_id = sp.getString("CurrentID", "DEF");


        mClinicRef = FirebaseDatabase.getInstance().getReference("appointments");
        mClinicRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    Appointment appt = singleSnapshot.getValue(Appointment.class);
                    appt.clinic_id = appt.user_id;
                    System.out.println("gggg:"+appt.user_id);
                    System.out.println("ugggg:"+user_id);

                    if(appt.user_id.equals(user_id)){
                        datatoview.add(appt);
                        System.out.println("agggg:"+appt.date);
                    }
                }

                System.out.println("gggg:"+String.valueOf(datatoview.size()));

                 mAdapter = new BookedAdapter(getActivity(),datatoview);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
