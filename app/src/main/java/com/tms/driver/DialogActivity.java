package com.tms.driver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tms.driver.base.BaseActivityClass;

import java.util.HashMap;
import java.util.List;

public class DialogActivity extends BaseActivityClass {

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
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_dailog);
		if (getIntent() != null) {
			Bundle bundle = getIntent().getBundleExtra("notification bundle");
			String message = bundle.getString("notification messagee");
			ShowSingleButtonDialog(null, message,
					getString(R.string.alert_ok_button), 0, this, 1);

		}
	}

	@Override
	public void onPositiveButtonClick(int id) {
		// TODO Auto-generated method stub
		super.onPositiveButtonClick(id);
		finishAffinity();
		startActivity(new Intent(this, MainActivity.class));
	}
}
