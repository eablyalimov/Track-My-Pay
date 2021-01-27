package com.example.trackmypay;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

public class DeleteShiftDialog extends AppCompatDialogFragment {

    int shift_id;
    ShiftManager shiftManager = new ShiftManager(this.getActivity());

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Shift?").setMessage("Remove Shift from your Calendar?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                onRemoveShiftClick();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }


    public void onRemoveShiftClick()
    {

        Bundle args = getArguments();
        Bundle bundle = new Bundle();

        shiftManager.removeShift(args.getInt("shift_id"));
        Toast toast = Toast.makeText(this.getActivity(), "Shift has been removed", Toast.LENGTH_LONG);
        ShiftCalendar shiftCalendar = new ShiftCalendar();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(args.getLong("month_to_display"));
        bundle.putSerializable("month_to_display", cal);
        shiftCalendar.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right).replace(R.id.fragment_layout, shiftCalendar).commit();
        toast.show();
    }

    public void setShift_id(int _id)
    {
        this.shift_id = _id;
    }
}
