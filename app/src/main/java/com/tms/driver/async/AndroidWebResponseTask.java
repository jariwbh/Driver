package com.tms.driver.async;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.Header;
import org.apache.http.NoHttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager.BadTokenException;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * This java class is used to get/post response from/to server
 * 
 * @author user
 * 
 */
public final class AndroidWebResponseTask {
	String TAG = "Driver Request:";
	/*
	 * Instance
	 */
	private final AsyncHttpClient client = new AsyncHttpClient();
	private Handler responseHandler;
	private static AndroidWebResponseTask instance;
	/*
	 * Interface Reference
	 */
	private static WebResponse listener;
	/*
	 * Variable
	 */
	private static Context con;
	private final String INTERNET_ERROR = "Please connect to internet.";
	private final String SERVER_ERROR = "The target server failed to respond";
	private final String SOCKETIMEOUT = "Failed to connect to server.";
	private boolean isSendingSessionId = false;

	/*
	 * Interface
	 */
	public interface WebResponse {
		void onSuccess(String response, int UNIQUE_ID);

		void onSuccess(String[] response, int UNIQUE_ID);

		void onFailure(Throwable e, String error, int UNIQUE_ID);
	}

	/**
	 * This method is used to create instance of this class if not created.
	 * 
	 * @param context
	 *            Set context to pass
	 * @return instance
	 */
	public static AndroidWebResponseTask getInstance(Context context) {
		con = context;
		if (instance == null) {
			instance = new AndroidWebResponseTask();
		}
		return instance;
	}

	public void setCallback(WebResponse callback) {
		listener = callback;
	}

	/*
	 * Default constructor created to stop creating instance of this class
	 */
	private AndroidWebResponseTask() {

	}

	public Context getContext() {
		return con;
	}

	/**
	 * This method is used to get values from web
	 * 
	 * @param url
	 *            url from which response is needed
	 * @param id
	 *            UniqueId
	 * @param isDialog
	 *            want to show progress dialog or not
	 */
	public void get(String url, int id, boolean isDialog) {
		if (isOnline()) {
			responseHandler = new Handler(id, isDialog);
			try {
				client.get(url, responseHandler);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			listener.onFailure(null, INTERNET_ERROR, id);
		}
	}

	/**
	 * This method is used to get value from server
	 * 
	 * @param url
	 *            set url to hit
	 * @param key
	 *            set key
	 * @param value
	 *            set values
	 * @param id
	 *            set uniqueId
	 * @param isDialog
	 *            want to show progress dialog or not
	 */
	public void get(String url, String[] key, String[] value, int id,
			boolean isDialog) {
		if (isOnline()) {
			responseHandler = new Handler(id, isDialog);
			RequestParams params = getParams(key, value);
			try {
				client.get(url, params, responseHandler);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			listener.onFailure(null, INTERNET_ERROR, id);
		}

	}

	/**
	 * This method is used to post values to webserver
	 *

	 * @param key
	 *            key to send to server
	 * @param value
	 *            value again the key
	 * @param id
	 *            UniqueId
	 * @param isDialog
	 *            want to show progress dialog or not
	 */
	public void post(String url, String[] key, String[] value, int id,
			boolean isDialog) {
		if (isOnline()) {
			responseHandler = new Handler(id, isDialog);
			RequestParams params = getParams(key, value);
			try {
				client.post(url, params, responseHandler);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			listener.onFailure(null, INTERNET_ERROR, id);
		}
	}

	/**
	 * This method is used to post to server in jsonobject
	 * 
	 * @param url
	 *            set url to hit
	 * @param key
	 *            set key of jsonobject
	 * @param value
	 *            set value of jsonobject
	 * @param id
	 *            set uniqueId
	 * @param isDialog
	 *            want to show progress dialog or not
	 */
	public void postJsonObject(String url, String[] key, String[] value,
			int id, boolean isDialog, String headerKey, String headerValue) {
		if (isOnline()) {
			client.setTimeout(130000);
			responseHandler = new Handler(id, isDialog);
			StringEntity entity = null;
			try {
				if (key != null && value != null) {
					entity = new StringEntity(encodeJson(key, value));
				}
				if (headerKey != null && headerValue != null
						&& !headerKey.equalsIgnoreCase("")
						&& !headerValue.equalsIgnoreCase("")) {
					isSendingSessionId = true;
					client.addHeader(headerKey, headerValue);
				} else {
					isSendingSessionId = false;
				}
				if (entity != null) {
					client.post(getContext(), url, entity, "application/json",
							responseHandler);
				} else {
					client.post(url, responseHandler);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			listener.onFailure(null, INTERNET_ERROR, id);
		}
	}

	public void postXML(String Url, String data, boolean isDialog, int id) {
		if (isOnline()) {
			client.setTimeout(130000);
			responseHandler = new Handler(id, isDialog);
			StringEntity entity = null;
			try {

				entity = new StringEntity(data);

				// if (entity == null) {
				// client.post(Url, responseHandler);
				//
				// } else {
				client.post(getContext(), Url, entity, "application/xml",
						responseHandler);

				// }
		Log.d(TAG,"url"+Url+"\n request:"+data+"\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			listener.onFailure(null, INTERNET_ERROR, id);
		}
	}

	// public void postJsonObject(String url, String[] key, String[] value,
	// int id, boolean isDialog) {
	// if (isOnline()) {
	// client.setTimeout(130000);
	// isSendingSessionId = false;
	// responseHandler = new Handler(id, isDialog);
	// try {
	// String encodedJson = "\""
	// + Utilities.getInstance(getContext()).encryptString(
	// encodeJson(key, value)) + "\"";
	// StringEntity entity = new StringEntity(encodedJson);
	// // StringEntity entity = new StringEntity(Utilities.getInstance(
	// // getContext()).encryptString(encodeJson(key, value)));
	//
	// client.post(getContext(), url, entity, "application/json",
	// responseHandler);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// } else {
	// listener.onFailure(null, INTERNET_ERROR, id);
	// }
	// }

	/**
	 * This method is used to post jsonArray to server
	 * 
	 * @param url
	 *            set the url to hit
	 * @param jsonArray
	 *            set jsonArray as string
	 * @param id
	 *            unique id
	 * @param isDialog
	 *            want to show progress dialog or not
	 */
	public void postJsonArray(String url, String jsonArray, int id,
			boolean isDialog) {
		if (isOnline()) {
			responseHandler = new Handler(id, isDialog);
			try {
				if (!TextUtils.isEmpty(jsonArray)) {
					StringEntity entity = new StringEntity(jsonArray);
					client.post(getContext(), url, entity, "application/json",
							responseHandler);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			listener.onFailure(null, INTERNET_ERROR, id);
		}
		Log.d(TAG,"url"+url+"\n request:"+jsonArray+"\n");
	}

	/**
	 * This method is used put value using put
	 * 
	 * @param url
	 * @param id
	 * @param isDialog
	 *            want to show progress dialog or not
	 */
	public void put(String url, int id, boolean isDialog) {
		if (isOnline()) {
			responseHandler = new Handler(id, isDialog);
			try {
				client.put(url, responseHandler);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			listener.onFailure(null, INTERNET_ERROR, id);
		}
	}

	/**
	 * This method is used to put value to server
	 * 
	 * @param url
	 *            set url to hit
	 * @param key
	 *            set key
	 * @param value
	 *            set value
	 * @param id
	 *            set unique Id
	 * @param isDialog
	 *            want to show progress dialog or not
	 */
	public void put(String url, String[] key, String[] value, int id,
			boolean isDialog) {
		if (isOnline()) {
			responseHandler = new Handler(id, isDialog);
			RequestParams params = getParams(key, value);
			try {
				client.put(url, params, responseHandler);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			listener.onFailure(null, INTERNET_ERROR, id);
		}
	}

	/**
	 * This method is used to put to server in jsonobject
	 * 
	 * @param url
	 *            set url to hit
	 * @param key
	 *            set key of jsonobject
	 * @param value
	 *            set value of jsonobject
	 * @param id
	 *            set uniqueId
	 * @param isDialog
	 *            want to show progress dialog or not
	 */
	public void putJsonObject(String url, String[] key, String[] value, int id,
			boolean isDialog) {
		if (isOnline()) {
			responseHandler = new Handler(id, isDialog);
			try {
				StringEntity entity = new StringEntity(encodeJson(key, value));
				client.put(getContext(), url, entity, "application/json",
						responseHandler);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			listener.onFailure(null, INTERNET_ERROR, id);
		}
	}

	/**
	 * This class is Used to get resposne from the webserver
	 * 
	 * @author user
	 * 
	 */
	private class Handler extends AsyncHttpResponseHandler {
		private int UNIQUE_ID;
		private boolean isDialog;
		private ProgressDialog mProgressDialog;

		public Handler(int id, boolean isDialog) {
			UNIQUE_ID = id;
			this.isDialog = isDialog;
			if (isDialog) {
				mProgressDialog = new ProgressDialog(getContext());
				mProgressDialog.setMessage("Please Wait...");
				mProgressDialog.setCanceledOnTouchOutside(false);
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// TODO Auto-generated method stub
			if (listener != null) {
				if (error.getClass().equals(UnknownHostException.class)) {
					listener.onFailure(error, INTERNET_ERROR, UNIQUE_ID);
				} else if (error.getClass()
						.equals(SocketTimeoutException.class)) {
					listener.onFailure(error, SOCKETIMEOUT, UNIQUE_ID);
				} else if (error.getClass().equals(ConnectException.class)) {
					listener.onFailure(error, INTERNET_ERROR, UNIQUE_ID);
				} else if (error.getClass().equals(
						NoHttpResponseException.class)) {
					listener.onFailure(error, SERVER_ERROR, UNIQUE_ID);
				} else if (error.getClass().equals(
						ConnectTimeoutException.class)) {
					listener.onFailure(error, SOCKETIMEOUT, UNIQUE_ID);
				} else {
					listener.onFailure(error, content, UNIQUE_ID);
				}

			}
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			try {
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.dismiss();

				// UrlConstants.isShowingDialog = false;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			try {
				if (mProgressDialog != null && this.isDialog)
					// UrlConstants.isShowingDialog = true;

					mProgressDialog.show();
			} catch (BadTokenException e) {
				e.printStackTrace();

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		@Override
		protected void handleSuccessMessage(int statusCode, Header[] headers,
				String responseBody) {
			Log.e("Header Values", "" + headers[0].toString());
			Log.e("response body", "" + responseBody);
			Log.e("Status Code", "" + statusCode);

			// TODO Auto-generated method stub
			super.handleSuccessMessage(statusCode, headers, responseBody);
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, String content) {
			// TODO Auto-generated method stub

			if (listener != null) {

				listener.onSuccess(content, UNIQUE_ID);

			}

		}
		// @Override
		// public void onSuccess(String content) {
		// // TODO Auto-generated method stub
		// if (listener != null)
		// listener.onSuccess(content, UNIQUE_ID);
		// }

	}

	/**
	 * This method is used to make key/value pair into jsonobject and pass to
	 * postserver
	 * 
	 * @param key
	 *            set key[]
	 * @param value
	 *            set value[]
	 * @return Json.toString
	 */
	private String encodeJson(String[] key, String[] value) {
		JSONObject obj = new JSONObject();
		int length = key.length;
		for (int i = 0; i < length; i++) {
			try {
				obj.put(key[i], value[i]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return obj.toString();
	}

	/**
	 * This method is used to getParams from the key and value
	 * 
	 * @param key
	 *            set the key
	 * @param value
	 *            set values against the key
	 * @return RequestParams
	 */
	private RequestParams getParams(String[] key, String[] value) {
		RequestParams params = new RequestParams();
		if (key != null && value != null && key.length > 0 && value.length > 0) {
			for (int i = 0; i < key.length; i++) {
				params.put(key[i], value[i]);
			}
		}
		return params;
	}

	/**
	 * This method is used to detect intenet connection is working or not
	 * 

	 * @return connected(boolean)
	 */
	private boolean isOnline() {
		boolean mConnected;
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) getContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			mConnected = networkInfo != null && networkInfo.isAvailable()
					&& networkInfo.isConnected();

		} catch (Exception e) {
			mConnected = false;
		}
		return mConnected;

	}
}
