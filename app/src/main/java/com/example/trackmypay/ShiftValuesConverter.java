package com.example.trackmypay;

import android.util.Log;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ShiftValuesConverter {



    public static String DateStringConvert(long date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(new Date(date));
    }

    public static String StartTimeStringConvert(long startTime)
    {

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mma");
        return dateFormat.format(new Date(startTime));
    }



    public static String EndTimeStringConvert(long endTime)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mma");
        return dateFormat.format(new Date(endTime));
    }




    public static long DateStringConvert(String date)
    {
        Date shiftDateVar = null;

        SimpleDateFormat DateFormat = new SimpleDateFormat("MM/dd/yyyy");

        try
        {
            shiftDateVar = DateFormat.parse(date);
        }

        catch(ParseException e)
        {

        }

        return shiftDateVar.getTime();

    }

    public static long StartTimeStringConvert(String startTime)
    {

        Date startTimeVar = null;

        SimpleDateFormat StartTimeFormat = new SimpleDateFormat("hh:mma");


        try
        {
            startTimeVar = StartTimeFormat.parse(startTime);
        }

        catch(ParseException e)
        {
            Log.d("EXCEPTION:", e.getMessage());
        }

        Log.d("TEST VALUE", String.valueOf(startTimeVar.getTime()));

        return startTimeVar.getTime();
    }

    public static long EndTimeStringConvert(String comment)
    {

        Date endTimeVar = null;

        SimpleDateFormat EndTimeFormat = new SimpleDateFormat("hh:mma");

        try {

            endTimeVar = EndTimeFormat.parse(comment);
        }

        catch (ParseException e)
        {

        }

        return endTimeVar.getTime();

    }

}
