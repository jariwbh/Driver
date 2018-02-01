package com.tms.driver;

import android.os.Bundle;
import android.view.View;

import com.tms.driver.base.BaseActivityClass;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.constant.TaxiExConstants.FRAGMENT_TAGS;
import com.tms.driver.fragments.FareSummaryFragment;

import java.util.HashMap;
import java.util.List;

public class FareSummaryActivity extends BaseActivityClass {
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_duty_status);
		if (getIntent() != null)

		{
			Bundle bundle = getIntent().getBundleExtra(
					TaxiExConstants.BookingDetailsBundleKey);
			// FareSummaryFragment fareSummaryFragment = new
			// FareSummaryFragment();
			// fareSummaryFragment.setArguments(bundle);
			// replaceFragment(R.id.acitivity_main_fragment,
			// fareSummaryFragment,
			// FRAGMENT_TAGS.FARESUMMARYFRAGMENT.name(), false, this);
			boolean isFromEndTrip = false;
			if (bundle.containsKey(TaxiExConstants.IsFromEndTrip)) {
				isFromEndTrip = bundle
						.getBoolean(TaxiExConstants.IsFromEndTrip);

			}
			/*if (isFromEndTrip) {*/
				bundle.putString(TaxiExConstants.PaymentMode, "0");

				FareSummaryFragment fareSummaryFragment = new FareSummaryFragment();
				fareSummaryFragment.setArguments(bundle);
				replaceFragment(R.id.acitivity_duty_status_frag,
						fareSummaryFragment,
						FRAGMENT_TAGS.FARESUMMARYFRAGMENT.name(), false, this);
			/*} else {

				PaymentModeFragment paymentModeFragmentFragment = new PaymentModeFragment();
				paymentModeFragmentFragment.setArguments(bundle);
				replaceFragment(R.id.acitivity_duty_status_frag,
						paymentModeFragmentFragment,
						FRAGMENT_TAGS.PAYMENTMODE_FRAGMENT.name(), false, this);

			}
*/
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
		// super.onBackPressed();
		// Do Nothing
	}

}
