package com.example.tirhal;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UpcomingRecyclerAdapter extends RecyclerView.Adapter<UpcomingRecyclerAdapter.ViewHolder> {
    List items;
    Context context;
    Activity activity;
    public UpcomingRecyclerAdapter(Context context,List items,Activity activity) {
        this.activity = activity;
        this.items = items;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater  = LayoutInflater .from(parent.getContext());
        View view = inflater.inflate(R.layout.trip_upcoming_item , parent ,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip= (Trip) items.get(position);
        holder.nameTxt.setText(trip.getTripName());
        holder.timeTxt.setText(trip.getTime());
        holder.dateTxt.setText(trip.getDate());
        holder.sourceTxt.setText(trip.getStartPoint());
        holder.destinationTxt.setText(trip.getEndPoint());
        holder.dateTxt.setText(trip.getDate());
//        Trip trip= (Trip) items.get(position);
//
//        holder . timeOfTrip.setText(trip.getTripName());
//        holder . TypeOfTrip.setText(trip.getTime());
//        holder . fromTrip.setText(trip.getDate());
//        holder . toTrip.setText(trip.getEndPoint());
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0 ;
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
//        EditText timeOfTrip;
//        EditText TypeOfTrip;
//        EditText fromTrip;
//        EditText toTrip;
       TextView sourceTxt,destinationTxt,nameTxt,timeTxt,dateTxt;
        ImageView trip_img;
        ImageButton start_btn;
        ImageButton editNotes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            timeOfTrip = itemView.findViewById(R.id.timeLbl);
//            TypeOfTrip= itemView.findViewById(R.id.typeLbl);
//            fromTrip= itemView.findViewById(R.id.fromLbl);
//            toTrip = itemView.findViewById(R.id.toLbl);
            sourceTxt=itemView.findViewById(R.id.source_txt);
            destinationTxt=itemView.findViewById(R.id.destination_txt);
            nameTxt=itemView.findViewById(R.id.name_txt);
            timeTxt=itemView.findViewById(R.id.time_txt);
            dateTxt=itemView.findViewById(R.id.date_txt);
            trip_img=itemView.findViewById(R.id.trip_img);
            start_btn= itemView.findViewById(R.id.start_btn);
            editNotes=itemView.findViewById(R.id.btn_editNote);
        }
    }
}
