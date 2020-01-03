package com.example.trackmypay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;


import androidx.fragment.app.FragmentActivity;

import com.applandeo.materialcalendarview.EventDay;
import com.example.trackmypay.domain.DataBaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ShiftManager {


    private DataBaseHelper dbHelper;

    ShiftManager(FragmentActivity context)
    {
        dbHelper = DataBaseHelper.getInstance(context);
    }




    public void addShift(Shift shift)
    {
        ContentValues newShift = new ContentValues();
        newShift.put(DataBaseHelper.DATE, shift.getDate());
        newShift.put(DataBaseHelper.START_TIME, shift.getStartTime());
        newShift.put(DataBaseHelper.END_TIME, shift.getEndTime());
        newShift.put(DataBaseHelper.COMMENTS, shift.getComments());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(DataBaseHelper.TABLE_NAME, null, newShift);

    }



    public List<EventDay> retrieveShifts(long firstDay, long lastDay)
    {
        List <EventDay> shifts =  new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME + " WHERE " + DataBaseHelper.DATE + " >= " + firstDay + " AND " + DataBaseHelper.DATE + " <=" + lastDay, null);
            if (cursor.moveToFirst())
            {
                while(!cursor.isAfterLast())
                {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(DataBaseHelper.DATE)));
                    EventDay shift = new EventDay(cal, R.drawable.ic_work);
                    shifts.add(shift);
                    cursor.moveToNext();

                }


            }


        cursor.close();
        return shifts;
    }


    public Shift retrieveShiftInfo(long shiftDate)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Shift shift = null;

        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME + " WHERE date = ?", new String[] {String.valueOf(shiftDate)});

        if (cursor.moveToFirst())
        {
            while(!cursor.isAfterLast())
            {
                shift = new Shift(cursor.getLong(cursor.getColumnIndex(DataBaseHelper.DATE)),
                        cursor.getLong(cursor.getColumnIndex(DataBaseHelper.START_TIME)),
                        cursor.getLong(cursor.getColumnIndex(DataBaseHelper.END_TIME)),
                        cursor.getString(cursor.getColumnIndex(DataBaseHelper.COMMENTS))
                        );
                cursor.moveToNext();
            }
        }

        cursor.close();
        return shift;

    }

}
