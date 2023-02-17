package com.example.mycarfootprint;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class AddTripFragment extends DialogFragment {
//    these are implemented in MainActivity
    interface AddTripDialogListener {
        void addTrip(Trip trip);
        void editTrip(int position, Trip trip);

        void deleteTrip(int position);
    }
    private AddTripDialogListener listener;
    private int position;
    private Trip orig_name = null;

    public AddTripFragment(){
        super();
    }

    public AddTripFragment(int position, Trip name) {
        super();
        this.position = position;
        this.orig_name = name;
    }

    public Boolean CheckFieldEmpty (String string){
        if (string.matches("")){
            return true;
        }
        else return false;
    }


    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if (context instanceof AddTripDialogListener){
            listener = (AddTripDialogListener) context;
        } else{
            throw new RuntimeException(context + "must implement AddCityDialogListener");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_trip, null);

        EditText editTripName = view.findViewById(R.id.edit_text_trip_text);
//        EditText editDateName = view.findViewById(R.id.edit_text_date_text);
        EditText editFuelType = view.findViewById(R.id.edit_fuel_type_text);
        EditText editLitreName = view.findViewById(R.id.edit_text_litre_text);
        EditText editPerLitreName = view.findViewById(R.id.edit_text_perLitre_text);

        EditText editDateName = view.findViewById(R.id.edit_text_date_text);
//        final Calendar myCalendar = Calendar.getInstance();

        TextView FuelCost = view.findViewById(R.id.Fuel_change_to);
        TextView CarbonFootPrint = view.findViewById(R.id.Edit_CarbonFootPrint);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


        if (this.orig_name != null) {

            editTripName.setText(this.orig_name.getTripName());
            editDateName.setText(this.orig_name.getTripDate());
            editFuelType.setText(this.orig_name.getFuelType());
            editLitreName.setText(this.orig_name.getLitre().toString());
            editPerLitreName.setText(this.orig_name.getPerLitre().toString());



            FuelCost.setText(this.orig_name.setFuelCost());

//            https://stackoverflow.com/questions/3994315/integer-value-in-textview
            CarbonFootPrint.setText(""+Math.round(this.orig_name.setCarbonF()));


//          setting the delete button red
//          referenced from: https://blog.mindorks.com/spannable-string-text-styling-with-spans/#:~:text=Text%20styling%20is%20one%20of,text%20more%20attractive%20and%20appealing.
            SpannableString deleteButton = new SpannableString("Delete");
            deleteButton.setSpan(new ForegroundColorSpan(Color.RED), 0, deleteButton.length(), 0);
            editDateName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            editDateName.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(c.DATE));
                    datePickerDialog.show();
                }
            });

            return builder
//              builder for editing/viewing trips
                    .setView(view)
                    .setTitle("Edit/View Trip")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String TripName = editTripName.getText().toString();
                            String Date = editDateName.getText().toString();
                            String FuelType = editFuelType.getText().toString();
                            Float LitreName = Float.valueOf(editLitreName.getText().toString());
                            Float PerLitreName = Float.valueOf(editPerLitreName.getText().toString());

                            String LitreCheckValid = LitreName.toString();
                            String PerLitreCheckValid = PerLitreName.toString();


//                            Assertions for input protection
//                            NOTE: the checks do not work when editing the fuel and fuel/L to empty. This is the only major flaw with this code
//                            I beleive it has to do with the way SetCf() in Trip that is causing this issue (assume it is a logic issue)
//                            Checks work in add except for when user wants to add empty every field. As long as one is entered and rest is not, assertions work.
                            if (CheckFieldEmpty(TripName) || CheckFieldEmpty(Date) || CheckFieldEmpty(FuelType) || CheckFieldEmpty(LitreCheckValid) || CheckFieldEmpty(PerLitreCheckValid) ){
                                Toast.makeText(getContext(), "1 or more Fields Empty: Could Not Edit Trip.", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(), "Please Ensure All Entries are Valid!", Toast.LENGTH_SHORT).show();

                            }
                            else if (!FuelType.equalsIgnoreCase("gasoline") && !FuelType.equalsIgnoreCase("diesel")){
                                Toast.makeText(getContext(), "Invalid Fuel Type: Could Not Edit Trip", Toast.LENGTH_SHORT).show();

                            }
                            else if (TripName.length() > 30){
                                Toast.makeText(getContext(), "Trip Name Too Long: Could Not Edit Trip", Toast.LENGTH_SHORT).show();

                            }
                            else if ((LitreName <= 0) || PerLitreName<=0){
                                Toast.makeText(getContext(), "Litre input invalid. Must be Greater Than Zero: Could Not Add Trip", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                listener.editTrip(position, new Trip(TripName, Date, FuelType, LitreName, PerLitreName));
//                                Toast.makeText(getContext(), "This is going to Add!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    // delete button done
                    .setNeutralButton(deleteButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listener.deleteTrip(position);
                        }
                    })
                    .create();
        }
        else {
//            referenced teammate Nathan Wong: ncw1 for the calendar click
//            and : https://www.geeksforgeeks.org/how-to-popup-datepicker-while-clicking-on-edittext-in-android/
            editDateName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            editDateName.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(c.DATE));
                    datePickerDialog.show();
                }
            });
            return builder
                    .setView(view)
                    .setTitle("Add a Trip")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", (dialog, which) -> {
                        String TripName = editTripName.getText().toString();
                        String DateName = editDateName.getText().toString();
                        String FuelType = editFuelType.getText().toString();

                        Float LitreName = Float.valueOf(editLitreName.getText().toString());


                        Float PerLitreName = Float.valueOf(editPerLitreName.getText().toString());



                        String LitreCheckValid = LitreName.toString();
                        String PerLitreCheckValid = PerLitreName.toString();
//
//                        Assertions for input protection
                        //                            NOTE: the checks do not work when editing the fuel and fuel/L to empty. This is the only major flaw with this code
//                            I beleive it has to do with the way SetCf() in Trip that is causing this issue (assume it is a logic issue)
//                            checks work in add except for when user wants to add empty every field. As long as one is entered and rest is not, assertions work.
                        if (CheckFieldEmpty(TripName) || CheckFieldEmpty(DateName) || CheckFieldEmpty(FuelType) || CheckFieldEmpty(LitreCheckValid) || CheckFieldEmpty(PerLitreCheckValid) ){
                            Toast.makeText(getContext(), "1 or more Fields Empty: Could Not Add Trip", Toast.LENGTH_SHORT).show();
                        }
                        else if (!FuelType.equalsIgnoreCase("gasoline") && !FuelType.equalsIgnoreCase("diesel")){
                            Toast.makeText(getContext(), "Invalid Fuel Type: Could Not Add Trip", Toast.LENGTH_SHORT).show();
                        }
                        else if (TripName.length() > 30){
                            Toast.makeText(getContext(), "Trip Name Too Long: Could Not Add Trip", Toast.LENGTH_SHORT).show();
                        }
                        else if ((LitreName <= 0) || PerLitreName<=0){
                            Toast.makeText(getContext(), "Litre input invalid. Must be Greater Than Zero: Could Not Add Trip", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            listener.addTrip(new Trip(TripName, DateName, FuelType, LitreName, PerLitreName));
                        }
                    })
                    .create();
        }
    }
}
