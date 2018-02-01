package com.tms.driver;

import android.os.Bundle;
import android.view.View;

import com.tms.driver.base.BaseActivityClass;
import com.tms.driver.constant.TaxiExConstants.FRAGMENT_TAGS;

import java.util.HashMap;
import java.util.List;

public class ReservationDetails extends BaseActivityClass{

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
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(String[] response, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_duty_status);
		 com.tms.driver.fragments.ReservationDetails fragment=new com.tms.driver.fragments.ReservationDetails();
		
		Bundle bundle=getIntent().getExtras();
		
		fragment.setArguments(bundle);
		replaceFragment(R.id.acitivity_duty_status_frag,
				fragment,
				FRAGMENT_TAGS.RESERVATIONDETAILS.name(), false, this);
		
		
		
	}
	
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		if(GcmIntentService.player!=null){

			GcmIntentService.player.stop();
			GcmIntentService.player=null;
			
		}
	}

}
