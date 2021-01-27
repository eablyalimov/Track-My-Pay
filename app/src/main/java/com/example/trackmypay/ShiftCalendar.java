package com.example.trackmypay;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.trackmypay.customEscentricCalendar.CalendarCustomView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class ShiftCalendar extends Fragment {


    private CalendarCustomView calendarCustomView;
    private WorkedSummary workedSummary;
    //private CalendarView calendarView;// using a third party dependency from this repo: https://github.com/Applandeo/Material-Calendar-View

    ShiftManager shiftManager;
    Button goToCurrentMonth;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View vw = inflater.inflate(R.layout.shift_calendar, container, false);
        goToCurrentMonth = vw.findViewById(R.id.go_to_current_month);

        calendarCustomView = vw.findViewById(R.id.custom_calendar);

        calendarCustomView.initializeUILayout();



        shiftManager = new ShiftManager(this.getActivity());



        if (getArguments() != null && getArguments().getSerializable("month_to_display") != null)
        {

            Log.d("Correct", "statement");
            Calendar cal = (Calendar)getArguments().getSerializable("month_to_display");
            calendarCustomView.setCalendar(cal);
            calendarCustomView.setUpCalendarAdapter(R.anim.calendar_animation_right);
        }

        else
        {
            Calendar cal = Calendar.getInstance();
            calendarCustomView.setCalendar(cal);

            calendarCustomView.setUpCalendarAdapter(R.anim.calendar_animation_right);
        }

        calendarCustomView.setNextButtonClickEvent();
        calendarCustomView.setPreviousButtonClickEvent();

        goToCurrentMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                calendarCustomView.setCalendar(cal);
                calendarCustomView.setUpCalendarAdapter(R.anim.calendar_animation_left);
            }
        });






        calendarCustomView.calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Date date = (Date) parent.getItemAtPosition(position);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                ArrayList<Shift> shiftsToDisplay = shiftManager.retrieveShiftInfo(cal.getTimeInMillis());

                Log.d("Just a Date:", String.valueOf(cal.getTimeInMillis()));
//
                if (!shiftsToDisplay.isEmpty()) {

                    Collections.sort(shiftsToDisplay, new Comparator<Shift>() {
                        @Override
                        public int compare(Shift o1, Shift o2) {
                            return Long.compare(o1.getStartTime(), o2.getStartTime());
                        }
                    });
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ShiftDetailedInfo shiftDetailedInfo = new ShiftDetailedInfo();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("shift_to_display", shiftsToDisplay);
                    shiftDetailedInfo.setArguments(bundle);

                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right).replace(R.id.fragment_layout, shiftDetailedInfo).addToBackStack(null).commit();

                } else {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    AddShiftFragment addShiftFragment = new AddShiftFragment();
                    Bundle bundle = new Bundle();

                    bundle.putLong("shift_date", ((Date) parent.getItemAtPosition(position)).getTime());
                    bundle.putBoolean("called_from_empty_date", true);

                    addShiftFragment.setArguments(bundle);


                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right).replace(R.id.fragment_layout, addShiftFragment).addToBackStack(null).commit();


                }

            }


        });



        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_summary, new WorkedSummary(), "worked_summary").commit();

        return vw;

    }

}