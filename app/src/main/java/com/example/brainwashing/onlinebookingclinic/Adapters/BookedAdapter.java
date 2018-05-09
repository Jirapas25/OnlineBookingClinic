package com.example.brainwashing.onlinebookingclinic.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brainwashing.onlinebookingclinic.Models.Appointment;
import com.example.brainwashing.onlinebookingclinic.Models.ClinicDataModel;
import com.example.brainwashing.onlinebookingclinic.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class BookedAdapter extends RecyclerView.Adapter<BookedAdapter.ViewHolder> {
    private List<Appointment> appointmentData = new ArrayList<>();
    private Context mContext;

    public BookedAdapter(Context context, List<Appointment> data){
        appointmentData = data;
        mContext = context;
    }

    @Override
    public BookedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.booked_list_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        final Appointment adata = appointmentData.get(position);
        System.out.println(adata.date);
        String test = "2018-5-9";
        String[] date_split = test.split("-");
        int date = Integer.parseInt(date_split[2]);
        int month= Integer.parseInt(date_split[1]);
        int year = Integer.parseInt(date_split[0]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, date);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);


        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(calendar.getTime());

        holder.b_date.setText(String.valueOf(calendar.DAY_OF_MONTH));
        holder.b_time.setText(adata.booked_slot);
        holder.b_name.setText("fff");
        holder.month.setText(month_name);
        holder.status.setText(adata.status);

    }

    @Override
    public int getItemCount() {
        return appointmentData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView b_date,month,b_name,b_time,status;


        public ViewHolder(View view) {
            super(view);
            b_date = (TextView) view.findViewById(R.id.b_date);
            month = (TextView) view.findViewById(R.id.b_month);
            b_name = (TextView) view.findViewById(R.id.b_name);
            b_time = (TextView) view.findViewById(R.id.b_time);
            status = (TextView) view.findViewById(R.id.b_status);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}