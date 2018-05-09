package com.example.brainwashing.onlinebookingclinic.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.brainwashing.onlinebookingclinic.BookingInfoActivity;
import com.example.brainwashing.onlinebookingclinic.ClinicDetailsActivity;
import com.example.brainwashing.onlinebookingclinic.Models.Appointment;
import com.example.brainwashing.onlinebookingclinic.Models.Booking_time_slots;
import com.example.brainwashing.onlinebookingclinic.Models.ClinicDataModel;
import com.example.brainwashing.onlinebookingclinic.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ShowClinicHorizonAdapter extends RecyclerView.Adapter<ShowClinicHorizonAdapter.ViewHolder> {

    private Context mContext;
    private ClinicDataModel clinicData;
    private List<String> timeSlot = new ArrayList<>();
    private Calendar calendar;
    private DatabaseReference mAppmRef;
    private SimpleDateFormat sdf;
    public List<Appointment> appointmentData = new ArrayList<>();

    public void updateTime(ClinicDataModel data){
        Log.i("do","update time");
        clinicData = data;
        notifyDataSetChanged();
    }

    /*public void getAppointment(){

        mAppmRef = FirebaseDatabase.getInstance().getReference("appointments");
        mAppmRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    Appointment appm = singleSnapshot.getValue(Appointment.class);
                    appointmentData.add(appm);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("err", "error get db");
            }
        });
    }*/

    public ShowClinicHorizonAdapter(Context context, ClinicDataModel data) {
        clinicData = data;
        mContext = context;
        //getAppointment();
        String[] date_split = data.date_pick.split("-");
        int date = Integer.parseInt(date_split[2]);
        int month= Integer.parseInt(date_split[1]);
        int year = Integer.parseInt(date_split[0]);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, date);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println("DAYYYY : "+ calendar.getTime());
        System.out.println("DAYYYY : "+ today);
        switch (today) {
            case Calendar.SUNDAY:
                for(int i=0;i<clinicData.getBooking_time_slots().getSun().size();i++){
                    timeSlot.add(clinicData.getBooking_time_slots().getSun().get(i));
                }
                break;
            case Calendar.MONDAY:
                for(int i=0;i<clinicData.getBooking_time_slots().getMon().size();i++){
                    timeSlot.add(clinicData.getBooking_time_slots().getMon().get(i));
                }
                break;
            case Calendar.TUESDAY:
                for(int i=0;i<clinicData.getBooking_time_slots().getTue().size();i++){
                    timeSlot.add(clinicData.getBooking_time_slots().getTue().get(i));
                }
                break;
            case Calendar.WEDNESDAY:
                for(int i=0;i<clinicData.getBooking_time_slots().getWed().size();i++){
                    timeSlot.add(clinicData.getBooking_time_slots().getWed().get(i));
                }
                break;
            case Calendar.THURSDAY:
                for(int i=0;i<clinicData.getBooking_time_slots().getThu().size();i++){
                    timeSlot.add(clinicData.getBooking_time_slots().getThu().get(i));
                }
                break;
            case Calendar.FRIDAY:
                for(int i=0;i<clinicData.getBooking_time_slots().getFri().size();i++){
                    timeSlot.add(clinicData.getBooking_time_slots().getFri().get(i));
                }
                break;
            case Calendar.SATURDAY:
                for(int i=0;i<clinicData.getBooking_time_slots().getSat().size();i++){
                    timeSlot.add(clinicData.getBooking_time_slots().getSat().get(i));
                }
                break;
            default:
                break;
        }

    }

    @Override
    public ShowClinicHorizonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_horizon_layout, parent, false);

        ShowClinicHorizonAdapter.ViewHolder viewHolder = new ShowClinicHorizonAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String timetxt = timeSlot.get(position);
        holder.btn_time.setText(timetxt);


        if(!isSlotAvailable(position)){
            holder.btn_time.setEnabled(false);
        }

        holder.btn_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("hz", "click" + position);
                Intent in = new Intent(mContext, BookingInfoActivity.class);
                in.putExtra("clinicID",clinicData.clinic_id);
                in.putExtra("bookTime",timeSlot.get(position));
                in.putExtra("clinicName",clinicData.getClinic_name());
                in.putExtra("clinicAddress",clinicData.getClinic_address());
                in.putExtra("datePick",clinicData.date_pick);
                mContext.startActivity(in);
            }
        });
    }

    public boolean isSlotAvailable(int position){
        sdf = new SimpleDateFormat("yyyy-MM-dd");

        for(int i=0;i<appointmentData.size();i++){
            System.out.println("select:"+appointmentData.get(i).date);
            if(appointmentData.get(i).date == sdf.format(calendar.getTime())){

                if (appointmentData.get(i).date == timeSlot.get(position)){


                    return false;
                }
            }
        }
        //System.out.println("select:"+appointmentData.get(0).book_date);
        //System.out.println("yesss");
        return true;
    }

    @Override
    public int getItemCount() {
        return timeSlot.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Button btn_time;
        public ViewHolder(View view) {
            super(view);
            btn_time = (Button)view.findViewById(R.id.btn_show_time);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}