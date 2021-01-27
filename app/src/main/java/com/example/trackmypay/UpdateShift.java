package com.example.trackmypay;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class UpdateShift extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {

    View vw;

    private ShiftManager shiftManager;

    private EditText updateShiftDate;
    private EditText updateStartTime;
    private EditText updateEndTime;
    private EditText updateHourlyRate;
    private EditText updatePaidBreakMin;
    private EditText updateUnpaidBreakMin;
    private EditText updateBonus;
    private EditText updateExpenses;
    private EditText updateComments;
    private TextView updateIsCommissionLabel;
    private Switch updateIsCommission;
    private TextView updateSalesMadeLabel;
    private EditText updateSalesMade;
    private EditText updateTarget;
    private TextView updateTargetLabel;
    private Shift shift;
    private Button updateShift;
    private Button clearInfo;
    private int timeFieldSelector;



    public void PopulateUpdateShiftValues(Shift shift)
    {
        updateShiftDate.setText(ShiftValuesConverter.DateStringConvert(shift.getDate()));
        updateStartTime.setText(ShiftValuesConverter.StartTimeStringConvert(shift.getStartTime()));
        updateStartTime.setHint("ie: " + ShiftValuesConverter.StartTimeStringConvert(shift.getStartTime()));
        updateEndTime.setText(ShiftValuesConverter.EndTimeStringConvert(shift.getEndTime()));
        updateEndTime.setHint("ie: " + ShiftValuesConverter.EndTimeStringConvert(shift.getEndTime()));
        updateComments.setText(shift.getComments());
        updateHourlyRate.setText(String.valueOf(shift.getHourlyRate()));
        updatePaidBreakMin.setText(String.valueOf(shift.getPaidBreakMin() / 60000));
        updateUnpaidBreakMin.setText(String.valueOf(shift.getUnpaidBreakMin() / 60000));
        updateBonus.setText(String.valueOf(shift.getBonus()));
        updateExpenses.setText(String.valueOf(shift.getExpenses()));
        updateIsCommission.setChecked(shift.isCommission());
        updateSalesMade.setText(String.valueOf(shift.getSalesMade()));
        updateTarget.setText(String.valueOf(shift.getTarget()));
        commissionFieldsViewChanger(shift.isCommission());

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        shift = (Shift)getArguments().getSerializable("shift_to_update");

        shiftManager = new ShiftManager(this.getActivity());
        vw = inflater.inflate(R.layout.update_shift, container, false);
        updateShiftDate = vw.findViewById(R.id.update_shift_date);
        updateShiftDate.setEnabled(false);
        updateStartTime = vw.findViewById(R.id.update_start_time);
        updateEndTime = vw.findViewById(R.id.update_end_time);
        updateComments = vw.findViewById(R.id.update_comments);
        updateShift = vw.findViewById(R.id.update_shift);
        updateHourlyRate = vw.findViewById(R.id.update_hourly_rate);
        updatePaidBreakMin = vw.findViewById(R.id.update_paid_break_duration);
        updateUnpaidBreakMin = vw.findViewById(R.id.update_unpaid_break_duration);
        updateBonus = vw.findViewById(R.id.update_bonus_amount);
        updateExpenses = vw.findViewById(R.id.update_expenses_amount);
        updateIsCommissionLabel = vw.findViewById(R.id.update_commission_label);
        updateIsCommission = vw.findViewById(R.id.update_is_commission);
        updateSalesMadeLabel = vw.findViewById(R.id.update_sales_made_amount_label);
        updateSalesMade = vw.findViewById(R.id.update_sales_made_amount);
        updateTargetLabel = vw.findViewById(R.id.update_target_amount_label);
        updateTarget = vw.findViewById(R.id.update_target_amount);
        clearInfo = vw.findViewById(R.id.update_clearInput);


        TextView updateShiftDateLabel = vw.findViewById(R.id.update_shift_date_label);
        TextView updateStartTimeLabel = vw.findViewById(R.id.update_start_time_label);
        TextView updateEndTimeLabel = vw.findViewById(R.id.update_end_time_label);

        PopulateUpdateShiftValues(shift);

        updateShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateButtonClick();
            }
        });

        updateShiftDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showDatePickerDialog();
            }
        });
        updateShiftDateLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showDatePickerDialog();
            }
        });

        clearInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("addOrUpdate", "update");
                ClearShiftValues clearShiftValues = new ClearShiftValues();
                clearShiftValues.setArguments(bundle);
                clearShiftValues.show(getFragmentManager(), "Clear Values");
            }
        });


        updateIsCommission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

               commissionFieldsViewChanger(isChecked);
            }
        });



        updateStartTimeLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFieldSelector = 1;
                showTimePickerDialog();
            }
        });

        updateStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFieldSelector = 1;
                showTimePickerDialog();
            }
        });

        updateEndTimeLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFieldSelector = 2;
                showTimePickerDialog();
            }
        });

        updateEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFieldSelector = 2;
                showTimePickerDialog();
            }
        });

        return vw;
    }




    private void showDatePickerDialog() {

        DatePickerDialog datePickerDialog;
        Calendar cal = Calendar.getInstance();

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

            if (updateStartTime.getHint().toString().startsWith("ie:")) {
                cal.setTimeInMillis(ShiftValuesConverter.StartTimeStringConvert(updateStartTime.getHint().toString().subSequence(4, updateStartTime.getHint().toString().length()).toString()));
            }

            else
            {
                cal.setTimeInMillis(ShiftValuesConverter.StartTimeStringConvert(updateStartTime.getText().toString()));
            }


        }

        else if(timeFieldSelector == 2)
        {
            if (updateEndTime.getHint().toString().startsWith("ie:")) {
                cal.setTimeInMillis(ShiftValuesConverter.StartTimeStringConvert(updateEndTime.getHint().toString().subSequence(4, updateEndTime.getHint().toString().length()).toString()));
            }

            else
            {
                cal.setTimeInMillis(ShiftValuesConverter.StartTimeStringConvert(updateEndTime.getText().toString()));
            }

        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getActivity(), this,
                cal.get(cal.HOUR_OF_DAY),
                cal.get(cal.MINUTE), DateFormat.is24HourFormat(this.getActivity())
        );
        timePickerDialog.show();

    }

    private void commissionFieldsViewChanger(boolean isCommission)
    {
        if (!isCommission)
        {
            updateSalesMadeLabel.setVisibility(View.GONE);
            updateSalesMade.setVisibility(View.GONE);
            updateTargetLabel.setVisibility(View.GONE);
            updateTarget.setVisibility(View.GONE);
        }

        else if(isCommission)
        {
            updateSalesMadeLabel.setVisibility(View.VISIBLE);
            updateSalesMade.setVisibility(View.VISIBLE);
            updateTargetLabel.setVisibility(View.VISIBLE);
            updateTarget.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String currentDate = format.format(c.getTime());
        updateShiftDate.setText(currentDate);

    }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        SimpleDateFormat format = new SimpleDateFormat("hh:mma");
        String timeHolder = format.format(c.getTime());

        if (timeFieldSelector == 1) {

            updateStartTime.setText(timeHolder);
        }
        else if (timeFieldSelector == 2)
        {
            updateEndTime.setText(timeHolder);
        }


    }

    private void ShiftFieldsValidator() throws ShiftFieldsException {

        if(!updateIsCommission.isChecked())
        {
            updateSalesMade.setText("0");
            updateTarget.setText("0");
        }

        if (updateShiftDate.getText().toString().length() == 0)
        {
            throw new ShiftFieldsException("Please Populate the Date");
        }

        else if (updateStartTime.getText().toString().length() == 0)
        {
            throw new ShiftFieldsException("Please Populate the Start Time");
        }

        else if (updateEndTime.getText().toString().length() == 0)
        {
            throw new ShiftFieldsException("Please Populate the End Time");
        }

        else if (ShiftValuesConverter.StartTimeStringConvert(updateStartTime.getText().toString()) > ShiftValuesConverter.EndTimeStringConvert(updateEndTime.getText().toString()))
        {
            throw new ShiftFieldsException("Start Time cannot be earlier than End Time");
        }

        else if  (updateHourlyRate.getText().toString().length() == 0)
        {
            throw new ShiftFieldsException("Please Enter Hourly Rate");
        }

    }


    private void onUpdateButtonClick()
    {


        try {
            ShiftFieldsValidator();

            long tempPaidBreakMin;
            long tempUnpaidBreakMin;
            double tempBonus;
            double tempExpenses;
            double tempSalesMade;
            double tempTarget;

            tempPaidBreakMin = (updatePaidBreakMin.getText().toString().length() == 0) ? 0 : (long) (Integer.valueOf(updatePaidBreakMin.getText().toString()) * 60000);
            tempUnpaidBreakMin = (updateUnpaidBreakMin.getText().toString().length() == 0) ? 0 : (long) (Integer.valueOf(updateUnpaidBreakMin.getText().toString()) * 60000);
            tempBonus = (updateBonus.getText().toString().length() == 0) ? 0 : (Double.valueOf(updateBonus.getText().toString()));
            tempExpenses = (updateBonus.getText().toString().length() == 0) ? 0 : (Double.valueOf(updateBonus.getText().toString()));
            tempSalesMade = (updateSalesMade.getText().toString().length() == 0) ? 0 : Double.valueOf(updateSalesMade.getText().toString());
            tempTarget = (updateTarget.getText().toString().length() == 0) ? 0 : Double.valueOf(updateTarget.getText().toString());


            shiftManager.updateShiftInfo(new Shift(shift.getId(),
                    ShiftValuesConverter.DateStringConvert(updateShiftDate.getText().toString()),
                    ShiftValuesConverter.StartTimeStringConvert(updateStartTime.getText().toString()),
                    ShiftValuesConverter.EndTimeStringConvert(updateEndTime.getText().toString()),
                    Double.valueOf(updateHourlyRate.getText().toString()),
                    tempPaidBreakMin,
                    tempUnpaidBreakMin,
                    tempBonus,
                    tempExpenses,
                    updateComments.getText().toString(),
                    updateIsCommission.isChecked(),
                    tempSalesMade,
                    tempTarget));


            FragmentManager fragmentManager = getFragmentManager();
            Bundle bundle = new Bundle();
            Calendar cal = Calendar.getInstance();
            ShiftCalendar shiftCalendar = new ShiftCalendar();

            cal.setTimeInMillis(ShiftValuesConverter.DateStringConvert(updateShiftDate.getText().toString()));
            bundle.putSerializable("month_to_display", cal);
            shiftCalendar.setArguments(bundle);



            for(int i = 0; i < getFragmentManager().getBackStackEntryCount(); i++)
            {
                getFragmentManager().popBackStack();
            }

            fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.fragment_layout, shiftCalendar).commit();






        }

        catch (ShiftFieldsException e)
        {
            Toast toast = Toast.makeText(this.getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}
