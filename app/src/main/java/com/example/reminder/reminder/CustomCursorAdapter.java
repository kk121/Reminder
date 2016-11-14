package com.example.reminder.reminder;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";

    // Default constructor
    public CustomCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewTitle = (TextView) view.findViewById(R.id.text1);
        String title = cursor.getString(cursor.getColumnIndex(RemindersDbAdapter.KEY_TITLE));
        textViewTitle.setText(title);
        TextView dateTime = (TextView) view.findViewById(R.id.reminder_time);
        String date = cursor.getString(cursor.getColumnIndex(RemindersDbAdapter.KEY_DATE_TIME));
        try {
            String formatedDate = formatToYesterdayOrToday(date);
            dateTime.setText(formatedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // R.layout.list_row is your xml layout for each row
        return cursorInflater.inflate(R.layout.reminder_row, parent, false);
    }

    public String formatToYesterdayOrToday(String dateString) throws ParseException {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date date = dateTimeFormat.parse(dateString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        DateFormat timeFormatter = new SimpleDateFormat("hh:mma");
        DateFormat dateTimeFormatter = new SimpleDateFormat("dd MMM ',' hh:mma");
        DateFormat dateTimeFormatterWithYr = new SimpleDateFormat("dd MMM yyyy',' hh:mma");

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return timeFormatter.format(date);
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "Yesterday," + timeFormatter.format(date);
        } else if (calendar.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR)) {
            return "Tomorrow," + timeFormatter.format(date);
        } else if (calendar.get(Calendar.YEAR) > today.get(Calendar.YEAR)) {
            return dateTimeFormatterWithYr.format(date);
        } else {
            return dateTimeFormatter.format(date);
        }
    }
}
