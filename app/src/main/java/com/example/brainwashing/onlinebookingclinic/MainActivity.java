package com.example.brainwashing.onlinebookingclinic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.brainwashing.onlinebookingclinic.Fragments.BookedFragment;
import com.example.brainwashing.onlinebookingclinic.Fragments.ProfileFragment;
import com.example.brainwashing.onlinebookingclinic.Fragments.SavedFragment;
import com.example.brainwashing.onlinebookingclinic.Fragments.SearchFragment;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private ActionBar actionBar;

    private BottomNavigationViewEx.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
           = new BottomNavigationViewEx.OnNavigationItemSelectedListener() {

       @Override
       public boolean onNavigationItemSelected(@NonNull MenuItem item) {
           switch (item.getItemId()) {
               case R.id.navigation_search:
                   fragment = new SearchFragment();
                   break;
               case R.id.navigation_booked:
                   fragment = new BookedFragment();
                   break;
               case R.id.navigation_saved:
                   fragment = new SavedFragment();
                   break;
               case R.id.navigation_me:
                   fragment = new ProfileFragment();
                   break;
           }
           final FragmentTransaction transaction = fragmentManager.beginTransaction();
           transaction.replace(R.id.content, fragment).commit();
           return true;
       }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bnve);
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);
        bnve.setIconSize(28,28);

        fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragment = new SearchFragment();
        transaction.replace(R.id.content, fragment).commit();
        bnve.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
