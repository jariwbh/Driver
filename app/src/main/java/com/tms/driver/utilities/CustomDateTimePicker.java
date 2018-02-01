package com.tms.driver.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

/***
 * This is Singleton class. Instance of this class is not created.
 * {@link android.app.TimePickerDialog.OnTimeSetListener} callback is used for set time
 * 
 * {@link android.app.DatePickerDialog.OnDateSetListener} callback is used for date
 * 
 * @author Amit
 * 
 */
public class CustomDateTimePicker {

	private OnDateTimeSelectedListener onDateSetListener;

	public interface OnDateTimeSelectedListener {
		public void onDateSet(int year, int monthOfYear, int dayOfMonth,
                              String date);

		public void onTimeSet12hrs(int hourOfDay, int minute, String format,
                                   String time);

		public void onTimeSet24hrs(int hourOfDay, int minute, String time);
	}

	/***
	 * OpenTimePicker opens the time dialog to set time with following
	 * parameters
	 * 
	 * @param context
	 *            context to be passed

	 * @param is24hrs
	 *            time format is of 24 hrs or not set boolean
	 */
	public void openTimePicker(Context context, boolean is24hrs,
			OnDateTimeSelectedListener callback) {
		onDateSetListener = callback;
		Calendar calendarTime = Calendar.getInstance();
		int hourOfDay = calendarTime.get(Calendar.HOUR_OF_DAY);
		int minute = calendarTime.get(Calendar.MINUTE);
		TimePickerDialog timePickerDialog = new TimePickerDialog(context,
				new DateTime(), hourOfDay, minute, is24hrs);
		timePickerDialog.setTitle("Set Time");
		timePickerDialog.setCancelable(false);
		timePickerDialog.show();

	}

	/***
	 * openDatePicker open dateDialog to set date in your application with
	 * following parameters
	 * 
	 * @param context
	 *            context to be passed

	 */
	public void openDatePicker(Context context,
			OnDateTimeSelectedListener callback) {
		onDateSetListener = callback;
		Calendar calendarDate = Calendar.getInstance();
		int monthOfYear = calendarDate.get(Calendar.MONTH);
		int dayOfMonth = calendarDate.get(Calendar.DAY_OF_MONTH);
		int year = calendarDate.get(Calendar.YEAR);
		DatePickerDialog datePickerDialog = new DatePickerDialog(context,
				new DateTime(), year, monthOfYear, dayOfMonth);
		datePickerDialog.setTitle("Set Date");
		datePickerDialog.setCancelable(false);
		datePickerDialog.show();
	}

	/*********************************************************************
	 * This method is used to convert 24hrto12hrs
	 * 
	 * @param hour
	 *            hour
	 * @param minute
	 *            minute
	 * @return 12hr
	 *********************************************************************/
	public String convert24hrTo12hr(int hour, int minute) {

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, 00);
		SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
		String convert = displayFormat.format(cal.getTime());
		return convert;

	}

	/****************************************************************************
	 * This class is used to get callback from the date and Time picker and
	 * result is traced here and manipulations are done.
	 * 
	 * @author amit.singh
	 * 
	 ****************************************************************************/
	private class DateTime implements OnTimeSetListener, OnDateSetListener {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			if (onDateSetListener == null) {
				throw new RuntimeException(
						"Interface OnDateTimeSelectedListener can't be null at this stage.");
			}
			Calendar cal = Calendar.getInstance();
			cal.clear();
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, monthOfYear);
			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			SimpleDateFormat parseFormat = new SimpleDateFormat("dd/MM/yyyy");
			String date = parseFormat.format(cal.getTime());
			monthOfYear = monthOfYear + 1;
			onDateSetListener.onDateSet(year, monthOfYear, dayOfMonth, date);

		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			if (onDateSetListener == null) {
				throw new RuntimeException(
						"Interface OnDateTimeSelectedListener can't be null at this stage.");
			}
			onDateSetListener.onTimeSet24hrs(hourOfDay, minute, hourOfDay + ":"
					+ minute);
			String convert = convert24hrTo12hr(hourOfDay, minute);
			String[] abc = convert.split(":");
			String ar[] = abc[1].split(" ");
			onDateSetListener.onTimeSet12hrs(Integer.valueOf(abc[0]),
					Integer.valueOf(ar[0]), ar[1], convert);

		}
	}

}
