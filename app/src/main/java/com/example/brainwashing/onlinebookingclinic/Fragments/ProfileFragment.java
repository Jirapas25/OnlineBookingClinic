package com.example.brainwashing.onlinebookingclinic.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.brainwashing.onlinebookingclinic.LoginActivity;
import com.example.brainwashing.onlinebookingclinic.R;
import com.google.android.gms.auth.api.Auth;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button signoutBtn = (Button)view.findViewById(R.id.signoutBtn);
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        return view;
    }

    public void signOut(){
        SharedPreferences sp = this.getActivity().getSharedPreferences("PREF_LOGIN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("CurrentID", null);
        editor.putString("AuthProvider", null);
        editor.commit();

        startActivity(new Intent(this.getActivity(), LoginActivity.class));
        this.getActivity().finish();

    }

}
