package com.tms.driver;

import android.os.Bundle;
import android.view.View;

import com.tms.driver.base.BaseActivityClass;
import com.tms.driver.constant.TaxiExConstants.FRAGMENT_TAGS;
import com.tms.driver.fragments.WayBillFragment;

import java.util.HashMap;
import java.util.List;

public class WayBillActivity extends BaseActivityClass {
	@Override
	public void onFailure(Throwable e, String error, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		ShowSingleButtonDialog(null, error,
				getString(R.string.alert_ok_button), 0, this, 1);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_duty_status);
		Bundle bundle = null;
		if (getIntent() != null) {
			bundle = getIntent().getBundleExtra("id bundle");

		}

		WayBillFragment wayBillFragment = new WayBillFragment();
		wayBillFragment.setArguments(bundle);
		replaceFragment(R.id.acitivity_duty_status_frag, wayBillFragment,
				FRAGMENT_TAGS.WAYBILLFRAGMENT.name(), false, this);
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

}
