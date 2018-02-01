package com.tms.driver;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class ApplicationContext extends Application {

	public FragmentActivity activityContext;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Fabric.with(this, new Crashlytics());
	}

	public void setActivityContext(FragmentActivity activityContext) {
		this.activityContext = activityContext;

        /*

         */

	}

	public FragmentActivity getActivityContext() {
		return activityContext;
	}

	public Context getActivity() {
		return activityContext;
	}
}
