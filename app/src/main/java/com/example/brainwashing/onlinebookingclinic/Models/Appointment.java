package com.example.brainwashing.onlinebookingclinic.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Appointment implements Serializable {

    public String firstname;
    public String lastname;
    public String booked_slot;
    public String id_card_number;
    public String phone_number;
    public String status;
    public String user_id;
    public String symptom;
    public String date; //2018-05-01
    public String clinic_id; //09:00


    public Appointment() {
        // Default constructor required for calls to DataSnapshot.getValue()
    }

    public Appointment(String fname, String lname, String idCardNumber, String phoneNumber, String symptom, String booked_slot, String status, String user_id, String date, String clinic_id) {
        this.firstname = fname;
        this.lastname = lname;
        this.id_card_number = idCardNumber;
        this.phone_number = phoneNumber;
        this.symptom = symptom;
        this.booked_slot = booked_slot;
        this.status = status;
        this.user_id = user_id;
        this.date = date;
        this.clinic_id = clinic_id;
    }

}