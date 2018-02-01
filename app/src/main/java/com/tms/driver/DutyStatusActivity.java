package com.tms.driver;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tms.driver.base.BaseActivityClass;
import com.tms.driver.constant.TaxiExConstants.FRAGMENT_TAGS;
import com.tms.driver.fragments.DutyStatusFragment;
import com.tms.driver.utilities.Utilities;

import java.util.HashMap;
import java.util.List;

public class DutyStatusActivity extends BaseActivityClass {
	@Override
	public void onFailure(Throwable e, String error, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		Utilities.getInstance(this).showHideKeyboard(false);
		ShowSingleButtonDialog(null, error,
				getString(R.string.alert_ok_button), 0, DutyStatusActivity.this, 1);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
        //test

		setContentView(R.layout.activity_duty_status);
		replaceFragment(R.id.acitivity_duty_status_frag,
				new DutyStatusFragment(),
				FRAGMENT_TAGS.DUTYSTATUSFRAGMENT.name(), false, this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

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
		 super.onBackPressed();
		/*FragmentManager fragManager = this
				.getSupportFragmentManager();
		Fragment fragment = (Fragment) fragManager
				.findFragmentById(R.id.acitivity_duty_status_frag);
		

				replaceFragment(R.id.acitivity_duty_status_frag,
						new ReservationHistory(),
						FRAGMENT_TAGS.RESERVATIONHISTORY.name(), true,
						DutyStatusActivity.this);
			 
		 }else{
			
		 }
		*/

	}

	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		super.onSuccess(response, UNIQUE_ID);
		Log.e("Logout message", response);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// postXMl(TaxiExConstants.DriverLogoutUrl,
		// createXml(TaxiExConstants.LogoutXMlKeys, new String[] { "",
		// UserLoginModel.getInstance().getId() }), false,
		// TaxiExConstants.LogoutWebserviceResponse);
	}

}
