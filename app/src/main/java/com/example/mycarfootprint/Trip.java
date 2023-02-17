package com.example.mycarfootprint;

import java.text.DecimalFormat;
import java.util.Locale;

public class Trip {
//    Trip Object having content of the trip for requirements
//
    private String Location;
    private String TripDate;
    private String FuelType;
    private Float Litre;
    private Float PerLitre;

    public Trip(String name, String date, String fuelType,Float litre, Float per) {
        this.Location = name;
        this.TripDate = date;
        this.FuelType = fuelType;
        this.Litre = litre;
        this.PerLitre = per;
    }
    //    getters
    public String getTripName() {
        return Location;
    }
    public String getTripDate() {
        return TripDate;
    }
    public Float getLitre() {
        return Litre;
    }
    public Float getPerLitre() {
        return PerLitre;
    }
    public String getFuelType() {
        return FuelType;
    }
    public String setFuelCost(){
        // reference: https://mkyong.com/java/how-to-round-double-float-value-to-2-decimal-points-in-java/
            final DecimalFormat df = new DecimalFormat("0.00");
            double FuelCost = this.PerLitre * this.Litre;
            return df.format(FuelCost);
    }



    public int setCarbonF(){
        // coefficients to calculate
        double gasoline = 2.32;
        double diesel = 2.69;
        String fuelType = getFuelType().toLowerCase(Locale.ROOT);
        if (fuelType.equals("gasoline")){
            return (int) (gasoline * Math.round(Float.valueOf(setFuelCost()))+.5);
        }
        else if (fuelType.equals("diesel")){
            return (int) (diesel * Math.round(Float.valueOf(setFuelCost()))+.5);
        }
        else return 0;
    }
}



