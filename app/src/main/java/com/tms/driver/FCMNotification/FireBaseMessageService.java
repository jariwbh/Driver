package com.tms.driver.FCMNotification;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tms.driver.ApplicationContext;
import com.tms.driver.GCMDialog_Activity;
import com.tms.driver.GcmIntentService;
import com.tms.driver.MainActivity;
import com.tms.driver.OnlineStatusActivity;
import com.tms.driver.R;
import com.tms.driver.ReservationDetails;
import com.tms.driver.async.AndroidWebResponseTask;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.customviews.CustomLinearLayout;
import com.tms.driver.fragments.HeaderFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class FireBaseMessageService extends FirebaseMessagingService implements AndroidWebResponseTask.WebResponse {

    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static int count = 0;
    String message;
    private OnlineStatusActivity onLineStatusActivity;
    private SharedPreferences mSharedprefrences;
    private SharedPreferences.Editor editPrefs;
    public static boolean isBookingRequest = false;

    public static MediaPlayer player;
    Intent intent;
    String bookingId;
    private String APP_NAME="TMS";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        mSharedprefrences = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
         editPrefs = mSharedprefrences.edit();

        SetNotification(remoteMessage);
    }

    private void SetNotification(RemoteMessage remoteMessage){
        message = remoteMessage.getData().get("message");
        try {
            Bundle bundle = new Bundle();
            String extraDetails = remoteMessage.getData().get("extraDetails");

            if (extraDetails != null) {
                JSONObject jsonObject = new JSONObject(extraDetails);

                if (jsonObject.has("pushno")) {
                    String pushNo = jsonObject.getString("pushno");

                    if (pushNo.equals("8")) {

                        Intent i = new Intent(this,
                                ReservationDetails.class);
                        i.putExtra("RESERVATION_ID",
                                jsonObject.getString("reservationId"));
                        i.putExtra("TYPE", 5);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(i);

                    }

                } else

                if (jsonObject.has("bookingId")) {
                    bookingId = jsonObject.getString("bookingId");
                    bundle.putString("bookingId", bookingId);


                    OnlineStatusActivity.COUNTER_COUNT=15;

                    if (OnlineStatusActivity.mIsInBackGround && !GCMDialog_Activity.mIsInForeGround) {
                        bundle.putString("message", message);

                        intent = new Intent(FireBaseMessageService.this,
                                OnlineStatusActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        intent.putExtra("message", message);
                        intent.putExtra("bookingId", bookingId);
                        intent.putExtra("FROM_NOTIFICATION", true);
                        // If you were starting a service, you wouldn't
                        // using getActivity() here

                        PendingIntent cid = null;
                        cid = PendingIntent.getActivity(
                                FireBaseMessageService.this,
                                (int) System.currentTimeMillis(), intent,
                                PendingIntent.FLAG_ONE_SHOT);

                        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        Notification.Builder builder = new Notification.Builder(
                                FireBaseMessageService.this);
                        builder.setContentTitle(APP_NAME)
                                .setContentText(message)
                                .setTicker(message)
                                .setSmallIcon(R.drawable.app_icon)
                                .setContentIntent(cid)
                                .setAutoCancel(true);

                        player = MediaPlayer.create(
                                FireBaseMessageService.this, R.raw.sec);
                        player.setVolume(1f, 1f);
                        player.start();

                        // auto cancel means the notification will
                        // remove itself when pressed
                        mNotificationManager.notify(
                                (int) System.currentTimeMillis(),
                                builder.build());

                        new Timer().schedule(new TimerTask() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                try {
                                    mNotificationManager.cancelAll();

                                    if (player != null
                                            && player.isPlaying()) {

                                        player.stop();

                                    }
                                } catch (Exception e) {
                                    // TODO: handle exception
                                }
                            }
                        }, 15000);

                    } else

                    if (isActivityRunning(OnlineStatusActivity.class) || isActivityRunning(GCMDialog_Activity.class)) {
                        bundle.putString("message", message);


                        if(GCMDialog_Activity.activity!=null){

                            GCMDialog_Activity.activity.finish();
                        }


                        try{


                            CustomLinearLayout.isIntercepting = true;
                            ApplicationContext applicationContext = (ApplicationContext) getApplicationContext();
                            OnlineStatusActivity baseActivityClass = (OnlineStatusActivity) applicationContext
                                    .getActivityContext();

                            if (baseActivityClass != null) {
                                // if (baseActivityClass instanceof
                                // OnlineStatusActivity) {
                                isBookingRequest = true;
                                onLineStatusActivity = (OnlineStatusActivity) baseActivityClass;
                                onLineStatusActivity.setUpCountDown(bundle);
                                // }
                            }

                        }catch(Exception e){

                        }

                    } else {
                        bundle.putString("message", message);

                        intent = new Intent(FireBaseMessageService.this,
                                OnlineStatusActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        intent.putExtra("message", message);
                        intent.putExtra("bookingId", bookingId);
                        intent.putExtra("FROM_NOTIFICATION", true);
                        // If you were starting a service, you wouldn't
                        // using getActivity() here

                        PendingIntent ci = null;
                        ci = PendingIntent.getActivity(
                                FireBaseMessageService.this,
                                (int) System.currentTimeMillis(), intent,
                                PendingIntent.FLAG_ONE_SHOT);

                        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        Notification.Builder builder = new Notification.Builder(
                                FireBaseMessageService.this);
                        builder.setContentTitle(APP_NAME)
                                .setContentText(message)
                                .setTicker(message)
                                .setSmallIcon(R.drawable.app_icon)
                                .setContentIntent(ci)
                                .setAutoCancel(true)

                                .setSound(
                                        Uri.parse("android.resource://com.tms.driver/"
                                                + R.raw.sec));

                        player = MediaPlayer.create(
                                FireBaseMessageService.this, R.raw.sec);
                        player.setVolume(1f, 1f);
                        player.start();

                        // + R.raw.sec));

                        // auto cancel means the notification will
                        // remove itself when pressed
                        mNotificationManager.notify(
                                (int) System.currentTimeMillis(),
                                builder.build());

                        new Timer().schedule(new TimerTask() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                try {
                                    mNotificationManager.cancelAll();
                                    if (player != null
                                            && player.isPlaying()) {

                                        player.stop();

                                    }

											/*
											 * postXMl(TaxiExConstants.
											 * CancelBookinfUrl, createXml(
											 * TaxiExConstants
											 * .CancelBookingXMLKeys, new
											 * String[] { "", UserLoginModel
											 * .getInstance() .getId(),
											 * bookingId, "0", "", "3" }), true,
											 * TaxiExConstants
											 * .CancelBookingWebserviceResponse,
											 * GcmIntentService.this);
											 */

                                } catch (Exception e) {
                                    // TODO: handle exception
                                }
                            }
                        }, 15000);

                    }
                } else if (jsonObject.has("cancel")) {
                    bundle.putString("canceledtrip",
                            jsonObject.getString("cancel"));

                    // if(
                    // !isActivityRunning(OnlineStatusActivity.class)){

                    /*editPrefs
                            .remove(TaxiExConstants.pref_Booking_details);
                    editPrefs.commit();*/

                    mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                    if(player!=null && player.isPlaying()){

                        player.stop();

                    }

                    // }

                    try {
                        mNotificationManager.cancelAll();

                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                    FragmentActivity activity = ((ApplicationContext) getApplicationContext())
                            .getActivityContext();
                    if (activity != null) {
                        FragmentManager fragManager = activity
                                .getSupportFragmentManager();
                        Fragment fragment = null;
                        HeaderFragment headerFragment = null;
                        if (activity instanceof OnlineStatusActivity) {
                            // fragment = fragManager
                            // .findFragmentById(R.id.acitivity_main_fragment);
                            headerFragment = (HeaderFragment) fragManager
                                    .findFragmentById(R.id.activity_online_fragment);

                        }
                        try {
                            if (headerFragment != null) {

                                if (!isActivityRunning(OnlineStatusActivity.class)) {

                                    try {

                                        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                        mNotificationManager
                                                .cancelAll();


                                        Notification.Builder builder = new Notification.Builder(
                                                FireBaseMessageService.this);
                                        builder.setContentTitle(APP_NAME)
                                                .setContentText(message)
                                                .setTicker(message)
                                                .setSmallIcon(R.drawable.app_icon)

                                                .setAutoCancel(true);

                                        mNotificationManager.notify(
                                                (int) System.currentTimeMillis(),
                                                builder.build());

										/*		Intent i = new Intent(
														this,
														GCMDialog_Activity.class);
												i.putExtra("messageType",
														"cancel");
												i.putExtra("message", message);

												i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

												startActivity(i);
*/
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }

                                } else {

                                    if (!isActivityRunning(OnlineStatusActivity.class)) {
                                        try {

                                            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                            mNotificationManager
                                                    .cancelAll();


                                            Notification.Builder builder = new Notification.Builder(
                                                    FireBaseMessageService.this);
                                            builder.setContentTitle(APP_NAME)
                                                    .setContentText(message)
                                                    .setTicker(message)
                                                    .setSmallIcon(R.drawable.app_icon)

                                                    .setAutoCancel(true);

                                            mNotificationManager.notify(
                                                    (int) System.currentTimeMillis(),
                                                    builder.build());

											/*		Intent i = new Intent(
															this,
															GCMDialog_Activity.class);
													i.putExtra("messageType",
															"cancel");
													i.putExtra("message", message);

													i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

													startActivity(i);
			*/
                                        } catch (Exception e) {
                                            // TODO: handle exception
                                        }
                                    }else{




                                        Intent i = new Intent(this,
                                                GCMDialog_Activity.class);
                                        i.putExtra("messageType", "cancel");
                                        i.putExtra("message", message);

                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);




                                        startActivity(i);

											/*
											 *
											 *
											 *
											 * headerFragment
											 * .CancelBooking(message); Intent i
											 * = new Intent(this,
											 * GCMDialog_Activity.class);
											 * i.putExtra("messageType",
											 * "cancel"); i.putExtra("message",
											 * message);
											 *
											 * i.setFlags(Intent.
											 * FLAG_ACTIVITY_NEW_TASK );
											 *
											 * startActivity(i);
											 */}}

                                // headerFragment.CancelBooking(message);
                                // } else {
                                // Intent i = new Intent(this,
                                // GCMDialog_Activity.class);
                                // i.putExtra("messageType", "cancel");
                                // i.putExtra("message", message);
                                //
                                // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                // | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                //
                                //
                                //
                                // startActivity(i);
                                // }
                            }else {




                                Intent i = new Intent(this,
                                        GCMDialog_Activity.class);
                                i.putExtra("messageType", "cancel");
                                i.putExtra("message", message);

                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(i);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        if (!isActivityRunning(OnlineStatusActivity.class)) {
                            try {

                                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                mNotificationManager
                                        .cancelAll();


                                Notification.Builder builder = new Notification.Builder(
                                        FireBaseMessageService.this);
                                builder.setContentTitle(APP_NAME)
                                        .setContentText(message)
                                        .setTicker(message)
                                        .setSmallIcon(R.drawable.app_icon)

                                        .setAutoCancel(true);

                                mNotificationManager.notify(
                                        (int) System.currentTimeMillis(),
                                        builder.build());

								/*		Intent i = new Intent(
												this,
												GCMDialog_Activity.class);
										i.putExtra("messageType",
												"cancel");
										i.putExtra("message", message);

										i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

										startActivity(i);
*/
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }else{
                            Intent i = new Intent(this,
                                    GCMDialog_Activity.class);
                            i.putExtra("messageType", "cancel");
                            i.putExtra("message", message);

                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(i);
                        }
                    }

                } else if (jsonObject.has("logoutStatus")) {

                    AndroidWebResponseTask androidWebResponseTask = AndroidWebResponseTask
                            .getInstance(getApplicationContext());
                    FragmentActivity activity = ((ApplicationContext) getApplicationContext())
                            .getActivityContext();
                    // if (activity != null) {
                    // FragmentManager fragManager = activity
                    // .getSupportFragmentManager();
                    // Fragment fragment = null;
                    // HeaderFragment headerFragment = null;
                    // if (activity instanceof DutyStatusActivity) {
                    //
                    // headerFragment = (HeaderFragment) fragManager
                    // .findFragmentById(R.id.activity_duty_status_header_fragment);
                    //
                    // } else if (activity instanceof
                    // OnlineStatusActivity) {
                    // // fragment = fragManager
                    // //
                    // .findFragmentById(R.id.acitivity_main_fragment);
                    // headerFragment = (HeaderFragment) fragManager
                    // .findFragmentById(R.id.activity_online_fragment);
                    //
                    // } else if (activity instanceof WayBillActivity) {
                    // headerFragment = (HeaderFragment) fragManager
                    // .findFragmentById(R.id.activity_duty_status_header_fragment);
                    //
                    // } else if (activity instanceof
                    // FareSummaryActivity) {
                    //
                    // headerFragment = (HeaderFragment) fragManager
                    // .findFragmentById(R.id.activity_duty_status_header_fragment);
                    // }
                    // try {
                    // if (headerFragment != null) {
                    // headerFragment.logoutFromApp(message);
                    // }
                    //
                    // } catch (Exception e) {
                    // e.printStackTrace();
                    // }
                    // } else {

                    Intent i = new Intent(this,
                            GCMDialog_Activity.class);
                    i.putExtra("messageType", "logoutStatus");
                    i.putExtra("message", message);

                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(i);
                    // }

                } else if (jsonObject.has("reservationId")) {

                    player = MediaPlayer.create(FireBaseMessageService.this,
                            R.raw.reservation);
                    player.setVolume(1f, 1f);

                    player.start();

                    Intent i = new Intent(this,
                            ReservationDetails.class);
                    i.putExtra("RESERVATION_ID",
                            jsonObject.getString("reservationId"));
                    i.putExtra("TYPE", 4);

                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(i);

                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    protected Boolean isActivityRunning(Class activityClass) {
        ActivityManager activityManager = (ActivityManager) getBaseContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager
                .getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(
                    task.topActivity.getClassName()))

                return true;
        }

        return false;
    }

    @Override
    public void onSuccess(String response, int UNIQUE_ID) {
        switch (UNIQUE_ID) {
            case TaxiExConstants.LogoutWebserviceResponse:
                Log.e("Logout response", response);
                editPrefs.clear();
                editPrefs.commit();
                ((ApplicationContext) getApplicationContext()).getActivityContext()

                        .finishAffinity();

                startActivity(new Intent(this, MainActivity.class).setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK));

                break;
        }
    }

    @Override
    public void onSuccess(String[] response, int UNIQUE_ID) {

    }

    @Override
    public void onFailure(Throwable e, String error, int UNIQUE_ID) {

    }
}


