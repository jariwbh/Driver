package com.tms.driver;

import android.app.ActivityManager;
import android.app.IntentService;
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

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tms.driver.async.AndroidWebResponseTask;
import com.tms.driver.async.AndroidWebResponseTask.WebResponse;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.customviews.CustomLinearLayout;
import com.tms.driver.fragments.HeaderFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This Intent Service is used to get Push from the server end
 * 
 * GCM is Regester from @gmail.com under project WuleebDriver
 * 
 * 
 */
public class GcmIntentService extends IntentService implements WebResponse {

	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	public static int count = 0;
	String message;
	private OnlineStatusActivity onLineStatusActivity;
	private SharedPreferences mSharedprefrences;
	private SharedPreferences.Editor editPrefs;
	public static boolean isBookingRequest = false;

	public static MediaPlayer player;
	Intent nid;
	String bookingId;
	
	
	private String APP_NAME="TMS";

	public GcmIntentService() {
		super("GcmIntentService");
	}

	public static final String TAG = "GcmIntentService";

	@Override
	protected void onHandleIntent(Intent intent) {
		mSharedprefrences = getSharedPreferences(
				TaxiExConstants.SharedPreferenceName, 0);
		editPrefs = mSharedprefrences.edit();
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				// sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				// sendNotification("Deleted messages on server: "
				// + extras.toString());

				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				message = extras.getString("message");
				Bundle bundle = new Bundle();
				String extraDetails = extras.getString("extraDetails");
				String from = extras.getString("from");
				try {
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

								nid = new Intent(GcmIntentService.this,
										OnlineStatusActivity.class);
								nid.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
								nid.putExtra("message", message);
								nid.putExtra("bookingId", bookingId);
								nid.putExtra("FROM_NOTIFICATION", true);
								// If you were starting a service, you wouldn't
								// using getActivity() here

								PendingIntent cid = null;
								cid = PendingIntent.getActivity(
										GcmIntentService.this,
										(int) System.currentTimeMillis(), nid,
										PendingIntent.FLAG_ONE_SHOT);

								mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

								Notification.Builder builder = new Notification.Builder(
										GcmIntentService.this);
								builder.setContentTitle(APP_NAME)
										.setContentText(message)
										.setTicker(message)
										.setSmallIcon(R.drawable.app_icon)
										.setContentIntent(cid)
										.setAutoCancel(true);

								player = MediaPlayer.create(
										GcmIntentService.this, R.raw.sec);
								player.setVolume(1f, 1f);
								player.start();

								// auto cancel means the notification will
								// remove itself when pressed
								mNotificationManager.notify(
										(int) System.currentTimeMillis(),
										builder.getNotification());

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

								nid = new Intent(GcmIntentService.this,
										OnlineStatusActivity.class);
								nid.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
								nid.putExtra("message", message);
								nid.putExtra("bookingId", bookingId);
								nid.putExtra("FROM_NOTIFICATION", true);
								// If you were starting a service, you wouldn't
								// using getActivity() here

								PendingIntent ci = null;
								ci = PendingIntent.getActivity(
										GcmIntentService.this,
										(int) System.currentTimeMillis(), nid,
										PendingIntent.FLAG_ONE_SHOT);

								mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

								Notification.Builder builder = new Notification.Builder(
										GcmIntentService.this);
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
										GcmIntentService.this, R.raw.sec);
								player.setVolume(1f, 1f);
								player.start();

								// + R.raw.sec));

								// auto cancel means the notification will
								// remove itself when pressed
								mNotificationManager.notify(
										(int) System.currentTimeMillis(),
										builder.getNotification());

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

							editPrefs
									.remove(TaxiExConstants.pref_Booking_details);
							editPrefs.commit();

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
														GcmIntentService.this);
												builder.setContentTitle(APP_NAME)
														.setContentText(message)
														.setTicker(message)
														.setSmallIcon(R.drawable.app_icon)

														.setAutoCancel(true);

												mNotificationManager.notify(
														(int) System.currentTimeMillis(),
														builder.getNotification());

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
															GcmIntentService.this);
													builder.setContentTitle(APP_NAME)
															.setContentText(message)
															.setTicker(message)
															.setSmallIcon(R.drawable.app_icon)

															.setAutoCancel(true);

													mNotificationManager.notify(
															(int) System.currentTimeMillis(),
															builder.getNotification());

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
												GcmIntentService.this);
										builder.setContentTitle(APP_NAME)
												.setContentText(message)
												.setTicker(message)
												.setSmallIcon(R.drawable.app_icon)

												.setAutoCancel(true);

										mNotificationManager.notify(
												(int) System.currentTimeMillis(),
												builder.getNotification());

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

							player = MediaPlayer.create(GcmIntentService.this,
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
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
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

	// Put the message into a notification and post it.

	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(Throwable e, String error, int UNIQUE_ID) {
		// TODO Auto-generated method stub

	}

}
