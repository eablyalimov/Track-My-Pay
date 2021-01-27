package com.example.trackmypay.customEscentricCalendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.trackmypay.R;
import com.example.trackmypay.ShiftManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GridAdapter extends ArrayAdapter {


    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    private List<Calendar> allEvents;
    ShiftManager shiftManager;
    Calendar theDay = null;
    Context context;
    Calendar dateCal;
    Object lock = new Object();

    public GridAdapter(Context context, List<Date> monthlyDates, Calendar currentDate, List<Calendar> allEvents) {
        super(context, com.example.trackmypay.R.layout.single_cell_layout);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.allEvents = allEvents;
        shiftManager = new ShiftManager((FragmentActivity)context);
        mInflater = LayoutInflater.from(context);
        this.context = context;


    }


    @NonNull
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        Date mDate = monthlyDates.get(position);
        dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);

        dateCal.set(Calendar.HOUR_OF_DAY, 0);
        dateCal.set(Calendar.MINUTE, 0);
        dateCal.set(Calendar.SECOND, 0);
        dateCal.set(Calendar.MILLISECOND, 0);


        final int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        final int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        final int displayYear = dateCal.get(Calendar.YEAR);
        final int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        final int currentYear = currentDate.get(Calendar.YEAR);

        final View[] view = {convertView};

        //theDay = shiftManager.retrieveShiftsForCalendar(dateCal.getTimeInMillis());




        Runnable dbCall = new Runnable() {
            @Override
            public void run() {
                synchronized (lock)
                {
                    theDay = shiftManager.retrieveShiftsForCalendar(dateCal.getTimeInMillis());
                    lock.notify();
                    //Log.d("Thread Name:" ,Thread.currentThread().getName());

                }

            }
        };


        Thread execDbCall = new Thread(dbCall);




        Runnable buildUI = new Runnable() {
            @Override
            public void run() {

                synchronized (lock) {
                    try {

                        if (view[0] == null) {
                            view[0] = mInflater.inflate(com.example.trackmypay.R.layout.single_cell_layout, parent, false);

                        }

                        if (displayMonth != currentMonth || displayYear != currentYear) {

                            TextView dayNum = view[0].findViewById(R.id.calendar_date_id);
                            dayNum.setTextColor(Color.parseColor("#b4b5bf"));

                        }


                        //Add day to calendar
                        TextView cellNumber = view[0].findViewById(R.id.calendar_date_id);
                        cellNumber.setText(String.valueOf(dayValue));
                        Calendar tempC = Calendar.getInstance();
                        if (dayValue == tempC.get(Calendar.DAY_OF_MONTH) && currentMonth == tempC.get(Calendar.MONTH) + 1 && currentYear == tempC.get(Calendar.YEAR)) {
                            cellNumber.setTextColor(Color.parseColor("#01579B"));
                            cellNumber.setTypeface(Typeface.DEFAULT_BOLD);
                        }

                        //Log.d("Thread Name:" ,Thread.currentThread().getName());

                        lock.wait();
                        ImageView eventIndicator = view[0].findViewById(R.id.event_id);

                        if (theDay != null && dayValue == theDay.get(Calendar.DAY_OF_MONTH) && displayMonth == theDay.get(Calendar.MONTH) + 1 && displayYear == theDay.get(Calendar.YEAR)) {
                            eventIndicator.setImageResource(R.drawable.ic_work);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }
        };




        execDbCall.start();
        ((FragmentActivity)context).runOnUiThread(buildUI);

//        ((FragmentActivity)context).runOnUiThread(setEvent);


//

        return view[0];
    }

    @Override
    public int getCount() {
        return monthlyDates.size();
    }
    @Nullable
    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }
    @Override
    public int getPosition(Object item) {
        return monthlyDates.indexOf(item);
    }

}
