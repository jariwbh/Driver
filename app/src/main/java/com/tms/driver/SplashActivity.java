package com.tms.driver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.crittercism.app.Crittercism;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.models.UserLoginModel;
import com.tms.driver.utilities.Utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;


/**
 * 
 * @author arvind.agarwal
 *this class is splash screen 
 *gcm id generation,  and navigation to inner screens 
 */
public class SplashActivity extends Activity {
	private GoogleCloudMessaging gcm;
	private SharedPreferences mSharedprefrences, mSharedPreferences1;
	private SharedPreferences.Editor editPrefs, editPrefs1;
	private String regid;
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	
//	public static final String SENDER_ID="62597439953"; 	
	public static final String SENDER_ID="468143498678";
	private boolean mIsFirstTimeLogin = false;
	Dialog alertDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		Utilities.getInstance(this).showHideKeyboard(false);
		mSharedprefrences = getSharedPreferences(
				TaxiExConstants.SharedPreferenceName, 0);
		editPrefs = mSharedprefrences.edit();
		mSharedPreferences1 = getSharedPreferences(
				TaxiExConstants.SharedPreferenceName1, 0);
		editPrefs1 = mSharedPreferences1.edit();
		
		Crittercism.initialize(getApplicationContext(), "554c602d7365f84f7d3d718b");

		/**
		 * Check If play services are available in the device.
		 */

//		if (checkPlayServices()) {
//			gcm = GoogleCloudMessaging.getInstance(this);
//			regid = mSharedprefrences.getString(TaxiExConstants.Pref_GCM_key,
//					"");
//			Log.i("Splash", "reg Id = " + regid);
//			/**
//			 * Check if application is already registeed with GCM.
//			 */
//			if (TextUtils.isEmpty(regid)) {
//				mIsFirstTimeLogin = true;
//				registerInBackground();
//			} else {
//
//				navigateScreen();
//			}
//		}

		if (android.os.Build.VERSION.SDK_INT >= 23) {
			if ((ContextCompat.checkSelfPermission(SplashActivity.this,
					"android.permission.ACCESS_COARSE_LOCATION") != PackageManager.PERMISSION_GRANTED)
					&& (ContextCompat.checkSelfPermission(SplashActivity.this,
					"android.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED)
					&& (ContextCompat.checkSelfPermission(SplashActivity.this,
					"android.permission.WRITE_CALENDAR") != PackageManager.PERMISSION_GRANTED)) {
				AskRuntimePermission(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION","android.permission.WRITE_CALENDAR"}, 101, SplashActivity.this);
			}else{
				if(checkPlayServices()){
					regid = mSharedprefrences.getString(TaxiExConstants.Pref_GCM_key,
							"");
					if (TextUtils.isEmpty(regid)) {
						regid = FirebaseInstanceId.getInstance().getToken();
						if(regid==null || TextUtils.isEmpty(regid)){
							mIsFirstTimeLogin = true;
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									navigateScreen();
								}
							},1000);
						}
					} else {
						navigateScreen();
					}
				}
			}
		}else{
			if(checkPlayServices()){
				regid = mSharedprefrences.getString(TaxiExConstants.Pref_GCM_key,
						"");
				if (TextUtils.isEmpty(regid)) {
					regid = FirebaseInstanceId.getInstance().getToken();
					if(regid==null || TextUtils.isEmpty(regid)){
						mIsFirstTimeLogin = true;
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								navigateScreen();
							}
						},1000);
					}
				} else {
					navigateScreen();
				}
			}
		}



		// navigateScreen();
		// TODO Auto-generated method stub
	}

	public static void AskRuntimePermission(String[] permission, Integer requestCode, Activity activity) {
		if (ContextCompat.checkSelfPermission(activity,
				permission[0]) != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(
					activity, permission[0])) {
				ActivityCompat.requestPermissions(activity,
						new String[]{
								"android.permission.ACCESS_COARSE_LOCATION",
								"android.permission.ACCESS_FINE_LOCATION",
								"android.permission.WRITE_CALENDAR"
						}, requestCode);
			} else {
				ActivityCompat.requestPermissions(activity,
						new String[]{
								"android.permission.ACCESS_COARSE_LOCATION",
								"android.permission.ACCESS_FINE_LOCATION",
								"android.permission.WRITE_CALENDAR"
						}, requestCode);
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 101) {
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				if(checkPlayServices()){
					regid = mSharedprefrences.getString(TaxiExConstants.Pref_GCM_key,
							"");
					if (TextUtils.isEmpty(regid)) {
						regid = FirebaseInstanceId.getInstance().getToken();
						if(regid==null || TextUtils.isEmpty(regid)){
							mIsFirstTimeLogin = true;
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									navigateScreen();
								}
							},1000);
						}
					} else {
						navigateScreen();
					}
				}
			} else {
				SetPermissionDenyError();
			}

		}
	}

	private void navigateScreen() {
		new CountDownTimer(3000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				
				
				editPrefs1.putString(TaxiExConstants.Pref_GCM_key, regid);
				editPrefs1.commit();
				editPrefs.putString(TaxiExConstants.Pref_GCM_key, regid);
				editPrefs.commit();
				Intent intent = null;
			
				
				
				// if  user was online then navigate to OnLine status screen
				if (mSharedprefrences.getBoolean(TaxiExConstants.Pref_isUserOnLine,
						false)) {

					intent = new Intent(SplashActivity.this,
							OnlineStatusActivity.class);

				} else


					// if user was on trip, then navigate to Online status screen
				if (mSharedprefrences.getBoolean(TaxiExConstants.Pref_isOnline,
						false)) {

					intent = new Intent(SplashActivity.this,
							OnlineStatusActivity.class);

				} 
				// if user is already logged in, then navigate to Duty status screen
				else if (mSharedprefrences.getBoolean(
						TaxiExConstants.Pref_is_login, false)) {
					byte[] bytes = mSharedprefrences.getString(
							TaxiExConstants.Pref_Login_Details, "{}")
							.getBytes();
					if (bytes.length == 0) {
						return;
					}
					ByteArrayInputStream byteArray = new ByteArrayInputStream(
							bytes);
					Base64InputStream base64InputStream = new Base64InputStream(
							byteArray, Base64.DEFAULT);
					ObjectInputStream in;
					try {
						in = new ObjectInputStream(base64InputStream);
						// UserLoginModel loginModel =
						// UserLoginModel.getInstance();
						UserLoginModel.setInstance((UserLoginModel) in
								.readObject());
					} catch (StreamCorruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					intent = new Intent(SplashActivity.this,
							DutyStatusActivity.class);
				} 
				// if not login then navigate to login view
				else {
					intent = new Intent(SplashActivity.this, MainActivity.class);
					intent.putExtra(TaxiExConstants.FirsTimeLoginBundleKey,
							mIsFirstTimeLogin);

				}
				startActivity(intent);
				finish();
			}
		}.start();
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				finish();
			}
			return false;
		}
		return true;
	}


	// Registers the application with GCM servers asynchronously.

	private void SetPermissionDenyError() {
		if (alertDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
			builder.setTitle(SplashActivity.this.getResources().getString(R.string.gpsPermission));
			builder.setMessage(SplashActivity.this.getResources().getString(R.string.gpsError));
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialogInterface, int i) {
					// Show location settings when the user acknowledges the alert dialog
					alertDialog.dismiss();
					navigateScreen();
				}
			});
			builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialogInterface) {
					navigateScreen();
				}
			});
			alertDialog = builder.create();
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.show();
		}
	}

//	private void registerInBackground() {
//		new AsyncTask<Void, Void, String>() {
//			@Override
//			protected String doInBackground(Void... params) {
//				String msg = "";
//				try {
//					if (gcm == null) {
//						gcm = GoogleCloudMessaging
//								.getInstance(SplashActivity.this);
//					}
//					regid = gcm.register(SENDER_ID);
//					msg = "Device registered, registration ID=" + regid;
//
//				} catch (IOException ex) {
//					msg = "Error :" + ex.getMessage();
//
//				}
//				return regid;
//			}
//
//			@Override
//			protected void onPostExecute(String msg) {
//				Log.i("Splash", "Token = " + msg + "reg Id = " + regid);
//				editPrefs1.putString(TaxiExConstants.Pref_GCM_key, regid);
//				editPrefs1.commit();
//				editPrefs.putString(TaxiExConstants.Pref_GCM_key, regid);
//				editPrefs.commit();
//				navigateScreen();
//			}
//		}.execute();
//	}
}
