package com.tms.driver;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.models.UserLoginModel;
import com.tms.driver.utilities.SendXmlAsync;
import com.tms.driver.utilities.XmlListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

public class LocationUpdateService extends Service implements XmlListener {


	private SharedPreferences mSharedprefrences;
	private LocationManager manager;
	GoogleApiClient client;
	Location oldLocation;
	Double oldLat=0.0d,oldLong=0.0d;
	float distanceCalculated=0.0f;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub


		if(client==null ){



		client=new GoogleApiClient.Builder(LocationUpdateService.this)
				.addApi(LocationServices.API)
				.addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

					@Override
					public void onConnectionSuspended(int arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onConnected(Bundle arg0) {
						// TODO Auto-generated method stub
						LocationRequest request = new LocationRequest();

						//request.setSmallestDisplacement(5);
						request.setInterval(20000);
						request.setFastestInterval(10000);
						request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
						request.setSmallestDisplacement(10);

						LocationServices.FusedLocationApi
								.requestLocationUpdates(client,
										request,
										new LocationListener() {

											@Override
											public void onLocationChanged(
													Location arg0) {
												// TODO Auto-generated method
												// stub

												// TODO Auto-generated method stub



												if(UserLoginModel.getInstance()!=null){


												String booking_id="0";



												if(OnlineStatusActivity.mBookingId!=null){
													booking_id= OnlineStatusActivity.mBookingId;

												}



													if(oldLocation!=null && arg0.equals(oldLocation)){


														return;
													}



												if(oldLocation!=null){

												distanceCalculated=(arg0.distanceTo(oldLocation));
												}




													double miles = distanceCalculated * 0.000621;

												final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><driverStatus><driverId><![CDATA["
														+ UserLoginModel
																.getInstance()
																.getId()
														+ "]]></driverId><latitdue><![CDATA["
														+ arg0.getLatitude()
														+ "]]></latitdue><longitude><![CDATA["
														+ arg0.getLongitude()
														+ "]]></longitude><bearingDegree><![CDATA["
														+ arg0.getBearing()
														+ "]]></bearingDegree><bearingWR><![CDATA["
														+ arg0.getBearing()
														+ "]]></bearingWR><bookingId><![CDATA["
														+ booking_id
														+ "]]></bookingId><distance><![CDATA["
														+ miles
														+ "]]></distance></driverStatus>";

												Log.i("xml", xml);

										//		Toast.makeText(LocationUpdateService.this,  ""+arg0.getBearing(), Toast.LENGTH_SHORT).show();

												new SendXmlAsync(
														TaxiExConstants.LatLongUpdateWithDistanceURL,
														xml,
														LocationUpdateService.this,
														LocationUpdateService.this,
														false).execute();


												oldLat=arg0.getLatitude();
												oldLong=arg0.getLongitude();
												oldLocation=new Location("old Location");

												oldLocation.setLatitude(arg0.getLatitude());
												oldLocation.setLongitude(arg0.getLongitude());
												}





											}
										});

					}
				})
				.addOnConnectionFailedListener(
						new GoogleApiClient.OnConnectionFailedListener() {

							@Override
							public void onConnectionFailed(ConnectionResult arg0) {
								// TODO Auto-generated method stub
								client.connect();
							}
						}).build();

		}

		client.connect();





		/*if(manager!=null){

		manager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);

		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 10, new android.location.LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub

				location.getBearing();

			}
		});
		}

		if (locationClient == null) {

			locationClient = new LocationClient(LocationUpdateService.this,
					new ConnectionCallbacks() {

						@Override
						public void onDisconnected() {
							// TODO Auto-generated method stub

						}

						@Override
						public void onConnected(Bundle arg0) {
							// TODO Auto-generated method stub

							LocationRequest request = new LocationRequest();

							request.setSmallestDisplacement(10);
							request.setInterval(20000);
							request.setFastestInterval(5000);
							request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

							locationClient.requestLocationUpdates(request,
									new LocationListener() {

										@Override
										public void onLocationChanged(
												Location arg0) {
											// TODO Auto-generated method stub

											final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><driverStatus><driverId><![CDATA["
													+ UserLoginModel
															.getInstance()
															.getId()
													+ "]]></driverId><latitdue><![CDATA["
													+ arg0.getLatitude()
													+ "]]></latitdue><longitude><![CDATA["
													+ arg0.getLongitude()
													+ "]]></longitude><bearingDegree><![CDATA["
													+ arg0.getBearing()
													+ "]]></bearingDegree><bearingWR><![CDATA["
													+ arg0.getBearing()
													+ "]]></bearingWR><bookingId><![CDATA["
													+ "0"
													+ "]]></bookingId><distance><![CDATA["
													+ "0"
													+ "]]></distance></driverStatus>";

											Log.i("xml", xml);

											Toast.makeText(LocationUpdateService.this,  ""+arg0.getBearing(), Toast.LENGTH_SHORT).show();

											new SendXmlAsync(
													TaxiExConstants.LatLongUpdateWithDistanceURL,
													xml,
													LocationUpdateService.this,
													LocationUpdateService.this,
													false).execute();

										}
									});

						}
					}, new OnConnectionFailedListener() {

						@Override
						public void onConnectionFailed(ConnectionResult arg0) {
							// TODO Auto-generated method stub

							locationClient.connect();

						}
					});

		}


		locationClient.connect();*/


		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onResponse(String respose) {
		// TODO Auto-generated method stub

		Log.i("updated Lat long", respose);

	}

	private void getUserInfo() {

		if (UserLoginModel.getInstance() == null) {

			mSharedprefrences = getSharedPreferences(
					TaxiExConstants.SharedPreferenceName, 0);

			byte[] bytes = mSharedprefrences.getString(
					TaxiExConstants.Pref_Login_Details, "{}").getBytes();
			if (bytes.length == 0) {
				return;
			}
			ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
			Base64InputStream base64InputStream = new Base64InputStream(
					byteArray, Base64.DEFAULT);
			ObjectInputStream in;
			try {
				in = new ObjectInputStream(base64InputStream);
				// UserLoginModel loginModel = UserLoginModel.getInstance();
				UserLoginModel.setInstance((UserLoginModel) in.readObject());
				base64InputStream.close();
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();


		if(client!=null){

			client.disconnect();
		}
		/*if (locationClient != null) {
			locationClient.disconnect();
		}*/
	}

}
