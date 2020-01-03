package com.example.trackmypay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ShiftDetailedInfo extends Fragment {
    TextView dateText;
    TextView startTime;
    TextView endTime;
    TextView comments;
    TextView hoursWorked;
    TextView grossPay;
    ShiftManager shiftManager;
    Button editShift;
    Shift shiftToDisplay;
    UpdateShift updateShift;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vw = inflater.inflate(R.layout.shift_detailed_info, container, false);
        shiftManager = new ShiftManager(this.getActivity());

        dateText = vw.findViewById(R.id.shift_date);
        startTime = vw.findViewById(R.id.start_time);
        endTime = vw.findViewById(R.id.end_time);
        comments = vw.findViewById(R.id.comments);
        hoursWorked = vw.findViewById(R.id.hours_worked);
        grossPay = vw.findViewById(R.id.gross_pay);
        editShift = vw.findViewById(R.id.edit_shift);

        double hoursWorkedVal;



        editShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateShift = new UpdateShift();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_layout, updateShift).addToBackStack(null).commit();
            }
        });



        hoursWorkedVal = ((shiftToDisplay.getEndTime() - shiftToDisplay.getStartTime()) / 3600000);

        dateText.setText(ShiftValuesConverter.DateStringConvert(shiftToDisplay.getDate()));
        startTime.setText(ShiftValuesConverter.StartTimeStringConvert(shiftToDisplay.getStartTime()));
        endTime.setText(ShiftValuesConverter.EndTimeStringConvert(shiftToDisplay.getEndTime()));
        comments.setText(shiftToDisplay.getComments());
        hoursWorked.setText(hoursWorkedVal + getString(R.string.hours));
        grossPay.setText(getString(R.string.dollar_sign) + hoursWorkedVal * 14.00);












        return vw;

    }

    public void passShift(Shift paramShift)
    {
        shiftToDisplay = paramShift;
    }
}
