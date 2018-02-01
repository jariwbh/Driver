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
import android.os.Bundle;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tms.driver.DutyStatusActivity;
import com.tms.driver.R;
import com.tms.driver.base.BaseFragment;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.constant.TaxiExConstants.FRAGMENT_TAGS;
import com.tms.driver.fragments.TermsConditionsWebFragment;
import com.tms.driver.models.UserLoginModel;
import com.tms.driver.models.VehicleListModel;
import com.tms.driver.parse.ParseContent;
import com.tms.driver.parse.ParseContent.CONTENT_TYPE;

public class TermsandConditionsFragment extends BaseFragment {

	private SharedPreferences mSharedprefrences, mSharedPreferences1;
	private SharedPreferences.Editor editPrefs, editPrefs1;

	private int ALERT_PASSWORD_BLANK = 3;

	@Override
	public void getServerValues(String response, int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setServerError(int id, String msg) {
		// TODO Auto-generated method stub

	}
	
	
	/**
	 * handling parsed response
	 */

	@Override
	public void onParsingSuccessful(String response, int callback) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		
		
		// handling login respose
		if (response.equalsIgnoreCase("Login Successful.")) {

			if (getActivity().getIntent() != null) {

			}
			try {
				editPrefs.putBoolean(TaxiExConstants.Pref_is_login, true);
				editPrefs.putString(
						TaxiExConstants.Pref_login_username,
						getArguments().getString(
								TaxiExConstants.USER_NAME_INTENT));

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

					
					// saving rider details
					editPrefs.putString(TaxiExConstants.Pref_Login_Details,
							new String(out.toByteArray()));

					editPrefs.commit();

				} catch (IOException e) {
					e.printStackTrace();
				}

				if (VehicleListModel.getInstance().getId() != null) {

					upDateTaxiInfo();
				}
				// mCarSelectedImageView.
			} catch (Exception e) {
				e.printStackTrace();

			}
			/*
			 * if (mIsFirstTimeLogin) {
			 * replaceFragment(R.id.acitivity_main_fragment, new
			 * TermsandConditionsFragment(),
			 * FRAGMENT_TAGS.TERMSANDCONDITION_FRAGMENT.name(), true,
			 * getActivity());
			 * 
			 * } else {
			 */
			
			
			getActivity().finish();

			getActivity().startActivity(
					new Intent(getActivity(), DutyStatusActivity.class));
			// }

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

	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		super.onSuccess(response, UNIQUE_ID);
		switch (UNIQUE_ID) {
		case TaxiExConstants.LogoutWebserviceResponse:
			Log.e("Logout response", response);

			popFragment(R.id.acitivity_main_fragment, getActivity());

			break;

		case TaxiExConstants.LoginWebserviceResponse:
			try {
				ParseContent.getInstance().parseContentFromjson(
						CONTENT_TYPE.LOGIN, new JSONObject(response), this,
						ALERT_PASSWORD_BLANK);
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	@Override
	public void onSuccess(String[] response, int UNIQUE_ID) {
		// TODO Auto-generated method stub

	}

	private View mTermsandConditionsView;
	private TextView mPrivacyPolicyTextView, mRulesandRegulationsTextView,
			mIndependentContracts;
	private Button mAcceptTermsButton;

	@Override
	public void onBackPress(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public View setContentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mTermsandConditionsView == null) {
			mTermsandConditionsView = inflater.inflate(
					R.layout.frag_terms_and_conditions, null);
			initializeComponents();
		}
		return mTermsandConditionsView;
	}

	private void initializeComponents() {

		mSharedprefrences = getActivity().getSharedPreferences(
				TaxiExConstants.SharedPreferenceName, 0);
		editPrefs = mSharedprefrences.edit();
		mSharedPreferences1 = getActivity().getSharedPreferences(
				TaxiExConstants.SharedPreferenceName1, 0);
		editPrefs1 = mSharedPreferences1.edit();

		mPrivacyPolicyTextView = (TextView) mTermsandConditionsView
				.findViewById(R.id.frag_terms_conditions_privacy_policy);
		mRulesandRegulationsTextView = (TextView) mTermsandConditionsView
				.findViewById(R.id.frag_terms_conditions_rules_and_regulations);
		mIndependentContracts = (TextView) mTermsandConditionsView
				.findViewById(R.id.frag_terms_conditions_independent_contracts);
		mAcceptTermsButton = (Button) mTermsandConditionsView
				.findViewById(R.id.terms_conditions_accept);
		mAcceptTermsButton.setOnClickListener(this);
		mPrivacyPolicyTextView.setOnClickListener(this);
		mRulesandRegulationsTextView.setOnClickListener(this);
		mIndependentContracts.setOnClickListener(this);
	}

	@Override
	public void onFailure(Throwable e, String error, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		ShowSingleButtonDialog(null, error,
				getString(R.string.alert_ok_button), 0, this, 1);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		TermsConditionsWebFragment fragment = new TermsConditionsWebFragment();
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		case R.id.frag_terms_conditions_privacy_policy:
			bundle.putInt(TaxiExConstants.TermsConditionsBundleKey,
					TermsConditionsWebFragment.PRIVACY_POLICY);
			fragment.setArguments(bundle);
			replaceFragment(R.id.acitivity_main_fragment, fragment,
					FRAGMENT_TAGS.TERMSWEBFRAGMENT.name(), true, getActivity());
			break;
		case R.id.frag_terms_conditions_rules_and_regulations:
			bundle.putInt(TaxiExConstants.TermsConditionsBundleKey,
					TermsConditionsWebFragment.RULES_REGULATIONS);
			fragment.setArguments(bundle);
			replaceFragment(R.id.acitivity_main_fragment, fragment,
					FRAGMENT_TAGS.TERMSWEBFRAGMENT.name(), true, getActivity());

			break;
		case R.id.frag_terms_conditions_independent_contracts:
			bundle.putInt(TaxiExConstants.TermsConditionsBundleKey,
					TermsConditionsWebFragment.INDEPENDENT_CONTRACTORS);
			fragment.setArguments(bundle);
			replaceFragment(R.id.acitivity_main_fragment, fragment,
					FRAGMENT_TAGS.TERMSWEBFRAGMENT.name(), true, getActivity());

			break;
		case R.id.terms_conditions_accept:

			ShowDoubleButtonDialog(null,
					getString(R.string.alert_terms_conditions),
					getString(R.string.alert_agree_button),
					getString(R.string.alert_no_button), 0, this, 1);

			break;
		default:
			break;
		}
	}

	@Override
	public void onNegativeButtonClick(int id) {
		// TODO Auto-generated method stub
		super.onNegativeButtonClick(id);
		/*
		 * AndroidWebResponseTask androidWebResponseTask =
		 * AndroidWebResponseTask .getInstance(getActivity());
		 * androidWebResponseTask.setCallback(this); Utilities
		 * .getInstance(getActivity()) .postXml(
		 * TaxiExConstants.DriverLogoutUrl,
		 * Utilities.getInstance(getActivity()).createXML(
		 * TaxiExConstants.LogoutXMlKeys, new String[] { "",
		 * UserLoginModel.getInstance().getId() }), true,
		 * TaxiExConstants.LogoutWebserviceResponse, androidWebResponseTask);
		 */

		getActivity().finish();

	}

	@Override
	public void onPositiveButtonClick(int id) {
		// TODO Auto-generated method stub
		super.onPositiveButtonClick(id);
		switch (id) {
		
		case 3:
			
			getActivity().onBackPressed();
			
			break;
		
		
		case 1:
			
			editPrefs.putBoolean(TaxiExConstants.Pref_isFIrstTime, false);

			editPrefs.commit();
			String[] keys = new String[] { "userLogin", "email", "password",
					"deviceToken", "appVersion", "deviceType" };
			String[] values = new String[] {
					"",
					getArguments().getString(TaxiExConstants.USER_NAME_INTENT),
					getArguments().getString(
							TaxiExConstants.USER_PASSWORD_INTENT),
					mSharedPreferences1.getString(TaxiExConstants.Pref_GCM_key,
							""), "1", "2" };
			// ShowSingleButtonDialog(null, createXml(keys, values),
			// getString(R.string.alert_ok_button), 0, this,
			// ALERT_PASSWORD_BLANK);

			postXMl(TaxiExConstants.LoginURl, createXml(keys, values), true,
					TaxiExConstants.LoginWebserviceResponse, this);
			/*
			 * getActivity().finish();
			 * 
			 * getActivity().startActivity( new Intent(getActivity(),
			 * DutyStatusActivity.class));
			 */

			break;

		default:
			break;
		}
	}

	private void upDateTaxiInfo() {

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
