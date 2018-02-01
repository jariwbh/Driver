package com.tms.driver.fragments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.tms.driver.MainActivity;
import com.tms.driver.OnlineStatusActivity;
import com.tms.driver.R;
import com.tms.driver.SplashActivity;
import com.tms.driver.base.BaseFragment;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.constant.TaxiExConstants.FRAGMENT_TAGS;
import com.tms.driver.fragments.ChangeVehicleFragment;
import com.tms.driver.models.UserLoginModel;
import com.tms.driver.models.VehicleListModel;
import com.tms.driver.utilities.Utilities;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * 
 * @author arvind.agarwal
 * 
 *         this fragment shows duty status of driver
 *
 */
public class DutyStatusFragment extends BaseFragment {
	public static VehicleListModel mVehicleListModel;
	private TextView mVehicleStatusTextView;
	private SharedPreferences mSharedprefrences, mSharedprefrences1;
	private SharedPreferences.Editor editPrefs;
	private static final int ALERT_ALREADY_LOGIN = 101;
	private static final int ALERT_INVALID_USER = 102;
	private static final int ALERT_SERVER_ERROR = 103;

	private GoogleCloudMessaging gcm;
	String GCM = "";

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

	/**
	 * this interface catches response from server
	 */
	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		super.onSuccess(response, UNIQUE_ID);
		switch (UNIQUE_ID) {
		case TaxiExConstants.DriverStatusWebserviceResponse:


			if (!checkIfStringIsNullOrEmpty(response)) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					int driverStatusCode = jsonObject
							.getInt("driverStatusOnline");
					String message = jsonObject.getString("message");
					switch (driverStatusCode) {
					case 1:
						startActivity(new Intent(getActivity(),
								OnlineStatusActivity.class));

						break;
					case -3:
						ShowSingleButtonDialog(null, message,
								getString(R.string.alert_ok_button), 0, this,
								ALERT_ALREADY_LOGIN);
						break;
					case -1:
						ShowSingleButtonDialog(null, message,
								getString(R.string.alert_ok_button), 0, this,
								ALERT_INVALID_USER);

						break;
					case -2:
						ShowSingleButtonDialog(null, message,
								getString(R.string.alert_ok_button), 0, this,
								ALERT_SERVER_ERROR);

						break;

					default:
						break;
					}

				} catch (Exception e) {
					e.printStackTrace();

				}

			}
			// startActivity(new Intent(getActivity(),
			// OnlineStatusActivity.class));
			break;

		default:
			break;
		}
	}

	/**
	 * this interface is used for tackling positive button click
	 */
	@Override
	public void onPositiveButtonClick(int id) {
		// TODO Auto-generated method stub
		super.onPositiveButtonClick(id);

		switch (id) {
		case ALERT_ALREADY_LOGIN:
			editPrefs.clear();
			editPrefs.commit();
			if (getActivity() != null) {

				getActivity().finishAffinity();
				startActivity(new Intent(getActivity(), MainActivity.class));
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

	View dutyStatusView;
	private TextView mChangeVehicleTextView, mDriverNameTextView;
	private ImageView mCarSelectedImageView, mGoOnlineTextView;

	@Override
	public View setContentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

        if(dutyStatusView==null) {

            dutyStatusView = inflater.inflate(R.layout.frag_duty_status, null);
            initializeComponents();
        }

		return dutyStatusView;
	}

	/**
	 * initializing view
	 */
	private void initializeComponents() {
		// TODO Auto-generated method stub
		mSharedprefrences = getActivity().getSharedPreferences(
				TaxiExConstants.SharedPreferenceName, 0);
		mSharedprefrences1 = getActivity().getSharedPreferences(
				TaxiExConstants.SharedPreferenceName1, 0);
		editPrefs = mSharedprefrences.edit();

		mGoOnlineTextView = (ImageView) dutyStatusView
				.findViewById(R.id.frag_duty_go_online_text);
		mChangeVehicleTextView = (TextView) dutyStatusView
				.findViewById(R.id.frag_duty_status_change_vehicle);
		mDriverNameTextView = (TextView) dutyStatusView
				.findViewById(R.id.fragduty_driver_name_text);
		mGoOnlineTextView.setOnClickListener(this);
		mChangeVehicleTextView.setOnClickListener(this);
		mVehicleStatusTextView = (TextView) dutyStatusView
				.findViewById(R.id.frag_duty_status_vehicle_details);
		mCarSelectedImageView = (ImageView) dutyStatusView
				.findViewById(R.id.frag_duty_status_car_selected_image);
		dutyStatusView.findViewById(R.id.fragduty_go_online_image)
				.setOnClickListener(this);
		mDriverNameTextView.setText("Welcome \n"
				+ UserLoginModel.getInstance().getFirstName() + " "
				+ UserLoginModel.getInstance().getLastName());
		byte[] bytes = mSharedprefrences.getString(
				TaxiExConstants.Pref_selected_vehicle, "").getBytes();
		if (bytes.length == 0) {

			mVehicleListModel = null;
			return;
		}
		ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
		Base64InputStream base64InputStream = new Base64InputStream(byteArray,
				Base64.DEFAULT);
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(base64InputStream);
			mVehicleListModel = (VehicleListModel) in.readObject();
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

		if (mVehicleListModel != null) {
			updatUi();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();


		// setting header title
		setHeaderTitle(getString(R.string.duty_status_title));

		// set right icon visibility
		setRightIconVisibility(View.VISIBLE);
		// set left icon visibility
		setLeftIconVisibility(View.VISIBLE);
		// set left icon background
		setLeftIcon(R.drawable.selector_profile_button);
		// set right icon background
		
		if (TaxiExConstants.IS_RESERVATION_AVAILABLE) {
			setRightIcon(R.drawable.button_reservation_listing);
		} else {
			setRightIcon(R.drawable.selector_logout_button);
		}
		
		

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.fragduty_go_online_image:
			try {
				if (mVehicleListModel != null) {

					if (!TextUtils.isEmpty(mSharedprefrences.getString(
							TaxiExConstants.Pref_GCM_key, ""))) {// if GCM id is
																	// not empty
						GCM = mSharedprefrences.getString(
								TaxiExConstants.Pref_GCM_key, "");

						// changing driver status to online and navigating to
						// Onlinestatus screen

						postXMl(TaxiExConstants.DriverStatusOnlineUrl,
								createXml(
										TaxiExConstants.DriverOnlineStatusXMLKeys,
										new String[] {
												"",
												UserLoginModel.getInstance()
														.getId(), GCM }), true,
								TaxiExConstants.DriverStatusWebserviceResponse,
								this);

					} else if (!TextUtils.isEmpty(mSharedprefrences1.getString(
							TaxiExConstants.Pref_GCM_key, ""))) {// if GCM id is
																	// not empty

						GCM = mSharedprefrences1.getString(
								TaxiExConstants.Pref_GCM_key, "");

						// changing driver status to online and navigating to
						// Onlinestatus screen
						postXMl(TaxiExConstants.DriverStatusOnlineUrl,
								createXml(
										TaxiExConstants.DriverOnlineStatusXMLKeys,
										new String[] {
												"",
												UserLoginModel.getInstance()
														.getId(), GCM }), true,
								TaxiExConstants.DriverStatusWebserviceResponse,
								this);

					} else {// if GCM id is empty, registering for GCM

//						registerInBackground();
						GCM = FirebaseInstanceId.getInstance().getToken();
						if(GCM!=null && !GCM.equals("")){
							postXMl(TaxiExConstants.DriverStatusOnlineUrl,
									createXml(
											TaxiExConstants.DriverOnlineStatusXMLKeys,
											new String[] {
													"",
													UserLoginModel.getInstance()
															.getId(), GCM }), true,
									TaxiExConstants.DriverStatusWebserviceResponse,
									this);
						}
					}

				} else {
					Utilities.getInstance(getActivity());
					ShowSingleButtonDialog(null,
							"Please first choose a vehicle.",
							getString(R.string.alert_ok_button), 0, this, 1);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.frag_duty_go_online_text:
			// replaceFragment(R.id.acitivity_duty_status_frag,
			// new MyProfileFragment(),
			// FRAGMENT_TAGS.MYPROFILEFRGMENT.name(), true, getActivity());

			try {
				if (mVehicleListModel != null) {

					if (!TextUtils.isEmpty(mSharedprefrences.getString(
							TaxiExConstants.Pref_GCM_key, ""))) {// if GCM id is
																	// not empty
																	// in
																	// sharedPreference
																	// 1
						GCM = mSharedprefrences.getString(
								TaxiExConstants.Pref_GCM_key, "");

						// changing driver status to online and navigating to
						// Onlinestatus screen

						postXMl(TaxiExConstants.DriverStatusOnlineUrl,
								createXml(
										TaxiExConstants.DriverOnlineStatusXMLKeys,
										new String[] {
												"",
												UserLoginModel.getInstance()
														.getId(), GCM }), true,
								TaxiExConstants.DriverStatusWebserviceResponse,
								this);

					} else if (!TextUtils.isEmpty(mSharedprefrences1.getString(
							TaxiExConstants.Pref_GCM_key, ""))) {// if GCM id is
																	// not empty
																	// in
																	// sharedPreference
																	// 2

						GCM = mSharedprefrences1.getString(
								TaxiExConstants.Pref_GCM_key, "");

						// changing status of Driver to online

						postXMl(TaxiExConstants.DriverStatusOnlineUrl,
								createXml(
										TaxiExConstants.DriverOnlineStatusXMLKeys,
										new String[] {
												"",
												UserLoginModel.getInstance()
														.getId(), GCM }), true,
								TaxiExConstants.DriverStatusWebserviceResponse,
								this);

					} else {

						// registering GCM id
						GCM = FirebaseInstanceId.getInstance().getToken();
						if(GCM!=null && !GCM.equals("")){
							postXMl(TaxiExConstants.DriverStatusOnlineUrl,
									createXml(
											TaxiExConstants.DriverOnlineStatusXMLKeys,
											new String[] {
													"",
													UserLoginModel.getInstance()
															.getId(), GCM }), true,
									TaxiExConstants.DriverStatusWebserviceResponse,
									this);
						}
//						registerInBackground();

					}

				} else {// if selected vehicle model is null

					// alert message to select a vehicle first
					Utilities.getInstance(getActivity());
					ShowSingleButtonDialog(null,
							"Please first choose a vehicle.",
							getString(R.string.alert_ok_button), 0, this, 1);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.frag_duty_status_change_vehicle:

			replaceFragment(R.id.acitivity_duty_status_frag,
					new ChangeVehicleFragment(),
					FRAGMENT_TAGS.CHANGEVEHICLEFRAGMENT.name(), true,
					getActivity());
			break;

		default:
			break;
		}
	}

	@Override
	public void onFailure(Throwable e, String error, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		ShowSingleButtonDialog(null, error,
				getString(R.string.alert_ok_button), 0, this, 1);
	}

	/**
	 * setting selected car type
	 */
	private void updatUi() {
		// TODO Auto-generated method stub
		mVehicleStatusTextView.setText(mVehicleListModel.getTaxiModel() + "\n"
				+ mVehicleListModel.getTaxiNumber());

		if (mVehicleListModel.getTaxiTypeId().equalsIgnoreCase("1")) {
			mCarSelectedImageView.setImageDrawable(getResources().getDrawable(
					R.drawable.car_13));
		}

		else if (mVehicleListModel.getTaxiTypeId().equalsIgnoreCase("2")) {
			mCarSelectedImageView.setImageDrawable(getResources().getDrawable(
					R.drawable.car_12));
		}

		else if (mVehicleListModel.getTaxiTypeId().equalsIgnoreCase("3")) {
			mCarSelectedImageView.setImageDrawable(getResources().getDrawable(
					R.drawable.car_11));
		}	else if (mVehicleListModel.getTaxiTypeId().equalsIgnoreCase("4")) {
			mCarSelectedImageView.setImageDrawable(getResources().getDrawable(
					R.drawable.car_12));
		}else if (mVehicleListModel.getTaxiTypeId().equalsIgnoreCase("5")) {
            mCarSelectedImageView.setImageDrawable(getResources().getDrawable(
                    R.drawable.car_11));
        }

		

	}

	/**
	 * 
	 * @param bundle
	 * 
	 * 
	 *            this method is used to update selected vehicle info fetches
	 *            vehicle information from shared preferences in byte form and
	 *            add it to mVehicleListModel object
	 * 
	 */
	public void updateDutyStatusUI(Bundle bundle) {

		if (bundle != null) {
			try {
				mVehicleListModel = (VehicleListModel) bundle
						.getSerializable(TaxiExConstants.VehicleChangedBundleKey);
				updatUi();
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

				ObjectOutputStream objectOutput;
				try {
					objectOutput = new ObjectOutputStream(arrayOutputStream);
					objectOutput.writeObject(mVehicleListModel);
					byte[] data = arrayOutputStream.toByteArray();
					objectOutput.close();
					arrayOutputStream.close();

					ByteArrayOutputStream out = new ByteArrayOutputStream();
					Base64OutputStream b64 = new Base64OutputStream(out,
							Base64.DEFAULT);
					b64.write(data);
					b64.close();
					out.close();

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

	}

	/**
	 * this method is used to register GCM id with Google cloud server
	 */

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(getActivity());
					}
					GCM = gcm.register(SplashActivity.SENDER_ID);
					msg = "Device registered, registration ID=" + GCM;

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();

				}
				return GCM;
			}

			@Override
			protected void onPostExecute(String msg) {

				postXMl(TaxiExConstants.DriverStatusOnlineUrl,
						createXml(TaxiExConstants.DriverOnlineStatusXMLKeys,
								new String[] { "",
										UserLoginModel.getInstance().getId(),
										GCM }), true,
						TaxiExConstants.DriverStatusWebserviceResponse,
						DutyStatusFragment.this);
			}
		}.execute();
	}
}
