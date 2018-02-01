package com.tms.driver.fragments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Base64InputStream;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tms.driver.GCMDialog_Activity;
import com.tms.driver.OnlineStatusActivity;
import com.tms.driver.R;
import com.tms.driver.SplashActivity;
import com.tms.driver.base.BaseFragment;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.models.UserLoginModel;


/**
 *
 * @author arvind.agarwal
 *
 *this fragment is used to show reservation details  and its status
 */
public class ReservationDetails extends BaseFragment {

	private View mReservationsDetailsView;
	private TextView txtName, txtPick, txtDestination, txtPickTime, txtPaymentMode,
			txtPickDate, txtPhNumber, txtCarType, txtAirLine, txtFlight, txtFare, txtItem, txtWeight, txtDimension, txtComments;
	private Button btnConfirm, btnDecline, btnMark;

	private boolean confirmationBool = false;
	private SharedPreferences mSharedprefrences;
	private SharedPreferences.Editor editPrefs;

	private String timeTag;
//	private Button btn;

	@Override
	public void onPositiveButtonClick(int id) {
		// TODO Auto-generated method stub
		super.onPositiveButtonClick(id);
		switch (id) {
			case 1:

				getActivity().finish();

				break;

			default:
				break;
		}
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

	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		super.onSuccess(response, UNIQUE_ID);

		switch (UNIQUE_ID) {


			// reservation details response
			case TaxiExConstants.ReservationDetailsResponse:

				// {"reservationDetail":{"id":"1","driverId":"0","name":"Kapil Bansal","picUpLoc":"DT Cinema Chandigarh","dropOffLoc":"ISBT 43 Chandigarh","picUpTime":"January 15, 2015","picUpDate":"February 12, 2015","phone":"+919780666296","fareQuote":"39","carType":"2","reservationStatus":"Confirmation Pending.","driverStatus":"0","paymentStatus":"1","invoiceId":"54b3ae746c671","airline":"Flight Name","flightNumber":"123456","reservStatus":"1"},"message":"You have reservations."}
				if (!checkIfStringIsNullOrEmpty(response)) {
					try {

						JSONObject jsonObject = new JSONObject(response);

						JSONObject jsonObjectChild = jsonObject
								.getJSONObject("reservationDetail");

						txtName.setText(jsonObjectChild.getString("name"));
						txtPick.setText(jsonObjectChild.getString("picUpLoc"));
						txtDestination.setText(jsonObjectChild
								.getString("dropOffLoc"));
						txtPickDate.setText(jsonObjectChild.getString("picUpDate"));
						txtPickTime.setText(jsonObjectChild.getString("picUpTime"));
						txtPhNumber.setText(jsonObjectChild.getString("phone"));
						txtCarType.setText(jsonObjectChild.getString("carType"));
						txtAirLine.setText(jsonObjectChild.getString("airline"));
						txtFlight
								.setText(jsonObjectChild.getString("flightNumber"));

						txtFare.setText(jsonObjectChild.getString("fareQuote"));

						txtPaymentMode.setText(jsonObjectChild.getString("paymentOption"));


						timeTag = jsonObjectChild.getString("picUpTime1");

						if (!jsonObjectChild.getString("itemToMove").equals("")) {

							txtItem.setText(jsonObjectChild.getString("itemToMove"));
							txtWeight.setText(jsonObjectChild.getString("weight"));
							txtDimension.setText(jsonObjectChild.getString("dimension"));
							txtComments.setText(jsonObjectChild.getString("comment"));
							txtItem.setVisibility(View.VISIBLE);
							txtWeight.setVisibility(View.VISIBLE);

							txtDimension.setVisibility(View.VISIBLE);
							txtComments.setVisibility(View.VISIBLE);
						}


						if (jsonObjectChild.getString("reservationSts").equals("1")) {

							btnMark.setText("ON SITE");
							btnMark.setVisibility(View.VISIBLE);
							btnConfirm.setVisibility(View.GONE);
							btnDecline.setVisibility(View.GONE);
						} else if (jsonObjectChild.getString("reservationSts").equals("2")) {
							btnMark.setText("ON SITE");
							btnMark.setVisibility(View.GONE);
							btnConfirm.setVisibility(View.VISIBLE);
							btnDecline.setVisibility(View.VISIBLE);

						} else if (jsonObjectChild.getString("reservationSts").equals("3")) {
							btnMark.setText("ON SITE");
							btnMark.setVisibility(View.GONE);
							btnConfirm.setVisibility(View.VISIBLE);
							btnDecline.setVisibility(View.VISIBLE);

						} else {
							btnMark.setText("ON SITE");
							btnMark.setVisibility(View.GONE);
							btnConfirm.setVisibility(View.GONE);
							btnDecline.setVisibility(View.GONE);

						}
					
					
				/*	if(jsonObjectChild.getString("driverStatus").equals("1") ){
						
						if(jsonObjectChild.getString("reservationCurStatus").equals("1")){
							
							btnMark.setText("ON SITE");
							btnMark.setVisibility(View.VISIBLE);
							btnConfirm.setVisibility(View.GONE);
							btnDecline.setVisibility(View.GONE);
						}else 	if(jsonObjectChild.getString("reservationCurStatus").equals("2")){
							
							btnMark.setText("MARK AS COMPLETE");
							btnMark.setVisibility(View.GONE);
						}else {
							
							btnMark.setVisibility(View.GONE);
						}
						
						
					}else{
						btnMark.setVisibility(View.GONE);
					}*/

					} catch (Exception e) {
						// TODO: handle exception
					}

				}

				break;

			case TaxiExConstants.ReservationConfirmResponse:


				// reservation confirmation response
			/*
			 * Success :
			 * {"acceptResBooking":"1","message":"Confirmation Successful."}
			 * Already confirmed:
			 * {"acceptResBooking":"-3","message":"you have already confirm."}
			 */
				if (!checkIfStringIsNullOrEmpty(response)) {

					try {
						JSONObject jsonObject = new JSONObject(response);
						if (jsonObject.getString("acceptResBooking").equals("-3")) {
							ShowSingleButtonDialog(null,
									jsonObject.getString("message"),
									getString(R.string.alert_ok_button), 0, this, 1);

						} else if (jsonObject.getString("acceptResBooking").equals("-4")) {

							Intent i = new Intent(getActivity(),
									GCMDialog_Activity.class);
							i.putExtra("messageType", "cancel");
							i.putExtra("message", jsonObject.getString("message"));

							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TASK);

							startActivity(i);


						} else if (jsonObject.getString("acceptResBooking").equals(
								"1")) {

							if (confirmationBool) {

								ShowSingleButtonDialog(null,
										jsonObject.getString("message"),
										getString(R.string.alert_ok_button), 0, this, 1);
								btnMark.setVisibility(View.GONE);

								SimpleDateFormat dateFormat = new SimpleDateFormat(
										"yyyy-MM-dd hh:mm a");

								Date date = null;
								try {
									date = dateFormat.parse(timeTag);

								} catch (Exception e) {
									e.printStackTrace();
								}


								if (android.os.Build.VERSION.SDK_INT >= 23) {
									if ((ContextCompat.checkSelfPermission(getActivity(),
											"android.permission.WRITE_CALENDAR") == PackageManager.PERMISSION_GRANTED)) {
										addReminder(date.getTime() - 60 * 60 * 1000, txtPickTime.getText().toString().trim(), txtPick.getText().toString().trim());
									}
								}else{
									addReminder(date.getTime() - 60 * 60 * 1000, txtPickTime.getText().toString().trim(), txtPick.getText().toString().trim());
								}


						/*ShowSingleButtonDialog(null,
								jsonObject.getString("message"),
								getString(R.string.alert_ok_button), 0, this, 2);
						
						btnMark.setVisibility(View.VISIBLE);
						btnMark.setText("ON SITE");*/
							} else {
								ShowSingleButtonDialog(null,
										jsonObject.getString("message"),
										getString(R.string.alert_ok_button), 0, this, 1);
								btnMark.setVisibility(View.GONE);

							}

							btnConfirm.setVisibility(View.GONE);
							btnDecline.setVisibility(View.GONE);

						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				break;


			case TaxiExConstants.ReservationMarkResponse:

				if (!checkIfStringIsNullOrEmpty(response)) {
					try {
						JSONObject jsonObject = new JSONObject(response);


						ShowSingleButtonDialog(null,
								jsonObject.getString("message"),
								getString(R.string.alert_ok_button), 0, this, 1);


					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}


				break;


			case TaxiExConstants.ReservationOnSiteResponse:

				if (!checkIfStringIsNullOrEmpty(response)) {


					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(response);


						//	{"reservationOnsite":"-2","message":"Server Error."}

						if (Integer.parseInt(jsonObject.getString("reservationOnsite")) > 0) {

							startActivity(new Intent(getActivity(),
									OnlineStatusActivity.class).putExtra(TaxiExConstants.INTENT_TAG, TaxiExConstants.RESERVATION_TO_ONLINE).putExtra(TaxiExConstants.BOOKING_ID, jsonObject.getString("reservationOnsite")).putExtra(TaxiExConstants.PICK_UP_ADDRESS, txtPick.getText().toString()));

							btnMark.setVisibility(View.GONE);
							btnMark.setText("MARK AS COMPLETE");
						} else if (Integer.parseInt(jsonObject.getString("reservationOnsite")) == -4) {

							Intent i = new Intent(getActivity(),
									GCMDialog_Activity.class);
							i.putExtra("messageType", "cancel");
							i.putExtra("message", jsonObject.getString("message"));

							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TASK);

							startActivity(i);

						} else {
							ShowSingleButtonDialog(null,
									jsonObject.getString("message"),
									getString(R.string.alert_ok_button), 0, this, 1);
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


				}

				break;

			default:
				break;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		setRightIconVisibility(View.GONE);
		setLeftIconVisibility(View.GONE);
		setHeaderTitle(getResources().getString(R.string.reservation_details));

		if (getArguments() != null) {

			if ((getArguments().getInt("TYPE") == 0)) {
				btnConfirm.setVisibility(View.VISIBLE);
				btnDecline.setVisibility(View.VISIBLE);
				btnMark.setVisibility(View.GONE);

			} else if ((getArguments().getInt("TYPE") == 1)) {
				btnConfirm.setVisibility(View.GONE);
				btnDecline.setVisibility(View.GONE);

				btnMark.setVisibility(View.GONE);

			} else if ((getArguments().getInt("TYPE") == 2)) {
				btnConfirm.setVisibility(View.GONE);
				btnDecline.setVisibility(View.GONE);

				btnMark.setVisibility(View.GONE);

			} else if ((getArguments().getInt("TYPE") == 3)) {
				btnConfirm.setVisibility(View.GONE);
				btnDecline.setVisibility(View.GONE);

				btnMark.setVisibility(View.GONE);

			} else if ((getArguments().getInt("TYPE") == 4)) {
				btnConfirm.setVisibility(View.VISIBLE);
				btnDecline.setVisibility(View.VISIBLE);
				btnMark.setVisibility(View.GONE);
				setHeaderTitle("NEW RESERVATION REQUEST");

			} else if ((getArguments().getInt("TYPE") == 5)) {
				btnConfirm.setVisibility(View.GONE);
				btnDecline.setVisibility(View.GONE);
				btnMark.setVisibility(View.GONE);

				setHeaderTitle("RESERVATION CANCELLED");


			}


			postXMl(TaxiExConstants.ReservationDetails,
					createXml(
							TaxiExConstants.ReservationDetailsXMLKeys,
							new String[]{"",
									getArguments().getString("RESERVATION_ID")}),
					true, TaxiExConstants.ReservationDetailsResponse, this);

		}
	}

	@Override
	public View setContentView(LayoutInflater inflater, ViewGroup container,
							   Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		mReservationsDetailsView = inflater.inflate(
				R.layout.frag_reservation_details, null);
		initializecomponents();

		// <?xml version="1.0" encoding="UTF-8"
		// ?><reservationList><reservationId><![CDATA[1]]></reservationId></reservationList>


		return mReservationsDetailsView;
	}

	/**
	 * initializing views
	 */

	private void initializecomponents() {
//		btn=(Button)mReservationsDetailsView.findViewById(R.id.btn);
		mSharedprefrences = getActivity().getSharedPreferences(
				TaxiExConstants.SharedPreferenceName, 0);
		editPrefs = mSharedprefrences.edit();
		upDatingDriverInstance();

		txtItem = (TextView) mReservationsDetailsView.findViewById(R.id.edtItemToMove);
		txtComments = (TextView) mReservationsDetailsView.findViewById(R.id.edtComments);
		txtDimension = (TextView) mReservationsDetailsView.findViewById(R.id.edtDimensions);
		txtWeight = (TextView) mReservationsDetailsView.findViewById(R.id.edtWeight);

		txtName = (TextView) mReservationsDetailsView
				.findViewById(R.id.edtName);
		txtPick = (TextView) mReservationsDetailsView
				.findViewById(R.id.edtPLoc);
		txtDestination = (TextView) mReservationsDetailsView
				.findViewById(R.id.edtDLoc);
		txtPickTime = (TextView) mReservationsDetailsView
				.findViewById(R.id.edtPTime);
		txtPickDate = (TextView) mReservationsDetailsView
				.findViewById(R.id.edtPDate);
		txtPhNumber = (TextView) mReservationsDetailsView
				.findViewById(R.id.edtPhNumber);
		txtCarType = (TextView) mReservationsDetailsView
				.findViewById(R.id.edtCarType);
		txtAirLine = (TextView) mReservationsDetailsView
				.findViewById(R.id.edtAirLine);
		txtFlight = (TextView) mReservationsDetailsView
				.findViewById(R.id.edtFlight);
		txtPaymentMode = (TextView) mReservationsDetailsView.findViewById(R.id.edtPaymentMode);
		btnConfirm = (Button) mReservationsDetailsView
				.findViewById(R.id.btnConfirm);
		btnDecline = (Button) mReservationsDetailsView
				.findViewById(R.id.btnDecline);
		txtFare = (TextView) mReservationsDetailsView.findViewById(R.id.edtFare);
		btnMark = (Button) mReservationsDetailsView.findViewById(R.id.btnMark);
		btnMark.setOnClickListener(this);
		btnConfirm.setOnClickListener(this);
		btnDecline.setOnClickListener(this);
		//	btn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {


			case R.id.btnMark:

				//		"<?xml version="1.0" encoding="UTF-8" ?><reservationList><reservationId><![CDATA[1]]></reservationId></reservationOnsite>"


				if (btnMark.getText().toString().equals("ON SITE")) {
					SharedPreferences mSharedprefrences = getActivity().getSharedPreferences(
							TaxiExConstants.SharedPreferenceName, 0);


					String deviceToken = mSharedprefrences.getString(
							TaxiExConstants.Pref_GCM_key, "");
					postXMl(TaxiExConstants.ReservationOnSite, createXml(TaxiExConstants.ReservationOnSiteKeys,
							new String[]{"", getArguments().getString("RESERVATION_ID"), deviceToken
							}), true, TaxiExConstants.ReservationOnSiteResponse, this);

				} else {

					postXMl(TaxiExConstants.ReservationMarkAsComplete
									+ getArguments().getString("RESERVATION_ID"),
							"", true, TaxiExConstants.ReservationMarkResponse, this);
				}
				break;
			case R.id.btnConfirm:

				confirmationBool = true;
				// if accepting set reserStatus =1

				SharedPreferences mSharedprefrences = getActivity().getSharedPreferences(
						TaxiExConstants.SharedPreferenceName, 0);


				String deviceToken = mSharedprefrences.getString(
						TaxiExConstants.Pref_GCM_key, "");

				postXMl(TaxiExConstants.ReservationConfirm
								+ getArguments().getString("RESERVATION_ID") + "&driverId="
								+ UserLoginModel.getInstance().getId() + "&reserStatus=1" + "&deviceToken="
								+ deviceToken,
						"", true, TaxiExConstants.ReservationConfirmResponse, this);

				break;
			case R.id.btnDecline:


				// if declining set reserStatus =2
				SharedPreferences mSharedprefrences2 = getActivity().getSharedPreferences(
						TaxiExConstants.SharedPreferenceName, 0);


				String deviceToken2 = mSharedprefrences2.getString(
						TaxiExConstants.Pref_GCM_key, "");

				confirmationBool = false;
				postXMl(TaxiExConstants.ReservationConfirm
								+ getArguments().getString("RESERVATION_ID") + "&driverId="
								+ UserLoginModel.getInstance().getId() + "&reserStatus=2" + "&deviceToken="
								+ deviceToken2,
						"", true, TaxiExConstants.ReservationConfirmResponse, this);

				break;

			default:
				break;
		}
	}


	/**
	 *  updating diver instance if directly comes to this screen from Notification
	 */
	private void upDatingDriverInstance() {
		byte[] bytes = mSharedprefrences.getString(
				TaxiExConstants.Pref_Login_Details, "{}").getBytes();
		if (bytes.length == 0) {
			return;
		}
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
		}
	}


	/**
	 *
	 * @param timeInMills(time in milliseconds to add reminder)
	 * @param time(time to show in reminder message)
	 * @param placeToPick(place to show where to pick)
	 */
	@SuppressWarnings("MissingPermission")
	private void addReminder(long timeInMills, String time, String placeToPick) {
		Calendar cal = Calendar.getInstance();


		// Uri.parse(getCalendarUriBase(getActivity()) + "events");
		ContentResolver cr = getActivity().getContentResolver();


		// event insert
		ContentValues values = new ContentValues();
		values.put("calendar_id", 1);
		values.put("eventTimezone", TimeZone.getDefault().getID());
		values.put("title", "Sky Limo Reservation");
		values.put("allDay", 0);
		values.put("dtstart", timeInMills); // event starts at 11 minutes from now
		values.put("dtend", timeInMills + 60 * 1000); // ends 60 minutes from now
		values.put("description", "You have reservation at " + time + " from " + placeToPick + ".");
		//	values.put("visibility", 0);
		values.put("hasAlarm", 1);


		//	Uri EVENTS_URI =cr.insert(Events.CONTENT_URI, values);
		Uri event = cr.insert(Events.CONTENT_URI, values);

		// reminder insert
	//	Uri REMINDERS_URI = cr.insert(Reminders.CONTENT_URI, values);//Uri.parse(getCalendarUriBase(getActivity()) + "reminders");
		values = new ContentValues();
		values.put( "event_id", Long.parseLong(event.getLastPathSegment()));
		values.put( "method", 1 );
		values.put( "minutes", 10 );
		cr.insert( Reminders.CONTENT_URI, values );
	}
}
