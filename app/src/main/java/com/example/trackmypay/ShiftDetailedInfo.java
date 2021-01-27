package com.example.trackmypay;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShiftDetailedInfo extends Fragment {

    private ArrayList <Shift> shiftsToDisplay;
    Button addAnotherShift;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View vw = inflater.inflate(R.layout.list_view, container, false);

        Bundle bundle = getArguments();
        shiftsToDisplay = bundle.getParcelableArrayList("shift_to_display");

        addAnotherShift = vw.findViewById(R.id.add_another_shift);

        addAnotherShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddShiftFragment addShiftFragment = new AddShiftFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("shift_date", shiftsToDisplay.get(0).getDate());
                bundle.putBoolean("called_from_empty_date", true);

                addShiftFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right).replace(R.id.fragment_layout, addShiftFragment).addToBackStack(null).commit();


            }
        });

        ShiftsAdapter adapter = new ShiftsAdapter(this.getActivity(), R.layout.shift_detailed_info, shiftsToDisplay, getFragmentManager());





        ListView listView = vw.findViewById(R.id.list_view_data);
        listView.setAdapter(adapter);


        return vw;

    }

}
