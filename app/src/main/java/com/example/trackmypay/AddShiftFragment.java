package com.example.trackmypay;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddShiftFragment extends Fragment implements OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    View vw;
    private ShiftManager shiftManager;
    private int timeFieldSelector; // used to check which time field needs to be populated
    private EditText shiftDate;
    private EditText startTime;
    private EditText endTime;
    private EditText hourlyRate;
    private EditText paidBreakMin;
    private EditText unpaidBreakMin;
    private EditText bonus;
    private EditText expenses;
    private EditText comments;
    private TextView hourlyRateLabel;
    private TextView paidBreakLabel;
    private TextView unpaidBreakLabel;
    private TextView bonusLabel;
    private TextView expensesLabel;

    private Button addShift;

    private TextView commissionLabel;
    private Switch commissionSwitch;

    private TextView salesMadeLabel;
    private EditText salesMade;
    private TextView targetAmountLabel;
    private EditText targetAmount;
    private int decideButtonColor = 1;
    private Toast toastShiftAdded;
    private Toast toastFieldError;

    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        vw = inflater.inflate(R.layout.fragment_add_shift, container, false);

        TextView shiftDateLabel = vw.findViewById(R.id.shift_date_label);
        addShift = vw.findViewById(R.id.addShift);
        Button clearInput = vw.findViewById(R.id.clearInput);
        TextView startTimeLabel = vw.findViewById(R.id.start_time_label);
        TextView endTimeLabel = vw.findViewById(R.id.end_time_label);
        hourlyRateLabel = vw.findViewById(R.id.hourly_rate_label);
        paidBreakLabel = vw.findViewById(R.id.paid_break_duration_label);
        unpaidBreakLabel = vw.findViewById(R.id.unpaid_break_duration_label);
        bonusLabel = vw.findViewById(R.id.bonus_amount_label);
        expensesLabel = vw.findViewById(R.id.expenses_amount_label);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());


        shiftManager = new ShiftManager(this.getActivity());
        shiftDate = vw.findViewById(R.id.shift_date);
        startTime = vw.findViewById(R.id.start_time);
        endTime = vw.findViewById(R.id.end_time);
        hourlyRate = vw.findViewById(R.id.hourly_rate);
        paidBreakMin = vw.findViewById(R.id.paid_break_duration);
        unpaidBreakMin = vw.findViewById(R.id.unpaid_break_duration);
        bonus = vw.findViewById(R.id.bonus_amount);
        expenses = vw.findViewById(R.id.expenses_amount);
        comments = vw.findViewById(R.id.comments);
        commissionLabel = vw.findViewById(R.id.commission_label);
        commissionSwitch = vw.findViewById(R.id.is_commission);
        salesMadeLabel = vw.findViewById(R.id.sales_made_amount_label);
        salesMade = vw.findViewById(R.id.sales_made_amount);
        targetAmountLabel = vw.findViewById(R.id.target_amount_label);
        targetAmount = vw.findViewById(R.id.target_amount);
        setVisibleFields();
        setCursorPositionToEnd();

        toastShiftAdded = Toast.makeText(this.getActivity(), "Shift has been added", Toast.LENGTH_SHORT);
        toastFieldError = Toast.makeText(this.getActivity(), "", Toast.LENGTH_SHORT);




        if (getArguments() != null && getArguments().getBoolean("called_from_empty_date"))
        {
            shiftDate.setHint("");
            shiftDate.setText(ShiftValuesConverter.DateStringConvert(getArguments().getLong("shift_date")));
            shiftDate.setEnabled(false);
        }
        else
        {
            shiftDate.setHint("ie: " + ShiftValuesConverter.DateStringConvert(Calendar.getInstance().getTimeInMillis()));
        }
        startTime.setHint("ie: " + ShiftValuesConverter.StartTimeStringConvert(Calendar.getInstance().getTimeInMillis()));
        endTime.setHint("ie: " + ShiftValuesConverter.EndTimeStringConvert(Calendar.getInstance().getTimeInMillis()));


        // ONCLICK LISTENERS
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


        commissionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCommissionSwitch(isChecked);
            }
        });



        addShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonClick();
            }
        });
        addShift.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onAddButtonLongPress();
                buttonColorSelector();

                return true;
            }
        });

        clearInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearButtonClick();
            }
        });




        return vw;

    }


    private void buttonColorSelector()
    {
        int[] arrayOfColors = {R.color.addButton, R.color.addButtonColor2, R.color.addButtonColor3};
        addShift.setBackgroundColor(getResources().getColor(arrayOfColors[decideButtonColor]));

        decideButtonColor++;

        if (decideButtonColor == 3)
        {
            decideButtonColor = 0;
        }
    }





    private void setCursorPositionToEnd()
    {

        unpaidBreakMin.setSelection(unpaidBreakMin.getText().length());
        bonus.setSelection(bonus.getText().length());
        expenses.setSelection(expenses.getText().length());
    }


    private void setVisibleFields()
    {

        DecimalFormat df = new DecimalFormat("#.##");

        hourlyRate.setText(df.format(Double.valueOf(sharedPreferences.getString(SharedPrefs.HOURLY_RATE, "14.00"))));


        if (!sharedPreferences.getBoolean(SharedPrefs.HOURLY_RATE_SWITCH, true))
        {
            hourlyRate.setVisibility(View.GONE);
            hourlyRateLabel.setVisibility(View.GONE);
        }

        if (!sharedPreferences.getBoolean(SharedPrefs.PAID_BREAK_SWITCH, true))
        {
            paidBreakMin.setText("0");
            paidBreakMin.setVisibility(View.GONE);
            paidBreakLabel.setVisibility(View.GONE);
        }

        else
        {
            paidBreakMin.setText(sharedPreferences.getString(SharedPrefs.PAID_BREAK_DURATION, "15"));
        }

        if (!sharedPreferences.getBoolean(SharedPrefs.UNPAID_BREAK_SWITCH, true))
        {
            unpaidBreakMin.setText("0");
            unpaidBreakMin.setVisibility(View.GONE);
            unpaidBreakLabel.setVisibility(View.GONE);
        }

        else
        {
            unpaidBreakMin.setText(sharedPreferences.getString(SharedPrefs.UNPAID_BREAK_DURATION, "30"));
        }

        if (!sharedPreferences.getBoolean(SharedPrefs.BONUS_SWITCH, true))
        {
            bonus.setText("0");
            bonus.setVisibility(View.GONE);
            bonusLabel.setVisibility(View.GONE);
        }

        else
        {
            bonus.setText(df.format(0));
        }

        if (!sharedPreferences.getBoolean(SharedPrefs.EXPENSES_SWITCH, true))
        {
            expenses.setText("0");
            expenses.setVisibility(View.GONE);
            expensesLabel.setVisibility(View.GONE);
        }

        else
        {
            expenses.setText(df.format(0));
        }

        if(!sharedPreferences.getBoolean(SharedPrefs.COMMISSION, true))
        {
            commissionLabel.setVisibility(View.GONE);
            commissionSwitch.setVisibility(View.GONE);
        }



    }


    private void showDatePickerDialog() {

        DatePickerDialog datePickerDialog;
        Calendar cal = Calendar.getInstance();

        if (shiftDate.getHint().toString().startsWith("ie:"))
        {
            cal.setTimeInMillis(ShiftValuesConverter.DateStringConvert(shiftDate.getHint().toString().subSequence(4, shiftDate.getHint().toString().length()).toString()));
        }

        else
        {
            cal.setTimeInMillis(ShiftValuesConverter.DateStringConvert(shiftDate.getHint().toString()));
        }


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

            if (startTime.getHint().toString().startsWith("ie:")) {
                cal.setTimeInMillis(ShiftValuesConverter.StartTimeStringConvert(startTime.getHint().toString().subSequence(4, startTime.getHint().toString().length()).toString()));
            }

            else
            {
                cal.setTimeInMillis(ShiftValuesConverter.EndTimeStringConvert(startTime.getHint().toString()));
            }

        }

        else if(timeFieldSelector == 2)
        {
            if (endTime.getHint().toString().startsWith("ie:")) {
                cal.setTimeInMillis(ShiftValuesConverter.EndTimeStringConvert(endTime.getHint().toString().subSequence(4, endTime.getHint().toString().length()).toString()));
            }
            else
            {
                cal.setTimeInMillis(ShiftValuesConverter.EndTimeStringConvert(endTime.getHint().toString()));
            }

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
        shiftDate.setHint("ie: " + currentDate);

    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        SimpleDateFormat format = new SimpleDateFormat("hh:mma");
        String timeHolder = format.format(c.getTime());

        if (timeFieldSelector == 1) {

            startTime.setText(timeHolder);
            startTime.setHint("ie: " + timeHolder);
        }
        else if (timeFieldSelector == 2)
        {
            endTime.setText(timeHolder);
            endTime.setHint("ie: " + timeHolder);
        }


    }

    private void ShiftFieldsValidator() throws ShiftFieldsException {


        if (!commissionSwitch.isChecked())
        {
            salesMade.setText("0");
            targetAmount.setText("0");
        }


        if (shiftDate.getText().toString().length() == 0)
        {
            throw new ShiftFieldsException("Please Enter The Date");
        }

        else if (startTime.getText().toString().length() == 0)
        {
            throw new ShiftFieldsException("Please Enter the Start Time");
        }

        else if (endTime.getText().toString().length() == 0)
        {
            throw new ShiftFieldsException("Please Enter the End Time");
        }

        else if (ShiftValuesConverter.StartTimeStringConvert(startTime.getText().toString()) > ShiftValuesConverter.EndTimeStringConvert(endTime.getText().toString()))
        {
            throw new ShiftFieldsException("Start Time cannot be earlier than End Time");
        }

        else if  (hourlyRate.getText().toString().length() == 0)
        {
            throw new ShiftFieldsException("Please Enter Hourly Rate");
        }

    }


    public void onCommissionSwitch(boolean isChecked)
    {

        if (!isChecked)
        {
            salesMadeLabel.setVisibility(View.GONE);
            salesMade.setVisibility(View.GONE);
            targetAmountLabel.setVisibility(View.GONE);
            targetAmount.setVisibility(View.GONE);
        }

        else if(isChecked)
        {
            salesMadeLabel.setVisibility(View.VISIBLE);
            salesMade.setVisibility(View.VISIBLE);
            targetAmountLabel.setVisibility(View.VISIBLE);
            targetAmount.setVisibility(View.VISIBLE);

        }



    }

    public void onAddButtonLongPress()
    {

        try {
            ShiftFieldsValidator();
            long tempPaidBreakMin;
            long tempUnpaidBreakMin;
            double tempBonus;
            double tempExpenses;
            double tempSalesMade;
            double tempTarget;
            tempSalesMade = (salesMade.getText().toString().length() == 0) ? 0 : Double.valueOf(salesMade.getText().toString());
            tempTarget = (targetAmount.getText().toString().length() == 0) ? 0 : Double.valueOf(targetAmount.getText().toString());

            tempPaidBreakMin = (paidBreakMin.getText().toString().length() == 0) ? 0 : (long) (Integer.valueOf(paidBreakMin.getText().toString()) * 60000);
            tempUnpaidBreakMin = (unpaidBreakMin.getText().toString().length() == 0) ? 0 : (long) (Integer.valueOf(unpaidBreakMin.getText().toString()) * 60000);
            tempBonus = (bonus.getText().toString().length() == 0) ? 0 : (Double.valueOf(bonus.getText().toString()));
            tempExpenses = (expenses.getText().toString().length() == 0) ? 0 : (Double.valueOf(expenses.getText().toString()));

            Shift shiftToAdd = new Shift(0,
                    ShiftValuesConverter.DateStringConvert(shiftDate.getText().toString()),
                    ShiftValuesConverter.StartTimeStringConvert(startTime.getText().toString()),
                    ShiftValuesConverter.EndTimeStringConvert(endTime.getText().toString()),
                    Double.valueOf(hourlyRate.getText().toString()),
                    tempPaidBreakMin,
                    tempUnpaidBreakMin,
                    tempBonus,
                    tempExpenses,
                    comments.getText().toString(),
                    commissionSwitch.isChecked(),
                    tempSalesMade,
                    tempTarget);


            shiftManager.addShift(shiftToAdd);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(shiftToAdd.getDate());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            shiftDate.setText(ShiftValuesConverter.DateStringConvert(cal.getTimeInMillis()));
            shiftDate.setEnabled(true);
            shiftDate.setHint(ShiftValuesConverter.DateStringConvert(cal.getTimeInMillis()));

            if (toastShiftAdded.getView() != null && !toastShiftAdded.getView().isShown())
            {

                toastShiftAdded.show();
            }


        } catch (ShiftFieldsException e) {
            Toast toast = Toast.makeText(this.getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    private void onAddButtonClick()
    {

        try {
            ShiftFieldsValidator();
            long tempPaidBreakMin;
            long tempUnpaidBreakMin;
            double tempBonus;
            double tempExpenses;
            double tempSalesMade;
            double tempTarget;
            tempSalesMade = (salesMade.getText().toString().length() == 0) ? 0 : Double.valueOf(salesMade.getText().toString());
            tempTarget = (targetAmount.getText().toString().length() == 0) ? 0 : Double.valueOf(targetAmount.getText().toString());

            tempPaidBreakMin = (paidBreakMin.getText().toString().length() == 0) ? 0 : (long) (Integer.valueOf(paidBreakMin.getText().toString()) * 60000);
            tempUnpaidBreakMin = (unpaidBreakMin.getText().toString().length() == 0) ? 0 : (long) (Integer.valueOf(unpaidBreakMin.getText().toString()) * 60000);
            tempBonus = (bonus.getText().toString().length() == 0) ? 0 : (Double.valueOf(bonus.getText().toString()));
            tempExpenses = (expenses.getText().toString().length() == 0) ? 0 : (Double.valueOf(expenses.getText().toString()));

            Shift shiftToAdd = new Shift(0,
                    ShiftValuesConverter.DateStringConvert(shiftDate.getText().toString()),
                    ShiftValuesConverter.StartTimeStringConvert(startTime.getText().toString()),
                    ShiftValuesConverter.EndTimeStringConvert(endTime.getText().toString()),
                    Double.valueOf(hourlyRate.getText().toString()),
                    tempPaidBreakMin,
                    tempUnpaidBreakMin,
                    tempBonus,
                    tempExpenses,
                    comments.getText().toString(),
                    commissionSwitch.isChecked(),
                    tempSalesMade,
                    tempTarget);

            Log.d("Date saved:", String.valueOf(ShiftValuesConverter.DateStringConvert(shiftDate.getText().toString())));

             shiftManager.addShift(shiftToAdd);

            Bundle bundle = new Bundle();
            Calendar cal = Calendar.getInstance();
            FragmentManager fragmentManager = getFragmentManager();
            ShiftCalendar shiftCalendar = new ShiftCalendar();
            if (getArguments() != null && getArguments().getBoolean("called_from_empty_date")) {
                cal.setTimeInMillis(getArguments().getLong("shift_date"));
                bundle.putSerializable("month_to_display", cal);

            }
            else
            {
                cal.setTimeInMillis(ShiftValuesConverter.DateStringConvert(shiftDate.getText().toString()));
                bundle.putSerializable("month_to_display", cal);

            }
            shiftCalendar.setArguments(bundle);
            for(int i = 0; i < getFragmentManager().getBackStackEntryCount(); i++)
            {
                getFragmentManager().popBackStack();

            }
            fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.fragment_layout, shiftCalendar).commit();

        }

        catch (ShiftFieldsException e)
        {
            if (!toastFieldError.getView().isShown())
            {

                toastFieldError.setText(e.getMessage());
                toastFieldError.show();
            }

        }
    }

    private void onClearButtonClick()
    {

        ClearShiftValues clearShiftValues = new ClearShiftValues();

        Bundle bundle = new Bundle();
        if (getArguments() != null && getArguments().getBoolean("called_from_empty_date"))
        {
            bundle.putBoolean("called_from_empty_date", getArguments().getBoolean("called_from_empty_date"));
            bundle.putString("addOrUpdate", "add");
        }

        else
        {
            bundle.putString("addOrUpdate", "add");
        }
        clearShiftValues.setArguments(bundle);

        clearShiftValues.show(getFragmentManager(), "Clear Values");

    }

}
