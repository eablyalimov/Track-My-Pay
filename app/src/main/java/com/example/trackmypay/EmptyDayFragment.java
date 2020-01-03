package com.example.trackmypay;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class EmptyDayFragment extends Fragment {

    View vw;
    Button addShiftButton;
    private long emptyDayDate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vw = inflater.inflate(R.layout.shift_not_exist_layout, container, false);
        addShiftButton = vw.findViewById(R.id.add_shift_from_empty_date);
        addShiftButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddShiftFragment addShiftFragment = new AddShiftFragment();
                addShiftFragment.setDateIfCalledFromEmptyDay(emptyDayDate);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_layout, addShiftFragment).addToBackStack(null).commit();
            }
        }));
        return vw;
    }

    public void emptyDayDateSet(long emptyDayDate)
    {
        this.emptyDayDate = emptyDayDate;
    }

}
