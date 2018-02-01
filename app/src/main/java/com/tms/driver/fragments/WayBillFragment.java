package com.tms.driver.fragments;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tms.driver.R;
import com.tms.driver.base.BaseFragment;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.models.UserLoginModel;
import com.tms.driver.models.WayBillModel;
import com.tms.driver.parse.ParseContent;
import com.tms.driver.parse.ParseContent.CONTENT_TYPE;

public class WayBillFragment extends BaseFragment {

	private WayBillModel mWayBillModel;

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

		switch (callback) {
		case TaxiExConstants.BookingIdParsingid:
			if (modelClass != null) {
				mWayBillModel = (WayBillModel) modelClass;
				updateUI(mWayBillModel);
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

	String mBookingId = "";

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (getArguments() != null) {
			Bundle bundle = getArguments();
			mBookingId = bundle.getString("bookingId");
		}
		getUrlHit(TaxiExConstants.BookingIdUrl + ""
				+ UserLoginModel.getInstance().getId() + "&bookingId="
				+ mBookingId, TaxiExConstants.BookingIdWebServiceResponse);
		setHeaderTitle(getString(R.string.way_bill_title));
		setLeftIconVisibility(View.GONE);
		setRightIconVisibility(View.GONE);
	}

	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		super.onSuccess(response, UNIQUE_ID);
		switch (UNIQUE_ID) {
		case TaxiExConstants.BookingIdWebServiceResponse:
			Log.e("Way Bill response", response);

			if (!checkIfStringIsNullOrEmpty(response)) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					ParseContent.getInstance().parseContentFromjson(
							CONTENT_TYPE.WAYBILL, jsonObject, this,
							TaxiExConstants.BookingIdParsingid);

				} catch (Exception e) {
					e.printStackTrace();
					ShowSingleButtonDialog(null, "No Details Found!",
							getString(R.string.alert_ok_button), 0, this, 1);
				}

			}
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
		case 1:

			getActivity().finish();
			break;

		default:
			break;
		}
	}

	private TextView mTaxiNameTextView, mDriverNameTextView,
			mDriverLicenseTextView, mDriverLicensePlateTextView, mTimeTextView,
			mRateTextView, mPassengersTextView, mTripTextView, mViaTextView,
			mPassengerNameTextView, mFromTextView, mWayBillToTextView;
	private View mWayBillView;

	@Override
	public View setContentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mWayBillView == null) {
			mWayBillView = inflater.inflate(R.layout.frag_way_bill, null);
			initializecomponents();

		}

		return mWayBillView;
	}

	private void initializecomponents() {
		mTaxiNameTextView = (TextView) mWayBillView
				.findViewById(R.id.frag_way_bill_taxi_name);
		mDriverNameTextView = (TextView) mWayBillView
				.findViewById(R.id.way_bill_driver_name_textView);
		mDriverLicenseTextView = (TextView) mWayBillView
				.findViewById(R.id.way_bill_driver_license_textView);
		mDriverLicensePlateTextView = (TextView) mWayBillView
				.findViewById(R.id.way_bill_driver_license_plate_textView);
		mTimeTextView = (TextView) mWayBillView
				.findViewById(R.id.way_bill_time_textView);
		mRateTextView = (TextView) mWayBillView
				.findViewById(R.id.way_bill_rate_textView);
		mPassengersTextView = (TextView) mWayBillView
				.findViewById(R.id.way_bill_passengers_textView);
		mTripTextView = (TextView) mWayBillView
				.findViewById(R.id.way_bill_trip_textView);
		mViaTextView = (TextView) mWayBillView
				.findViewById(R.id.way_bill_via_textView);
		mPassengerNameTextView = (TextView) mWayBillView
				.findViewById(R.id.way_bill_passenger_name_textView);
		mFromTextView = (TextView) mWayBillView
				.findViewById(R.id.way_bill_from_textView);
		mWayBillToTextView = (TextView) mWayBillView
				.findViewById(R.id.way_bill_to_textView);

	}

	@Override
	public void onFailure(Throwable e, String error, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		try {
			ShowSingleButtonDialog(null, error,
					getString(R.string.alert_ok_button), 0, this, 1);
		} catch (Exception e2) {
			e2.printStackTrace();
		}

	}

	private void updateUI(WayBillModel wayBillModel) {

		if (wayBillModel != null) {
			mTaxiNameTextView.setText(wayBillModel.getCompanyName());
			mDriverNameTextView.setText(wayBillModel.getDriverName());
			mDriverLicenseTextView.setText(wayBillModel.getDriverLicence());
			mDriverLicensePlateTextView.setText(wayBillModel.getTaxiNumber());
			mTimeTextView.setText(wayBillModel.getTripDate());
			mRateTextView.setText(wayBillModel.getBaseFare() + "Base Fare + "
					+ wayBillModel.getMinFare() + " per minute");
			mPassengersTextView.setText(wayBillModel.getPersons());
			mTripTextView.setText(wayBillModel.getDriverStatus());
			mViaTextView.setText(wayBillModel.getTaxiName());
			mPassengerNameTextView.setText(wayBillModel.getRiderName());
			mFromTextView.setText(wayBillModel.getTripFrom());
			mWayBillToTextView.setText(wayBillModel.getTripTo());
		}
	}
}
