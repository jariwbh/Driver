package com.tms.driver.utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.xmlpull.v1.XmlSerializer;

import android.Manifest.permission;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tms.driver.R;
import com.tms.driver.async.AndroidWebResponseTask;
import com.tms.driver.interfaces.OnAlertDialogFragmentListener;
import com.tms.driver.utilities.AlertDialogUtil;
import com.tms.driver.utilities.CustomProgressDialog;

public class Utilities {
	private static Utilities utilityInstance;
	private static Context context;

	// private EncryptionDecryption mEncryptionDecryptionInstance;

	// public void setisSessionClear(boolean setSessionStatus) {
	//
	// // UrlConstants.isSessionClear = setSessionStatus;
	//
	// }

	// public boolean getSessionStatus() {
	// // return UrlConstants.isSessionClear;
	// }

	/*
	 * 
	 * Default Constructor
	 */
	private Utilities() {

	}

	TimePickerDialog timePicker;

	public void postXml(String Url, String data, boolean isDialog, int Id,
			AndroidWebResponseTask mWebserviceAsync) {
		// TODO Auto-generated method stub
		mWebserviceAsync.postXML(Url, data, isDialog, Id);
	}

	public String createXML(String[] keys, String[] values) {
		XmlSerializer xmlSerializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();

		try {
			xmlSerializer.setOutput(writer);
			// start DOCUMENT
			xmlSerializer.startDocument("UTF-8", true);
			xmlSerializer.startTag("", keys[0]);
			for (int count = 1; count < keys.length; count++) {
				xmlSerializer.startTag("", keys[count]);
				xmlSerializer.text(values[count]);
				xmlSerializer.endTag("", keys[count]);

			}
			// end DOCUMENT
			xmlSerializer.endTag("", keys[0]);
			xmlSerializer.endDocument();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return writer.toString();

	}

	/***
	 * create single instance of the Utility class. Check if the instance is
	 * null then it will create new instance else
	 * 
	 * @param ctx
	 *            of the activity
	 * @return instance
	 */
	public static Utilities getInstance(Context ctx) {
		if (utilityInstance == null) {
			utilityInstance = new Utilities();

		}

		context = ctx;
		return utilityInstance;
	}

	public String getDateAndTime(String dateandTime, String currentFormat,
			String selectedFormat) {
		StringBuilder dateTime = new StringBuilder();
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

			Date tempDate = simpleDateFormat.parse(dateandTime.split("T")[0]);
			Date tempTime = timeFormat.parse(dateandTime.split("T")[1]);
			// String[] dateTimeString = dateandTime.split("T");
			SimpleDateFormat dateFormat = new SimpleDateFormat(selectedFormat);

			String date = dateFormat.format(tempDate);

			dateTime.append(date.toString());

		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateTime.toString();

	}

	public String getDateAndTime1(String dateandTime, String currentFormat,
			String selectedFormat) {
		StringBuilder dateTime = new StringBuilder();
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					currentFormat);

			Date tempDate = simpleDateFormat.parse(dateandTime);

			// String[] dateTimeString = dateandTime.split("T");
			SimpleDateFormat dateFormat = new SimpleDateFormat(selectedFormat);

			String date = dateFormat.format(tempDate);

			dateTime.append(date.toString());

		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateTime.toString();

	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
						LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	// public String encryptString(String valueToEncypt) {
	// return EncryptionDecryption.encryptString(valueToEncypt);
	// }

	public String getDateAndTime(String dateandTime) {
		StringBuilder dateTime = new StringBuilder();
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

			Date tempDate = simpleDateFormat.parse(dateandTime.split("T")[0]);
			Date tempTime = timeFormat.parse(dateandTime.split("T")[1]);
			// String[] dateTimeString = dateandTime.split("T");
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			SimpleDateFormat actualTimeFormat = new SimpleDateFormat("hh:mm a");
			String date = dateFormat.format(tempDate);

			String time = actualTimeFormat.format(tempTime);
			dateTime.append(date.toString());
			dateTime.append("\t");

			dateTime.append(time.toString());

		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateTime.toString();

	}

	public boolean checkEndDateConstraints(String startDate, String endDate) {
		boolean isgreater = false;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		try {
			if (startDate != null && endDate != null) {
				Date date1 = sdf.parse(startDate);
				Date date2 = sdf.parse(endDate);
				if (date1.before(date2)) {
					isgreater = false;
				} else if (date1.compareTo(date2) == 0) {
					isgreater = false;
				} else {
					isgreater = true;
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isgreater;

	}

	public String getCurrentDateAndTime(boolean isDateonly) {
		GregorianCalendar currentDay = (GregorianCalendar) GregorianCalendar
				.getInstance();
		String currentDateandTime;
		if (isDateonly) {
			currentDateandTime = (String) android.text.format.DateFormat
					.format("MM/dd/yyyy", currentDay);
		} else {
			currentDateandTime = (String) android.text.format.DateFormat
					.format("MM/dd/yyyy hh:mm a", currentDay);
		}

		return currentDateandTime;
	}

	public String set15MinutePrior(String dateandTime) {
		Calendar currentDate = Calendar.getInstance();
		Calendar selectedDate = (Calendar) currentDate.clone();
		SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

		Date tempDate;
		try {
			tempDate = timeFormat.parse(dateandTime);
			selectedDate.setTime(tempDate);
			selectedDate.add(Calendar.MINUTE, -15);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return (String) android.text.format.DateFormat.format(
				"MM/dd/yyyy hh:mm a", selectedDate);

	}

	/**
	 * This method is used to show Toast message with custom message and the
	 * custom duration. It will show the toast if
	 * 
	 * @param message
	 *            not equal to null
	 * @param duration
	 *            in milliseconds greater than zero else it will show the toast
	 *            with default Toast duration.
	 */
	public void showToast(String message, int duration) {

		if (!TextUtils.isEmpty(message) && !TextUtils.equals("null", message)) {
			try {
				if (duration < 0) {
					Toast.makeText(context.getApplicationContext(), message,
							duration).show();
				} else {
					Toast.makeText(context.getApplicationContext(), message,
							Toast.LENGTH_SHORT).show();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method is used to show the custom Toast with custom layout and
	 * duration
	 * 
	 * @param view
	 *            inflated layout to show
	 * 
	 * @param duration
	 *            for toast to show on UI
	 * @param gravity
	 *            for the toast. For Example (LEFT,RIGHT,TOP,BOTTOM,CENTER)
	 */

	public void showCustomToast(View view, int duration, int gravity) {
		Toast customToast = new Toast(context);
		if (view != null) {
			customToast.setView(view);

			if (duration > 0) {
				customToast.setDuration(duration);

			} else {
				customToast.setDuration(Toast.LENGTH_SHORT);
			}
			if (gravity > 0) {
				customToast.setGravity(gravity, 0, 0);
			}

			customToast.show();
		}

	}

	/**
	 * This method is used to get width and height of screen using Display class
	 * methods. These methods are deprecated in new Android API's
	 * 
	 * @return width and height in int[]
	 */
	public int[] getDisplay_Width_Height() {
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		return new int[] { width, height };
	}

	/**
	 * This method is used to get width and height of screen using Display
	 * Metrices
	 * 
	 * @return width and height in int[]
	 */

	public int[] getDisplayMetrices() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int width = displayMetrics.widthPixels;
		int height = displayMetrics.heightPixels;
		return new int[] { width, height };
	}

	/**
	 * This method is used to get Window Manager Services.
	 * 
	 * @return WindowManager
	 */
	public WindowManager getWindowManager() {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return windowManager;
	}

	/**
	 * Show single option alert dialog
	 * 
	 * @param title
	 *            of the dialog
	 * @param msg
	 *            to show in alert dialog
	 * @param button1
	 *            text
	 * @param iconResId
	 *            if any icon to show
	 * @param listener
	 *            for this alertdialog
	 * @param activityId
	 *            to which it will callback
	 */
	public static AlertDialog mAlertDialog;
	
	
	public void hideAlertDialog(){
		
		if(mAlertDialog!=null){
			mAlertDialog.dismiss();
		}
	}
	

	public void showSingleOption_AlertDialog(String title, String msg,
			String button1, int iconResId,
			OnAlertDialogFragmentListener listener, int activityId) {
		try {
			showHideKeyboard(false);
			if (mAlertDialog != null) {
				if (mAlertDialog.isShowing()) {
					mAlertDialog.cancel();

				}
				mAlertDialog = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mAlertDialog = new AlertDialogUtil().singleOptionAlertDialog(context,
				title, msg, button1, iconResId, listener, activityId);
	}

	/**
	 * Show single option alert dialog
	 * 
	 * @param title
	 *            of the dialog
	 * @param msg
	 *            to show in alert dialog
	 * @param button1
	 *            text
	 * @param button2
	 *            text
	 * @param iconResId
	 *            if any icon to show
	 * @param listener
	 *            for this alertdialog
	 * @param activityId
	 *            to which it will callback
	 */

	public void showDoubleOption_AlertDialog(String title, String msg,
			String button1, String button2, int iconResId,
			OnAlertDialogFragmentListener listener, int activityId) {
		showHideKeyboard(false);

		new AlertDialogUtil().doubleOptionAlertDialog(context, title, msg,
				button1, button2, iconResId, listener, activityId);
	}

	/**
	 * Show single option alert dialog
	 * 
	 * @param title
	 *            of the dialog
	 * @param msg
	 *            to show in alert dialog
	 * @param button1
	 *            text
	 * @param button2
	 *            text
	 * @param button3
	 *            text
	 * @param iconResId
	 *            if any icon to show
	 * @param listener
	 *            for this alertdialog
	 * @param activityId
	 *            to which it will callback
	 */

	public void showThreeOption_AlertDialog(String title, String msg,
			String button1, String button2, String button3, int iconResId,
			OnAlertDialogFragmentListener listener, int activityId) {
		showHideKeyboard(false);

		new AlertDialogUtil().threeOptionAlertDialog(context, title, msg,
				button1, button2, button3, iconResId, listener, activityId);
	}

	/*	*//**
	 * Used to request data from server. Or to send data in request.
	 * 
	 * @param url
	 *            to hit
	 * @param id
	 *            callback id where to get response
	 * @param callback
	 *            where to send the response
	 * @param key
	 *            to send as embedded in url. if null simple get method will be
	 *            called
	 * @param value
	 *            to send with related keys embedded in url.if null simple get
	 *            method will be called
	 */

	// public void getUrl(String url, int id, OnWebServiceProcess callback,
	// String[] key, String[] value) {
	// if (key != null && value != null) {
	// if (key.length > 0 && value.length > 0) {
	// WebServiceAsync.getInstance(context, callback).get(url, id,
	// key, value);
	//
	// }
	// } else {
	// WebServiceAsync.getInstance(context, callback).get(url, id);
	// }
	// }

	/**
	 * Used to post data to server. Or to send data to server in Basic name
	 * value pair or as jsonObject .
	 * 
	 * @param url
	 *            to hit
	 * @param id
	 *            callback id where to get response
	 * @param callback
	 *            where to send the response
	 * @param key
	 *            to send as embedded in url. if null simple get method will be
	 *            called
	 * @param value
	 *            to send with related keys embedded in url.if null simple get
	 *            method will be called
	 * @param isJsonobject
	 *            if true then the key value pair will be send as jsonobject
	 *            else BasicNamevaluepair will be used
	 */

	// public void postUrl(String url, int id, OnWebServiceProcess callback,
	// String[] key, String[] value, boolean isJsonobject) {
	// if (key != null && value != null) {
	// if (key.length > 0 && value.length > 0) {
	// WebServiceAsync.getInstance(context, callback).post(url, id,
	// isJsonobject, key, value);
	//
	// }
	// }
	// }

	/**
	 * Type of Log. Used in ShowLog method to choose the type of log to show.
	 * 
	 * @author siddharth.brahmi
	 * 
	 */
	public enum Type {
		DEBUG, VERBOSE, ERROR, INFO, WARN
	};

	/**
	 * This method is used to print log.if no type is matched then it will print
	 * the message with default type.
	 * 
	 * @param type
	 *            of log to print.(For example use Type.Error show message as
	 *            error)
	 * @param TAG
	 *            to represents Log.
	 * @param message
	 *            to print
	 */

	public void showLog(Type type, String TAG, String message) {
		try {
			if (type != null) {
				switch (type) {
				case DEBUG:
					Log.d(TAG, message);
					break;
				case VERBOSE:
					Log.v(TAG, message);
					break;

				case ERROR:
					Log.e(TAG, message);
					break;

				case INFO:
					Log.i(TAG, message);
					break;

				case WARN:
					Log.w(TAG, message);
					break;
				default:
					Log.e(TAG, message);
					break;
				}
			} else {
				Log.e(TAG, message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public AssetManager getAssetManager() {
		return context.getAssets();
	}

	public Resources getResources() {
		return context.getResources();
	}

	/**
	 * Check if media is mounted. Is External Storage is available
	 * 
	 * @return true if available else false.
	 */
	public boolean isMediaMounted() {
		if (TextUtils.equals(Environment.getExternalStorageState(),
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/***
	 * This method is used to write data on file. It will check for the media
	 * mounted and write external storage permission before writing the file to
	 * SDcard.
	 * 
	 * @param filePath
	 *            where to store the file on Sdcard
	 * @param fileName
	 *            name of file to write
	 * @param inputStream
	 *            inputstream of the file
	 * @return file path in String format where the file has been written
	 *         successfully else return null.
	 */
	public String writetoFile(String filePath, String fileName,
			InputStream inputStream) {
		File expPath = null;
		if (isMediaMounted()) {
			if (checkPermissions(BasicPermissions.WRITE_PERMISSION)) {
				try {
					File root = Environment.getExternalStorageDirectory();
					if (filePath != null && fileName != null) {
						expPath = new File(root.toString() + "/" + filePath,
								fileName);
						if (!expPath.exists()) {
							if (inputStream != null) {
								FileUtils.copyInputStreamToFile(inputStream,
										expPath);
							}
						}
					}
					return expPath.getPath();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				showToast(
						"Write External Storage permission is not available.",
						1000);
			}
		} else {
			showToast("No External media found to write the file", 1000);
		}
		return null;
	}

	/**
	 * Enum to check permission to perform related operation with the
	 * permissions in the application
	 * 
	 * @author siddharth.brahmi
	 * 
	 */
	public enum BasicPermissions {
		MODIFY_PHONE_STATE, CHANGE_NETWORK_STATE, INTERNET, WRITE_PERMISSION, ACCESS_NETWORK_STATE, READ_CONTACTS, WRITE_CONTACTS, CAMERA, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
	};

	/**
	 * This method is used to check if the specific permission is declared in
	 * the manifest file for given operation.
	 * 
	 * @param permissions
	 * @return true if permission granted else false
	 */
	public boolean checkPermissions(BasicPermissions permissions) {
		int res;
		switch (permissions) {

		case INTERNET:
			res = context.checkCallingOrSelfPermission(permission.INTERNET);

			return (res == PackageManager.PERMISSION_GRANTED);

		case WRITE_PERMISSION:
			res = context
					.checkCallingOrSelfPermission(permission.WRITE_EXTERNAL_STORAGE);

			return (res == PackageManager.PERMISSION_GRANTED);

		case ACCESS_NETWORK_STATE:

			res = context
					.checkCallingOrSelfPermission(permission.ACCESS_NETWORK_STATE);

			return (res == PackageManager.PERMISSION_GRANTED);
		case READ_CONTACTS:

			res = context
					.checkCallingOrSelfPermission(permission.READ_CONTACTS);

			return (res == PackageManager.PERMISSION_GRANTED);
		case WRITE_CONTACTS:

			res = context
					.checkCallingOrSelfPermission(permission.WRITE_CONTACTS);

			return (res == PackageManager.PERMISSION_GRANTED);

		case CAMERA:
			res = context.checkCallingOrSelfPermission(permission.CAMERA);
			return (res == PackageManager.PERMISSION_GRANTED);

		case ACCESS_FINE_LOCATION:
			res = context
					.checkCallingOrSelfPermission(permission.ACCESS_FINE_LOCATION);
			return (res == PackageManager.PERMISSION_GRANTED);
		case ACCESS_COARSE_LOCATION:
			res = context
					.checkCallingOrSelfPermission(permission.ACCESS_COARSE_LOCATION);
			return (res == PackageManager.PERMISSION_GRANTED);
		case CHANGE_NETWORK_STATE:
			res = context
					.checkCallingOrSelfPermission(permission.CHANGE_NETWORK_STATE);
			return (res == PackageManager.PERMISSION_GRANTED);
		case MODIFY_PHONE_STATE:
			res = context
					.checkCallingOrSelfPermission(permission.MODIFY_PHONE_STATE);
			return (res == PackageManager.PERMISSION_GRANTED);
		default:
			return false;
		}
		// return false;

	}

	/**
	 * Method to link the text
	 * 
	 * @param textView
	 * @param textViewText
	 * @param texttoLinkArray
	 * @param linkedTextArray
	 * @param ifUrl
	 */
	// public void linkifyText(TextView textView, String textViewText,
	// String[] texttoLinkArray, LinkTextModel[] linkedTextArray,
	// String ifUrl) {
	// LinkifyText.linkifyText(context, textView, textViewText,
	// texttoLinkArray, linkedTextArray, LinkType.ACTIVITY);
	// }

	/**
	 * Enum class to declare the types of networks used in android. It will be
	 * used in checkInternetConnection method to check the connectivity of the
	 * selected type
	 * 
	 * @author siddharth.brahmi
	 * 
	 */
	public enum NetworkType {
		WIFI, MOBILE
	};

	/**
	 * This method is used to check the connectivity of the selected network
	 * type.
	 * 
	 * @param networkType
	 * @return true if selected network is connected else return false.
	 */
	public boolean checkInternetConnection(NetworkType networkType) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		switch (networkType) {
		case WIFI:
			for (NetworkInfo ni : netInfo) {

				if (ni.getTypeName().equalsIgnoreCase("WIFI"))
					if (ni.isConnected())
						return true;
			}

		case MOBILE:
			for (NetworkInfo ni : netInfo) {

				if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
					if (ni.isConnected())
						return true;
			}

		default:
			return false;
		}
	}

	public void showProgressDialog(String message, int resID) {
		CustomProgressDialog.getInstance(context).showDialog(message, resID);
	}

	/*
	 * public void showCustomProgressDialog(int resLayoutID) {
	 * CustomProgressDialog.getInstance(context).showCustomDialog(resLayoutID);
	 * }
	 * 
	 * public void showProgressDialog(View view) {
	 * CustomProgressDialog.getInstance(context).showCustomDialog(view); }
	 */

	/**
	 * This method is used to show and hide the keyboard
	 * 
	 * @param isShow
	 *            if true show keyboard else hide.
	 */
	public static void showHideKeyboard(boolean isShow) {
		try {

			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (isShow) {
				imm.showSoftInput(((Activity) context).getCurrentFocus(),
						InputMethodManager.SHOW_FORCED);
			} else {
				if (imm.isAcceptingText()) {
					imm.hideSoftInputFromWindow(((Activity) context)
							.getCurrentFocus().getWindowToken(), 0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method to get the current version of the Application
	 * 
	 * @return
	 */
	public int getAppVersion() {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * This method is used to enable or disable Gps programatically
	 * 

	 */
	public void toogleGps(boolean enabledGPS) {
		if (enabledGPS) {
			Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
			intent.putExtra("enabled", true);
			context.sendBroadcast(intent);
		} else {
			Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
			intent.putExtra("enabled", false);
			context.sendBroadcast(intent);

		}
	}

	/**
	 * This method is used to enable and disable mobile network from the
	 * application
	 * 
	 * @param enableNetwork
	 *            set accessible else false
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void toggleNetwork(boolean enableNetwork) {
		if (isSdkVersionAboveGingerBread()) {
			ConnectivityManager conman = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			Class conmanClass;
			try {
				conmanClass = Class.forName(conman.getClass().getName());

				Field iConnectivityManagerField = conmanClass
						.getDeclaredField("mService");
				iConnectivityManagerField.setAccessible(true);
				Object iConnectivityManager = iConnectivityManagerField
						.get(conman);
				Class iConnectivityManagerClass = Class
						.forName(iConnectivityManager.getClass().getName());
				@SuppressWarnings("unchecked")
				Method setMobileDataEnabledMethod = iConnectivityManagerClass
						.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
				setMobileDataEnabledMethod.setAccessible(true);

				setMobileDataEnabledMethod.invoke(iConnectivityManager,
						enableNetwork);

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Method dataConnSwitchmethod;
			Class telephonyManagerClass;
			Object ITelephonyStub;
			Class ITelephonyClass;
			if (checkPermissions(BasicPermissions.MODIFY_PHONE_STATE)) {
				try {
					TelephonyManager telephonyManager = (TelephonyManager) context
							.getSystemService(Context.TELEPHONY_SERVICE);

					if (telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED) {
						enableNetwork = true;
					} else {
						enableNetwork = false;
					}

					telephonyManagerClass = Class.forName(telephonyManager
							.getClass().getName());
					Method getITelephonyMethod = telephonyManagerClass
							.getDeclaredMethod("getITelephony");
					getITelephonyMethod.setAccessible(true);
					ITelephonyStub = getITelephonyMethod
							.invoke(telephonyManager);
					ITelephonyClass = Class.forName(ITelephonyStub.getClass()
							.getName());

					if (enableNetwork) {
						dataConnSwitchmethod = ITelephonyClass
								.getDeclaredMethod("disableDataConnectivity");
					} else {
						dataConnSwitchmethod = ITelephonyClass
								.getDeclaredMethod("enableDataConnectivity");
					}
					dataConnSwitchmethod.setAccessible(true);
					dataConnSwitchmethod.invoke(ITelephonyStub);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Check application build API.If equal to GINGERBREAD or above return true
	 * else false.
	 * 
	 * @return
	 */
	public boolean isSdkVersionAboveGingerBread() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
			return true;
		}
		return false;
	}

	public boolean isSdkVersionAboveJellyBean() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			return true;
		}
		return false;
	}

	/**
	 * Used to send SMS from the application. User can also send long text
	 * message using this method
	 * 
	 * @param phoneNumber
	 *            of the used to whom the message to be sent
	 * @param message
	 *            to send
	 */
	public void sendLongSMS(String phoneNumber, String message) {
		SmsManager smsManager = SmsManager.getDefault();
		ArrayList<String> parts = smsManager.divideMessage(message);
		smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null,
				null);
	}

	/**
	 * Fragment Utility Methods
	 */
	/**
	 * This method is used to replaceFragment with another fragment
	 * 
	 * @param replaceId
	 *            Set id of the view on which fragment is to replaced
	 * @param fragment
	 *            fragment which is to called
	 * @param tag
	 *            Set tag if needed otherwise set null
	 * @param isBackStack
	 *            Set true if need backStack else false
	 */
	public final void replaceFragment(int replaceId, Fragment fragment,
			String tag, boolean isBackStack, Context activityContext) {
		FragmentTransaction ft = ((FragmentActivity) activityContext)
				.getSupportFragmentManager().beginTransaction();
		if (!checkIfStringIsNullOrEmpty(tag)) {
			ft.replace(replaceId, fragment, tag);
		} else {
			ft.replace(replaceId, fragment);
		}
		if (isBackStack) {
			ft.addToBackStack(tag);
		}
		// ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.commit();
	}

	/**
	 * This method is used to addFragment for the first time
	 * 
	 * @param replaceId
	 *            Set id of the view on which fragment is to replaced
	 * @param fragment
	 *            fragment which is to called
	 * @param tag
	 *            Set tag if needed otherwise set null
	 * @param isBackStack
	 *            Set true if need backStack else false
	 */
	public final void addFragment(int replaceId, Fragment fragment, String tag,
			boolean isBackStack, Context activityContext) {
		FragmentTransaction ft = ((FragmentActivity) activityContext)
				.getSupportFragmentManager().beginTransaction();
		if (!checkIfStringIsNullOrEmpty(tag)) {
			ft.add(replaceId, fragment, tag);
		} else {
			ft.add(replaceId, fragment);
		}
		if (isBackStack) {
			ft.addToBackStack(tag);
		}
		// ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.commit();
	}

	/**
	 * This method is used to popFragment from stack
	 */
	public final void popFragment(int replaceId, Context activityContext) {

		if (((FragmentActivity) activityContext).getSupportFragmentManager()
				.getBackStackEntryCount() > 0) {
			FragmentTransaction fragTrans = ((FragmentActivity) activityContext)
					.getSupportFragmentManager().beginTransaction();
			fragTrans.remove(((FragmentActivity) activityContext)
					.getSupportFragmentManager().findFragmentById(replaceId));
			fragTrans.commit();
			((FragmentActivity) activityContext).getSupportFragmentManager()
					.popBackStackImmediate();
		} else {
			((Activity) activityContext).finish();
			// if (((Activity) activityContext) instanceof
			// CommonHomeScreenActivity) {
			// BaseActivityClass.isRunning = false;
			// }
		}
	}

	/**
	 * This method is used to clear all the fragments from stack
	 */
	public final void clearBackStack(Context activityContext) {
		try {

			FragmentManager fm = ((FragmentActivity) activityContext)
					.getSupportFragmentManager();
			for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
				String fragTag = fm.getBackStackEntryAt(i).getName();
				Fragment fragment = fm.findFragmentByTag(fragTag);
				FragmentTransaction fragTrans = ((FragmentActivity) activityContext)
						.getSupportFragmentManager().beginTransaction();
				fragTrans.remove(fragment);
				fragTrans.commit();
				fm.popBackStack();

			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public boolean checkIfStringIsNullOrEmpty(String value) {
		boolean isnullorEmpty = false;
		if (TextUtils.equals(value, null)) {
			isnullorEmpty = true;
		} else if (TextUtils.equals(value, "")) {
			isnullorEmpty = true;
		} else if (TextUtils.equals(value, "null")) {
			isnullorEmpty = true;
		} else if (TextUtils.equals(value, "[]")) {
			isnullorEmpty = true;
		}

		return isnullorEmpty;
	}

	public static String DECRYPTION_PUBLIC_KEY = "abcuniverse";

	public void postJsonObjectWebserviceHit(String url, String[] key,
			String[] value, int id, boolean isDialog,
			boolean isSendingSessionId,
			AndroidWebResponseTask mWebserviceAsnycInstance) {
		if (isSendingSessionId) {
			// mWebserviceAsnycInstance.postJsonObject(url, key, value, id,
			// isDialog, UrlConstants.SessionRequestHeaderKey,
			// LoginDataModel.getLoginModelInstance().getmSessionId());
		} else {

			// mWebserviceAsnycInstance.postJsonObject(url, key, value, id,
			// isDialog);
		}
	}

	public void getUrlHit(String Url, AndroidWebResponseTask webserviceAsync,
			int callBackId) {
		webserviceAsync.get(Url, callBackId, true);
	}

	public void postJsonArraytWebserviceHit(String url, String jsonArray,
			int id, boolean isDialog,
			AndroidWebResponseTask mWebserviceAsnycInstance) {
		mWebserviceAsnycInstance.postJsonArray(url, jsonArray, id, isDialog);
	}

	public void showDatePickerDialog(
			final DatePickerdialogListener datePickerListener,
			final int callBackId) {
		// UrlConstants.isShowingDialog = true;
		Calendar calender = Calendar.getInstance();
		OnDateSetListener onDateChangeListener = new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// UrlConstants.isShowingDialog = false;
				datePickerListener.onDateChanged(year, monthOfYear, dayOfMonth,
						callBackId);
			}

		};
		DatePickerDialog datePicker = new DatePickerDialog(context,
				onDateChangeListener, calender.get(Calendar.YEAR),
				calender.get(Calendar.MONTH),
				calender.get(Calendar.DAY_OF_MONTH));
		// datePicker.setCancelable(false);
		datePicker.show();
		datePicker.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				// UrlConstants.isShowingDialog = false;
			}
		});
	}

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

	public void ShowTimePicker(
			final OnTimePickerDialogListener timePickerListener,
			final int callBackId) {
		// UrlConstants.isShowingDialog = true;
		Calendar calender = Calendar.getInstance();
		OnTimeSetListener onTimeChangeListener = new OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				// UrlConstants.isShowingDialog = false;
				String in12Hours = convert24hrTo12hr(hourOfDay, minute);
				timePickerListener.onTimeChanged(hourOfDay, minute, in12Hours,
						callBackId);
			}
		};
		try {
			if (timePicker != null) {
				timePicker.cancel();
				timePicker = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (timePicker == null) {
			timePicker = new TimePickerDialog(context, onTimeChangeListener,
					calender.HOUR_OF_DAY, calender.MINUTE, true);
			// datePicker.setCancelable(false);
		}
		timePicker.show();

		timePicker.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				// UrlConstants.isShowingDialog = false;
			}
		});

	}

	public interface OnTimePickerDialogListener {
		public void onTimeChanged(int hourOfDay, int minute,
                                  String formattedValue, int callBackId);
	}

	public interface DatePickerdialogListener {

		public void onDateChanged(int year, int monthOfYear, int dayOfMonth,
                                  int callBackId);
	}

	public void showSpinnerDialog(ArrayAdapter<String> listAdapter,
			String title,
			final OnSpinnerDialogListener onSpinnerDialogListener,
			final int callbackId) {
		new AlertDialog.Builder(context).setTitle(title)
				.setAdapter(listAdapter, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						onSpinnerDialogListener.onSpinnerDialogClick(which,
								callbackId);
						dialog.dismiss();
					}
				}).create().show();

	}

	public void showSpinnerDialogCustom(ArrayAdapter<String> listAdapter,
			String title,
			final OnSpinnerDialogListener onSpinnerDialogListener,
			final int callbackId) {
		final Dialog alert = new Dialog(context);
		alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alert.setContentView(R.layout.localsource_list_inflated_view);
		TextView msg = (TextView) alert.findViewById(R.id.dialog_title);
		msg.setText(title);
		ListView list = (ListView) alert
				.findViewById(R.id.localsource_listview);
		list.setAdapter(listAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onSpinnerDialogListener.onSpinnerDialogClick(position,
						callbackId);
				alert.dismiss();

			}

		});
		alert.show();

	}

	public void showSpinnerDialog1(ArrayAdapter<?> listAdapter, String title,
			final OnSpinnerDialogListener onSpinnerDialogListener,
			final int callbackId) {

		new AlertDialog.Builder(context).setTitle(title)
				.setAdapter(listAdapter, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						onSpinnerDialogListener.onSpinnerDialogClick(which,
								callbackId);
						dialog.dismiss();
					}
				}).create().show();

	}

	private Pattern pattern;
	private Matcher matcher;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * Validate hex with regular expression
	 * 
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public boolean isEmailValid(final String hex) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(hex);
		return matcher.matches();

	}

	public interface OnSpinnerDialogListener {
		void onSpinnerDialogClick(int position, int callBackId);

	}

}
