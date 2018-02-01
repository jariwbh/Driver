package com.tms.driver;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Base64InputStream;
import android.view.View;

import com.tms.driver.async.AndroidWebResponseTask;
import com.tms.driver.base.BaseActivityClass;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.models.UserLoginModel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.List;

public class GCMDialog_Activity extends BaseActivityClass {
	private SharedPreferences mSharedprefrences;
	private SharedPreferences.Editor editPrefs;
	
	public static boolean mIsInForeGround=false;
	
	public static Activity activity; 
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mIsInForeGround=true;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mIsInForeGround=false;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.gcm_dialogactivity);
		
		activity=this;
		
		/*if(OnlineStatusActivity.activity!=null){
			
			OnlineStatusActivity.activity.finish();
		}*/
		String message = null;
		mSharedprefrences = getSharedPreferences(
				TaxiExConstants.SharedPreferenceName, 0);
		editPrefs = mSharedprefrences.edit();
		if (getIntent() != null) {
			if (getIntent().hasExtra("message")) {
				message = getIntent().getStringExtra("message");
			}
			if (getIntent().hasExtra("messageType")) {
				String messageType = getIntent().getStringExtra("messageType");
				if (messageType.equalsIgnoreCase("logoutStatus")) {
					// message =
					// getResources().getString(R.string.app_lock_msg);
					ShowSingleButtonDialog(null, message,
							getString(R.string.alert_ok_button), 0, this, 1);
				} else if (messageType.equalsIgnoreCase("cancel")) {
					// message =
					// getIntent().getStringExtra("reminder_notification");
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
						base64InputStream.close();
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
					AndroidWebResponseTask.getInstance(this).setCallback(this);
					
					
					
					
					// postXMl(TaxiExConstants.DriverStatusOfflineUrl,
					// createXml(TaxiExConstants.DriverStatusXMLKeys,
					// new String[] {
					// "",
					// UserLoginModel.getInstance()
					// .getId() }), false,
					// TaxiExConstants.DriverStatusWebserviceResponse,
					// this);
					ShowSingleButtonDialog(null, message,
							getString(R.string.alert_ok_button), 0, this, 2);
				}
			}
		}
	}

	@Override
	public void onBackPress(int id) {
		// TODO Auto-generated method stub
		//super.onBackPress(id);
		switch (id) {
		case 1:
			editPrefs.clear();
			editPrefs.commit();
			finishAffinity();
			startActivity(new Intent(GCMDialog_Activity.this,
					MainActivity.class)	.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
			finish();
			break;
		case 2:
			 //finishAffinity();

			/*startActivity(new Intent(GCMDialog_Activity.this,
					OnlineStatusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));*/
		//	.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));

			break;
		default:
			break;
		}
		//finish();
	}

	@Override
	public void onPositiveButtonClick(int id) {
		// TODO Auto-generated method stub
		super.onPositiveButtonClick(id);

		switch (id) {



		case 1:
			editPrefs.clear();
			editPrefs.commit();
			finishAffinity();
			startActivity(new Intent(GCMDialog_Activity.this,
					MainActivity.class)	.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
			break;

		case 2:

			// finishAffinity();


			OnlineStatusActivity.mBookingDetailsRequestModel=null;
			startActivity(new Intent(GCMDialog_Activity.this,
					OnlineStatusActivity.class)
			.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
			break;
		default:
			break;
		}
	}

	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		super.onSuccess(response, UNIQUE_ID);
		switch (UNIQUE_ID) {
		case TaxiExConstants.DriverStatusWebserviceResponse:

			editPrefs.putBoolean(TaxiExConstants.Pref_isOnline, false);
			editPrefs.commit();
			// finishAffinity();
			// finishAffinity();
			// startActivity(new Intent(GCMDialog_Activity.this,
			// DutyStatusActivity.class));
			break;

		default:
			break;
		}
	}

	//
	// @SuppressWarnings("deprecation")
	// private void showAlertDialog(String message, final boolean isAppLock) {
	// AlertDialog.Builder b = new AlertDialog.Builder(this);
	// final AlertDialog a = b.create();
	// a.setTitle("");
	// a.setMessage(message);
	// a.setCancelable(false);
	// a.setButton("OK", new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface arg0, int arg1) {
	//
	// a.dismiss();
	// if (isAppLock) {
	// finishAffinity();
	// } else {
	// moveTaskToBack(true);
	//
	// }
	// }
	// });
	// a.show();
	//
	// }

	@Override
	public void onParsingSuccessful(String response, int callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onParsingSuccessful(HashMap<String, List<?>> modelList,
			String response, int callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onParsingError(String errorMessages, int callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onParsingSuccessful(Object modelClass, int callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(String[] response, int UNIQUE_ID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		//finish();
	}
}