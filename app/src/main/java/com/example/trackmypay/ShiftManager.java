package com.example.trackmypay;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.fragment.app.FragmentActivity;

import com.example.trackmypay.domain.DataBaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ShiftManager {


    private DataBaseHelper dbHelper;

    public ShiftManager(FragmentActivity context)
    {
        dbHelper = DataBaseHelper.getInstance(context);
    }









    public long addShift(Shift shift)
    {
        ContentValues newShift = new ContentValues();
        newShift.put(DataBaseHelper.DATE, shift.getDate());
        newShift.put(DataBaseHelper.START_TIME, shift.getStartTime());
        newShift.put(DataBaseHelper.END_TIME, shift.getEndTime());
        newShift.put(DataBaseHelper.HOURLY_RATE, shift.getHourlyRate());
        newShift.put(DataBaseHelper.PAID_BREAK_DURATION, shift.getPaidBreakMin());
        newShift.put(DataBaseHelper.UNPAID_BREAK_DURATION, shift.getUnpaidBreakMin());
        newShift.put(DataBaseHelper.BONUS, shift.getBonus());
        newShift.put(DataBaseHelper.EXPENSES, shift.getExpenses());
        newShift.put(DataBaseHelper.COMMENTS, shift.getComments());
        newShift.put(DataBaseHelper.IS_COMMISSION, shift.isCommission());
        newShift.put(DataBaseHelper.SALES_MADE_AMOUNT, shift.getSalesMade());
        newShift.put(DataBaseHelper.TARGET_AMOUNT, shift.getTarget());


        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long entryID;

        entryID = db.insert(DataBaseHelper.TABLE_NAME, null, newShift);

        return entryID;


    }




    public List <Calendar> retrieveShifts(long firstDay, long lastDay)
    {

        final List <Calendar> shifts =  new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        final Cursor cursor = db.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME + " WHERE " + DataBaseHelper.DATE + " >= " + firstDay + " AND " + DataBaseHelper.DATE + " <=" + lastDay, null);
            if (cursor.moveToFirst())
            {
                        while(!cursor.isAfterLast())
                        {
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(DataBaseHelper.DATE)));
                            shifts.add(cal);
                            cursor.moveToNext();

                        }


            }
        cursor.close();
        return shifts;
    }


    public Calendar retrieveShiftsForCalendar(long shiftDate)
    {



        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Calendar cal = null;

        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME + " WHERE date = ?", new String[] {String.valueOf(shiftDate)});



        if (cursor.moveToFirst() && cursor.getCount() > 0)
        {
            while(!cursor.isAfterLast())
            {
                cal = Calendar.getInstance();
                cal.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(DataBaseHelper.DATE)));
                cursor.moveToNext();
            }

        }

        cursor.close();

        return cal;
    }


    public List<Shift> retrieveShiftsBetweenDates(long firstDay, long lastDay)
    {
        List <Shift> shifts =  new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME + " WHERE " + DataBaseHelper.DATE + " >= " + firstDay + " AND " + DataBaseHelper.DATE + " <=" + lastDay, null);
        if (cursor.moveToFirst())
        {
            while(!cursor.isAfterLast())
            {
               Shift shift = new Shift(
                        cursor.getInt(cursor.getColumnIndex(DataBaseHelper.ID)),
                        cursor.getLong(cursor.getColumnIndex(DataBaseHelper.DATE)),
                        cursor.getLong(cursor.getColumnIndex(DataBaseHelper.START_TIME)),
                        cursor.getLong(cursor.getColumnIndex(DataBaseHelper.END_TIME)),
                        cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.HOURLY_RATE)),
                        cursor.getLong(cursor.getColumnIndex(DataBaseHelper.PAID_BREAK_DURATION)),
                        cursor.getLong(cursor.getColumnIndex(DataBaseHelper.UNPAID_BREAK_DURATION)),
                        cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.BONUS)),
                        cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.EXPENSES)),
                        cursor.getString(cursor.getColumnIndex(DataBaseHelper.COMMENTS)),
                       cursor.getInt(cursor.getColumnIndex(DataBaseHelper.IS_COMMISSION)) != 0,
                       cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.SALES_MADE_AMOUNT)),
                               cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.TARGET_AMOUNT)));

               shifts.add(shift);
               cursor.moveToNext();

            }


        }
        cursor.close();
        return shifts;
    }



    public void updateShiftInfo(Shift shift)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DataBaseHelper.DATE, shift.getDate());
        cv.put(DataBaseHelper.START_TIME, shift.getStartTime());
        cv.put(DataBaseHelper.END_TIME, shift.getEndTime());
        cv.put(DataBaseHelper.HOURLY_RATE, shift.getHourlyRate());
        cv.put(DataBaseHelper.PAID_BREAK_DURATION, shift.getPaidBreakMin());
        cv.put(DataBaseHelper.UNPAID_BREAK_DURATION, shift.getUnpaidBreakMin());
        cv.put(DataBaseHelper.BONUS, shift.getBonus());
        cv.put(DataBaseHelper.EXPENSES, shift.getExpenses());
        cv.put(DataBaseHelper.COMMENTS, shift.getComments());
        cv.put(DataBaseHelper.IS_COMMISSION, shift.isCommission());
        cv.put(DataBaseHelper.SALES_MADE_AMOUNT, shift.getSalesMade());
        cv.put(DataBaseHelper.TARGET_AMOUNT, shift.getTarget());
        db.update(DataBaseHelper.TABLE_NAME, cv, "_id=?", new String[]{String.valueOf(shift.getId())});
    }

    public void removeShift(int _id)
    {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DataBaseHelper.TABLE_NAME, "_id=?", new String[] {String.valueOf(_id)});
    }


    public ArrayList<Shift> retrieveShiftInfo(long shiftDate)
    {


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Shift shift;
        ArrayList<Shift> shifts = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_NAME + " WHERE date = ?", new String[] {String.valueOf(shiftDate)});

        if (cursor.moveToFirst())
        {
            while(!cursor.isAfterLast())
            {
                shift = new Shift(
                        cursor.getInt(cursor.getColumnIndex(DataBaseHelper.ID)),
                        cursor.getLong(cursor.getColumnIndex(DataBaseHelper.DATE)),
                        cursor.getLong(cursor.getColumnIndex(DataBaseHelper.START_TIME)),
                        cursor.getLong(cursor.getColumnIndex(DataBaseHelper.END_TIME)),
                        cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.HOURLY_RATE)),
                        cursor.getLong(cursor.getColumnIndex(DataBaseHelper.PAID_BREAK_DURATION)),
                        cursor.getLong(cursor.getColumnIndex(DataBaseHelper.UNPAID_BREAK_DURATION)),
                        cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.BONUS)),
                        cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.EXPENSES)),
                        cursor.getString(cursor.getColumnIndex(DataBaseHelper.COMMENTS)),
                        cursor.getInt(cursor.getColumnIndex(DataBaseHelper.IS_COMMISSION)) != 0,
                        cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.SALES_MADE_AMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(DataBaseHelper.TARGET_AMOUNT))

                        );
                shifts.add(shift);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return shifts;

    }

}
