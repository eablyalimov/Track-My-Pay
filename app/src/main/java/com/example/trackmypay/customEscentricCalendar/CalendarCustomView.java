package com.example.trackmypay.customEscentricCalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import com.example.trackmypay.R;
import com.example.trackmypay.SharedPrefs;
import com.example.trackmypay.ShiftManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarCustomView extends LinearLayout {

    private static final String TAG = CalendarCustomView.class.getSimpleName();
    private ImageView previousButton, nextButton;
    private TextView currentDate;
    public GridViewScrollable calendarGridView;
    private GridView daysOfWeekLabels;
    private static final int MAX_CALENDAR_COLUMN = 35;
    private int month, year;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar cal;
    private Context context;
    private GridAdapter mAdapter;
    private DaysOfWeekAdapter dayLabelAdapter;
    List<String> dayLabels;
    ShiftManager shiftManager;
    int daysToSub;

    SharedPreferences sharedPreferences;


    public CalendarCustomView(Context context) {
        super(context);
    }


    public CalendarCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

    }


    public CalendarCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCalendar(Calendar cal)
    {
        this.cal = cal;
    }


    public void initializeUILayout(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        previousButton = (ImageView)view.findViewById(R.id.previous_month);
        nextButton = (ImageView)view.findViewById(R.id.next_month);
        currentDate = (TextView)view.findViewById(R.id.display_current_date);
        calendarGridView = (GridViewScrollable)view.findViewById(R.id.calendar_grid);
        daysOfWeekLabels = view.findViewById(R.id.days_of_week_grid);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        int daysToSub = Integer.parseInt(sharedPreferences.getString(SharedPrefs.FIRST_DAY_OF_WEEK, "1"));

        dayLabelAdapter = new DaysOfWeekAdapter(context, setFirstWeekDayLabel(daysToSub));
    }


    public void setPreviousButtonClickEvent(){
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, -1);
                setUpCalendarAdapter(R.anim.calendar_animation_left);
            }
        });
    }







    public void setNextButtonClickEvent(){
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, 1);
                setUpCalendarAdapter(R.anim.calendar_animation_right);
            }
        });
    }

    public List<String> setFirstWeekDayLabel(int dayOfWeek)
    {
        List<String> daysOfWeek = new ArrayList<>();

        if(dayOfWeek == 1)
        {
            daysOfWeek.add("SUN");
            daysOfWeek.add("MON");
            daysOfWeek.add("TUE");
            daysOfWeek.add("WED");
            daysOfWeek.add("THU");
            daysOfWeek.add("FRI");
            daysOfWeek.add("SAT");
        }

        else
        {
            daysOfWeek.add("MON");
            daysOfWeek.add("TUE");
            daysOfWeek.add("WED");
            daysOfWeek.add("THU");
            daysOfWeek.add("FRI");
            daysOfWeek.add("SAT");
            daysOfWeek.add("SUN");
        }

        return daysOfWeek;

    }





    public void setUpCalendarAdapter(int animResource){

        shiftManager = new ShiftManager((FragmentActivity) context);

        Calendar firstDayOfMonth = Calendar.getInstance();
        Calendar lastDayOfMonth = Calendar.getInstance();

        firstDayOfMonth.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 0);
        lastDayOfMonth.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.getMaximum(Calendar.DAY_OF_MONTH));

        List<Date> dayValueInCells = new ArrayList<>();


        List<Calendar> mEvents = shiftManager.retrieveShifts(firstDayOfMonth.getTimeInMillis(), lastDayOfMonth.getTimeInMillis());
        Calendar mCal = (Calendar)cal.clone();

        mCal.set(Calendar.DAY_OF_MONTH, 1);

        daysToSub = Integer.parseInt(sharedPreferences.getString(SharedPrefs.FIRST_DAY_OF_WEEK, "1"));


        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - daysToSub;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);

        while(dayValueInCells.size() < MAX_CALENDAR_COLUMN){
            dayValueInCells.add(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);

        mAdapter = new GridAdapter(context, dayValueInCells, cal, mEvents);

        Animation anim = AnimationUtils.loadAnimation(getContext(), animResource);

        // By default all grid items will animate together and will look like the gridview is
        // animating as a whole. So, experiment with incremental delays as below to get a
        // wave effect.

        calendarGridView.setAnimation(anim);
        anim.start();
        calendarGridView.setAdapter(mAdapter);
        daysOfWeekLabels.setAdapter(dayLabelAdapter);

    }

}
