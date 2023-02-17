package com.example.mycarfootprint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TripArrayAdapter extends ArrayAdapter<Trip> {
    public TripArrayAdapter(Context context, ArrayList<Trip> trips) {
        super(context, 0, trips);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        View view;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.content,
                    parent, false);
        } else {
            view = convertView;
        }
        Trip trip = getItem(position);
        TextView TripName = view.findViewById(R.id.location_text);
        TripName.setText(trip.getTripName());

//        TextView TripFuelCost = view.findViewById(R.id.gas_text);
//        TripFuelCost.setText(trip.setFuelCost());


//        TextView Date = view.findViewById(R.id.date_text);
//        TripName.setText(trip.getTripName());
//        Date.setText(trip.getTripDate());
//            Date.setText((trip.getTripDate()));

        return view;
    }
}

