package com.example.trackmypay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ClearShiftValues extends AppCompatDialogFragment {

    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmation").setMessage("Clear Shift Values?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                onConfirmClearForm();



            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }

    public void onConfirmClearForm()
    {
        Bundle args = getArguments();
        if (args.getString("addOrUpdate") == "add")
        {

            EditText shiftDate = getActivity().findViewById(R.id.shift_date);
            EditText startTime = getActivity().findViewById(R.id.start_time);
            EditText endTime = getActivity().findViewById(R.id.end_time);
            EditText hourlyRate = getActivity().findViewById(R.id.hourly_rate);
            EditText paidBreakMin = getActivity().findViewById(R.id.paid_break_duration);
            EditText unpaidBreakMin = getActivity().findViewById(R.id.unpaid_break_duration);
            EditText bonus = getActivity().findViewById(R.id.bonus_amount);
            EditText expenses = getActivity().findViewById(R.id.expenses_amount);
            EditText comments = getActivity().findViewById(R.id.comments);
            EditText salesMade = getActivity().findViewById(R.id.sales_made_amount);
            EditText targetAmount = getActivity().findViewById(R.id.target_amount);
            if(args.getBoolean("called_from_empty_date"))
            {
                startTime.setText("");
                endTime.setText("");
                comments.setText("");
                hourlyRate.setText("");
                paidBreakMin.setText("");
                unpaidBreakMin.setText("");
                bonus.setText("");
                expenses.setText("");
                salesMade.setText("");
                targetAmount.setText("");
            }
            else
            {
                endTime.setText("");
                comments.setText("");
                hourlyRate.setText("");
                paidBreakMin.setText("");
                unpaidBreakMin.setText("");
                bonus.setText("");
                expenses.setText("");
                salesMade.setText("");
                targetAmount.setText("");
            }
        }

        else if(args.getString("addOrUpdate") == "update")
        {

            EditText updateStartTime = getActivity().findViewById(R.id.update_start_time);
            EditText updateEndTime = getActivity().findViewById(R.id.update_end_time);
            EditText updateComments = getActivity().findViewById(R.id.update_comments);
            EditText updateHourlyRate = getActivity().findViewById(R.id.update_hourly_rate);
            EditText updatePaidBreakMin = getActivity().findViewById(R.id.update_paid_break_duration);
            EditText updateUnpaidBreakMin = getActivity().findViewById(R.id.update_unpaid_break_duration);
            EditText updateBonus = getActivity().findViewById(R.id.update_bonus_amount);
            EditText updateExpenses = getActivity().findViewById(R.id.update_expenses_amount);
            EditText updateSalesMade = getActivity().findViewById(R.id.update_sales_made_amount);
            EditText updateTarget = getActivity().findViewById(R.id.update_target_amount);

            updateStartTime.setText("");
            updateEndTime.setText("");
            updateComments.setText("");
            updateHourlyRate.setText("");
            updatePaidBreakMin.setText("");
            updateUnpaidBreakMin.setText("");
            updateBonus.setText("");
            updateExpenses.setText("");
            updateSalesMade.setText("");
            updateTarget.setText("");

        }
    }
}
