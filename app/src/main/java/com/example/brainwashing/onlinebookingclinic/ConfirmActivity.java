package com.example.brainwashing.onlinebookingclinic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ConfirmActivity extends AppCompatActivity {
    private TextView date,time,clinic,address,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        date = (TextView) findViewById(R.id.c_date);
        time = (TextView) findViewById(R.id.c_date);
        clinic = (TextView) findViewById(R.id.c_date);
        address = (TextView) findViewById(R.id.c_date);
        status = (TextView) findViewById(R.id.c_date);

        Bundle bundle = getIntent().getExtras();
        String date_v = bundle.getString("date");
        String time_v = bundle.getString("time");
        String clinic_v = bundle.getString("clinic");
        String address_v = bundle.getString("address");
        String status_v = "pending";
    }
}
