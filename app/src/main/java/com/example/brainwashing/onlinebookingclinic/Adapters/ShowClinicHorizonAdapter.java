package com.example.brainwashing.onlinebookingclinic.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.brainwashing.onlinebookingclinic.Models.Booking_time_slots;
import com.example.brainwashing.onlinebookingclinic.Models.ClinicDataModel;
import com.example.brainwashing.onlinebookingclinic.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ShowClinicHorizonAdapter extends RecyclerView.Adapter<ShowClinicHorizonAdapter.ViewHolder> {

    private Context mContext;
    private ClinicDataModel clinicData;
    List<String> timeSlot = new ArrayList<>();

    public ShowClinicHorizonAdapter(Context context, ClinicDataModel data) {
        clinicData = data;
        mContext = context;
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);

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


        holder.btn_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("hz", "click" + position);
            }
        });
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