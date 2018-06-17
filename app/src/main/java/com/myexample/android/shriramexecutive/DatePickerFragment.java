package com.myexample.android.shriramexecutive;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import java.util.Calendar;

/**
 * Created by Samp on 9/19/2017.
 *
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        month++;
        Log.v("DatePickerFragment", year + "-" + month + "-" + day);
        String yearstr,monthstr,daystr;
        yearstr = year+"";
        monthstr=month+"";
        daystr=day+"";

        if(month<10){
            monthstr="0"+month;
        }
        if(day<10){
            daystr="0"+day;
        }
        BranchDetailsFragment.sdate=daystr+"-"+monthstr+"-"+yearstr;
        BranchDetailsFragment.rdate=yearstr+"-"+monthstr+"-"+daystr;
        BranchDetailsFragment.mSetDate.setText(BranchDetailsFragment.sdate);
    }
}