
package com.example.brainwashing.onlinebookingclinic.Models;

import java.io.Serializable;
import java.util.List;

public class ClinicDataModel implements Serializable {
    private final static long serialVersionUID = -2137762088817515042L;
    private String clinic_address;
    private String clinic_email;
    private String clinic_image;
    private String clinic_name;
    private String clinic_phone;
    private List<Disable_time_slot> disable_time_slot = null;
    private List<String> day_off = null;
    private Location location;
    private List<String> specialty = null;
    private Booking_time_slots booking_time_slots;
    private Open_hours open_hours;
    public Boolean is_open;
    public float distance;
    public String clinic_id;

    public String getClinic_address() {
        return clinic_address;
    }

    public void setClinic_address(String clinic_address) {
        this.clinic_address = clinic_address;
    }

    public String getClinic_email() {
        return clinic_email;
    }

    public void setClinic_email(String clinic_email) {
        this.clinic_email = clinic_email;
    }

    public String getClinic_image() {
        return clinic_image;
    }

    public void setClinic_image(String clinic_image) {
        this.clinic_image = clinic_image;
    }

    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public String getClinic_phone() {
        return clinic_phone;
    }

    public void setClinic_phone(String clinic_phone) {
        this.clinic_phone = clinic_phone;
    }

    public List<Disable_time_slot> getDisable_time_slot() {
        return disable_time_slot;
    }

    public void setDisable_time_slot(List<Disable_time_slot> disable_time_slot) {
        this.disable_time_slot = disable_time_slot;
    }

    public List<String> getDay_off() {
        return day_off;
    }

    public void setDay_off(List<String> day_off) {
        this.day_off = day_off;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<String> getSpecialty() {
        return specialty;
    }

    public void setSpecialty(List<String> specialty) {
        this.specialty = specialty;
    }

    public Booking_time_slots getBooking_time_slots() {
        return booking_time_slots;
    }

    public void setBooking_time_slots(Booking_time_slots booking_time_slots) {
        this.booking_time_slots = booking_time_slots;
    }

    public Open_hours getOpen_hours() {
        return open_hours;
    }

    public void setOpen_hours(Open_hours open_hours) {
        this.open_hours = open_hours;
    }

}
