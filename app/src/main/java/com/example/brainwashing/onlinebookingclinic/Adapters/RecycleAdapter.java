package com.example.brainwashing.onlinebookingclinic.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.brainwashing.onlinebookingclinic.Models.ClinicDataModel;
import com.example.brainwashing.onlinebookingclinic.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
    List<ClinicDataModel> clinicListAll = new ArrayList<>();
    List<ClinicDataModel> clinicList = new ArrayList<>();
    List<Bitmap> photo = new ArrayList<>();
    private Context mContext;

    public RecycleAdapter(Context context, List<ClinicDataModel> dataset) {
        clinicList = dataset;
        mContext = context;
    }

    public void NoFilter(List<ClinicDataModel> data){
        //clinicList.clear();
        clinicList = data;
        notifyDataSetChanged();

        Log.i("do",String.valueOf(clinicListAll.size()));
    }

    public void FilterByOpenNow(List<ClinicDataModel> data){
        Log.i("do",String.valueOf(clinicListAll.size()));

        clinicList = data;
        notifyDataSetChanged();
        Log.i("do","nonow");

    }

    public void updateData(List<ClinicDataModel> data){

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ClinicDataModel clinic_model = clinicList.get(position);
        float dist = clinic_model.distance/1000;
        String show_dist = String.format("%.01f", dist);

        holder.mName.setText(clinic_model.getClinic_name());
        holder.mAddress.setText(clinic_model.getClinic_address());
        holder.mDistance.setText(show_dist+" Km From your location");
        if(clinic_model.is_open){
            holder.mOpen.setText("Open Now");
            holder.mOpen.setTextColor(Color.parseColor("#7CFC00"));
        }else {
            holder.mOpen.setText("Closed");
            holder.mOpen.setTextColor(Color.parseColor("#DC143C"));
        }
        Glide.with(mContext)
                .load(clinic_model.getClinic_image())
                .into(holder.mImage);

        /*ShowClinicHorizonAdapter hzAdapter = new ShowClinicHorizonAdapter(mContext,clinic_model);
        holder.recyclerListHz.setHasFixedSize(true);
        LinearLayoutManager horizontalManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerListHz.setLayoutManager(horizontalManager);
        holder.recyclerListHz.setAdapter(hzAdapter);*/
        

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("ver", "click" + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clinicList.size();

    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mName;
        public TextView mAddress;
        public TextView mDistance;
        public TextView mOpen;
        public ImageView mImage;
        //public RecyclerView recyclerListHz;

        public ViewHolder(View view) {
            super(view);
            mName = (TextView) view.findViewById(R.id.textView_clinic_name);
            mAddress = (TextView) view.findViewById(R.id.textView_clinic_address);
            mImage = (ImageView) view.findViewById(R.id.imageView_clinic_logo);
            mOpen = (TextView) view.findViewById(R.id.txt_open);
            mDistance = (TextView) view.findViewById(R.id.txt_distance);
            //recyclerListHz = (RecyclerView)view.findViewById(R.id.recycler_view_list);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //do nothing
        }
    }


}