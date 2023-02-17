package com.example.mycarfootprint;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AddTripFragment.AddTripDialogListener {

    private ArrayList<Trip> dataList;
    private ListView TripList;
    private TripArrayAdapter TripAdapter;


    public void updateListViewTotals(){
        TextView TotalFuelCostUpdate = findViewById(R.id.TotalFuelCostUpdate);
        TextView TotalCFUpdate = findViewById(R.id.CF_to_update);
        double carbon = 0;
        double total = 0.0;
        for (Trip trip: dataList){
            double temp = trip.setCarbonF();
            total +=  (Float.valueOf(trip.setFuelCost()));
            carbon += temp;
        }
        TotalFuelCostUpdate.setText(String.format("%.2f", total));
        TotalCFUpdate.setText(String.valueOf((int) Math.round(carbon)));
    }

    @Override
    // because addCity was from an interface, we need to use implement from that class to override
    public void addTrip(Trip trip) {
        TripAdapter.add(trip);
        updateListViewTotals();
        TripAdapter.notifyDataSetChanged();
    }


//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String[] TripName = { };
        String[] Date = { };
        String[] FuelType = { };

//        gas
        Float[] Gas = {};
        Float[] PerLitre = {};





        dataList = new ArrayList<>();
        for (int i = 0; i < TripName.length; i++) {
//            create a tuple for gathering city and province
            dataList.add(new Trip(TripName[i], Date[i], FuelType[i],Gas[i], PerLitre[i]));
        }



        TripAdapter = new TripArrayAdapter(this, dataList);

        TripList = findViewById(R.id.trip_list);
        TripList.setAdapter(TripAdapter);

        FloatingActionButton fab = findViewById(R.id.button_add_trip);
        fab.setOnClickListener(v -> {
            new AddTripFragment().show(getSupportFragmentManager(), "Add Trip");
        });

        TripList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Trip orig_location = TripAdapter.getItem(i);
                new AddTripFragment(i, orig_location).show(getSupportFragmentManager(), "Edit Trip");
            }
        });



    }

    @Override
    public void editTrip(int position, Trip trip) {
        dataList.set(position, trip);
        updateListViewTotals();
        TripAdapter.notifyDataSetChanged();
    }

    @Override
//    deleteTrip method
    public void deleteTrip(int position){
        dataList.remove(position);
        updateListViewTotals();
        TripAdapter.notifyDataSetChanged();
    }
}
