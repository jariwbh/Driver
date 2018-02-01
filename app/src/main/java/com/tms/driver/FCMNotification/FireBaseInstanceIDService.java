package com.tms.driver.FCMNotification;


import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tms.driver.constant.TaxiExConstants;

public class FireBaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        SharedPreferences mSharedprefrences = getSharedPreferences(
                TaxiExConstants.SharedPreferenceName, 0);
        SharedPreferences.Editor editPrefs,editPrefs1;
        editPrefs = mSharedprefrences.edit();
        SharedPreferences mSharedPreferences1 = getSharedPreferences(
                TaxiExConstants.SharedPreferenceName1, 0);
        editPrefs1 = mSharedPreferences1.edit();
        editPrefs1.putString(TaxiExConstants.Pref_GCM_key, FirebaseInstanceId.getInstance().getToken());
        editPrefs1.commit();
        editPrefs.putString(TaxiExConstants.Pref_GCM_key, FirebaseInstanceId.getInstance().getToken());
        editPrefs.commit();
    }


}
