package com.example.brainwashing.onlinebookingclinic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.brainwashing.onlinebookingclinic.Models.Appointment;
import com.example.brainwashing.onlinebookingclinic.Models.ClinicDataModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookingInfoActivity extends AppCompatActivity {

    private Button book_btn;
    private DatabaseReference mClinicRef;
    private EditText fname,lname,idCardNumber,phoneNumber,symptom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_info);
        mClinicRef = FirebaseDatabase.getInstance().getReference("appointments");
        book_btn = (Button) findViewById(R.id.bookInfoBtn);
        fname = (EditText) findViewById(R.id.p_fname);
        lname = (EditText) findViewById(R.id.p_lname);
        idCardNumber = (EditText) findViewById(R.id.p_idcard);
        phoneNumber = (EditText) findViewById(R.id.p_number);
        symptom = (EditText) findViewById(R.id.p_symptom);
        Bundle bundle = getIntent().getExtras();

        final SharedPreferences sp = getSharedPreferences("PREF_LOGIN", Context.MODE_PRIVATE);

        final String status = "pending";
        final String booked_slot = bundle.getString("bookTime");
        final String user_id = sp.getString("CurrentID","def");
        //final String date = sp.getString("datePick","def");
        final String date = "2018-5-9";
        final String clinic_id = bundle.getString("clinicID");

        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClinicRef.push().setValue(new Appointment(fname.getText().toString(), lname.getText().toString(),idCardNumber.getText().toString(),phoneNumber.getText().toString(),symptom.getText().toString(),booked_slot,  status,  user_id,  date,  clinic_id));
                Intent in = new Intent(BookingInfoActivity.this,ConfirmActivity.class);
                in.putExtra("date",date);
                in.putExtra("time",booked_slot);
                in.putExtra("clinic",sp.getString("clinicName","def"));
                in.putExtra("address",sp.getString("clinicAddress","def"));
                startActivity(in);
            }
        });
    }
}
