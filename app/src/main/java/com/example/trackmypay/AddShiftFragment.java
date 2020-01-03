package com.example.trackmypay;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddShiftFragment extends Fragment implements OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    View vw;
    private EditText shiftDate;
    private boolean isCalledFromEmptyDay;
    private long dateIfCalledFromEmptyDay;
    private EditText startTime;
    private EditText endTime;
    private EditText comments;
    private ShiftManager shiftManager;
    private int timeFieldSelector; // used to check which time field needs to be populated


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        vw = inflater.inflate(R.layout.fragment_add_shift, container, false);
        shiftManager = new ShiftManager(this.getActivity());
        shiftDate = vw.findViewById(R.id.shift_date);
        TextView shiftDateLabel = vw.findViewById(R.id.shift_date_label);
        Button addShift = vw.findViewById(R.id.addShift);
        TextView startTimeLabel = vw.findViewById(R.id.start_time_label);
        startTime = vw.findViewById(R.id.start_time);
        TextView endTimeLabel = vw.findViewById(R.id.end_time_label);
        comments = vw.findViewById(R.id.comments);


        endTime = vw.findViewById(R.id.end_time);
        if (isCalledFromEmptyDay)
        {
            shiftDate.setHint("");
            shiftDate.setText(ShiftValuesConverter.DateStringConvert(dateIfCalledFromEmptyDay));
            shiftDate.setEnabled(false);
        }
        else
        {
            shiftDate.setHint(ShiftValuesConverter.DateStringConvert(Calendar.getInstance().getTimeInMillis()));
        }
        startTime.setHint(ShiftValuesConverter.StartTimeStringConvert(Calendar.getInstance().getTimeInMillis()));
        endTime.setHint(ShiftValuesConverter.EndTimeStringConvert(Calendar.getInstance().getTimeInMillis()));




        shiftDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               showDatePickerDialog();
           }
       });
        shiftDateLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    showDatePickerDialog();
            }
        });




        startTimeLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFieldSelector = 1;
                showTimePickerDialog();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFieldSelector = 1;
                showTimePickerDialog();
            }
        });

        endTimeLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFieldSelector = 2;
                showTimePickerDialog();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFieldSelector = 2;
                showTimePickerDialog();
            }
        });



        addShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonClick();
            }
        });
        return vw;

    }

    public void setDateIfCalledFromEmptyDay(long date)
    {
        dateIfCalledFromEmptyDay = date;
        isCalledFromEmptyDay = true;
    }


    private void showDatePickerDialog() {

        DatePickerDialog datePickerDialog;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ShiftValuesConverter.DateStringConvert(shiftDate.getHint().toString()));
            datePickerDialog = new DatePickerDialog(this.getActivity(), this,
                    cal.get(cal.YEAR),
                    cal.get(cal.MONTH),
                    cal.get(cal.DAY_OF_MONTH)

            );

            datePickerDialog.show();

        }


    private void showTimePickerDialog()
    {

        Calendar cal = Calendar.getInstance();

        if (timeFieldSelector == 1) {
            cal.setTimeInMillis(ShiftValuesConverter.StartTimeStringConvert(startTime.getHint().toString()));

        }

        else if(timeFieldSelector == 2)
        {
            cal.setTimeInMillis(ShiftValuesConverter.EndTimeStringConvert(endTime.getHint().toString()));

        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getActivity(), this,
                cal.get(cal.HOUR_OF_DAY),
                cal.get(cal.MINUTE), DateFormat.is24HourFormat(this.getActivity())
                );
        timePickerDialog.show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String currentDate = format.format(c.getTime());
        shiftDate.setText(currentDate);
        shiftDate.setHint(currentDate);

    }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        SimpleDateFormat format = new SimpleDateFormat("hh:mma");
        String timeHolder = format.format(c.getTime());

        if (timeFieldSelector == 1) {

            startTime.setText(timeHolder);
            startTime.setHint(timeHolder);
        }
        else if (timeFieldSelector == 2)
        {
            endTime.setText(timeHolder);
            endTime.setHint(timeHolder);
        }


    }

    private void ShiftFieldsValidator() throws ShiftFieldsException {
        if (shiftDate.getText().toString().length() == 0)
        {
            throw new ShiftFieldsException("Please Populate the Date");
        }

        else if (startTime.getText().toString().length() == 0)
        {
            throw new ShiftFieldsException("Please Populate the Start Time");
        }

        else if (endTime.getText().toString().length() == 0)
        {
            throw new ShiftFieldsException("Please Populate the End Time");
        }

        else if (ShiftValuesConverter.StartTimeStringConvert(startTime.getText().toString()) > ShiftValuesConverter.EndTimeStringConvert(endTime.getText().toString()))
        {
            throw new ShiftFieldsException("Start Time cannot be earlier than End Time");
        }
    }

    private void onAddButtonClick()
    {

        try {
            ShiftFieldsValidator();
            Shift shiftToAdd = new Shift(ShiftValuesConverter.DateStringConvert(shiftDate.getText().toString()), ShiftValuesConverter.StartTimeStringConvert(startTime.getText().toString()), ShiftValuesConverter.EndTimeStringConvert(endTime.getText().toString()), comments.getText().toString());

            shiftManager.addShift(shiftToAdd);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_layout, new ShiftCalendar()).commit();
        }

        catch (ShiftFieldsException e)
        {
            Toast toast = Toast.makeText(this.getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
