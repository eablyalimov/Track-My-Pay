package com.example.trackmypay;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.CheckBoxPreference;
import androidx.preference.DropDownPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

public class AppSettings extends PreferenceFragmentCompat {

    EditTextPreference hourRate;
    EditTextPreference userName;
    EditTextPreference unpaidBreakDuration;
    EditTextPreference paidBreakDuration;
    SwitchPreference paidBreakSwitch;
    SwitchPreference unpaidBreakSwitch;
    SwitchPreference bonusSwitch;
    SwitchPreference expensesSwitch;
    SwitchPreference hourlyRateSwitch;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ListPreference currencyValue;
    ListPreference firstDayOfWeek;
    SwitchPreference isCommissionDeficitCarryOver;
    SwitchPreference commission;



    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        sharedPreferences = getActivity().getSharedPreferences(SharedPrefs.SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        addPreferencesFromResource(R.xml.preference_main);
        hourRate = findPreference("hourly_rate");
        userName = findPreference("user_name");
        paidBreakDuration = findPreference("paid_break_duration");
        unpaidBreakDuration = findPreference("unpaid_break_duration");
        hourlyRateSwitch = findPreference("hourly_rate_switch");
        paidBreakSwitch = findPreference("paid_break_switch");
        unpaidBreakSwitch = findPreference("unpaid_break_switch");
        bonusSwitch = findPreference("bonus_switch");
        expensesSwitch = findPreference("expenses_switch");
        currencyValue = findPreference("currency");
        isCommissionDeficitCarryOver = findPreference("is_commission_deficit_carried_over");
        commission = findPreference("commission");
        firstDayOfWeek = findPreference("first_day_of_week");



        paidBreakDuration.setSummary(paidBreakDuration.getText());
        unpaidBreakDuration.setSummary(unpaidBreakDuration.getText());



        paidBreakDuration.setEnabled(paidBreakSwitch.isChecked());
        unpaidBreakDuration.setEnabled(unpaidBreakSwitch.isChecked());
        currencyValue.setSummary(currencyValue.getValue());
        hourRate.setSummary(sharedPreferences.getString(SharedPrefs.CURRENCY, "$") + hourRate.getText());
        isCommissionDeficitCarryOver.setEnabled(sharedPreferences.getBoolean(SharedPrefs.COMMISSION, false));


        hourRate.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            }
        });

        firstDayOfWeek.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                editor.putString(SharedPrefs.FIRST_DAY_OF_WEEK, newValue.toString()).apply();

                return true;
            }
        });

        paidBreakDuration.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        unpaidBreakDuration.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        hourRate.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                editor.putFloat(SharedPrefs.HOURLY_RATE, Float.valueOf(newValue.toString())).apply();
                hourRate.setSummary(sharedPreferences.getString(SharedPrefs.CURRENCY, "$") + newValue.toString());
                return true;
            }
        });

        paidBreakSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                editor.putBoolean(SharedPrefs.PAID_BREAK_SWITCH, Boolean.valueOf(newValue.toString()));
                paidBreakDuration.setEnabled(Boolean.valueOf(newValue.toString()));
                return true;
            }
        });

        unpaidBreakSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                editor.putBoolean(SharedPrefs.UNPAID_BREAK_SWITCH, Boolean.valueOf(newValue.toString()));
                unpaidBreakDuration.setEnabled(Boolean.valueOf(newValue.toString()));
                return true;
            }
        });

        bonusSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                editor.putBoolean(SharedPrefs.BONUS_SWITCH, Boolean.valueOf(newValue.toString()));
                return true;
            }
        });
        expensesSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                editor.putBoolean(SharedPrefs.EXPENSES_SWITCH, Boolean.valueOf(newValue.toString()));
                return true;
            }
        });

        hourlyRateSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                editor.putBoolean(SharedPrefs.HOURLY_RATE_SWITCH, Boolean.valueOf(newValue.toString()));
                return true;
            }
        });




        userName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                editor.putString(SharedPrefs.USER_NAME, newValue.toString()).commit();
                Toast toast = Toast.makeText(getActivity(),"Restart the app to see changes", Toast.LENGTH_LONG);
                toast.show();
                return true;
            }
        });

        paidBreakDuration.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                editor.putInt(SharedPrefs.PAID_BREAK_DURATION, Integer.valueOf(newValue.toString())).apply();
                paidBreakDuration.setSummary(newValue.toString());
                return true;
            }
        });

        unpaidBreakDuration.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                editor.putInt(SharedPrefs.UNPAID_BREAK_DURATION, Integer.valueOf(newValue.toString())).apply();
                unpaidBreakDuration.setSummary(newValue.toString());
                return true;
            }
        });

        currencyValue.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                editor.putString(SharedPrefs.CURRENCY, newValue.toString());

                currencyValue.setSummary(newValue.toString());
                hourRate.setSummary(newValue.toString() + hourRate.getText());

                return true;
            }



        });

        isCommissionDeficitCarryOver.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                editor.putBoolean(SharedPrefs.COMMISSION_DEFICIT, (Boolean.valueOf(newValue.toString()))).apply();
                return true;
            }
        });

        commission.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                editor.putBoolean(SharedPrefs.COMMISSION, (Boolean.valueOf(newValue.toString()))).apply();
                isCommissionDeficitCarryOver.setEnabled((Boolean.valueOf(newValue.toString())));
                return true;
            }
        });

    }
}


