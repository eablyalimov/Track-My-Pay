package com.example.trackmypay;

import android.util.Log;


import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    public static String timeWorkedConvert(long time)
    {


        String formatted = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(time), TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)));
        return formatted + " hours";
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
