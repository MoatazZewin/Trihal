package com.example.tirhal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UpcomingRecyclerAdapter extends RecyclerView.Adapter<UpcomingRecyclerAdapter.ViewHolder> {
    List<ModelHistory> items;
    public UpcomingRecyclerAdapter(List<ModelHistory> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater  = LayoutInflater .from(parent.getContext());
        View view = inflater.inflate(R.layout.upcoming_item , parent ,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelHistory item = items.get(position);
        holder . timeOfTrip.setText(item.getTimeOfTrip());
        holder . TypeOfTrip.setText(item.getTypeOfTrip());
        holder . fromTrip.setText(item.getFromTrip());
        holder . toTrip.setText(item.getToTrip());
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0 ;
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        EditText timeOfTrip;
        EditText TypeOfTrip;
        EditText fromTrip;
        EditText toTrip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeOfTrip = itemView.findViewById(R.id.timeLbl);
            TypeOfTrip= itemView.findViewById(R.id.typeLbl);
            fromTrip= itemView.findViewById(R.id.fromLbl);
            toTrip = itemView.findViewById(R.id.toLbl);
        }
    }
}
