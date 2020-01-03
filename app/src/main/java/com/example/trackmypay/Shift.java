package com.example.trackmypay;

import android.content.ContentValues;

import com.applandeo.materialcalendarview.EventDay;
import com.example.trackmypay.domain.DataBaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Shift {

    private long date;
    private long startTime;
    private long endTime;
    private String comments;
    private EventDay calendarHandler;

    Shift(long date, long startTime, long endTime, String comments)
    {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.comments = comments;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(this.date));
        this.calendarHandler = new EventDay(cal);
    }




    Shift(long date, long startTime, long endTime)
    {
        this(date, startTime, endTime, null);
    }




    public long getStartTime() {


        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
