package com.example.trackmypay;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.trackmypay.saveToExcel.SaveToExcelReport;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class WorkedSummary extends Fragment implements DatePickerDialog.OnDateSetListener {

    ShiftManager shiftManager;

    EditText shiftSearchFromDate;
    EditText shiftSearchToDate;
    TextView hoursWorkedTotal;
    TextView amountEarnedTotal;
    TextView commissionEarned;
    GridLayout shiftsSummary;
    Button searchShifts;
    Button saveReport;
    int dateFieldSelector;
    SharedPreferences sharedPreferences;
    List<Shift> shifts;



    View vw;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vw = inflater.inflate(R.layout.worked_summary, container, false);
        shiftManager = new ShiftManager(this.getActivity());
        shiftSearchFromDate = vw.findViewById(R.id.shift_search_from_date);
        shiftSearchToDate = vw.findViewById(R.id.shift_search_to_date);
        hoursWorkedTotal = vw.findViewById(R.id.hours_worked_total);
        amountEarnedTotal = vw.findViewById(R.id.amount_earned_total);
        shiftsSummary = vw.findViewById(R.id.shifts_summary);
        searchShifts = vw.findViewById(R.id.search_shifts_button);
        commissionEarned = vw.findViewById(R.id.commission_earned_total);
        commissionEarned.setVisibility(View.GONE);
        saveReport = vw.findViewById(R.id.save_to_excel_btn);



        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        Calendar fromDate = Calendar.getInstance();
        fromDate.add(Calendar.DAY_OF_MONTH, -14);
        Calendar toDate = Calendar.getInstance();

        shiftSearchFromDate.setText(ShiftValuesConverter.DateStringConvert(fromDate.getTimeInMillis()));
        shiftSearchToDate.setText(ShiftValuesConverter.DateStringConvert(toDate.getTimeInMillis()));


        shiftSearchFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFieldSelector = 1;
                showDatePickerDialog();
            }
        });

        shiftSearchToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFieldSelector = 2;
                showDatePickerDialog();
            }
        });

        searchShifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShiftSummaryClick();
            }
        });

        saveReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveReportButtonClick();
            }
        });



        return vw;



    }


    private void showDatePickerDialog() {

        DatePickerDialog datePickerDialog;
        Calendar cal = Calendar.getInstance();

        if (dateFieldSelector == 1) {
            cal.setTimeInMillis(ShiftValuesConverter.DateStringConvert(shiftSearchFromDate.getText().toString()));
        } else if (dateFieldSelector == 2) {
            cal.setTimeInMillis(ShiftValuesConverter.DateStringConvert(shiftSearchToDate.getText().toString()));
        }


        datePickerDialog = new DatePickerDialog(this.getActivity(), this,
                cal.get(cal.YEAR),
                cal.get(cal.MONTH),
                cal.get(cal.DAY_OF_MONTH)

        );

        datePickerDialog.show();


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String currentDate = format.format(c.getTime());

        if (dateFieldSelector == 1) {
            shiftSearchFromDate.setText(currentDate);
        } else if (dateFieldSelector == 2) {
            shiftSearchToDate.setText(currentDate);
        }

    }

    private void onShiftSummaryClick()
    {

        DecimalFormat df = new DecimalFormat("#.##");


        long hoursWorkedTotalNumber = 0;
        double amountEarnedTotalNumber = 0;
        double commissionEarnedTotalNumber = 0;



        shifts = shiftManager.retrieveShiftsBetweenDates(ShiftValuesConverter.DateStringConvert(shiftSearchFromDate.getText().toString()), ShiftValuesConverter.DateStringConvert(shiftSearchToDate.getText().toString()));


        for (Shift shift:
             shifts) {

           hoursWorkedTotalNumber += shift.calculateTimeWorked();
           amountEarnedTotalNumber += shift.calculateGrossPay();
           commissionEarnedTotalNumber += shift.calculateCommission(sharedPreferences.getBoolean(SharedPrefs.COMMISSION_DEFICIT, true));

        }

        hoursWorkedTotal.setText(ShiftValuesConverter.timeWorkedConvert(hoursWorkedTotalNumber) + " worked.");
        amountEarnedTotal.setText(getString(R.string.worked_summary_earned, String.valueOf(df.format(amountEarnedTotalNumber)), sharedPreferences.getString(SharedPrefs.CURRENCY, "$")));
        if (commissionEarnedTotalNumber != 0) {
            commissionEarned.setText(getString(R.string.commission_earned, String.valueOf(df.format(commissionEarnedTotalNumber))));
            commissionEarned.setVisibility(View.VISIBLE);
        }

        else if(commissionEarnedTotalNumber == 0)
        {
            commissionEarned.setVisibility(View.GONE);
        }

        shiftsSummary.setVisibility(View.VISIBLE);
        shiftsSummary.setAnimation(AnimationUtils.loadAnimation(this.getActivity(), R.anim.slide_up));






    }

    private void onSaveReportButtonClick()
    {

        if(ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            SaveToExcelReport saveToExcelReport = new SaveToExcelReport(shifts, getActivity());
            saveToExcelReport.start();
        }

        else
        {
            requestStoragePermission();
        }


    }

    private void requestStoragePermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {

            new AlertDialog.Builder(this.getActivity()).setTitle("Permission is needed")
                    .setMessage("Storage Permission is required to save Excel Reports")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();

        }

        else
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.d("KEK", "I AM HERE");
                SaveToExcelReport saveToExcelReport = new SaveToExcelReport(shifts, getActivity());
                saveToExcelReport.start();
            }
        }
    }
}
