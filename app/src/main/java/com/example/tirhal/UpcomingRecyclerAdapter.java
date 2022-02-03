package com.example.tirhal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                customTwoButtonsDialog(((Trip) items.get(position)),position-1);
                items.remove((Trip) items.get(position));
                return false;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTrip((Trip) items.get(position));
                notifyDataSetChanged();
                holder.itemView.setVisibility(View.INVISIBLE);
            }
        });
        holder.start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initMap(((Trip) items.get(position)).getEndPointLat(),((Trip) items.get(position)).getEndPointLong());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentMainActivity.database.tripDAO().updateTripStatus(FragmentMainActivity.fireBaseUseerId,((Trip) items.get(position)).getId(),"finished");
                    }
                }).start();

            }
        });
        holder.editNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNotes((Trip) items.get(position));
            }
        });
//        Trip trip= (Trip) items.get(position);
//
//        holder . timeOfTrip.setText(trip.getTripName());
//        holder . TypeOfTrip.setText(trip.getTime());
//        holder . fromTrip.setText(trip.getDate());
//        holder . toTrip.setText(trip.getEndPoint());
    }
    public void updateTrip(Trip trip){
        Intent intent = new Intent(context,AddTripActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("KEY",2);
        bundle.putInt("ID",trip.getId());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void initMap(double latitude, double longtitude){
        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=&destination="+latitude+","+longtitude+"&travelmode=driving");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }
    public void editNotes(Trip trip){
        Intent intent = new Intent(context,AddTripActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("KEY",3);
        bundle.putInt("ID",trip.getId());
        intent.putExtras(bundle);
        context.startActivity(intent);
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
    public void customTwoButtonsDialog(Trip trip , int position){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context,R.style.AlertDialogTheme);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_permission_dialog,(ConstraintLayout) activity.findViewById(R.id.dialogLayoutContainer));
        builder.setView(view);
        ((TextView)view.findViewById(R.id.textTitle)).setText(Constants.APP_NAME);
        ((TextView)view.findViewById(R.id.textMessage)).setText("Do you want to delete this trip ?");
        ((Button)view.findViewById(R.id.btnCancel)).setText(Constants.PER_DIALOG_CANCEL);
        ((Button)view.findViewById(R.id.btnOk)).setText(Constants.PER_DIALOG_CONFIRM);
        ((ImageView)view.findViewById(R.id.imgTitle)).setImageResource(R.drawable.ic_baseline_hourglass_bottom_24);

        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        view.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentMainActivity.database.tripDAO().updateTripStatus(FragmentMainActivity.fireBaseUseerId,trip.getId(),"cancelled");
//                        unregisterAlarm(trip);
                        // tripList.remove(trip);
                    }
                }).start();
                //  notifyItemRemoved(position);
                notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });

//        if(alertDialog.getWindow() !=null){
//            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        }
//        alertDialog.show();
    }
}
