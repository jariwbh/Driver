package com.tms.driver.fragments;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tms.driver.R;
import com.tms.driver.base.BaseFragment;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.constant.TaxiExConstants.FRAGMENT_TAGS;
import com.tms.driver.customviews.CustomButton;
import com.tms.driver.fragments.*;
import com.tms.driver.fragments.FareSummaryFragment;

/**
 * 
 * @author arvind.agarwal this fragment is used to show payment mode
 */

public class PaymentModeFragment extends BaseFragment {

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

	View mPaymentModeView;
	private CustomButton mCashPaymentButton, mCreditPaymentButton;

	@Override
	public View setContentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

			mPaymentModeView = inflater.inflate(R.layout.dialog_payment_mode,
					null);
			intiailizePaymentModeComponents();

		return mPaymentModeView;
	}

	/**
	 * initializing View
	 */
	private void intiailizePaymentModeComponents() {
		FragmentManager fragManager = getActivity().getSupportFragmentManager();
		FragmentTransaction fragTransaction = fragManager.beginTransaction();
		com.tms.driver.fragments.HeaderFragment headerFragment = (com.tms.driver.fragments.HeaderFragment) fragManager
				.findFragmentById(R.id.activity_duty_status_header_fragment);
		if (headerFragment != null) {
			fragTransaction.hide(headerFragment).commit();
		}

		mCashPaymentButton = (CustomButton) mPaymentModeView
				.findViewById(R.id.dailog_payment_cash_button);
		mCreditPaymentButton = (CustomButton) mPaymentModeView
				.findViewById(R.id.dailog_payment_credit_button);
		mCashPaymentButton.setOnClickListener(this);
		mCreditPaymentButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.dailog_payment_cash_button:
			if (getArguments() != null) {
				// if cash add payment mode to 0 and move Fare summary fragment
				
				Bundle bundle = getArguments();
				bundle.putString(TaxiExConstants.PaymentMode, "0");

				FareSummaryFragment fareSummaryFragment = new FareSummaryFragment();
				fareSummaryFragment.setArguments(bundle);
				replaceFragment(R.id.acitivity_duty_status_frag,
						fareSummaryFragment,
						FRAGMENT_TAGS.FARESUMMARYFRAGMENT.name(), false,
						getActivity());
			}
			break;
		case R.id.dailog_payment_credit_button:
			if (getArguments() != null) {
				
				// if credit card add payment mode to 0 and move Fare summary fragment
				Bundle bundle = getArguments();
				bundle.putString(TaxiExConstants.PaymentMode, "1");
				FareSummaryFragment fareSummaryFragment = new FareSummaryFragment();
				fareSummaryFragment.setArguments(bundle);
				replaceFragment(R.id.acitivity_duty_status_frag,
						fareSummaryFragment,
						FRAGMENT_TAGS.FARESUMMARYFRAGMENT.name(), false,
						getActivity());
			}
			break;
		default:
			break;
		}
	}
}
