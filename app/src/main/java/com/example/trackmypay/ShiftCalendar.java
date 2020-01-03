package com.example.trackmypay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.util.Calendar;



public class ShiftCalendar extends Fragment {


    private CalendarView calendarView; // using a third party dependency from this repo: https://github.com/Applandeo/Material-Calendar-View


    ShiftManager shiftManager;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View vw = inflater.inflate(R.layout.shift_calendar, container, false);
        shiftManager = new ShiftManager(this.getActivity());
        calendarView = vw.findViewById(R.id.shifts_calendar);
        calendarView.showCurrentMonthPage();

        getShiftsForMonth();
        calendarView.setOnDayClickListener(new OnDayClickListener() {

            @Override
            public void onDayClick(EventDay eventDay) {

                Shift shiftToDisplay = shiftManager.retrieveShiftInfo(eventDay.getCalendar().getTimeInMillis());

                if (shiftToDisplay != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    ShiftDetailedInfo shiftDetailedInfo = new ShiftDetailedInfo();
                    shiftDetailedInfo.passShift(shiftToDisplay);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_layout, shiftDetailedInfo).addToBackStack(null).commit();
                }

                else
                {
                    FragmentManager fragmentManager = getFragmentManager();
                    EmptyDayFragment emptyDayFragment = new EmptyDayFragment();
                    emptyDayFragment.emptyDayDateSet(eventDay.getCalendar().getTimeInMillis());
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_layout, emptyDayFragment).addToBackStack(null).commit();
                }

            }
        });

        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                getShiftsForMonth();
            }

        });

        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                getShiftsForMonth();
            }
        });

        return vw;

    }

    private void getShiftsForMonth()
    {
        Calendar firstDayOfMonth = Calendar.getInstance();
        Calendar lastDayOfMonth = Calendar.getInstance();
        firstDayOfMonth.set(calendarView.getCurrentPageDate().get(Calendar.YEAR), calendarView.getCurrentPageDate().get(Calendar.MONTH), 0);
        lastDayOfMonth.set(calendarView.getCurrentPageDate().get(Calendar.YEAR), calendarView.getCurrentPageDate().get(Calendar.MONTH), calendarView.getCurrentPageDate().getMaximum(Calendar.DAY_OF_MONTH));

//        int firstDay = calendarView.getCurrentPageDate().getMinimum(Calendar.DAY_OF_MONTH);
//        int lastDay = calendarView.getCurrentPageDate().getMaximum(Calendar.DAY_OF_MONTH);
//        int month = calendarView.getCurrentPageDate().get(Calendar.MONTH);
//        int year = calendarView.getCurrentPageDate().get(Calendar.YEAR);
//        Calendar firstDayOfMonth = Calendar.getInstance();
//        Calendar lastDayOfMonth = Calendar.getInstance();
//        firstDayOfMonth.set(year, month, firstDay);
//        lastDayOfMonth.set(year, month, lastDay);

        calendarView.setEvents(shiftManager.retrieveShifts(firstDayOfMonth.getTimeInMillis(), lastDayOfMonth.getTimeInMillis()));

    }
}
