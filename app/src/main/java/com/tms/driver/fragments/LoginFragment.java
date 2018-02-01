package com.tms.driver.fragments;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;
import com.tms.driver.DutyStatusActivity;
import com.tms.driver.fragments.ForgotPasswordFragment;
import com.tms.driver.fragments.TermsandConditionsFragment;
import com.tms.driver.R;
import com.tms.driver.SplashActivity;
import com.tms.driver.base.BaseFragment;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.constant.TaxiExConstants.FRAGMENT_TAGS;
import com.tms.driver.customviews.CustomEditText;
import com.tms.driver.customviews.CustomTextView;

import com.tms.driver.models.UserLoginModel;
import com.tms.driver.models.VehicleListModel;
import com.tms.driver.parse.ParseContent;
import com.tms.driver.parse.ParseContent.CONTENT_TYPE;
import com.tms.driver.utilities.FONTS;
import com.tms.driver.utilities.Utilities;
import com.google.android.gms.gcm.GoogleCloudMessaging;


/**
 * 
 * @author arvind.agarwal
 * 
 * this fragment represents Login view 
 * 
 * Fields username/emailid and password
 *
 */

public class LoginFragment extends BaseFragment {
	private int ALERT_EMAIL_BLANK = 1, ALERT_INVALID_EMAIL = 2,
			ALERT_PASSWORD_BLANK = 3;
	CustomEditText mEmailEditText, mPasswordEditText;
	CustomTextView mForgotPasswordTextView;
	Button mLoginButton;
	View mLoginView;
	private SharedPreferences mSharedprefrences, mSharedPreferences1;
	private SharedPreferences.Editor editPrefs, editPrefs1;
	private boolean mIsFirstTimeLogin = false;
	
	//defining typefaces
	private Typeface typeFace, typeFaceForgot;
//	private GoogleCloudMessaging gcm;
	private String regid;

	@Override
	public View setContentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		
		

			mLoginView = inflater.inflate(R.layout.frag_login, null);
			// Utilities.getInstance(getActivity()).showHideKeyboard(true);
			initializeComponents();


		return mLoginView;
	}

	
	
	/**
	 * initializing view
	 * 
	 * 
	 */
	private void initializeComponents() {
		typeFace = Typeface.createFromAsset(getActivity().getAssets(), FONTS.TYPE_FACE);
		typeFaceForgot = Typeface.createFromAsset(getActivity().getAssets(),
				FONTS.TYPE_FACE_FORGOT);
		((CustomTextView)mLoginView. findViewById(R.id.txtInfo)).setTypeface(typeFaceForgot);
		((CustomTextView) mLoginView.findViewById(R.id.title)).setTypeface(typeFace);
		
	
		mSharedprefrences = getActivity().getSharedPreferences(
				TaxiExConstants.SharedPreferenceName, 0);
		editPrefs = mSharedprefrences.edit();
		mSharedPreferences1 = getActivity().getSharedPreferences(
				TaxiExConstants.SharedPreferenceName1, 0);
		editPrefs1 = mSharedPreferences1.edit();
		mEmailEditText = (CustomEditText) mLoginView
				.findViewById(R.id.login_screen_email_edittext);

		mPasswordEditText = (CustomEditText) mLoginView
				.findViewById(R.id.login_screen_password_edittext);
		mForgotPasswordTextView = (CustomTextView) mLoginView
				.findViewById(R.id.login_screen_frgt_pswd_text);
		mForgotPasswordTextView.setTypeface(typeFaceForgot);
		mLoginButton = (Button) mLoginView
				.findViewById(R.id.login_screen_login_button);
		mTaxiExWebLinkTextView = (CustomTextView) mLoginView
				.findViewById(R.id.frag_login_web_link);
		mTaxiExWebLinkTextView.setOnClickListener(this);
		mForgotPasswordTextView.setOnClickListener(this);
		mLoginButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.frag_login_web_link:
			
			/**
			 * connects to TaxiMobile solution web
			 */
			
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://taximobilesolutions.com"));
			startActivity(intent);
			break;
		case R.id.login_screen_frgt_pswd_text:
			
			
			/*
			 * 
			 * takes to forgot password screen
			 */
			replaceFragment(R.id.acitivity_main_fragment,
					new ForgotPasswordFragment(),
					FRAGMENT_TAGS.FORGOTPASSWORDFRAGMENT.name(), true,
					getActivity());
			break;

		case R.id.login_screen_login_button:
			
			
			
			submitLoginCredentials();
			break;

		default:
			break;
		}

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//Hiding keyboard
		Utilities.getInstance(getActivity()).showHideKeyboard(false);
	}

	private CustomTextView mTaxiExWebLinkTextView;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
//		gcm = GoogleCloudMessaging.getInstance(getActivity());
		regid = mSharedprefrences.getString(TaxiExConstants.Pref_GCM_key,
				"");
		Log.i("Splash", "reg Id = " + regid);
		/**
		 * Check if application is already registeed with GCM.
		 */
		if (TextUtils.isEmpty(regid)) {
			regid = FirebaseInstanceId.getInstance().getToken();
			if(regid!=null && !regid.equals("")){
				editPrefs1.putString(TaxiExConstants.Pref_GCM_key, regid);
				editPrefs1.commit();
				editPrefs.putString(TaxiExConstants.Pref_GCM_key, regid);
				editPrefs.commit();
			}
//			registerInBackground();
		} else {

			
		}
		
		/**
		 * getiing email id if it is saved in Shared prefernces
		 */
		mEmailEditText.requestFocus();
		Utilities.getInstance(getActivity()).showHideKeyboard(true);
		if (mSharedprefrences.contains(TaxiExConstants.Pref_login_username)) {
			mEmailEditText.setText(mSharedprefrences.getString(
					TaxiExConstants.Pref_login_username, ""));
		} else {
			mEmailEditText.setText("");

		}
		mPasswordEditText.setText("");
	}
	
	/**
	 * This method posts user credentials to server, necessary for login
	 * 
	 */

	private void submitLoginCredentials() {
		
		
		
		

		String emailAddress = mEmailEditText.getText().toString().trim();
		String pswd = mPasswordEditText.getText().toString().trim();

		if (!checkIfStringIsNullOrEmpty(emailAddress)) {
			if (isEmailValid(emailAddress)) {
				if (!checkIfStringIsNullOrEmpty(pswd)) {
					
					/*if (getActivity().getIntent() != null) {
						try {

							mIsFirstTimeLogin = getActivity().getIntent().getExtras()
									.getBoolean(TaxiExConstants.FirsTimeLoginBundleKey);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}*/
					
					
					if (mSharedprefrences.getBoolean(TaxiExConstants.Pref_isFIrstTime, true)) {
						
						
						TermsandConditionsFragment termsFrag=new TermsandConditionsFragment();
						
					Bundle bundle=new Bundle();
					
					bundle.putString(TaxiExConstants.USER_NAME_INTENT, emailAddress);
					bundle.putString(TaxiExConstants.USER_PASSWORD_INTENT, pswd);
						
						termsFrag.setArguments(bundle);
						
						replaceFragment(R.id.acitivity_main_fragment,
								termsFrag,
								FRAGMENT_TAGS.TERMSANDCONDITION_FRAGMENT.name(), true,
								getActivity());

					} else {
						
						String[] keys = new String[] { "userLogin", "email",
								"password", "deviceToken", "appVersion",
								"deviceType" };
						String[] values = new String[] {
								"",
								emailAddress,
								pswd,
								mSharedPreferences1.getString(
										TaxiExConstants.Pref_GCM_key, ""), "1", "2" };
						// ShowSingleButtonDialog(null, createXml(keys, values),
						// getString(R.string.alert_ok_button), 0, this,
						// ALERT_PASSWORD_BLANK);

						postXMl(TaxiExConstants.LoginURl, createXml(keys, values),
								true, ALERT_PASSWORD_BLANK, this);
						
					
					}
					
					
					
					

				} else {

					ShowSingleButtonDialog(null,
							getString(R.string.alert_password_left_blank),
							getString(R.string.alert_ok_button), 0, this,
							ALERT_PASSWORD_BLANK);
				}
			} else {
				ShowSingleButtonDialog(null,
						getString(R.string.alert_email_not_valid),
						getString(R.string.alert_ok_button), 0, this,
						ALERT_INVALID_EMAIL);
			}
		} else {
			ShowSingleButtonDialog(null,
					getString(R.string.alert_email_left_blank),
					getString(R.string.alert_ok_button), 0, this,
					ALERT_EMAIL_BLANK);

		}

	}

	@Override
	public void onFailure(Throwable e, String error, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		ShowSingleButtonDialog(null, error,
				getString(R.string.alert_ok_button), 0, this, 1);
	}

	@Override
	public void onPositiveButtonClick(int id) {
		// TODO Auto-generated method stub
		super.onPositiveButtonClick(id);

	}

	@Override
	public void getServerValues(String response, int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setServerError(int id, String msg) {
		// TODO Auto-generated method stub

	}
	
	
	/**
	 * This interface parsed  response from server and saves user info in share preferences
	 * 
	 * 
	 */

	@Override
	public void onParsingSuccessful(String response, int callback) {
		// TODO Auto-generated method stub
		if (response.equalsIgnoreCase("Login Successful.")) {
			
			if (getActivity().getIntent() != null) {
				try {

					mIsFirstTimeLogin = getActivity().getIntent().getExtras()
							.getBoolean(TaxiExConstants.FirsTimeLoginBundleKey);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			try {
				editPrefs.putBoolean(TaxiExConstants.Pref_is_login, true);
				editPrefs.putString(TaxiExConstants.Pref_login_username,
						mEmailEditText.getText().toString().trim());
				
				editPrefs.commit();
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				
				
				// data is saved in the byte array

				ObjectOutputStream objectOutput;
				try {
					objectOutput = new ObjectOutputStream(arrayOutputStream);
					objectOutput.writeObject(UserLoginModel.getInstance());
					byte[] data = arrayOutputStream.toByteArray();
					objectOutput.close();
					arrayOutputStream.close();

					ByteArrayOutputStream out = new ByteArrayOutputStream();
					Base64OutputStream b64 = new Base64OutputStream(out,
							Base64.DEFAULT);
					b64.write(data);
					b64.close();
					out.close();

					
					// saving driver info in shared preference
					editPrefs.putString(TaxiExConstants.Pref_Login_Details,
							new String(out.toByteArray()));

					editPrefs.commit();

				} catch (IOException e) {
					e.printStackTrace();
				}
				if(VehicleListModel.getInstance().getId()!=null){
					
					upDateTaxiInfo();
					}
				
				
				
				// mCarSelectedImageView.
			} catch (Exception e) {
				e.printStackTrace();

			}
		/*	if (mIsFirstTimeLogin) {
				replaceFragment(R.id.acitivity_main_fragment,
						new TermsandConditionsFragment(),
						FRAGMENT_TAGS.TERMSANDCONDITION_FRAGMENT.name(), true,
						getActivity());

			} else {*/
				getActivity().finish();

				getActivity().startActivity(
						new Intent(getActivity(), DutyStatusActivity.class));
//			}

		} else {
			ShowSingleButtonDialog(null, response,
					getString(R.string.alert_ok_button), 0, this,
					ALERT_PASSWORD_BLANK);
			Log.e("Login Screen Model data", UserLoginModel.getInstance()
					.getId());
		}
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

	
	
	/**
	 * 
	 * this interfaces catches data from webservice hit 
	 * 
	 * and further data is parsed
	 */
	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		super.onSuccess(response, UNIQUE_ID);

		try {
			ParseContent.getInstance().parseContentFromjson(CONTENT_TYPE.LOGIN,
					new JSONObject(response), this, ALERT_PASSWORD_BLANK);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onSuccess(String[] response, int UNIQUE_ID) {
		// TODO Auto-generated method stub

	}
	
	
	
	/**
	 * this method is used to save vehicle details in shared preference
	 */
	private void upDateTaxiInfo(){
		
		try {
			
			ByteArrayOutputStream arrayOutputStream1 = new ByteArrayOutputStream();

			ObjectOutputStream objectOutput1 = null;
			try {
				objectOutput1 = new ObjectOutputStream(arrayOutputStream1);
				objectOutput1.writeObject(VehicleListModel.getInstance());
				byte[] data = arrayOutputStream1.toByteArray();
				objectOutput1.close();
				arrayOutputStream1.close();

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				Base64OutputStream b64 = new Base64OutputStream(out,
						Base64.DEFAULT);
				b64.write(data);
				b64.close();
				out.close();
				
				// saving vehicle details in shared preference

				editPrefs.putString(TaxiExConstants.Pref_selected_vehicle,
						new String(out.toByteArray()));

				editPrefs.commit();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// mCarSelectedImageView.
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	// Registers the application with GCM servers asynchronously.

//	private void registerInBackground() {
//			new AsyncTask<Void, Void, String>() {
//				@Override
//				protected String doInBackground(Void... params) {
//					String msg = "";
//					try {
//						if (gcm == null) {
//							gcm = GoogleCloudMessaging
//									.getInstance(getActivity());
//						}
//						regid = gcm.register(SplashActivity.SENDER_ID);
//						msg = "Device registered, registration ID=" + regid;
//
//					} catch (IOException ex) {
//						msg = "Error :" + ex.getMessage();
//
//					}
//					return regid;
//				}
//
//				@Override
//				protected void onPostExecute(String msg) {
//					Log.i("Splash", "Token = " + msg + "reg Id = " + regid);
//					editPrefs1.putString(TaxiExConstants.Pref_GCM_key, regid);
//					editPrefs1.commit();
//					editPrefs.putString(TaxiExConstants.Pref_GCM_key, regid);
//					editPrefs.commit();
//
//				}
//			}.execute();
//		}
//

}
