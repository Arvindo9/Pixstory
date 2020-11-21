package com.app.pixstory.core.dialog.date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


import com.app.pixstory.R;
import com.app.pixstory.utils.General;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * Author       : Arvindo Mondal
 * Created on   : 24-06-2019
 * Email        : arvindomondal@gmail.com
 * Designation  : Programmer
 * About        : I am a human can only think, I can't be a person like machine which have lots of memory and knowledge.
 * Quote        : No one can measure limit of stupidity but stupid things bring revolutions
 * Strength     : Never give up
 * Motto        : To be known as great Mathematician
 * Skills       : Algorithms and logic
 */
public class DateDialog extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    public static final String TAG = DateDialog.class.getSimpleName();
    public static final int TYPE_DATE_BIRTH = 1;

    private DateInterface dateInterface;
    private int type;

    public interface DateInterface {
        /**
         *
         * @param readableData date will return in string format
         * @param sendDate date will return to send to server
         * @param params date will return in int format
         */
        void date(String readableData, String sendDate, String... params);
    }

    public void setup(DateInterface callback){
        this.dateInterface = callback;
    }

    public void setup(DateInterface callback, int type){
        this.dateInterface = callback;
        this.type = type;
    }

    public static DateDialog newInstance(DateInterface object) {
//        dateInterface = object;
        DateDialog fragment = new DateDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if(TYPE_DATE_BIRTH == type){
            return birthDateDialog();
        }

        DatePickerDialog dialog = new DatePickerDialog(Objects.requireNonNull(getContext()), R.style.DateDialog,
                this, year, month, day);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());

        return dialog;
    }

    private Dialog birthDateDialog() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -(365 * 12));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.AlertDialog);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.date_picker_dialog, null);
        final NumberPicker datePicker = dialog.findViewById(R.id.picker_date);
        final NumberPicker monthPicker = dialog.findViewById(R.id.picker_month);
        final NumberPicker yearPicker = dialog.findViewById(R.id.picker_year);

        datePicker.setMinValue(1);
        datePicker.setMaxValue(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        datePicker.setValue(calendar.get(Calendar.DATE));

        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(11);
        monthPicker.setValue(month);
        monthPicker.setDisplayedValues(getResources().getStringArray(R.array.months));

        monthPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            // Create a calendar object and set year and month
            Calendar mycal = new GregorianCalendar(year, newVal, 1);

            datePicker.setValue(calendar.get(Calendar.DATE));
            datePicker.setMaxValue(mycal.getActualMaximum(Calendar.DAY_OF_MONTH));
        });

//        yearPicker.setMinValue(year);     //use this
        yearPicker.setMinValue(year-200);       //comment this
        yearPicker.setMaxValue(year);
        yearPicker.setValue(year);

        builder.setMessage(getString(R.string.select_date));
        builder.setView(dialog)
                // Add action buttons
                .setPositiveButton(R.string.ok, (dialog12, id) -> {

//                    calendar.set(Calendar.YEAR, year);                     //use this
                    calendar.set(Calendar.YEAR, yearPicker.getValue());         //comment this
//                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.MONTH, monthPicker.getValue());
//                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    calendar.set(Calendar.DAY_OF_MONTH, datePicker.getValue());

                    String dateReadable = General.getDateReadable(calendar.getTime());
                    String sendDate = General.getDateSend(calendar.getTime());

                    int dateInt = General.getDateInt(calendar.getTime());

                    dateInterface.date(dateReadable, sendDate, String.valueOf(dateInt), String.valueOf(calendar.get(Calendar.DATE)),
                            String.valueOf(calendar.get(Calendar.MONTH)), String.valueOf(calendar.get(Calendar.YEAR)));
                })
                .setNegativeButton(R.string.cancel, (dialog1, id) -> {
//                        MonthYearPickerDialog.this.getDialog().cancel();
                });
        return builder.create();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, day);

        String dateReadable = General.getDateReadable(myCalendar.getTime());
        String sendDate = General.getDateSend(myCalendar.getTime());

        int dateInt = General.getDateInt(myCalendar.getTime());

        dateInterface.date(dateReadable, sendDate, String.valueOf(dateInt), String.valueOf(day),
                String.valueOf(month), String.valueOf(year));

        this.dismiss();
    }
}
