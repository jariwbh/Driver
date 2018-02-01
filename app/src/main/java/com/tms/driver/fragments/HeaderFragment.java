package com.tms.driver.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tms.driver.DutyStatusActivity;
import com.tms.driver.FCMNotification.FireBaseMessageService;
import com.tms.driver.GCMDialog_Activity;
import com.tms.driver.GcmIntentService;
import com.tms.driver.MainActivity;
import com.tms.driver.OnlineStatusActivity;
import com.tms.driver.R;
import com.tms.driver.adapters.LocalSourceAdapter;
import com.tms.driver.async.AndroidWebResponseTask;
import com.tms.driver.base.BaseFragment;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.constant.TaxiExConstants.FRAGMENT_TAGS;
import com.tms.driver.customviews.CustomButton;
import com.tms.driver.fragments.DutyStatusFragment;
import com.tms.driver.fragments.MyProfileFragment;
import com.tms.driver.fragments.ReservationHistory;
import com.tms.driver.fragments.ReservationListing;
import com.tms.driver.models.UserLoginModel;
import com.tms.driver.utilities.FONTS;
import com.tms.driver.utilities.Utilities;

public class HeaderFragment extends BaseFragment {

	View mHeaderFragmentView;
	TextView mHeadetTitle;
	public ImageView mLeftImageview, mRightImageView;
	private SharedPreferences mSharedprefrences;
	TextView mSaveImageTextView, mCancelImageTextView;
	private SharedPreferences.Editor editPrefs;
	OnlineStatusActivity onlineStatusActivity;
	private static final int Alert_logout_Admin = 123;
	private static final int Alert_Cancel_Booking = 124;
	List<String> options;
	private Typeface typeFace;

	@Override
	public View setContentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub


			mHeaderFragmentView = inflater.inflate(R.layout.frag_header, null);
			initializeComponents();


		return mHeaderFragmentView;
	}

	public void logoutFromApp(final String message) {
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					ShowSingleButtonDialog(null, message,
							getString(R.string.alert_ok_button), 0,
							HeaderFragment.this, Alert_logout_Admin);

				}
			});
		}

	}

	public void CancelBooking(final String message) {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ShowSingleButtonDialog(null, message,
						getString(R.string.alert_ok_button), 0,
						HeaderFragment.this, Alert_Cancel_Booking);
				try {

					// postXMl(TaxiExConstants.CancelBookinfUrl,
					// createXml(TaxiExConstants.CancelBookingXMLKeys,
					// new String[] { "",
					// UserLoginModel.getInstance().getId(),
					// onlineStatusActivity.mBookingId, "1",
					// "None of these", "3" }), true,
					// TaxiExConstants.CancelBookingWebserviceResponse, this);
					if (getActivity() instanceof OnlineStatusActivity) {
						OnlineStatusActivity onlineStatusActivity = (OnlineStatusActivity) getActivity();
						onlineStatusActivity.setUiOnCancelTrip();
					}
				} catch (Exception e) {

					e.printStackTrace();
				}

			}
		});
	}

	
	/**
	 * initializing views
	 */
	private void initializeComponents() {
		// TODO Auto-generated method stub
		
		
		
		typeFace=Typeface.createFromAsset(getActivity().getAssets(),FONTS.TYPE_FACE);
		
		
		mSharedprefrences = getActivity().getSharedPreferences(
				TaxiExConstants.SharedPreferenceName, 0);
		editPrefs = mSharedprefrences.edit();
		mHeadetTitle = (TextView) mHeaderFragmentView
				.findViewById(R.id.frag_header_title);
		mHeadetTitle.setTypeface(typeFace);
		mLeftImageview = (ImageView) mHeaderFragmentView
				.findViewById(R.id.frag_header_left_icon);
		mLeftImageview.setImageResource(R.drawable.selector_profile_button);
		mRightImageView = (ImageView) mHeaderFragmentView
				.findViewById(R.id.frag_header_right_icon);
		mSaveImageTextView = (TextView) mHeaderFragmentView
				.findViewById(R.id.frag_header_saveImage);
		mCancelImageTextView = (TextView) mHeaderFragmentView
				.findViewById(R.id.frag_header_cancel);
		mSaveImageTextView.setOnClickListener(this);
		mCancelImageTextView.setOnClickListener(this);
		mLeftImageview.setOnClickListener(this);
		mRightImageView.setOnClickListener(this);
		

	}
/*
 * (non-Javadoc)
 * this method is used right icon visibilty
 * 
 */
	public void setRightIconVisibility(int visibility) {
		mRightImageView.setVisibility(visibility);
	}

	
	/*
	 * (non-Javadoc)
	 * this method is used to left icon visibilty
	 * 
	 */
	public void setLeftIconVisibility(int visibility) {
		mLeftImageview.setVisibility(visibility);
	}

	
	public void setSaveButtonVisibility(int visibility) {
		mSaveImageTextView.setVisibility(visibility);
	}

	/*
	 * (non-Javadoc)
	 * this method is used to cancel button visibilty
	 * 
	 */
	
	
	public void setCancelButtonisibility(int visibility) {
		mCancelImageTextView.setVisibility(visibility);
	}

	
	/*
	 * (non-Javadoc)
	 * this method is used to right button imageresurce
	 * 
	 */
	public void setRightIcon(int drawable) {
		mRightImageView.setImageResource(drawable);
	}
	
	/*
	 * (non-Javadoc)
	 * this method is used to left button imageresurce
	 * 
	 */

	public void setLeftIcon(int drawable) {
		mLeftImageview.setImageResource(drawable);

	}
	
	
	/*
	 * (non-Javadoc)
	 * this method is used to title background imageresurce
	 * 
	 */
	public void setTitleBackGround(int drawable){
		mHeadetTitle.setBackgroundResource(drawable);
		mHeadetTitle.setText("");
		
	}
	
	

	@Override
	public void onFailure(Throwable e, String error, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		ShowSingleButtonDialog(null, error,
				getString(R.string.alert_ok_button), 0, this, 1);
	}

	/**
	 * handling webservice response
	 */
	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		super.onSuccess(response, UNIQUE_ID);
		switch (UNIQUE_ID) {
		
		case TaxiExConstants.CancelBookingHeaderWebserviceRedponse:
			
			
			editPrefs.remove(TaxiExConstants.pref_Booking_details);
			editPrefs.commit();
			
//			GcmIntentService.isBookingRequest = false;
			FireBaseMessageService.isBookingRequest = false;

			
			postXMl(TaxiExConstants.DriverStatusOfflineUrl,
					createXml(TaxiExConstants.DriverStatusXMLKeys,
							new String[] { "",
									UserLoginModel.getInstance().getId() }),
					true, TaxiExConstants.DriverStatusWebserviceResponse, this);
			
			break;
		
		
		case TaxiExConstants.DriverStatusWebserviceResponse:
			// when driver go online
			Log.e("Driver Status Response", response);
			if (getActivity() != null) {
				editPrefs.putBoolean(TaxiExConstants.Pref_isOnline, false);
				editPrefs.putBoolean(TaxiExConstants.Pref_isUserOnLine, false);
				editPrefs.commit();
				getActivity().finishAffinity();
				startActivity(new Intent(getActivity(),
						DutyStatusActivity.class));

			}
			break;
			
			// when driver log out
		case TaxiExConstants.LogoutWebserviceResponse:
			Log.e("Logout response", response);
			
			JSONObject jsonObject2;
			try {
				jsonObject2 = new JSONObject(response);
				int statusCode = jsonObject2
						.getInt("driverLogout");
				String message = jsonObject2.getString("message");

				
				if(statusCode == 1){
					String emailId="";
					
					if(mSharedprefrences.contains(TaxiExConstants.Pref_login_username)){
				 emailId=	mSharedprefrences.getString(
						TaxiExConstants.Pref_login_username, "");
					}
					
					editPrefs.clear();
					editPrefs.putString(TaxiExConstants.Pref_login_username, emailId);
				editPrefs.putBoolean(TaxiExConstants.Pref_isFIrstTime, false);
					editPrefs.commit();
					
					
					if (getActivity() != null) {

						getActivity().finishAffinity();
						startActivity(new Intent(getActivity(), MainActivity.class));
					}
				}else if(statusCode == -4){
					Intent i = new Intent(getActivity(),
							GCMDialog_Activity.class);
					i.putExtra("messageType", "cancel");
					i.putExtra("message", message);

					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TASK);

					startActivity(i);
				}else {
					Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
				}
			
				
			} catch (JSONException e1) {
				
				e1.printStackTrace();
			}
			
			
			
			break;
		case TaxiExConstants.CancelBookingWebserviceResponse:
			
			// when cancel booking
			
			if (!checkIfStringIsNullOrEmpty(response)) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					String message = jsonObject.getString("message");
					// ShowSingleButtonDialog(null, message,
					// getString(R.string.alert_ok_button), 0, this, 125);
					Log.e("Cancel Booking Response", response);

					if (TextUtils.equals(message, "Success.")) {
						if (getActivity() instanceof OnlineStatusActivity) {
							OnlineStatusActivity onlineStatusActivity = (OnlineStatusActivity) getActivity();
							onlineStatusActivity.setUiOnCancelTrip();

						}
					} else {
						ShowSingleButtonDialog(null, message,
								getString(R.string.alert_ok_button), 0, this,
								125);
					}
				} catch (Exception e) {
					e.printStackTrace();

				}

			}

			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		Utilities.getInstance(getActivity());
		switch (v.getId()) {

		case R.id.frag_header_saveImage:
			// if (getActivity() instanceof DutyStatusActivity) {
			// FragmentManager fragManager = getActivity()
			// .getSupportFragmentManager();
			// Fragment fragment = (Fragment) fragManager
			// .findFragmentById(R.id.acitivity_duty_status_frag);
			// if (fragment instanceof MyProfileFragment) {
			//
			// MyProfileFragment myProfileFrag = (MyProfileFragment) fragment;
			// myProfileFrag
			// .updateProfileImage(myProfileFrag.);
			//
			// } else {
			// popFragment(R.id.acitivity_duty_status_frag, getActivity());
			//
			// }
			// }
			break;
		case R.id.frag_header_cancel:
			break;

		case R.id.frag_header_left_icon:
			try {

				if (getActivity() instanceof OnlineStatusActivity) {// if OnlineStatus activity show driver's option
					onlineStatusActivity = (OnlineStatusActivity) getActivity();
					CustomButton startTripButton = (CustomButton) onlineStatusActivity
							.findViewById(R.id.frag_online_start_trip);
					if (startTripButton.getVisibility() == View.VISIBLE) {
						if (startTripButton
								.getText()
								.toString()
								.equalsIgnoreCase(
										getString(R.string.online_start_trip))
								|| startTripButton
										.getText()
										.toString()
										.equalsIgnoreCase(
												getString(R.string.online_arriving_now))) {
							// ShowDoubleButtonDialog("",
							// getString(R.string.alert_cancel_trip),
							// getString(R.string.alert_ok_button),
							// getString(R.string.alert_cancel_button), 0,
							// this, 5);
							
							
							// option for Driver to cancel trip
							options = new ArrayList<String>();
							options.add("Do not charge client");
							options.add("Client no show");
							options.add("Wrong Address");
							options.add("Client Requested Cancel");
							options.add("None of these");
							LocalSourceAdapter localSourceAdapter = new LocalSourceAdapter(
									getActivity(),
									R.layout.localsource_list_inflated_view,
									options);
							showCustomSpinner(
									localSourceAdapter,
									"Do you really want to cancel the request?",
									1);
							// showSpinnerDialog(
							// localSourceAdapter,
							// "Do you really want to cancel the request?",
							// 1);

						}
					} else {
						ShowDoubleButtonDialog("",
								getString(R.string.alert_offline),
								getString(R.string.alert_ok_button),
								getString(R.string.alert_cancel_button), 0,
								this, 1);
					}

				} else if (getActivity() instanceof DutyStatusActivity) {
					FragmentManager fragManager = getActivity()
							.getSupportFragmentManager();
					Fragment fragment = (Fragment) fragManager
							.findFragmentById(R.id.acitivity_duty_status_frag);
					if (fragment instanceof DutyStatusFragment) {

						
						
						// if present fragment is DutyStatusFragment, then add MyProfile Fragment
						replaceFragment(R.id.acitivity_duty_status_frag,
								new MyProfileFragment(),
								FRAGMENT_TAGS.MYPROFILEFRGMENT.name(), true,
								getActivity());

					} else {

						popFragment(R.id.acitivity_duty_status_frag,
								getActivity());

					}
				} else {
					popFragment(R.id.acitivity_duty_status_frag, getActivity());

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case R.id.frag_header_right_icon:
			if (getActivity() instanceof DutyStatusActivity) {
				FragmentManager fragManager = getActivity()
						.getSupportFragmentManager();
				Fragment fragment = (Fragment) fragManager
						.findFragmentById(R.id.acitivity_duty_status_frag);

				if (fragment instanceof DutyStatusFragment) {
					// if present fragment is DutyStatusFragment
					
					if (TaxiExConstants.IS_RESERVATION_AVAILABLE) {
						if(DutyStatusFragment.mVehicleListModel==null){//if selected vehicle model is empty
							ShowSingleButtonDialog(null, "Please first choose vehicle.",
									getString(R.string.alert_ok_button), 0, this, 9999);
						}else{
						replaceFragment(R.id.acitivity_duty_status_frag,//show reservation listing
								new ReservationListing(),
								FRAGMENT_TAGS.RESERVATION_LISTING.name(), true, getActivity());
					
						
						}
					} else {
						ShowDoubleButtonDialog("",
								getString(R.string.alert_logout),
								getString(R.string.alert_ok_button),
								getString(R.string.alert_cancel_button), 0, this, 2);

					}
					
					
				
					
					
					
					
					
				/*	ShowDoubleButtonDialog("",
							getString(R.string.alert_logout),
							getString(R.string.alert_ok_button),
							getString(R.string.alert_cancel_button), 0, this, 2);*/

				} else if (fragment instanceof MyProfileFragment) {
					
					
					// if fragment is an instance of MyProfilefragment
					// show alert for logout
					
					ShowDoubleButtonDialog("",
							getString(R.string.alert_logout),
							getString(R.string.alert_ok_button),
							getString(R.string.alert_cancel_button), 0, this, 2);

				}
				
				else if (fragment instanceof ReservationListing) {
					
					
					// if fragment is instance of Reservation listing then show reservation history
					
					replaceFragment(R.id.acitivity_duty_status_frag,
							new ReservationHistory(),
							FRAGMENT_TAGS.RESERVATIONHISTORY.name(), true,
							getActivity());			

				}
			}

			break;

		default:
			break;
		}
	}

	@Override
	public void onSpinnerDialogClick(int position, int callBackId) {
		// TODO Auto-generated method stub
		super.onSpinnerDialogClick(position, callBackId);
		switch (callBackId) {
		case 1:
			try {
				AndroidWebResponseTask.getInstance(getActivity()).setCallback(
						this);
				postXMl(TaxiExConstants.CancelBookinfUrl,
						createXml(TaxiExConstants.CancelBookingXMLKeys,
								new String[] { "",
										UserLoginModel.getInstance().getId(),
										onlineStatusActivity.mBookingId, "1",
										options.get(position), "3" }), true,
						TaxiExConstants.CancelBookingWebserviceResponse, this);
			} catch (Exception e) {

				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}
	
	
	/**
	 * interface to tackle positive button click of prompts
	 */

	@Override
	public void onPositiveButtonClick(int id) {
		// TODO Auto-generated method stub
		super.onPositiveButtonClick(id);
		AndroidWebResponseTask.getInstance(getActivity());

		switch (id) {
		
		
	
		
		
		case 1:

			if(!OnlineStatusActivity.mBookingId.equals("0")){
				
				if (OnlineStatusActivity.mCountDownTimer != null) {

					OnlineStatusActivity.mCountDownTimer.cancel();

				}
				postXMl(TaxiExConstants.CancelBookinfUrl,
						createXml(
								TaxiExConstants.CancelBookingXMLKeys,
								new String[] {
										"",
										UserLoginModel
												.getInstance()
												.getId(),
												OnlineStatusActivity.	mBookingId, "0", "", "3" }),
						true,
						TaxiExConstants.CancelBookingHeaderWebserviceRedponse,
						this);
				
				
			}else{
			
			
			postXMl(TaxiExConstants.DriverStatusOfflineUrl,
					createXml(TaxiExConstants.DriverStatusXMLKeys,
							new String[] { "",
									UserLoginModel.getInstance().getId() }),
					true, TaxiExConstants.DriverStatusWebserviceResponse, this);
			
			}

			break;

		case 2:
			
			 SharedPreferences mSharedprefrences=getActivity().getSharedPreferences(
						TaxiExConstants.SharedPreferenceName, 0);
			
			
			String deviceToken=mSharedprefrences.getString(
					TaxiExConstants.Pref_GCM_key, "");
			
			postXMl(TaxiExConstants.DriverLogoutUrl,
					createXml(TaxiExConstants.LogoutXMlKeys, new String[] { "",
							UserLoginModel.getInstance().getId(), deviceToken }), true,
					TaxiExConstants.LogoutWebserviceResponse, this);
			break;
		case 5:
			// try {
			//
			// postXMl(TaxiExConstants.CancelBookinfUrl,
			// createXml(TaxiExConstants.CancelBookingXMLKeys,
			// new String[] { "",
			// UserLoginModel.getInstance().getId(),
			// onlineStatusActivity.mBookingId, "1",
			// "None of these", "3" }), true,
			// TaxiExConstants.CancelBookingWebserviceResponse, this);
			// } catch (Exception e) {
			//
			// e.printStackTrace();
			// }
			break;
		case Alert_logout_Admin:
			editPrefs.clear();
			editPrefs.commit();
			if (getActivity() != null) {

				getActivity().finishAffinity();
				startActivity(new Intent(getActivity(), MainActivity.class));
			}
			// postXMl(TaxiExConstants.DriverLogoutUrl,
			// createXml(TaxiExConstants.LogoutXMlKeys, new String[] { "",
			// UserLoginModel.getInstance().getId() }), true,
			// TaxiExConstants.LogoutWebserviceResponse, this);
			break;
		case Alert_Cancel_Booking:
			// try {
			//
			// // postXMl(TaxiExConstants.CancelBookinfUrl,
			// // createXml(TaxiExConstants.CancelBookingXMLKeys,
			// // new String[] { "",
			// // UserLoginModel.getInstance().getId(),
			// // onlineStatusActivity.mBookingId, "1",
			// // "None of these", "3" }), true,
			// // TaxiExConstants.CancelBookingWebserviceResponse, this);
			// if (getActivity() instanceof OnlineStatusActivity) {
			// OnlineStatusActivity onlineStatusActivity =
			// (OnlineStatusActivity) getActivity();
			// onlineStatusActivity.setUiOnCancelTrip();
			// }
			// } catch (Exception e) {
			//
			// e.printStackTrace();
			// }
			break;
		// case 125:
		// if (getActivity() instanceof OnlineStatusActivity) {
		// OnlineStatusActivity onlineStatusActivity = (OnlineStatusActivity)
		// getActivity();
		// onlineStatusActivity.setUiOnCancelTrip();
		//
		// }
		// break;
		default:
			break;
		}

	}

	@Override
	public void getServerValues(String response, int id) {
		// TODO Auto-generated method stub

	}

	
	// setting header title 
	public void setHeaderFragmentTitle(String title) {
		mHeadetTitle.setText(title);
	}

	@Override
	public void setServerError(int id, String msg) {
		// TODO Auto-generated method stub

	}

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
	public void onSuccess(String[] response, int UNIQUE_ID) {
		// TODO Auto-generated method stub

	}

}
