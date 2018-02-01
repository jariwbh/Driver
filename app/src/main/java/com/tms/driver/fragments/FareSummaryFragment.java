package com.tms.driver.fragments;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.R.style;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.tms.driver.MainActivity;
import com.tms.driver.OnlineStatusActivity;
import com.tms.driver.models.VehicleListModel;
import com.tms.driver.R;
import com.tms.driver.SplashActivity;
import com.tms.driver.base.BaseFragment;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.customviews.CustomImageView;
import com.tms.driver.customviews.CustomTextView;
import com.tms.driver.fragments.*;
import com.tms.driver.models.BookingDetailsRequestModel;
import com.tms.driver.models.UserLoginModel;
import com.tms.driver.utilities.Utilities;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * 
 * @author arvind.agarwal this fragment shows Receipt or Fare Summary of Trip
 */
public class FareSummaryFragment extends BaseFragment {
	private SharedPreferences mSharedprefrences, mSharedprefrences1;

	private SharedPreferences.Editor editPrefs;
	private Utilities mABCUtilitiesInstance;
	private GoogleCloudMessaging gcm;
	private static final int ALERT_ALREADY_LOGIN = 101;
	private static final int ALERT_INVALID_USER = 102;
	private static final int ALERT_SERVER_ERROR = 103;

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
		case TaxiExConstants.RiderRatingWebserviceResponse:

			try {

				// postXMl(TaxiExConstants.DriverStatusOnlineUrl,
				// createXml(TaxiExConstants.DriverStatusXMLKeys,
				// new String[] {
				// "",
				// UserLoginModel.getInstance()
				// .getId() }), true,
				// TaxiExConstants.DriverStatusWebserviceResponse,
				// this);

				String GCM = "";

				if (!TextUtils.isEmpty(mSharedprefrences.getString(
						TaxiExConstants.Pref_GCM_key, ""))) {
					GCM = mSharedprefrences.getString(
							TaxiExConstants.Pref_GCM_key, "");

				} else if (!TextUtils.isEmpty(mSharedprefrences1.getString(
						TaxiExConstants.Pref_GCM_key, ""))) {

					GCM = mSharedprefrences1.getString(
							TaxiExConstants.Pref_GCM_key, "");

				} else {

//					gcm = GoogleCloudMessaging.getInstance(getActivity());

//					GCM = gcm.register(SplashActivity.SENDER_ID);

					GCM = FirebaseInstanceId.getInstance().getToken();

				}

				// changing driver status to online

				postXMl(TaxiExConstants.DriverStatusOnlineUrl,
						createXml(TaxiExConstants.DriverOnlineStatusXMLKeys,
								new String[] { "",
										UserLoginModel.getInstance().getId(),
										GCM }), true,
						TaxiExConstants.DriverStatusWebserviceResponse, this);
			}

			catch (Exception e) {
				e.printStackTrace();
			}

			/*
			 * postXMl(TaxiExConstants.DriverStatusOfflineUrl,
			 * createXml(TaxiExConstants.DriverStatusXMLKeys, new String[] { "",
			 * UserLoginModel.getInstance().getId() }), true,
			 * TaxiExConstants.DriverStatusWebserviceResponse, this);
			 */
			// getActivity().finishAffinity();
			// startActivity(new Intent(getActivity(),
			// DutyStatusFragment.class));
			break;
		case TaxiExConstants.DriverStatusWebserviceResponse:

			// change driver status response

			if (!checkIfStringIsNullOrEmpty(response)) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					int driverStatusCode = jsonObject
							.getInt("driverStatusOnline");
					String message = jsonObject.getString("message");
					switch (driverStatusCode) {
					case 1:

						// if success move to On line screen
						startActivity(new Intent(getActivity(),
								OnlineStatusActivity.class));

						break;
					case -3:

						// if already login
						ShowSingleButtonDialog(null, message,
								getString(R.string.alert_ok_button), 0, this,
								ALERT_ALREADY_LOGIN);
						break;
					case -1:

						// if invalid user
						ShowSingleButtonDialog(null, message,
								getString(R.string.alert_ok_button), 0, this,
								ALERT_INVALID_USER);

						break;
					case -2:
						// if server error
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

			/*
			 * case TaxiExConstants.DriverStatusWebserviceResponse:
			 * 
			 * Toast.makeText(getActivity(), "OffLine",
			 * Toast.LENGTH_SHORT).show();
			 * 
			 * Log.e("Driver Status Response", response); if (getActivity() !=
			 * null) { editPrefs.putBoolean(TaxiExConstants.Pref_isOnline,
			 * false); editPrefs.commit(); getActivity().finishAffinity();
			 * startActivity(new Intent(getActivity(),
			 * DutyStatusActivity.class));
			 * 
			 * } break;
			 */
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

	private View mFareSummaryView;
	private TextView mRidernNameTextView, mRiderPhoneNumberTextView,
			mVehicleModelTextView, mDateTextView, mTotalFareTextView,
			mRatingTextView, mPaymentDescriptonTextView;
	private RatingBar mRiderRatingBar;
	private EditText mRiderCommentsEditText;
	private CustomImageView mRiderImageView;
	private Button mSubmitFareSummaryButton;
	private BookingDetailsRequestModel mBookingDetailsRequestModel;
	private String mBookingDate, mBookingTip, mPaymentMode, discountAmount,
			paymentAmount;

	private CustomTextView ratingStatus, discountText, paymentText;
	private ScrollView scrollView;
	private TableRow discountRow, paymentRow;
    private VehicleListModel mVehicleListModel;

	@Override
	public View setContentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

			mFareSummaryView = inflater.inflate(R.layout.frag_fare_summary,
					null);
			if (getArguments() != null) {

				// getting booking details
				mBookingDetailsRequestModel = (BookingDetailsRequestModel) getArguments()
						.get(TaxiExConstants.BookingDetailsBundleKey);
				mBookingDate = getArguments().getString(
						TaxiExConstants.BookingDateBundleKey);
				mBookingTip = getArguments().getString(
						TaxiExConstants.BookingTipBundleKey);
				mPaymentMode = getArguments().getString(
						TaxiExConstants.PaymentMode);
				discountAmount = getArguments().getString(
						TaxiExConstants.UserDiscount);
				paymentAmount = getArguments().getString(
						TaxiExConstants.PaymentAmount);
			}
			// initializing components
			initializecomponents();

			// postXMl(TaxiExConstants.MyProfileDetailsUrl,
			// createXml(TaxiExConstants.MyProfileXMLKeys, new String[] {
			// "", UserLoginModel.getInstance().getId() }), true,
			// TaxiExConstants.MyProfileWebserviceResponse);

		return mFareSummaryView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// setting header title
		setHeaderTitle(" ");

		// setting title bar
		setTitleBackGround(R.drawable.title_bar);
		ratingStatus.setText("OK");
		mRiderRatingBar.setRating(3.0f);

		FragmentManager fragManager = getActivity().getSupportFragmentManager();
		FragmentTransaction fragTransaction = fragManager.beginTransaction();
		com.tms.driver.fragments.HeaderFragment headerFragment = (com.tms.driver.fragments.HeaderFragment) fragManager
				.findFragmentById(R.id.activity_duty_status_header_fragment);
		if (headerFragment != null) {
			fragTransaction.show(headerFragment).commit();
		}

		// hiding left and right icon
		setLeftIconVisibility(View.GONE);
		setRightIconVisibility(View.GONE);

	}

	// initializing components
	private void initializecomponents() {

		Utilities.showHideKeyboard(false);
		scrollView = (ScrollView) mFareSummaryView
				.findViewById(R.id.scrollView);
		scrollView.fullScroll(ScrollView.FOCUS_UP);
		mSharedprefrences = getActivity().getSharedPreferences(
				TaxiExConstants.SharedPreferenceName, 0);
		mSharedprefrences1 = getActivity().getSharedPreferences(
				TaxiExConstants.SharedPreferenceName1, 0);
		editPrefs = mSharedprefrences.edit();
		mABCUtilitiesInstance = Utilities.getInstance(getActivity());
		ratingStatus = (CustomTextView) mFareSummaryView
				.findViewById(R.id.ratingStatus);

		mRiderImageView = (CustomImageView) mFareSummaryView
				.findViewById(R.id.frag_fare_summary_image);
		mRiderCommentsEditText = (EditText) mFareSummaryView
				.findViewById(R.id.frag_fare_summary_comments);
		mRiderRatingBar = (RatingBar) mFareSummaryView
				.findViewById(R.id.frag_fare_summary_rating_bar);
		discountRow = (TableRow) mFareSummaryView
				.findViewById(R.id.frag_fare_discount_table);
		paymentRow = (TableRow) mFareSummaryView
				.findViewById(R.id.frag_fare_payment_table);
		discountText = (CustomTextView) mFareSummaryView
				.findViewById(R.id.frag_fare_summary_discount);
		paymentText = (CustomTextView) mFareSummaryView
				.findViewById(R.id.frag_fare_summary_payment);

		if (discountAmount.equals("0")||discountAmount.equals("")) {
			discountRow.setVisibility(View.GONE);
			paymentRow.setVisibility(View.GONE);

	}

		// rating bar change listener
		mRiderRatingBar
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						// TODO Auto-generated method stub
						if (rating == 1.0f) {// poor

							ratingStatus.setText("POOR");
						} else if (rating == 2.0f) {// fair

							ratingStatus.setText("FAIR");
						} else if (rating == 3.0f) {// average

							ratingStatus.setText("OK");
						} else if (rating == 4.0f) {// good

							ratingStatus.setText("GOOD");
						} else if (rating == 5.0f) {// great

							ratingStatus.setText("GREAT");
						}

					}
				});

		mRidernNameTextView = (TextView) mFareSummaryView
				.findViewById(R.id.frag_fare_summary_rider_name);
		mRiderPhoneNumberTextView = (TextView) mFareSummaryView
				.findViewById(R.id.frag_fare_summary_phn_number);
		mVehicleModelTextView = (TextView) mFareSummaryView
				.findViewById(R.id.frag_fare_summary_car_model);
		// mVehicleNumberTextView = (TextView) mFareSummaryView
		// .findViewById(R.id.frag_fare
		mDateTextView = (TextView) mFareSummaryView
				.findViewById(R.id.frag_fare_summary_date);
		mTotalFareTextView = (TextView) mFareSummaryView
				.findViewById(R.id.frag_fare_summary_total_fare);
		mRatingTextView = (TextView) mFareSummaryView
				.findViewById(R.id.frag_fare_summary_rating_textview);
		mSubmitFareSummaryButton = (Button) mFareSummaryView
				.findViewById(R.id.frag_fare_submit);
		mPaymentDescriptonTextView = (TextView) mFareSummaryView
				.findViewById(R.id.frag_fare_summary_payment_description);
		mSubmitFareSummaryButton.setOnClickListener(this);
		mRiderImageView.setImageUrl(mBookingDetailsRequestModel.getUserImage(),
				0);
		try {

			mDateTextView.setText(mBookingDate);
			mTotalFareTextView.setText("$ " + mBookingTip);
			discountText.setText("$ " + discountAmount);
			paymentText.setText("$ " + paymentAmount);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mRiderRatingBar.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View view, MotionEvent event) {
				float touchPositionX = event.getX();
				float width = mRiderRatingBar.getWidth();
				float starsf = (touchPositionX / width) * 5.0f;
				int stars = (int) starsf + 1;
				mRiderRatingBar.setRating(stars);
				return true;
			}
		});
		// mRiderRatingBar
		// .setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
		//
		// @Override
		// public void onRatingChanged(RatingBar ratingBar,
		// float rating, boolean fromUser) {
		//
		// mRiderRatingBar.setRating(rating);
		// }
		// });

		mRidernNameTextView.setText(mBookingDetailsRequestModel.getFirstName()
				+ " " + mBookingDetailsRequestModel.getLastName());
		mRiderPhoneNumberTextView.setText(mBookingDetailsRequestModel
				.getPhone());
        String cabTypeStr="";
        gettingCarDetails();
        if(mVehicleListModel!=null){
            cabTypeStr = mVehicleListModel.getMake()+" "+mVehicleListModel.getTaxiModel();
        }
		// cabTypeStr = mBookingDetailsRequestModel.getCabType();//VehicleListModel.getInstance().getTaxiModel();
       mVehicleModelTextView.setText(cabTypeStr);

		/* mBookingDetailsRequestModel.getCabType();*/
		/*if(TextUtils.equals(cabTypeStr, "1")){
			mVehicleModelTextView.setText(TaxiExConstants.CAR_1);
		}else if(TextUtils.equals(cabTypeStr, "2")){
			mVehicleModelTextView.setText(TaxiExConstants.CAR_2);
		}else if(TextUtils.equals(cabTypeStr, "3")){
			mVehicleModelTextView.setText(TaxiExConstants.CAR_3);
		}else if(TextUtils.equals(cabTypeStr, "4")){
            mVehicleModelTextView.setText(TaxiExConstants.CAR_4);
        }else{
			mVehicleModelTextView.setText(TaxiExConstants.CAR_5);
		}*/
		
		//.setText(mBookingDetailsRequestModel.getCabType());
		mRatingTextView.setText(mBookingDetailsRequestModel.getRating());

		if (mBookingDetailsRequestModel.getPayvia().equalsIgnoreCase("0")) {

			// ShowSingleButtonDialog("",
			// getString(R.string.fare_sumary_amount_paid_by_cash_description).replace("\n",
			// " "), getString(R.string.alert_ok_button), 0, this, 5);

			if(TextUtils.equals(paymentAmount, "0") || TextUtils.equals(paymentAmount, "0.00")){
				
			}else{
				showCashPopUp("$ " + paymentAmount);
			}
			
			

			mPaymentDescriptonTextView
					.setText(getString(R.string.fare_sumary_amount_paid_by_cash_description));
		} else if (mBookingDetailsRequestModel.getPayvia()
				.equalsIgnoreCase("1")) {
			mPaymentDescriptonTextView
					.setText(getString(R.string.fare_sumary_amount_paid_by_creditcard_description));

		}
		Drawable carIcon = null;
		// setting car icon

		if (mBookingDetailsRequestModel.getCabType().equals("1")) {

			carIcon = getActivity().getResources().getDrawable(
					R.drawable.car_regular);

		} else if (mBookingDetailsRequestModel.getCabType().equals("2")) {
			carIcon = getActivity().getResources().getDrawable(
					R.drawable.icon_mini_grey);

		} else if (mBookingDetailsRequestModel.getCabType().equals("3")) {
			carIcon = getActivity().getResources().getDrawable(
					R.drawable.icon_mini_grey);
		} else if (mBookingDetailsRequestModel.getCabType().equals("4")) {
            carIcon = getActivity().getResources().getDrawable(
                    R.drawable.car_luxury);
        } else {
			carIcon = getActivity().getResources().getDrawable(
					R.drawable.car_armour);
		}

		carIcon.setBounds(0, 0, 100, 100);
		mVehicleModelTextView.setCompoundDrawables(carIcon, null, null, null);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.frag_fare_submit:

			// "riderRating", "bookingId",
			// "driverId", "riderId", "rating", "comment"

			if (mRiderRatingBar.getRating() != 0) {
				String rating = String.valueOf(mRiderRatingBar.getRating());
				String comments = mRiderCommentsEditText.getText().toString();

				// submiting fare
				postXMl(TaxiExConstants.RiderRatingURL,
						createXml(
								TaxiExConstants.RiderRatinXMLKeys,
								new String[] {
										"",
										mBookingDetailsRequestModel
												.getBookingId(),

										UserLoginModel.getInstance().getId(),
										mBookingDetailsRequestModel
												.getRiderId(), rating, comments }),
						true, TaxiExConstants.RiderRatingWebserviceResponse,
						this);
				break;
			} else {
				ShowSingleButtonDialog(null, "Please select rating.",
						getString(R.string.alert_ok_button), 0, this, 1);
			}

		default:
			break;
		}
	}

	@Override
	public void onPositiveButtonClick(int id) {
		// TODO Auto-generated method stub
		super.onPositiveButtonClick(id);

		switch (id) {
		case ALERT_ALREADY_LOGIN:

			// if already login move to
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

	private void showCashPopUp(String amount) {

		final Dialog dialogCash = new Dialog(getActivity(),
				style.Theme_Translucent_NoTitleBar);

		dialogCash.setContentView(R.layout.dialog_collect_cash);
		TextView txtAmount = (TextView) dialogCash.findViewById(R.id.txtAmount);
		TextView txtComment = (TextView) dialogCash
				.findViewById(R.id.txtComment);
		Button btnOk = (Button) dialogCash.findViewById(R.id.btnOk);

		txtComment.setText("Please collect the amount by cash.");

		txtAmount.setText(amount);

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				dialogCash.dismiss();

			}
		});

		dialogCash.setCancelable(false);

		dialogCash.show();

	}

    private void gettingCarDetails(){
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
    }

}
