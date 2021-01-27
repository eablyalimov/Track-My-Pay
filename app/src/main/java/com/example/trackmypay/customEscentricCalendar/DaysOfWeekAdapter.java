package com.example.trackmypay.customEscentricCalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.trackmypay.R;

import java.util.List;

public class DaysOfWeekAdapter extends ArrayAdapter {

    List<String> daysLabels;
    LayoutInflater layoutInflater;

    DaysOfWeekAdapter(Context context, List<String> daysLabels) {
        super(context, R.layout.days_of_week_label);

        this.daysLabels = daysLabels;
        layoutInflater = LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {


        convertView = layoutInflater.inflate(R.layout.days_of_week_label, parent, false);
        String dayLabel = daysLabels.get(position);
        TextView dayLabelText = convertView.findViewById(R.id.dayOfWeek);
        dayLabelText.setText(dayLabel);


        return convertView;
    }

    @Override
    public int getCount() {
        return daysLabels.size();
    }
    @Nullable
    @Override
    public Object getItem(int position) {
        return daysLabels.get(position);
    }
    @Override
    public int getPosition(Object item) {
        return daysLabels.indexOf(item);
    }

}
