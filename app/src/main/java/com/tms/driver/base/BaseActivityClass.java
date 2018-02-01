package com.tms.driver.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.EditText;

import com.tms.driver.ApplicationContext;
import com.tms.driver.async.AndroidWebResponseTask;
import com.tms.driver.async.AndroidWebResponseTask.WebResponse;
import com.tms.driver.async.WebServiceAsync.OnWebServiceProcess;
import com.tms.driver.interfaces.OnAlertDialogFragmentListener;
import com.tms.driver.parse.ParseContent.onParsingContent;
import com.tms.driver.utilities.Utilities;

/**
 * BaseActivity Class extends from Activity. All the Activities will extend this
 * baseclass to use the methods implemented in it.
 * 
 * @author siddharth.brahmi
 * 
 */
public abstract class
		BaseActivityClass extends FragmentActivity implements
		OnWebServiceProcess, onParsingContent, OnAlertDialogFragmentListener,
		OnClickListener, WebResponse, OnFocusChangeListener, OnTouchListener

{
	@Override
	public void onBackPress(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (v instanceof EditText) {
			// Do nothing
		} else {
			Utilities.getInstance(this).showHideKeyboard(false);
		}

	}

	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mPrefseditor;

	@Override
	protected void onStart() {

		// applicationWillEnterForeground();

		super.onStart();
	}

	@Override
	protected void onStop() {

		super.onStop();

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		boolean isScreenOn = pm.isScreenOn();
		if (!isScreenOn) {
			// logoutSession();
		}

	}

	BroadcastReceiver powerOffBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i("PowerOff", "intent = " + intent.getAction());
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				// logoutSession();
			}
		}
	};

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		
	
	}

	public boolean checkIfStringIsNullOrEmpty(String value) {
		return mTaxiExUtilities.checkIfStringIsNullOrEmpty(value);
	}

	public Intent mFocusIntent;
	public static String ACTIVITY_IN_BACKGROUND = "set_to_background";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		try {

			View view = getWindow().getDecorView();

			view.setOnTouchListener(this);
		} catch (Exception e) {
			// TODO: handle exception
		}

		ApplicationContext applicationContext = (ApplicationContext) getApplicationContext();
		applicationContext.setActivityContext(this);
		mTaxiExUtilities = Utilities.getInstance(this);
		mWebserviceAsnycInstance = AndroidWebResponseTask.getInstance(this);
		mWebserviceAsnycInstance.setCallback(this);
		mSharedPreferences = getSharedPreferences("abcshared", MODE_PRIVATE);
		mPrefseditor = mSharedPreferences.edit();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ApplicationContext applicationContext = (ApplicationContext) getApplicationContext();
		applicationContext.setActivityContext(this);
	}

	public AndroidWebResponseTask mWebserviceAsnycInstance;
	private Utilities mTaxiExUtilities;

	public void postJsonObjectWebserviceHit(String url, String[] key,
			String[] value, int id, boolean isDialog, boolean isSendingSessionId) {
		mTaxiExUtilities.postJsonObjectWebserviceHit(url, key, value, id,
				isDialog, isSendingSessionId, mWebserviceAsnycInstance);

	}

	public void postJsonArraytWebserviceHit(String url, String jsonArray,
			int id, boolean isDialog) {
		mTaxiExUtilities.postJsonArraytWebserviceHit(url, jsonArray, id,
				isDialog, mWebserviceAsnycInstance);
	}

	public void showToast(String message, int duration) {
		mTaxiExUtilities.showToast(message, duration);
	}

	public void ShowSingleButtonDialog(String title, String msg,
			String button1, int iconResId,
			OnAlertDialogFragmentListener listener, int activityId) {
		mTaxiExUtilities.showSingleOption_AlertDialog(title, msg, button1,
				iconResId, listener, activityId);
	}

	public void ShowDoubleButtonDialog(String title, String msg,
			String button1, String button2, int iconResId,
			OnAlertDialogFragmentListener listener, int activityId) {
		mTaxiExUtilities.showDoubleOption_AlertDialog(title, msg, button1,
				button2, iconResId, listener, activityId);
	}

	public void ShowThreeButtonDialog(String title, String msg, String button1,
			String button2, String button3, int iconResId,
			OnAlertDialogFragmentListener listener, int activityId) {
		mTaxiExUtilities.showThreeOption_AlertDialog(title, msg, button1,
				button2, button3, iconResId, listener, activityId);
	}
	
	public void HideDialog(){
		mTaxiExUtilities.hideAlertDialog();
	}

	public void replaceFragment(int replaceId, Fragment fragment,
			String fragmentTag, boolean isBackStack, Context activityContext) {

		mTaxiExUtilities.replaceFragment(replaceId, fragment, fragmentTag,
				isBackStack, activityContext);
	}

	public void addFragment(int replaceId, Fragment fragment,
			String fragmentTag, boolean isBackStack, Context activityContext) {
		mTaxiExUtilities.addFragment(replaceId, fragment, fragmentTag,
				isBackStack, activityContext);
	}

	public void popFragment(int replaceId, Context activityContext) {
		mTaxiExUtilities.popFragment(replaceId, activityContext);
	}

	public void clearBackStack(Context activityContext) {
		mTaxiExUtilities.clearBackStack(activityContext);
	}

	public void postXMl(String Url, String data, boolean isDialog, int Id,
			WebResponse context) {
		mWebserviceAsnycInstance.setCallback(context);
		mTaxiExUtilities.postXml(Url, data, isDialog, Id,
				mWebserviceAsnycInstance);
	}

	public String createXml(String[] keys, String[] values) {
		return mTaxiExUtilities.createXML(keys, values);
	}

	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(Throwable e, String error, int UNIQUE_ID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPositiveButtonClick(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNegativeButtonClick(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNeutralizeButtonClick(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void interfaceAttachError(int fragmentId, String errorResponse) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getServerValues(String response, int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setServerError(int id, String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		// isLoginActivityCall = true;
		super.onPause();

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			mTaxiExUtilities.showHideKeyboard(false);
			break;

		default:
			break;
		}
		return true;
	}
}
