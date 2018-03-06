package com.example.brainwashing.onlinebookingclinic.Models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String email;
    public String firstname;
    public String lastname;
    public String gender;
    public String id_card_number;
    public String birth_date;
    public Integer age;
    public String phone_number;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String fname, String lname) {
        this.email = email;
        this.firstname = fname;
        this.lastname = lname;
        this.gender = "";
        this.id_card_number = "";
        this.birth_date = "";
        this.age = 0;
        this.phone_number = "";
    }

}
