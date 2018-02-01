package com.tms.driver;

import android.R.style;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tms.driver.FCMNotification.FireBaseMessageService;
import com.tms.driver.base.BaseActivityClass;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.customviews.CustomImageView;
import com.tms.driver.customviews.CustomLinearLayout;
import com.tms.driver.fragments.HeaderFragment;
import com.tms.driver.models.BookingDetailsRequestModel;
import com.tms.driver.models.MyProfileModel;
import com.tms.driver.models.UserLoginModel;
import com.tms.driver.models.VehicleListModel;
import com.tms.driver.parse.ParseContent;
import com.tms.driver.parse.ParseContent.CONTENT_TYPE;
import com.tms.driver.utilities.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.List;

public class OnlineStatusActivity extends BaseActivityClass implements
		OnTouchListener,OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,LocationListener  {
	private boolean isStartTrip=false;
	private RelativeLayout tap_accept;

	public static Activity activity;
	GoogleApiClient googleApiClient;

	public static int COUNTER_COUNT = 15;
	public static boolean mIsInBackGround = false;
	public static final int DAILOG_STARTTRIP_ID = 111;
	public static final int DAILOG_ENDTRIP_ID = 12;
	private boolean mIsAutoLogout = false;
	private ImageView mWayBillInfo;
	private LocationManager mLocationManager;
	private GoogleMap mMap;
	private MyProfileModel mMyProfileDetailsModel;
	private TextView mDriverRatingTextView, mDriverNameTextView,
			mDriverPhoneNumberTextView, mCarModelTextView;
	private TextView mShortRouteTextView, mShortRouteDestinationTextView;
	private RatingBar mDriverRatingBar;
	private CustomImageView mProfileImageView;
	private TextView mCountDownTimeTextView, mVehicleModel, mVehicleNumber;
	private String mLatitude, mLongitude;
	public String mOldLatitude, mOldLogitude;
	private boolean mIsAcceptedOnTouch = false;
	public static CountDownTimer mCountDownTimer;
	private Button mStartTripButton;
	private Location mCurrentLocation;
	private boolean isCountDown = false;
	private SharedPreferences mSharedprefrences;
	private SharedPreferences.Editor editPrefs;
	private LinearLayout mVehicleDetailsLinear, mShortRouteLinearLayout,
			mShortRouteDestinationLinearLayout;
	private RelativeLayout countDownLinear;
	private VehicleListModel mVehicleListModel;
	public static String mBookingId = "0";
	private OnMyLocationChangeListener mOnMyLocationChangeListener;
	public static BookingDetailsRequestModel mBookingDetailsRequestModel;
	private HeaderFragment mHeaderFragment;
	private boolean isFirstTime = false;
	private boolean isOffline = false;
	private ToneGenerator tg;
	private ProgressDialog mProgressDialog;
	private boolean isFromReservation = false;

	private NotificationManager mNotificationManager;

	private TextView txtDevice;
	boolean isPermssionGranted;
	private LocationRequest mLocationRequest;
	private String TAG ="OnlineStatus";

	private void afterAcceptingRequest() {
		mHeaderFragment.setLeftIcon(R.drawable.selector_cancel_tip_button);
		countDownLinear.setVisibility(View.GONE);
		tap_accept.setVisibility(View.GONE);
		mVehicleDetailsLinear.setVisibility(View.GONE);
		mCountDownTimeTextView.setVisibility(View.GONE);
		mStartTripButton.setVisibility(View.VISIBLE);
		mIsAcceptedOnTouch = true;
		isCountDown = false;
		CustomLinearLayout.isIntercepting = false;
		mStartTripButton.setText(getString(R.string.online_arriving_now));

		postXMl(TaxiExConstants.BookingDetails,
				createXml(TaxiExConstants.BookingDetailsXMLKeys, new String[] {
						"", UserLoginModel.getInstance().getId(), mBookingId }),
				true, TaxiExConstants.BookingDetailsWebserviceResponse, this);
	}

	private void afterArrivingWebserviceRequest() {
		mHeaderFragment.setLeftIcon(R.drawable.selector_cancel_tip_button);
		countDownLinear.setVisibility(View.GONE);
		tap_accept.setVisibility(View.GONE);
		mVehicleDetailsLinear.setVisibility(View.GONE);
		mCountDownTimeTextView.setVisibility(View.GONE);
		mStartTripButton.setVisibility(View.VISIBLE);
		mIsAcceptedOnTouch = true;
		isCountDown = false;
		CustomLinearLayout.isIntercepting = false;
		postXMl(TaxiExConstants.BookingDetails,
				createXml(TaxiExConstants.BookingDetailsXMLKeys, new String[] {
						"", UserLoginModel.getInstance().getId(), mBookingId }),
				true, TaxiExConstants.BookingDetailsWebserviceResponse, this);
		mStartTripButton.setBackgroundColor(getResources().getColor(
				R.color.green));
		mStartTripButton.setText(getString(R.string.online_start_trip));
	}

	private void afterStartTripWebserviceRequest() {

		isStartTrip=true;
		postXMl(TaxiExConstants.BookingDetails,
				createXml(TaxiExConstants.BookingDetailsXMLKeys, new String[] {
						"", UserLoginModel.getInstance().getId(), mBookingId }),
				true, TaxiExConstants.BookingDetailsWebserviceResponse, this);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				editPrefs.putBoolean(TaxiExConstants.Pref_isOnline, true);
				editPrefs.commit();
				if (mHeaderFragment != null) {
					if (mHeaderFragment.isVisible()) {
						mHeaderFragment.mLeftImageview.setVisibility(View.GONE);
					}
				}

			}
		});

		mCarModelTextView.setVisibility(View.GONE);

		countDownLinear.setVisibility(View.GONE);
		tap_accept.setVisibility(View.GONE);
		mVehicleDetailsLinear.setVisibility(View.GONE);
		mCountDownTimeTextView.setVisibility(View.GONE);
		mStartTripButton.setVisibility(View.VISIBLE);
		mIsAcceptedOnTouch = true;
		isCountDown = false;
		CustomLinearLayout.isIntercepting = false;

		mStartTripButton.setBackgroundColor(getResources()
				.getColor(R.color.red));
		mStartTripButton.setText(getString(R.string.online_end_trip));

	}

	private boolean isFromEndTrip = false;
	String dateCompleted;
	String tripFare, discount, payment;

	private void afterEndTripWebserviceRequest(String dateCompleted,
											   String tripFare, String payment, String discount) {
		isFromEndTrip = true;
		postXMl(TaxiExConstants.BookingDetails,
				createXml(TaxiExConstants.BookingDetailsXMLKeys, new String[] {
						"", UserLoginModel.getInstance().getId(), mBookingId }),
				true, TaxiExConstants.BookingDetailsWebserviceResponse, this);
		editPrefs.remove(TaxiExConstants.pref_Booking_details);
		editPrefs.commit();
		editPrefs.putBoolean(TaxiExConstants.Pref_isOnline, false);
		editPrefs.commit();
		// String tip = null,
		// bookingDate = null;
		// try {
		//
		// JSONObject jsonObject = new JSONObject(response);
		// tip = jsonObject.getString("tripAmount");
		// bookingDate = jsonObject.getString("completedDate");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		this.dateCompleted = dateCompleted;
		this.tripFare = tripFare;
		this.payment = payment;
		this.discount = discount;

	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mBookingId="0";
		setContentView(R.layout.frag_online_screen);
		activity=this;
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog
				.setProgressStyle(style.Widget_DeviceDefault_Light_ProgressBar_Small);
		mProgressDialog
				.setMessage(getString(R.string.alert_dialog_fetching_location));
		//mProgressDialog.show();


		googleApiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
		googleApiClient.connect();

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
//		mMap = ((SupportMapFragment) getSupportFragmentManager()
//				.findFragmentById(R.id.map)).getMap();
		ApplicationContext applicationContext = (ApplicationContext) getApplicationContext();
		applicationContext.setActivityContext(this);
		initializeComponents();


		if (android.os.Build.VERSION.SDK_INT >= 23) {
			if ((ContextCompat.checkSelfPermission(OnlineStatusActivity.this,
					"android.permission.ACCESS_COARSE_LOCATION") != PackageManager.PERMISSION_GRANTED)
					&& (ContextCompat.checkSelfPermission(OnlineStatusActivity.this,
					"android.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED)) {
				isPermssionGranted = false;
			} else {
				isPermssionGranted = true;
			}
		}else{
			isPermssionGranted = true;
		}

		byte[] bytes = mSharedprefrences.getString(
				TaxiExConstants.Pref_Login_Details, "{}").getBytes();
		if (bytes.length == 0) {
			return;
		}else{
			ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
			Base64InputStream base64InputStream = new Base64InputStream(byteArray,
					Base64.DEFAULT);
			ObjectInputStream in;
			try {
				in = new ObjectInputStream(base64InputStream);
				// UserLoginModel loginModel = UserLoginModel.getInstance();
				UserLoginModel.setInstance((UserLoginModel) in.readObject());
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
			}}

		byte[] bytes1 = mSharedprefrences.getString(
				TaxiExConstants.Pref_selected_vehicle, "{}").getBytes();
		if (bytes1.length == 0) {
			return;
		}
		ByteArrayInputStream byteArray1 = new ByteArrayInputStream(bytes1);
		Base64InputStream base64InputStream1 = new Base64InputStream(
				byteArray1, Base64.DEFAULT);
		ObjectInputStream in1;
		try {
			in1 = new ObjectInputStream(base64InputStream1);
			mVehicleListModel = (VehicleListModel) in1.readObject();
			mVehicleModel.setText(mVehicleListModel.getMake() + " "
					+ mVehicleListModel.getTaxiModel());
			mVehicleNumber.setText(mVehicleListModel.getTaxiNumber());
			base64InputStream1.close();


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
		/*if (mSharedprefrences.contains(TaxiExConstants.pref_Booking_details)) {
			byte[] bytes2 = mSharedprefrences.getString(
					TaxiExConstants.pref_Booking_details, "{}").getBytes();
			if (bytes2.length == 0) {
				return;
			}
			ByteArrayInputStream byteArray2 = new ByteArrayInputStream(bytes2);
			Base64InputStream base64InputStream2 = new Base64InputStream(
					byteArray2, Base64.DEFAULT);
			ObjectInputStream in2;
			try {
				in2 = new ObjectInputStream(base64InputStream2);
				mBookingDetailsRequestModel = (BookingDetailsRequestModel) in2
						.readObject();
				base64InputStream2.close();
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
		}*/
		postXMl(TaxiExConstants.MyProfileDetailsUrl,
				createXml(TaxiExConstants.MyProfileXMLKeys, new String[] { "",
						UserLoginModel.getInstance().getId() }), false,
				TaxiExConstants.MyProfileWebserviceResponse, this);

		// Utilities.getInstance(this).toogleGps(true);

		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mShortRouteLinearLayout.setVisibility(View.VISIBLE);
		mShortRouteDestinationLinearLayout.setVisibility(View.GONE);

	}

	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		super.onSuccess(response, UNIQUE_ID);

		switch (UNIQUE_ID) {


			case TaxiExConstants.EndTripFareWebserviceResponse:

				try {
					JSONObject jsonObjectFare=new JSONObject(response);

					String fareSuccess=jsonObjectFare.getString("endTripFare");

					if(fareSuccess.equals("1")){

						showAdditionalChargesPopUp();
					}

				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}


				break;

			case TaxiExConstants.CancelBookingWebserviceResponse:

				setUiOnCancelTrip();

				editPrefs.remove(TaxiExConstants.pref_Booking_details);
				editPrefs.commit();
				Log.e("Cancel Booking Response", "" + response);
				GcmIntentService.isBookingRequest = false;
			/*if (!GcmIntentService.isBookingRequest) {
				postXMl(TaxiExConstants.DriverStatusOfflineUrl,
						createXml(TaxiExConstants.DriverStatusXMLKeys,
								new String[] { "",
										UserLoginModel.getInstance().getId() }),
						true, TaxiExConstants.DriverStatusWebserviceResponse,
						this);
			}*/

				break;
			case TaxiExConstants.CheckTripWebserviceResponse:
				int bookingStatus = 0;
				mBookingId="0";

				try {
					if (!checkIfStringIsNullOrEmpty(response)) {
						JSONObject jsonObject = new JSONObject(response);
						if (jsonObject.has("bookingStatus")) {
							bookingStatus = jsonObject.getInt("bookingStatus");
						}
						int checkTripStatus = jsonObject.getInt("checkTrip");
						if (checkTripStatus == 0) {

							// DO nothing

						}else if (checkTripStatus == -4) {
							String message = jsonObject.getString("message");
							Intent i = new Intent(this,
									GCMDialog_Activity.class);
							i.putExtra("messageType", "cancel");
							i.putExtra("message", message);

							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TASK);

							startActivity(i);

						} else {
							mBookingId = String.valueOf(checkTripStatus);

							if (bookingStatus == 0) {

								CustomLinearLayout.isIntercepting = true;
//								GcmIntentService.isBookingRequest = true;
								FireBaseMessageService.isBookingRequest = true;

								Bundle bundle = new Bundle();
								bundle.putString("bookingId", mBookingId);
								bundle.putString("message", "");

								setUpCountDown(bundle);
								setIntent(new Intent());

								mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
								mNotificationManager.cancelAll();

//								if (GcmIntentService.player != null
//										&& GcmIntentService.player.isPlaying()) {
//									GcmIntentService.player.stop();
//
//								}

								if (FireBaseMessageService.player != null
										&& FireBaseMessageService.player.isPlaying()) {
									FireBaseMessageService.player.stop();

								}


							} else if (bookingStatus == 1) {
								afterAcceptingRequest();
								// After Accept Booking Functionality

								mShortRouteLinearLayout.setVisibility(View.VISIBLE);
								mShortRouteDestinationLinearLayout
										.setVisibility(View.GONE);

							} else if (bookingStatus == 2) {
								// After Arriving Button Functionality
								afterArrivingWebserviceRequest();

								mShortRouteLinearLayout.setVisibility(View.VISIBLE);
								mShortRouteDestinationLinearLayout
										.setVisibility(View.GONE);

							} else if (bookingStatus == 3) {
								// After Start Trip Functionality
								afterStartTripWebserviceRequest();
								mShortRouteLinearLayout.setVisibility(View.GONE);
								mShortRouteDestinationLinearLayout
										.setVisibility(View.VISIBLE);
								mCarModelTextView.setVisibility(View.GONE);
							} else if (bookingStatus == 4) {

								String completedDate = jsonObject
										.getString("completedDate");
								String tripfare = jsonObject.getString("tripFare");
								String userDiscount = jsonObject
										.getString("userDiscount");
								String paymentAmount = jsonObject
										.getString("paymentAmt");

								afterEndTripWebserviceRequest(completedDate,
										tripfare, paymentAmount, userDiscount);

								// After End Trip Functionality. Navigate to Receipt
								// Screen

								mShortRouteLinearLayout.setVisibility(View.GONE);
								mShortRouteDestinationLinearLayout
										.setVisibility(View.VISIBLE);

							}
						}
					}

				} catch (Exception e) {

					e.printStackTrace();
				}

				break;
			case TaxiExConstants.DriverStatusWebserviceResponse:

				Log.e("Driver Status Response", response);
				if (mIsAutoLogout) {
					mIsAutoLogout = false;

					SharedPreferences mSharedprefrences=getSharedPreferences(
							TaxiExConstants.SharedPreferenceName, 0);


					String deviceToken=mSharedprefrences.getString(
							TaxiExConstants.Pref_GCM_key, "");

					postXMl(TaxiExConstants.DriverLogoutUrl,
							createXml(TaxiExConstants.LogoutXMlKeys, new String[] {
									"", UserLoginModel.getInstance().getId(), deviceToken }),
							true, TaxiExConstants.LogoutWebserviceResponse,
							OnlineStatusActivity.this);
				} else {
					editPrefs.putBoolean(TaxiExConstants.Pref_isUserOnLine, false);
					editPrefs.putBoolean(TaxiExConstants.Pref_isOnline, false);
					editPrefs.commit();
					// finishAffinity();
					// startActivity(new Intent(this, DutyStatusActivity.class));

				}

				break;
			case TaxiExConstants.MyProfileWebserviceResponse:
				Log.e("My Profile Details", response);
				try {
					if (response != null) {

						JSONObject jsonObject = new JSONObject(response);
						ParseContent.getInstance().parseContentFromjson(
								CONTENT_TYPE.MYPROFILE, jsonObject, this,
								TaxiExConstants.MyProfileParsingResponseId);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case TaxiExConstants.LogoutWebserviceResponse:
				Log.e("Logout response", response);
				JSONObject jsonObject3;
				try {
					jsonObject3 = new JSONObject(response);
					int statusCode = jsonObject3
							.getInt("driverLogout");
					String message = jsonObject3.getString("message");


					if(statusCode == 1){
						String username = null;
						if (mSharedprefrences.contains(TaxiExConstants.Pref_login_username)) {
							username = mSharedprefrences.getString(
									TaxiExConstants.Pref_login_username, "");
						}

						editPrefs.clear();
						editPrefs.commit();
						editPrefs.putString(TaxiExConstants.Pref_login_username, username);
						editPrefs.commit();
						// editPrefs.putBoolean(TaxiExConstants.Pref_isOnline, false);
						editPrefs.putBoolean(TaxiExConstants.Pref_isUserOnLine, false);
						editPrefs.commit();

						finishAffinity();
						startActivity(new Intent(OnlineStatusActivity.this,
								MainActivity.class));
					}else if(statusCode == -4){
						Intent i = new Intent(OnlineStatusActivity.this,
								GCMDialog_Activity.class);
						i.putExtra("messageType", "cancel");
						i.putExtra("message", message);

						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TASK);

						startActivity(i);
					}else {
						Toast.makeText(OnlineStatusActivity.this, message, Toast.LENGTH_SHORT).show();
					}

				} catch (Exception e) {

				}



				break;
			case TaxiExConstants.AcceptBookingWebserviceResponse:


				JSONObject jsonObj;
				try {
					jsonObj = new JSONObject(response);
					if(jsonObj.getString("acceptBooking").equals("1")){

						Log.e("Accept Booking response", response);
						editPrefs.putBoolean(TaxiExConstants.Pref_isOnline, true);
						editPrefs.commit();
						mHeaderFragment.setLeftIcon(R.drawable.selector_cancel_tip_button);




					}else{

						ShowSingleButtonDialog(null, jsonObj.getString("message"),
								getString(R.string.alert_ok_button), 0, this, 1);				}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}




				// if (mHeaderFragment != null) {
				// if (mHeaderFragment.isVisible()) {
				// mHeaderFragment.mLeftImageview
				// .setOnClickListener(mHeaderFragment);
				// }
				// }

				break;
			case TaxiExConstants.BookingDetailsWebserviceResponse:

				try {
					if (!checkIfStringIsNullOrEmpty(response)) {
						JSONObject jsonObject = new JSONObject(response);
						ParseContent.getInstance().parseContentFromjson(
								CONTENT_TYPE.BOOKINGDETAILS, jsonObject, this,
								TaxiExConstants.BookingDetailsParsingResponse);
					}
				} catch (JSONException e) {
					// TODO: handle exception
				}
				break;
			case TaxiExConstants.LatLongUpdateWebserviceResponse:

				break;

			case TaxiExConstants.StarTripWebServiceResponse:
				Log.e("Start Trip Response", response);

				JSONObject jsonObj2;
				try {
					jsonObj2 = new JSONObject(response);
					if(jsonObj2.getString("startTrip").equals("1")){

						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								editPrefs.putBoolean(TaxiExConstants.Pref_isOnline, true);
								editPrefs.commit();
								if (mHeaderFragment != null) {
									if (mHeaderFragment.isVisible()) {
										mHeaderFragment.mLeftImageview
												.setVisibility(View.GONE);
									}
								}

							}
						});
						mStartTripButton.setBackgroundColor(getResources().getColor(
								R.color.red));
						mStartTripButton.setText(getString(R.string.online_end_trip));

						mShortRouteLinearLayout.setVisibility(View.GONE);
						mShortRouteDestinationLinearLayout.setVisibility(View.VISIBLE);
						mCarModelTextView.setVisibility(View.GONE);

					}else{

						ShowSingleButtonDialog(null, jsonObj2.getString("message"),
								getString(R.string.alert_ok_button), 0, this, 1);				}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}







				break;
			case TaxiExConstants.EndTripWebserviceResponse:

				JSONObject jsonObj3;
				try {
					jsonObj3 = new JSONObject(response);
					if(!jsonObj3.getString("endTrip").equals("1")){


						ShowSingleButtonDialog(null, jsonObj3.getString("message"),
								getString(R.string.alert_ok_button), 0, this, 1);


						return;



					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				//////

				Log.e("End Trip Response", response);
				editPrefs.remove(TaxiExConstants.pref_Booking_details);
				editPrefs.commit();
				editPrefs.putBoolean(TaxiExConstants.Pref_isOnline, false);
				editPrefs.putBoolean(TaxiExConstants.Pref_isUserOnLine, false);
				editPrefs.commit();
				String tip = null,
						bookingDate = null,
						userDiscount = null,
						paymentAmount = null;

				// {"endTrip":"1","userDiscount":0,"paymentAmt":3,"message":"Success","tripAmount":3,"completedDate":"18 August 2015"}
				try {

					JSONObject jsonObject = new JSONObject(response);
					tip = jsonObject.getString("tripAmount");
					bookingDate = jsonObject.getString("completedDate");
					userDiscount = jsonObject.getString("userDiscount");
					paymentAmount = jsonObject.getString("paymentAmt");

					mBookingDetailsRequestModel.setPayvia(jsonObject
							.getString("payvia"));

				} catch (Exception e) {
					e.printStackTrace();
				}

				finish();
				Intent fareSummaryIntent = new Intent(this,
						FareSummaryActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(TaxiExConstants.BookingDetailsBundleKey,
						mBookingDetailsRequestModel);
				bundle.putString(TaxiExConstants.BookingDateBundleKey, bookingDate);
				bundle.putString(TaxiExConstants.BookingTipBundleKey, tip);
				bundle.putString(TaxiExConstants.UserDiscount, userDiscount);
				bundle.putString(TaxiExConstants.PaymentAmount, paymentAmount);

				fareSummaryIntent.putExtra(TaxiExConstants.BookingDetailsBundleKey,
						bundle);

				startActivity(fareSummaryIntent);
				break;
			case TaxiExConstants.ArrivedWebserviceResponse:

				JSONObject jsonObj4;
				try {
					jsonObj4 = new JSONObject(response);
					if(!jsonObj4.getString("arrived").equals("1")){


						ShowSingleButtonDialog(null, jsonObj4.getString("message"),
								getString(R.string.alert_ok_button), 0, this, 1);


						return;



					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mStartTripButton.setBackgroundColor(getResources().getColor(
						R.color.green));
				mStartTripButton.setText(getString(R.string.online_start_trip));

				break;

			default:
				break;
		}

	}

	@Override
	public void onPositiveButtonClick(int id) {
		// TODO Auto-generated method stub
		super.onPositiveButtonClick(id);
		switch (id) {

			case DAILOG_STARTTRIP_ID:
				try {
					if (mOldLatitude != null && mOldLogitude != null) {
						postXMl(TaxiExConstants.StartTripUrl,
								createXml(
										TaxiExConstants.StartTripXMLKeys,
										new String[] {
												"",
												UserLoginModel.getInstance()
														.getId(),
												mBookingDetailsRequestModel
														.getRiderId(), mLatitude,
												mLongitude, mOldLatitude,
												mOldLogitude, mBookingId }), true,
								TaxiExConstants.StarTripWebServiceResponse, this);
					} else {
						postXMl(TaxiExConstants.StartTripUrl,
								createXml(
										TaxiExConstants.StartTripXMLKeys,
										new String[] {
												"",
												UserLoginModel.getInstance()
														.getId(),
												mBookingDetailsRequestModel
														.getRiderId(), mLatitude,
												mLongitude, mLatitude, mLongitude,
												mBookingId }), true,
								TaxiExConstants.StarTripWebServiceResponse, this);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case DAILOG_ENDTRIP_ID:

				if (!isFromReservation) {

					showAdditionalChargesPopUp();
		/*		String toll = "0", taxes = "0",fare="0";
				postXMl(TaxiExConstants.End_Trip_Fare,
						createXml(
								TaxiExConstants.EndTripFareKeys,
								new String[] {
										"",
										UserLoginModel.getInstance().getId(),
										mBookingDetailsRequestModel
												.getRiderId(),
										mBookingId,
										mBookingDetailsRequestModel.getPayvia(),
										toll, taxes }), true,
						TaxiExConstants.EndTripFareWebserviceResponse,
						OnlineStatusActivity.this);*/







				} else {
					String toll = "0", taxes = "0";

					mIsFromEndTrip = true;
					postXMl(TaxiExConstants.EndTripUrl,
							createXml(
									TaxiExConstants.EndTripXMLKeys,
									new String[] {
											"",
											UserLoginModel.getInstance().getId(),
											mBookingDetailsRequestModel
													.getRiderId(),
											mBookingId,
											mBookingDetailsRequestModel.getPayvia(),
											toll, taxes }), true,
							TaxiExConstants.EndTripWebserviceResponse,
							OnlineStatusActivity.this);

				}

				break;

			default:
				break;
		}

	}

	private boolean mIsFromEndTrip = false;

	@Override
	public void onFailure(Throwable e, String error, int UNIQUE_ID) {
		// TODO Auto-generated method stub
//		GcmIntentService.isBookingRequest = false;
		FireBaseMessageService.isBookingRequest = false;
		ShowSingleButtonDialog(null, error,
				getString(R.string.alert_ok_button), 0, this, 1);
	}

	ImageView mVehicleImageView;

	private void setVehicleImage() {
		if (mVehicleListModel != null) {
			mVehicleImageView = (ImageView) findViewById(R.id.online_vehicle_image);
			if (mVehicleListModel.getTaxiTypeId().equalsIgnoreCase("1")) {
				mVehicleImageView.setImageDrawable(getResources().getDrawable(
						R.drawable.car_13));
			}

			else if (mVehicleListModel.getTaxiTypeId().equalsIgnoreCase("2")) {
				mVehicleImageView.setImageDrawable(getResources().getDrawable(
						R.drawable.car_12));
			}

			else if (mVehicleListModel.getTaxiTypeId().equalsIgnoreCase("3")) {
				mVehicleImageView.setImageDrawable(getResources().getDrawable(
						R.drawable.car_11));
			} else if (mVehicleListModel.getTaxiTypeId().equalsIgnoreCase("4")) {
				mVehicleImageView.setImageDrawable(getResources().getDrawable(
						R.drawable.car_11));
			}else if (mVehicleListModel.getTaxiTypeId().equalsIgnoreCase("5")) {
				mVehicleImageView.setImageDrawable(getResources().getDrawable(
						R.drawable.car_11));
			}



		}

	}

	Double distancecalculated = 0.0;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mIsInBackGround = false;
		if(isPermssionGranted){
			startService(new Intent(OnlineStatusActivity.this, LocationUpdateService.class));
		}else{
			mProgressDialog.cancel();
		}
		CustomLinearLayout.isIntercepting = false;
		checkDriverStatusonResume();
		isFirstTime = true;
		mHeaderFragment = (HeaderFragment) getSupportFragmentManager()
				.findFragmentById(R.id.activity_online_fragment);
		mHeaderFragment.setLeftIcon(R.drawable.selector_icon_drive_button);
		mHeaderFragment.setRightIconVisibility(View.GONE);
		countDownLinear.setVisibility(View.GONE);
		tap_accept.setVisibility(View.GONE);
		mShortRouteLinearLayout.setVisibility(View.GONE);
		mShortRouteDestinationLinearLayout.setVisibility(View.GONE);


		mHeaderFragment.setHeaderTitle(getResources().getString(R.string.app_name_2));
		mHeaderFragment.setRightIcon(R.drawable.selector_icon_drive_button);

		if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			buildAlertMessageNoGps();

		}
		mOnMyLocationChangeListener = new OnMyLocationChangeListener() {

			@Override
			public void onMyLocationChange(Location arg0) {
				// TODO Auto-generated method stub
				// if (mStartTripButton.getVisibility() == View.INVISIBLE) {
				if(isPermssionGranted) {
					if (mProgressDialog != null) {

						if (mProgressDialog.isShowing()) {
							mProgressDialog.cancel();
						}
					}
					mLatitude = String.valueOf(arg0.getLatitude());
					mLongitude = String.valueOf(arg0.getLongitude());
					if (isFirstTime) {
						mOldLatitude = mLatitude;
						mOldLogitude = mLongitude;
						postXMl(TaxiExConstants.LatLongUpdateWithDistanceURL,
								createXml(
										TaxiExConstants.LatLongUpdateWithDistanceXMLKeys,
										new String[]{
												"",
												UserLoginModel.getInstance()
														.getId(),
												String.valueOf(arg0.getLatitude()),
												String.valueOf(arg0.getLongitude()),
												"0", String.valueOf("0"), String.valueOf(arg0.getBearing()), String.valueOf(arg0.getBearing())}), false,
								TaxiExConstants.LatLongUpdateWebserviceResponse,
								OnlineStatusActivity.this);
					}

					// showToast("Current" + arg0.getLatitude() + "old latitude = "
					// + mOldLatitude, 1000);
					mCurrentLocation = arg0;
					navigateToCurrentLocation(arg0);

					// } else if (mStartTripButton.getVisibility() == View.VISIBLE)
					// {
					Location old_location = new Location("locationA");

					if (mOldLogitude != null && mOldLatitude != null) {

						old_location.setLatitude(Double.valueOf(mOldLatitude));
						old_location.setLongitude(Double.valueOf(mOldLogitude));

					} else {
						old_location.setLatitude(Double.valueOf(mLatitude));
						old_location.setLongitude(Double.valueOf(mLongitude));

					}

					// float distanceInMeters = arg0.distanceTo(old_location);
					// DecimalFormat decimalFormat = new DecimalFormat("####.##");
					// String metersinString =
					// decimalFormat.format(distanceInMeters);
					// double km = distanceInMeters / 1000;
					// String kminString = decimalFormat.format(km);
					// // float meterInMiles = 0.00062137f;
					// double miles = Float.valueOf(metersinString) * 0.00062137f;
					// String distanceInString = String.valueOf(miles);
					// showToast("distance in metres" + distanceInMeters
					// + "distance in miles" + miles, 1000);

					String bookingId = "0";
					String endTripString = getString(R.string.online_end_trip);
					if (mStartTripButton.getText().toString()
							.equalsIgnoreCase(endTripString)) {
						bookingId = mBookingId;
					}
					Log.e("Booking id for lat long", bookingId);
					distancecalculated = (double) (arg0.distanceTo(old_location));
					// showToast("distance in meters" + distancecalculated, 500);

					if (distancecalculated > 20.0) {
						double miles = distancecalculated * 0.000621;
						Log.e("Distance In String", "" + miles);
						// showToast("distance in miles" + miles, 500);

						mOldLatitude = mLatitude;
						mOldLogitude = mLongitude;

						LatLng myLatLng = new LatLng(arg0.getLatitude(),
								arg0.getLongitude());
						mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng,
								16));
						// showToast("Distance grater than 30 meters", 1000);
						postXMl(TaxiExConstants.LatLongUpdateWithDistanceURL,
								createXml(
										TaxiExConstants.LatLongUpdateWithDistanceXMLKeys,
										new String[]{
												"",
												UserLoginModel.getInstance()
														.getId(),
												String.valueOf(arg0.getLatitude()),
												String.valueOf(arg0.getLongitude()),
												bookingId, String.valueOf(miles), String.valueOf(arg0.getBearing()), String.valueOf(arg0.getBearing())}),
								false,
								TaxiExConstants.LatLongUpdateWebserviceResponse,
								OnlineStatusActivity.this);
					}

					// }
					// LatLng myLatLng = new LatLng(arg0.getLatitude(), arg0
					// .getLongitude());
					//
					// mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng,
					// 16));
					// mMap.clear();
					// mMap.addMarker(new MarkerOptions().icon(
					// BitmapDescriptorFactory
					// .fromResource(R.drawable.map_pin)).position(
					// myLatLng));
				}
			}
		};



		// Check if driver is offline when application goes to background to
		// navigate it to the dutystatus screen otherwise use the startTrip
		// Option

		if (isOffline) {
			isOffline = false;
			if (!mIsFromWayBillActivity) {
				finishAffinity();
				startActivity(new Intent(this, DutyStatusActivity.class));
			} else {
				mIsFromWayBillActivity = false;
			}

		}
		// else {
		// new Handler().postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// if (mSharedprefrences.getBoolean(
		// TaxiExConstants.Pref_isOnline, false)) {
		// try {
		//
		// mProfileImageView.setImageUrl(
		// mBookingDetailsRequestModel.getUserImage(),
		// 0);
		// mDriverNameTextView
		// .setText(mBookingDetailsRequestModel
		// .getFirstName()
		// + " "
		// + mBookingDetailsRequestModel
		// .getLastName());
		// mDriverRatingTextView
		// .setText(mBookingDetailsRequestModel
		// .getRating());
		// mDriverPhoneNumberTextView
		// .setText(mBookingDetailsRequestModel
		// .getPhone());
		// mCarModelTextView.setVisibility(View.VISIBLE);
		// mShortRouteLinearLayout.setVisibility(View.VISIBLE);
		// mCarModelTextView
		// .setText(mBookingDetailsRequestModel
		// .getETA());
		// mShortRouteTextView
		// .setText(mBookingDetailsRequestModel
		// .getShortAddress());
		// countDownLinear.setVisibility(View.GONE);
		// mVehicleDetailsLinear.setVisibility(View.GONE);
		// mCountDownTimeTextView.setVisibility(View.GONE);
		// mStartTripButton.setVisibility(View.VISIBLE);
		// // if (mStartTripButton
		// // .getText()
		// // .toString()
		// // .equalsIgnoreCase(
		// // getString(R.string.online_start_trip))) {
		// // mStartTripButton
		// // .setText(getString(R.string.online_end_trip));
		// // mStartTripButton
		// // .setBackgroundColor(getResources()
		// // .getColor(R.color.red));
		// // if (mHeaderFragment != null) {
		// // if (mHeaderFragment.isVisible()) {
		// // mHeaderFragment.mLeftImageview
		// // .setVisibility(View.GONE);
		// // }
		// // }
		// // }
		// LatLng latLng = new LatLng(Double
		// .valueOf(mBookingDetailsRequestModel
		// .getLatitude()), Double
		// .valueOf(mBookingDetailsRequestModel
		// .getLongitude()));
		// mMap.addMarker(new MarkerOptions().icon(
		// BitmapDescriptorFactory
		// .fromResource(R.drawable.map_pin))
		// .position(latLng));
		// mIsAseptedOnTouch = true;
		// isCountDown = false;
		// CustomLinearLayout.isIntercepting = false;
		// editPrefs.putBoolean(TaxiExConstants.Pref_isOnline,
		// true);
		// editPrefs.commit();
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// }
		// }
		// }, 100);
		//
		// }

		setVehicleImage();
		/*
		 * if (getIntent().getBooleanExtra("FROM_NOTIFICATION", false)) {
		 * CustomLinearLayout.isIntercepting = true;
		 * GcmIntentService.isBookingRequest = true;
		 *
		 * Bundle bundle = new Bundle(); bundle.putString("bookingId",
		 * getIntent().getStringExtra("bookingId")); bundle.putString("message",
		 * getIntent().getStringExtra("message"));
		 *
		 * setUpCountDown(bundle); setIntent(new Intent());
		 *
		 * }
		 */
		if (getIntent().hasExtra(TaxiExConstants.INTENT_TAG)) {

			isFromReservation = false;
			mBookingId = getIntent().getStringExtra(TaxiExConstants.BOOKING_ID);
			mShortRouteTextView.setText(getIntent().getStringExtra(
					TaxiExConstants.DROPP_OFF_ADDRESS));

			mHeaderFragment.setLeftIconVisibility(View.GONE);

			mShortRouteTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					ShowSingleButtonDialog("Pickup Address", getIntent()
									.getStringExtra(TaxiExConstants.PICK_UP_ADDRESS),
							getString(R.string.alert_ok_button), 0,
							OnlineStatusActivity.this, 1);

				}
			});
			afterAcceptingRequest();

		}

	}

	/**
	 * This method is used to check trip status whenever user comes on this
	 * screen
	 */

	private void checkDriverStatusonResume() {

		SharedPreferences mSharedprefrences= getSharedPreferences(
				TaxiExConstants.SharedPreferenceName, 0);


		String deviceToken=mSharedprefrences.getString(
				TaxiExConstants.Pref_GCM_key, "");

		postXMl(TaxiExConstants.CheckTripUrl,
				createXml(TaxiExConstants.CheckTripXMLKeys, new String[] { "",
						UserLoginModel.getInstance().getId(), deviceToken }), false,

				TaxiExConstants.CheckTripWebserviceResponse, this);
	}

	public void setUiOnCancelTrip() {

		try{
			findViewById(R.id.txtPayvia).setVisibility(
					View.GONE);
		}catch (Exception e){
			e.printStackTrace();
		}


		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mCountDownTimer != null) {
					try {
						mBookingId="0";
						mCountDownTimer.cancel();
						isCountDown = false;

						// mCountDownTimer.cancel();

						if (m != null) {
							if (m.isPlaying()) {
								m.stop();
								m.release();
								m = null;
								// m = new MediaPlayer();
							}

						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				isFirstTime = true;
				mBookingDetailsRequestModel = null;
				mHeaderFragment = (HeaderFragment) getSupportFragmentManager()
						.findFragmentById(R.id.activity_online_fragment);
				mHeaderFragment
						.setLeftIcon(R.drawable.selector_icon_drive_button);
				mHeaderFragment.setLeftIconVisibility(View.VISIBLE);
				mHeaderFragment.setRightIconVisibility(View.GONE);
				countDownLinear.setVisibility(View.GONE);
				tap_accept.setVisibility(View.GONE);
				mShortRouteLinearLayout.setVisibility(View.GONE);
				mShortRouteDestinationLinearLayout.setVisibility(View.GONE);
				mVehicleDetailsLinear.setVisibility(View.VISIBLE);
				mStartTripButton.setVisibility(View.GONE);
				mStartTripButton.setText("");
				mMap.clear();
				editPrefs.putBoolean(TaxiExConstants.Pref_isOnline, false);
				editPrefs.commit();
				postXMl(TaxiExConstants.MyProfileDetailsUrl,
						createXml(TaxiExConstants.MyProfileXMLKeys,
								new String[] { "",
										UserLoginModel.getInstance().getId() }),
						true, TaxiExConstants.MyProfileWebserviceResponse,
						OnlineStatusActivity.this);

			}
		}, 500);
		// setRightIcon(R.drawable.selector_icon_drive_button);
	}

	private void navigateToCurrentLocation(Location location) {
		LatLng myLatLng = new LatLng(location.getLatitude(),
				location.getLongitude());
		mMap.clear();
		try {
			if (mBookingDetailsRequestModel != null) {
				LatLng latLng = new LatLng(
						Double.valueOf(mBookingDetailsRequestModel
								.getLatitude()),
						Double.valueOf(mBookingDetailsRequestModel
								.getLongitude()));

				mMap.addMarker(new MarkerOptions().icon(
						BitmapDescriptorFactory
								.fromResource(R.drawable.map_pin)).position(
						latLng));

			} else {
				mMap.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (isFirstTime) {

			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 16));
			isFirstTime = false;
		}

		// mMap.addMarker(new MarkerOptions().icon(
		// BitmapDescriptorFactory.fromResource(R.drawable.map_pin))
		// .position(myLatLng));

		// postXMl(TaxiExConstants.LatLongUpdateWithDistanceURL,
		// createXml(
		// TaxiExConstants.LatLongUpdateWithDistanceXMLKeys,
		// new String[] { "",
		// UserLoginModel.getInstance().getId(),
		// String.valueOf(location.getLatitude()),
		// String.valueOf(location.getLongitude()), "1",
		// "100" }), false,
		// TaxiExConstants.LatLongUpdateWebserviceResponse, this);
	}

	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Your GPS seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									@SuppressWarnings("unused") final DialogInterface dialog,
									@SuppressWarnings("unused") final int id) {
								startActivity(new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
										@SuppressWarnings("unused") final int id) {
						dialog.cancel();

						OnlineStatusActivity.this.finish();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	private void initializeComponents() {
		mSharedprefrences = getSharedPreferences(
				TaxiExConstants.SharedPreferenceName, 0);
		editPrefs = mSharedprefrences.edit();

		editPrefs.putBoolean(TaxiExConstants.Pref_isUserOnLine, true);
		editPrefs.commit();

		mWayBillInfo = (ImageView) findViewById(R.id.frag_online_waybill);
		// mNavigateToCurrentPosition = (ImageView)
		// findViewById(R.id.frag_online_navigate_to_position);
		// mNavigateToCurrentPosition.setOnClickListener(this);
		tap_accept = (RelativeLayout) findViewById(R.id.tap_accept);

		mDriverRatingTextView = (TextView) findViewById(R.id.frag_online_summary_rating_textview);
		mDriverNameTextView = (TextView) findViewById(R.id.frag_online_summary_rider_name);

		mProfileImageView = (CustomImageView) findViewById(R.id.frag_online_summary_image);
		mCountDownTimeTextView = (TextView) findViewById(R.id.frag_online_countdown_time);
		mStartTripButton = (Button) findViewById(R.id.frag_online_start_trip);
		countDownLinear = (RelativeLayout) findViewById(R.id.countdownlinear);
		mVehicleDetailsLinear = (LinearLayout) findViewById(R.id.vehicle_details_linear);
		mVehicleModel = (TextView) findViewById(R.id.online_vehicle_name);
		mVehicleNumber = (TextView) findViewById(R.id.online_vehicle_number);
		mDriverPhoneNumberTextView = (TextView) findViewById(R.id.frag_online_summary_phn_number);
		mCarModelTextView = (TextView) findViewById(R.id.frag_online_summary_car_model);
		mShortRouteTextView = (TextView) findViewById(R.id.frag_online_short_route);
		mShortRouteDestinationTextView = (TextView) findViewById(R.id.frag_online_short_route_destination);
		mShortRouteLinearLayout = (LinearLayout) findViewById(R.id.frag_online_route_layout);
		mShortRouteDestinationLinearLayout = (LinearLayout) findViewById(R.id.frag_online_destination_route_layout);

		findViewById(R.id.frag_online_linear).setOnTouchListener(this);
		mStartTripButton.setOnClickListener(this);
		mWayBillInfo.setOnClickListener(this);
		mProfileImageView.setOnTouchListener(this);
		try {
			descriptor = getAssets().openFd("beep.wav");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// setUpCountDown();
	}

	AssetFileDescriptor descriptor;

	public void setUpCountDown(final Bundle bundle) {

		try {
			HideDialog();
		} catch (Exception e) {
		}

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (bundle != null) {
					mBookingId = bundle.getString("bookingId");
				}
				// tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION,
				// 100);

				postXMl(TaxiExConstants.BookingDetails,
						createXml(TaxiExConstants.BookingDetailsXMLKeys,
								new String[] { "",
										UserLoginModel.getInstance().getId(),
										mBookingId }), true,
						TaxiExConstants.BookingDetailsWebserviceResponse,
						OnlineStatusActivity.this);
				isCountDown = true;
				countDownLinear.setVisibility(View.VISIBLE);
				tap_accept.setVisibility(View.VISIBLE);
				mCountDownTimeTextView.setVisibility(View.VISIBLE);
				mStartTripButton.setVisibility(View.INVISIBLE);
				mVehicleDetailsLinear.setVisibility(View.VISIBLE);

				mShortRouteLinearLayout.setVisibility(View.VISIBLE);
				mShortRouteDestinationLinearLayout.setVisibility(View.GONE);
				// mStartTripButton
				// .setText(getString(R.string.online_accept_trip));

				if (mCountDownTimer != null) {

					mCountDownTimer.cancel();

				}

				mCountDownTimer = new CountDownTimer(COUNTER_COUNT * 1000, 1000) {

					@Override
					public void onTick(final long millisUntilFinished) {
						// TODO Auto-generated method stub
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								// tg.s

								playBeep();
								// tg.startTone(ToneGenerator.TONE_PROP_BEEP2,
								// 1000);

								mCountDownTimeTextView.setText(""
										+ (millisUntilFinished / 1000));
								if (COUNTER_COUNT > 0) {
									COUNTER_COUNT = COUNTER_COUNT - 1;
								}

							}
						});

					}

					@Override
					public void onFinish() {
						try {
							if (m != null) {
								if (m.isPlaying()) {
									m.stop();
									m.reset();

								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						mMap.clear();



						CustomLinearLayout.isIntercepting = false;


						postXMl(TaxiExConstants.CancelBookinfUrl,
								createXml(TaxiExConstants.CancelBookingXMLKeys, new
										String[] { "", UserLoginModel.getInstance() .getId(),
										mBookingId, "0", "", "3" }), true,
								TaxiExConstants.CancelBookingWebserviceResponse,
								OnlineStatusActivity.this);


						// auto logout is commented
						/*
						 * try { mIsAutoLogout = true;
						 * postXMl(TaxiExConstants.CancelBookinfUrl, createXml(
						 * TaxiExConstants.CancelBookingXMLKeys, new String[] {
						 * "", UserLoginModel .getInstance() .getId(),
						 * mBookingId, "0", "", "3" }), true,
						 * TaxiExConstants.CancelBookingWebserviceResponse,
						 * OnlineStatusActivity.this); } catch (Exception e) {
						 *
						 * e.printStackTrace(); }
						 */
					}

				}.start();
			}
		});

	}

	private MediaPlayer m = new MediaPlayer();

	public void playBeep() {
		try {
			if (m == null) {
				m = new MediaPlayer();

			}

			m.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					Log.i("Completion Listener", "Song Complete");
					mp.stop();
					mp.reset();
					try {
						mp.setDataSource(descriptor.getFileDescriptor(),
								descriptor.getStartOffset(),
								descriptor.getLength());
						mp.prepare();
						mp.start();
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

				}
			});
			m.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			// descriptor.close();
			m.setVolume(1f, 1f);
			m.prepareAsync();
			m.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					m.start();
				}
			});
			// m.setLooping(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
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
		switch (callback) {
			case TaxiExConstants.MyProfileParsingResponseId:
				try {
					mMyProfileDetailsModel = (MyProfileModel) modelClass;
					mDriverRatingTextView.setText(mMyProfileDetailsModel
							.getRating());
					// mDriverRatingBar.setRating(Float.valueOf(mMyProfileDetailsModel
					// .getRating()));
					mDriverNameTextView.setText(mMyProfileDetailsModel
							.getFirstName()
							+ " "
							+ mMyProfileDetailsModel.getLastName());
					mProfileImageView.setImageUrl(
							mMyProfileDetailsModel.getDriverImage(),
							R.drawable.profile_pic);
					mDriverPhoneNumberTextView.setText(mMyProfileDetailsModel
							.getPhone());

					mDriverPhoneNumberTextView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {

						}
					});
					mCarModelTextView.setVisibility(View.GONE);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case TaxiExConstants.BookingDetailsParsingResponse:
				try {
				/*
				 * mShortRouteLinearLayout.setVisibility(View.VISIBLE);
				 * mShortRouteDestinationLinearLayout
				 * .setVisibility(View.VISIBLE);
				 */
					mBookingDetailsRequestModel = (BookingDetailsRequestModel) modelClass;

					// LatLng latLng = new LatLng(
					// Double.valueOf(mBookingDetailsRequestModel
					// .getLatitude()),
					// Double.valueOf(mBookingDetailsRequestModel
					// .getLongitude()));
					// mMap.addMarker(new MarkerOptions().icon(
					// BitmapDescriptorFactory
					// .fromResource(R.drawable.map_pin)).position(
					// latLng));

					mProfileImageView.setImageUrl(
							mBookingDetailsRequestModel.getUserImage(), 0);
					mDriverNameTextView.setText(mBookingDetailsRequestModel
							.getFirstName()
							+ " "
							+ mBookingDetailsRequestModel.getLastName());
					mDriverRatingTextView.setText(mBookingDetailsRequestModel
							.getRating());
					mDriverPhoneNumberTextView.setText(Html.fromHtml("<u>" + mBookingDetailsRequestModel
							.getPhone() + "</u>"));



					mDriverPhoneNumberTextView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View view) {
							Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
									+mBookingDetailsRequestModel.getPhone() ));
							startActivity(intent);

						}
					});
					mCarModelTextView.setVisibility(View.VISIBLE);
					mCarModelTextView.setText(mBookingDetailsRequestModel.getETA());
					mShortRouteTextView.setText(mBookingDetailsRequestModel
							.getPickUpAddressvalue());

					if (!mBookingDetailsRequestModel.getDropOffAddressvalue()
							.equals("")) {
						mShortRouteDestinationTextView
								.setText(mBookingDetailsRequestModel
										.getDropOffAddressvalue());
					} else {
						mShortRouteDestinationTextView.setText("Drop as directed");
						mShortRouteDestinationTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
						mShortRouteDestinationTextView.setEnabled(false);
					}

					if (mBookingDetailsRequestModel.getPayvia().equals("0")) {

						findViewById(R.id.txtPayvia).setVisibility(View.VISIBLE);
						/*new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								findViewById(R.id.txtPayvia).setVisibility(
										View.GONE);
							}
						}, 5000);
*/
					}

					mShortRouteTextView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

						/*
						 * ShowSingleButtonDialog("Pickup Address",
						 * mBookingDetailsRequestModel .getDropOffFullAddress(),
						 * getString(R.string.alert_ok_button), 0,
						 * OnlineStatusActivity.this, 1);
						 */

							showLocationDialog("Pickup Address",
									mBookingDetailsRequestModel, 1);

						/*
						 * ShowSingleButtonDialog("Pickup Address",
						 * mBookingDetailsRequestModel .getPickUpFullAddress(),
						 * getString(R.string.alert_ok_button), 0,
						 * OnlineStatusActivity.this, 1);
						 */

						}
					});
/************************/
					mShortRouteDestinationTextView
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub

									showLocationDialog("DropOff Address",
											mBookingDetailsRequestModel, 2);
								/*

								 * ShowSingleButtonDialog("DropOff Address",
								 * mBookingDetailsRequestModel
								 * .getDropOffFullAddress(),
								 * getString(R.string.alert_ok_button), 0,
								 * OnlineStatusActivity.this, 1);
								 */

								}
							});

					if (isFromEndTrip) {

						finish();
						Intent fareSummaryIntent = new Intent(this,
								FareSummaryActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable(
								TaxiExConstants.BookingDetailsBundleKey,
								mBookingDetailsRequestModel);
						bundle.putString(TaxiExConstants.BookingDateBundleKey,
								dateCompleted);
						bundle.putString(TaxiExConstants.BookingTipBundleKey,
								tripFare);
						bundle.putBoolean(TaxiExConstants.IsFromEndTrip,
								isFromEndTrip);

						bundle.putString(TaxiExConstants.UserDiscount, discount);
						bundle.putString(TaxiExConstants.PaymentAmount, payment);
						fareSummaryIntent.putExtra(
								TaxiExConstants.BookingDetailsBundleKey, bundle);

						isFromEndTrip = false;
						startActivity(fareSummaryIntent);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
		}

	}

	private boolean mIsFromWayBillActivity = false;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.frag_online_waybill:
				mIsFromWayBillActivity = true;
				Intent intent = new Intent(this, WayBillActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("bookingId", mBookingId);
				intent.putExtra("id bundle", bundle);
				startActivity(intent);
				break;
			// case R.id.frag_online_navigate_to_position:
			// navigateToCurrentLocation(mCurrentLocation);
			// break;

			case R.id.frag_online_start_trip:
				// postXMl(TaxiExConstants., data, isDialog, Id, context)

//				if (GcmIntentService.player != null
//						&& GcmIntentService.player.isPlaying()) {
//					GcmIntentService.player.stop();
//
//				}

				if (FireBaseMessageService.player != null
						&& FireBaseMessageService.player.isPlaying()) {
					FireBaseMessageService.player.stop();

				}

				if (m != null && m.isPlaying()) {

					m.stop();
				}

				if (mStartTripButton.getText().toString()
						.equalsIgnoreCase(getString(R.string.online_start_trip))) {
					ShowDoubleButtonDialog(null,
							getString(R.string.alert_start_trip),
							getString(R.string.alert_yes_button),
							getString(R.string.alert_no_button), 0, this,
							DAILOG_STARTTRIP_ID);

				} else if (mStartTripButton.getText().toString()
						.equalsIgnoreCase(getString(R.string.online_arriving_now))) {
					// TaxiExConstants
					postXMl(TaxiExConstants.ArrivedUrl,
							createXml(TaxiExConstants.ArrivedXMLKeys, new String[] {
									"", UserLoginModel.getInstance().getId(),
									mBookingId }), true,
							TaxiExConstants.ArrivedWebserviceResponse, this);
				} else if (mStartTripButton.getText().toString()
						.equalsIgnoreCase(getString(R.string.online_end_trip))) {
					ShowDoubleButtonDialog(null,
							getString(R.string.alert_end_trip),
							getString(R.string.alert_yes_button),
							getString(R.string.alert_no_button), 0, this,
							DAILOG_ENDTRIP_ID);

				}
				break;

			default:
				break;
		}

	}

	@Override
	public void onSuccess(String[] response, int UNIQUE_ID) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (isCountDown) {

			try {
				if (mCountDownTimer != null) {
					mCountDownTimer.cancel();
					mCountDownTimer = null;

				}
				// tg.stopTone();
				if (m.isPlaying()) {
					m.stop();
					m.reset();

					// m = new MediaPlayer();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			countDownLinear.setVisibility(View.GONE);
			tap_accept.setVisibility(View.GONE);
			mVehicleDetailsLinear.setVisibility(View.GONE);
			mCountDownTimeTextView.setVisibility(View.GONE);
			mStartTripButton.setVisibility(View.VISIBLE);
			mIsAcceptedOnTouch = true;
			isCountDown = false;
			CustomLinearLayout.isIntercepting = false;
			mStartTripButton.setBackgroundColor(getResources().getColor(
					R.color.yellow));
			mStartTripButton.setText(getString(R.string.online_arriving_now));
			if (mBookingId != null) {
				postXMl(TaxiExConstants.AcceptTripUrl,
						createXml(TaxiExConstants.AcceptBookingXMLKeys,
								new String[] { "",
										UserLoginModel.getInstance().getId(),
										mBookingId }), true,
						TaxiExConstants.AcceptBookingWebserviceResponse, this);

			}

		}
		return true;

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// isCountDown = false;
		// if (mCountDownTimer != null) {
		if (googleApiClient != null) {
			//LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
		}
		Utilities.showHideKeyboard(false);

		try {
			mCountDownTimer.cancel();

			if (m != null) {
				if (m.isPlaying()) {
					m.stop();
					m.release();
					m = null;
					// m = new MediaPlayer();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// }
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		setIntent(new Intent());

		if (m != null) {

			if (m.isPlaying()) {
				m.stop();
				m.release();
			}

		}

		mIsInBackGround = true;
		if (mSharedprefrences.contains(TaxiExConstants.Pref_isOnline)) {
			if (!mSharedprefrences.getBoolean(TaxiExConstants.Pref_isOnline,
					false)) {
				// isOffline = true;

				if (!isCountDown) {
					mBookingDetailsRequestModel = null;
				}

			} else {
				if (mBookingDetailsRequestModel != null) {
					ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

					ObjectOutputStream objectOutput;
					try {
						objectOutput = new ObjectOutputStream(arrayOutputStream);
						objectOutput.writeObject(mBookingDetailsRequestModel);
						byte[] data = arrayOutputStream.toByteArray();
						objectOutput.close();
						arrayOutputStream.close();

						ByteArrayOutputStream out = new ByteArrayOutputStream();
						Base64OutputStream b64 = new Base64OutputStream(out,
								Base64.DEFAULT);
						b64.write(data);
						b64.close();
						out.close();

						editPrefs.putString(
								TaxiExConstants.pref_Booking_details,
								new String(out.toByteArray()));

						editPrefs.commit();

					} catch (IOException e) {
						e.printStackTrace();
					}

				}

			}
		} else {
			// isOffline = true;

			if (!isCountDown) {
				mBookingDetailsRequestModel = null;

			}

		}


	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// Do Nothing

		//super.onBackPressed();
	}

	private void showAdditionalChargesPopUp() {

		final Dialog dialog = new Dialog(OnlineStatusActivity.this,
				style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.dialog_additional_charges);
		dialog.getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		dialog.setCancelable(true);

		final EditText edtToll, edtTaxes;
		final TextView txtTotalActual;
		Button btnOk, btnCancel;

		edtToll = (EditText) dialog.findViewById(R.id.edtToll);
		edtTaxes = (EditText) dialog.findViewById(R.id.edtTaxes);



		txtTotalActual = (TextView) dialog.findViewById(R.id.txtTotalActual);

		btnOk = (Button) dialog.findViewById(R.id.btnOk);
		btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
		edtToll.requestFocus();
		edtToll.setRawInputType(Configuration.KEYBOARD_12KEY);
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.showSoftInput(edtToll, InputMethodManager.SHOW_FORCED);




		edtToll.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				Float toll = 0.0f, taxes = 0.0f,total = 0.0f;

				if (!edtToll.getText().toString().trim().equals("")
						&& !edtToll.getText().toString().trim().equals(".")) {

					toll = Float
							.parseFloat(edtToll.getText().toString().trim());
				}

				if (!edtTaxes.getText().toString().trim().equals("")
						&& !edtTaxes.getText().toString().trim().equals(".")) {

					taxes = Float.parseFloat(edtTaxes.getText().toString()
							.trim());
				}



				total = toll + taxes;

				txtTotalActual.setText(String.valueOf(String.format("%.2f",
						total)));
			}
		});
		edtTaxes.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

				float toll = 0.0f, taxes = 0.0f, total = 0.0f;

				if (!edtToll.getText().toString().trim().equals("")
						&& !edtToll.getText().toString().trim().equals(".")) {

					toll = Float
							.parseFloat(edtToll.getText().toString().trim());
				}

				if (!edtTaxes.getText().toString().trim().equals("")
						&& !edtTaxes.getText().toString().trim().equals(".")) {

					taxes = Float.parseFloat(edtTaxes.getText().toString()
							.trim());
				}



				total = toll + taxes;

				txtTotalActual.setText(String.valueOf(String.format("%.2f",
						total)));

			}
		});
		edtTaxes.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
										  KeyEvent event) {
				// TODO Auto-generated method stub

				if (actionId == EditorInfo.IME_ACTION_GO) {

					// TODO Auto-generated method stub

					String toll = "0", taxes = "0";

					if (!edtToll.getText().toString().trim().equals("")
							&& !edtToll.getText().toString().trim().equals(".")) {

						toll = edtToll.getText().toString().trim();
					}

					if (!edtTaxes.getText().toString().trim().equals("")
							&& !edtTaxes.getText().toString().trim()
							.equals(".")) {

						taxes = edtTaxes.getText().toString().trim();
					}




					((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
							.showSoftInput(edtToll,
									InputMethodManager.HIDE_IMPLICIT_ONLY);
					mIsFromEndTrip = true;
					postXMl(TaxiExConstants.EndTripUrl,
							createXml(
									TaxiExConstants.EndTripXMLKeys,
									new String[] {
											"",
											UserLoginModel.getInstance()
													.getId(),
											mBookingDetailsRequestModel
													.getRiderId(),
											mBookingId,
											mBookingDetailsRequestModel
													.getPayvia(), toll, taxes }),
							true, TaxiExConstants.EndTripWebserviceResponse,
							OnlineStatusActivity.this);
					dialog.dismiss();

				}
				return false;
			}
		});

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String toll = "0", taxes = "0";

				if (!edtToll.getText().toString().trim().equals("")
						&& !edtToll.getText().toString().trim().equals(".")) {

					toll = edtToll.getText().toString().trim();
				}

				if (!edtTaxes.getText().toString().trim().equals("")
						&& !edtTaxes.getText().toString().trim().equals(".")) {

					taxes = edtTaxes.getText().toString().trim();
				}


				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
						.showSoftInput(edtToll,
								InputMethodManager.HIDE_IMPLICIT_ONLY);

				mIsFromEndTrip = true;
				postXMl(TaxiExConstants.EndTripUrl,
						createXml(
								TaxiExConstants.EndTripXMLKeys,
								new String[] {
										"",
										UserLoginModel.getInstance().getId(),
										mBookingDetailsRequestModel
												.getRiderId(),
										mBookingId,
										mBookingDetailsRequestModel.getPayvia(),
										toll, taxes }), true,
						TaxiExConstants.EndTripWebserviceResponse,
						OnlineStatusActivity.this);
				dialog.dismiss();

			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub

				String toll = "0", taxes = "0";

				mIsFromEndTrip = true;
				postXMl(TaxiExConstants.EndTripUrl,
						createXml(
								TaxiExConstants.EndTripXMLKeys,
								new String[] {
										"",
										UserLoginModel.getInstance().getId(),
										mBookingDetailsRequestModel
												.getRiderId(),
										mBookingId,
										mBookingDetailsRequestModel.getPayvia(),
										toll, taxes }), true,
						TaxiExConstants.EndTripWebserviceResponse,
						OnlineStatusActivity.this);

				dialog.dismiss();
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
						.showSoftInput(edtToll,
								InputMethodManager.HIDE_IMPLICIT_ONLY);

			}
		});
		dialog.show();

	}

	private void showLocationDialog(String title,
									final BookingDetailsRequestModel obj, final int type) {

		final Dialog dialogLocation = new Dialog(OnlineStatusActivity.this,
				style.Theme_Translucent_NoTitleBar);

		dialogLocation.setContentView(R.layout.dialog_locations);

		TextView txtHeader, txtLocations;
		Button btnGetLocations,btnGetLocationsWazeMap;

		ImageView btnCancel;

		txtHeader = (TextView) dialogLocation.findViewById(R.id.txtHeader);
		txtLocations = (TextView) dialogLocation.findViewById(R.id.txtLocation);

		btnCancel = (ImageView) dialogLocation.findViewById(R.id.imgTouch);
		btnGetLocations = (Button) dialogLocation
				.findViewById(R.id.btnGetLocation);
		btnGetLocationsWazeMap=(Button)dialogLocation.findViewById(R.id.btnWazeMap);

		txtHeader.setText(title);
		if (type == 1) {

			txtLocations.setText(obj.getPickUpAddressvalue());

		} else {

			if (!obj.getDropOffAddressvalue().equals("")) {
				txtLocations.setText(obj.getDropOffAddressvalue());
			} else {
				//mShortRouteDestinationTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
				txtLocations.setText("Drop as directed");
				mShortRouteDestinationTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
				mShortRouteDestinationTextView.setEnabled(false);
			}

		}
/*****************************************/
		if (type == 2 && obj.getDropOffLat().equals("")) {
			//mShortRouteDestinationTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
			btnGetLocations.setVisibility(View.INVISIBLE);
			btnGetLocationsWazeMap.setVisibility(View.INVISIBLE);
		}



		btnGetLocationsWazeMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				try
				{

					String url="";

					if(type == 1){

						url = "waze://?ll="+ obj.getLatitude()+","+obj.getLongitude()+"&navigate=yes";

					}else{
						url = "waze://?ll="+obj.getDropOffLat() + ","
								+ obj.getDropOffLong()+"&navigate=yes";
					}


					Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
					startActivity( intent );
				}
				catch ( ActivityNotFoundException ex  )
				{
					Intent intent =
							new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
					startActivity(intent);
				}

				dialogLocation.dismiss();
			}
		});

		btnGetLocations.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("MissingPermission")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Location currentLocation = null;
				if(isPermssionGranted) {
					currentLocation = LocationServices.FusedLocationApi
							.getLastLocation(googleApiClient);

					if (type == 1) {
						Intent intent = new Intent(
								Intent.ACTION_VIEW, Uri
								.parse("http://maps.google.com/maps?saddr="
										+ currentLocation
										.getLatitude()
										+ ","
										+ currentLocation
										.getLongitude() + "&daddr="
										+ obj.getLatitude() + ","
										+ obj.getLongitude()));

						startActivity(intent);

					} else {

						Intent intent = new Intent(
								Intent.ACTION_VIEW, Uri
								.parse("http://maps.google.com/maps?saddr="
										+ currentLocation
										.getLatitude()
										+ ","
										+ currentLocation
										.getLongitude() + "&daddr="
										+ obj.getDropOffLat() + ","
										+ obj.getDropOffLong()));

						startActivity(intent);

					}

				}

				dialogLocation.dismiss();
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogLocation.dismiss();
			}
		});

		dialogLocation.setCancelable(true);

		dialogLocation.show();

	}


	@SuppressWarnings("MissingPermission")
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.getUiSettings().setZoomControlsEnabled(true);
		if(isPermssionGranted) {
			mMap.setMyLocationEnabled(true);

		}
		Log.d(TAG,"onMapReady");
		mMap.setOnMyLocationChangeListener(mOnMyLocationChangeListener);
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
		Log.d(TAG,"onConnected");
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(1000);
		mLocationRequest.setFastestInterval(1000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);

	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}

	Location mLastLocation;
	Marker mCurrLocationMarker;
	private Circle mCircle;
	double radiusInMeters = 100.0;
	int strokeColor = 0xffff0000; //Color Code you want
	int shadeColor = 0x44ff0000; //opaque red fill

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG,"onLocationChanged"+location);
		mLastLocation = location;
		if (mCurrLocationMarker != null) {
			mCurrLocationMarker.remove();
		}

		//Place current location marker
		LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(latLng);
		markerOptions.title("Current Position");
		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
		mCurrLocationMarker = mMap.addMarker(markerOptions);

		CircleOptions addCircle = new CircleOptions().center(latLng).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
		mCircle = mMap.addCircle(addCircle);

		//move map camera
		mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

		locationChanged(location);
		//stop location updates
		/*if (googleApiClient != null) {
			LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
		}*/
	}

	public void locationChanged(Location arg0){
		// TODO Auto-generated method stub
		// if (mStartTripButton.getVisibility() == View.INVISIBLE) {
		if(isPermssionGranted) {
			if (mProgressDialog != null) {

				if (mProgressDialog.isShowing()) {
					mProgressDialog.cancel();
				}
			}
			mLatitude = String.valueOf(arg0.getLatitude());
			mLongitude = String.valueOf(arg0.getLongitude());
			if (isFirstTime) {
				mOldLatitude = mLatitude;
				mOldLogitude = mLongitude;
				postXMl(TaxiExConstants.LatLongUpdateWithDistanceURL,
						createXml(
								TaxiExConstants.LatLongUpdateWithDistanceXMLKeys,
								new String[]{
										"",
										UserLoginModel.getInstance()
												.getId(),
										String.valueOf(arg0.getLatitude()),
										String.valueOf(arg0.getLongitude()),
										"0", String.valueOf("0"), String.valueOf(arg0.getBearing()), String.valueOf(arg0.getBearing())}), false,
						TaxiExConstants.LatLongUpdateWebserviceResponse,
						OnlineStatusActivity.this);
			}

			// showToast("Current" + arg0.getLatitude() + "old latitude = "
			// + mOldLatitude, 1000);
			mCurrentLocation = arg0;
			navigateToCurrentLocation(arg0);

			// } else if (mStartTripButton.getVisibility() == View.VISIBLE)
			// {
			Location old_location = new Location("locationA");

			if (mOldLogitude != null && mOldLatitude != null) {

				old_location.setLatitude(Double.valueOf(mOldLatitude));
				old_location.setLongitude(Double.valueOf(mOldLogitude));

			} else {
				old_location.setLatitude(Double.valueOf(mLatitude));
				old_location.setLongitude(Double.valueOf(mLongitude));

			}

			String bookingId = "0";
			String endTripString = "END TRIP";// getString(R.string.online_end_trip);
			if (mStartTripButton.getText().toString()
					.equalsIgnoreCase(endTripString)) {
				bookingId = mBookingId;
			}
			Log.e("Booking id for lat long", bookingId);
			distancecalculated = (double) (arg0.distanceTo(old_location));
			// showToast("distance in meters" + distancecalculated, 500);

			if (distancecalculated > 20.0) {
				double miles = distancecalculated * 0.000621;
				Log.e("Distance In String", "" + miles);
				// showToast("distance in miles" + miles, 500);

				mOldLatitude = mLatitude;
				mOldLogitude = mLongitude;

				LatLng myLatLng = new LatLng(arg0.getLatitude(),
						arg0.getLongitude());
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng,
						16));
				// showToast("Distance grater than 30 meters", 1000);
				postXMl(TaxiExConstants.LatLongUpdateWithDistanceURL,
						createXml(
								TaxiExConstants.LatLongUpdateWithDistanceXMLKeys,
								new String[]{
										"",
										UserLoginModel.getInstance()
												.getId(),
										String.valueOf(arg0.getLatitude()),
										String.valueOf(arg0.getLongitude()),
										bookingId, String.valueOf(miles), String.valueOf(arg0.getBearing()), String.valueOf(arg0.getBearing())}),
						false,
						TaxiExConstants.LatLongUpdateWebserviceResponse,
						OnlineStatusActivity.this);
			}
		}
		}
}
