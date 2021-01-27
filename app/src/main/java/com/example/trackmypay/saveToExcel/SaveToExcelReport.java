package com.example.trackmypay.saveToExcel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.example.trackmypay.SharedPrefs;
import com.example.trackmypay.Shift;
import com.example.trackmypay.ShiftValuesConverter;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class SaveToExcelReport extends Thread{

    static SharedPreferences sharedPreferences;
    List<Shift> shifts;
    Context context;

     public SaveToExcelReport(List<Shift> shifts, Context context)
    {
        this.shifts = shifts;
        this.context = context;
    }


    public void run()
    {

        makeExcel(shifts, context);
        Log.d("THREAD:", Thread.currentThread().getName());
    }



    public void makeExcel(List<Shift> shifts, Context context)
    {

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        saveToFile(shifts, context);

    }


    private Workbook createExcel(List<Shift> shifts)
    {



        DecimalFormat dfForGrossPay = new DecimalFormat("#.##");

        long timeWorkedTotal = 0;
        double moneyEarnedTotal = 0;
        double salesMadeTotal = 0;
        double commissionEarnedTotal = 0;

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Shifts Report");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Shift Date");
        headerRow.createCell(1).setCellValue("Start Time");
        headerRow.createCell(2).setCellValue("End Time");
        headerRow.createCell(3).setCellValue("Hourly Rate");
        headerRow.createCell(4).setCellValue("Paid Break (Minutes)");
        headerRow.createCell(5).setCellValue("Unpaid Break (Minutes)");
        headerRow.createCell(6).setCellValue("Hours Worked");
        headerRow.createCell(7).setCellValue("$ earned");
        headerRow.createCell(8).setCellValue("Sales Made");
        headerRow.createCell(9).setCellValue("Target Commission Amount");
        headerRow.createCell(10).setCellValue("Commission Earned");

        for (int i = 0; i < shifts.size(); i++)
        {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(ShiftValuesConverter.DateStringConvert(shifts.get(i).getDate()));
            row.createCell(1).setCellValue(ShiftValuesConverter.StartTimeStringConvert(shifts.get(i).getStartTime()));
            row.createCell(2).setCellValue(ShiftValuesConverter.EndTimeStringConvert(shifts.get(i).getEndTime()));
            row.createCell(3).setCellValue(shifts.get(i).getHourlyRate());
            row.createCell(4).setCellValue(shifts.get(i).getPaidBreakMin() / 60000);
            row.createCell(5).setCellValue(shifts.get(i).getUnpaidBreakMin() / 60000);
            row.createCell(6).setCellValue(ShiftValuesConverter.timeWorkedConvert(shifts.get(i).calculateTimeWorked()));
            row.createCell(7).setCellValue(dfForGrossPay.format(shifts.get(i).calculateGrossPay()) + sharedPreferences.getString(SharedPrefs.CURRENCY, "$"));
            row.createCell(8).setCellValue(dfForGrossPay.format(shifts.get(i).getSalesMade()) + sharedPreferences.getString(SharedPrefs.CURRENCY, "$"));
            row.createCell(9).setCellValue(dfForGrossPay.format(shifts.get(i).getTarget()) + sharedPreferences.getString(SharedPrefs.CURRENCY, "$"));
            row.createCell(10).setCellValue(dfForGrossPay.format(shifts.get(i).calculateCommission(sharedPreferences.getBoolean(SharedPrefs.COMMISSION_DEFICIT, false))) + sharedPreferences.getString(SharedPrefs.CURRENCY, "$"));

            timeWorkedTotal += shifts.get(i).calculateTimeWorked();
            moneyEarnedTotal += shifts.get(i).calculateGrossPay();
            salesMadeTotal += shifts.get(i).getSalesMade();
            commissionEarnedTotal += shifts.get(i).calculateCommission(sharedPreferences.getBoolean(SharedPrefs.COMMISSION_DEFICIT, false));

        }

        Row footerRow = sheet.createRow(sheet.getLastRowNum());

        footerRow.createCell(6).setCellValue(ShiftValuesConverter.timeWorkedConvert(timeWorkedTotal));
        footerRow.createCell(7).setCellValue(moneyEarnedTotal  + sharedPreferences.getString(SharedPrefs.CURRENCY, "$"));
        footerRow.createCell(8).setCellValue(salesMadeTotal  + sharedPreferences.getString(SharedPrefs.CURRENCY, "$"));
        footerRow.createCell(9).setCellValue(commissionEarnedTotal +  sharedPreferences.getString(SharedPrefs.CURRENCY, "$"));

        return workbook;

    }

    private void saveToFile(List<Shift> shifts, final Context context)
    {
          try {
                File directory = new File(Environment.getExternalStorageDirectory() + "/ShiftReports/");
                directory.mkdirs();
                Log.d("Folder is created", "!!!!");
                final File spreadsheet = new File(directory, "shifts_report.XLSX");
                spreadsheet.createNewFile();
                Log.d("File is created", "!!!!");
                FileOutputStream fos = new FileOutputStream(spreadsheet);
                Workbook workbook = createExcel(shifts);
                workbook.write(fos);
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(context, "File is Created. Destination: " + spreadsheet.getPath(), Toast.LENGTH_LONG);
                        toast.setGravity(1, 0, 3);
                        toast.show();
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }


    }




}
