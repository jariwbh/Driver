package com.tms.driver.async;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

/**
 * This class is used as a background thread. The server call will be
 * established and the result will be get by the Http call according to user
 */
public class WebServiceAsync {
	private String methodName;

	public enum HttpMethod {
		GET,

		POST,

		PUT,

		DELETE
	}

	// String Variables and int variables

	private int isGetPost;
	// interface instance
	private static OnWebServiceProcess interfaceListener;
	// Log Tag
	private final String TAG = "WebServiceAsync";
	private final int POST = 1;
	// boolean Variable
	private boolean isSuccess;
	// User defined Classes
	private static WebServiceAsync instance;
	private final String CHAR_SET = "UTF-8",
			INTERNET_ERROR = "Please connect to internet.";
	private Context context;

	/**
	 * This interface used to reduce redundant use of code for server request
	 * and works with WebServiceAsync Class to fetch and insert the data on to
	 * server
	 */
	public interface OnWebServiceProcess {
		void getServerValues(String response, int id);

		void setServerError(int id, String msg);
	}

	/**
	 * This is sigleton method of {@link com.tms.driver.async.WebServiceAsync} class. This return new
	 * instance of this class is not created otherwise return old instance
	 * 
	 * @param context
	 *            {@link android.content.Context} of current class is passed

	 * @return {@link com.tms.driver.async.WebServiceAsync} instance returned
	 */
	public static WebServiceAsync getInstance(Context context) {
		return (instance == null) ? instance = new WebServiceAsync(context)
				: instance;
	}

	public void setCallback(OnWebServiceProcess callback) {
		interfaceListener = callback;
	}

	/*
	 * This is default contraster make private because its object is not allowed
	 * from outside this class
	 */
	private WebServiceAsync() {
		Log.i(TAG, "Instance Created");
	}

	/*
	 * This is default contraster make private because its object is not allowed
	 * from outside this class
	 */
	private WebServiceAsync(Context context) {
		Log.i(TAG, "Instance Created");
		this.context = context;
	}

	public Context getContext() {
		return this.context;
	}

	/**
	 * This method is used to runTask in background
	 * 
	 * @param url
	 *            from where you want to get response
	 * @param id
	 *            unique id to be passed
	 */
	@Deprecated
	public void get(String url, int id) {
		hit(url, id, HttpMethod.GET, false, null, null);
	}

	/**
	 * This method is used to get url in key/pair value
	 * 
	 * @param url
	 *            set url to execute
	 * @param id
	 *            set unique id
	 * @param key
	 *            set stirng[] keys
	 * @param value
	 *            set String[] values
	 */
	@Deprecated
	public void get(String url, int id, String[] key, String[] value) {
		hit(url, id, HttpMethod.GET, false, key, value);
	}

	/**
	 * This method is used to runTask in background for post
	 * 
	 * @param url
	 *            set the url to pass
	 * @param id
	 *            set uniqueId

	 */
	@Deprecated
	public void post(String url, int id, boolean isJsonobject, String[] key,
			String[] value) {
		hit(url, id, HttpMethod.POST, isJsonobject, key, value);
	}

	/**
	 * This method is used to hit service according to need
	 * 
	 * @param url
	 *            set url to hit
	 * @param id
	 *            set unique id to get response
	 * @param httpMethod
	 *            set httpMethod
	 * @param isJsonobject
	 *            set true if jsonObject needed else false
	 * @param key
	 *            set key array if have key to pass along with url
	 * @param value
	 *            set value array if have value to pass along with url
	 */

	public void hit(String url, int id, HttpMethod httpMethod,
			boolean isJsonobject, String[] key, String[] value) {
		if (isOnline(getContext())) {
			isGetPost = POST;
			methodName = httpMethod.name();
			Task task = new Task(id, isJsonobject);
			String data;
			if (key != null && value != null) {
				if (isJsonobject) {
					data = encodeJson(key, value);
					task.execute(url, data);
				} else {
					data = encodeUrl(key, value);
					task.execute(url, data);
				}
			} else {
				task.execute(url, null);
			}
		} else {
			interfaceListener.setServerError(id, INTERNET_ERROR);
		}
	}

	public void hitJsonArray(String url, int id, HttpMethod httpMethod,
			JSONArray jsonArray) {
		if (isOnline(getContext())) {
			isGetPost = POST;
			methodName = httpMethod.name();
			Task task = new Task(id, true);
			String data;
			data = jsonArray.toString();
			task.execute(url, data);
		} else {
			interfaceListener.setServerError(id, INTERNET_ERROR);
		}
	}

	/**
	 * This class is used to run in background
	 * 
	 * @author Amit
	 * 
	 */
	private class Task extends AsyncTask<String, Void, String> {
		private int receivedId;
		private boolean isJsonobject;

		public Task(int id, boolean isJsonobject) {
			receivedId = id;
			this.isJsonobject = isJsonobject;
		}

		@Override
		protected void onPostExecute(String result) {
			if (isSuccess == false) {
				interfaceListener.setServerError(receivedId, result);
			} else {
				interfaceListener.getServerValues(result, receivedId);
			}

		}

		@Override
		protected String doInBackground(String... params) {

			String getResult = null;
			StringBuilder content = new StringBuilder();
			switch (isGetPost) {
			case POST:
				try {
					URL url = new URL(params[0]);
					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();
					urlConnection.setReadTimeout(10 * 1000);
					urlConnection.setConnectTimeout(30 * 1000);
					urlConnection.setDoOutput(true);
					urlConnection.setRequestMethod(methodName);
					// urlConnection.addRequestProperty(
					// UrlConstants.SessionRequestHeaderKey,
					// LoginDataModel.getLoginModelInstance()
					// .getmSessionId());
					// urlConnection.setRequestProperty(
					// UrlConstants.SessionRequestHeaderKey,
					// LoginDataModel.getLoginModelInstance()
					// .getmSessionId());
					urlConnection
							.setRequestProperty("Accept-Charset", CHAR_SET);

					urlConnection.setRequestProperty(HTTP.CONTENT_TYPE,
							"application/x-www-form-urlencoded;charset="
									+ CHAR_SET);
					if (!TextUtils.isEmpty(params[1])) {
						if (isJsonobject) {
							urlConnection.setRequestProperty("Accept",
									"application/json");
							urlConnection.setRequestProperty(HTTP.CONTENT_TYPE,
									"application/json");
						}
						urlConnection.setFixedLengthStreamingMode(params[1]
								.getBytes().length);
						OutputStreamWriter wr = new OutputStreamWriter(
								urlConnection.getOutputStream());
						wr.write(params[1]);
						wr.flush();
					}
					int responsecode = urlConnection.getResponseCode();
					String respnsMessage = urlConnection.getResponseMessage();

					for (int i = 0; i < urlConnection.getHeaderFields().size(); i++) {
						String headerName = urlConnection.getHeaderFieldKey(i);
						String headerValue = urlConnection.getHeaderField(i);
						Log.e("Header Name = " + headerName, "Header Value ="
								+ headerValue);

					}
					// String responseHeaderUserID = urlConnection
					// .getHeaderField("UserId");
					// String responseError = urlConnection
					// .getHeaderField("UserId");
					// String responseMessage = urlConnection
					// .getHeaderField("UserId");
					// Log.e("Header Response values ", "" +
					// responseHeaderUserID
					// + "--" + responseError + "--" + responseMessage);
					if (responsecode == HttpURLConnection.HTTP_OK) {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(
										urlConnection.getInputStream()));
						String line = null;
						// Read Server Response
						while ((line = reader.readLine()) != null) {
							// Append server response in string
							content.append(line + "\n");
						}
						getResult = content.toString();
						isSuccess = true;
						reader.close();
					} else {
						getResult = "Response code " + responsecode + "error";
						isSuccess = false;
					}
				} catch (UnknownHostException e) {
					isSuccess = false;
					getResult = e.getMessage();
				} catch (SocketTimeoutException e) {
					isSuccess = false;
					getResult = e.getMessage();
				} catch (ConnectException e) {
					isSuccess = false;
					getResult = e.getMessage();
				} catch (Exception e) {
					isSuccess = false;
					getResult = e.getMessage();
					e.printStackTrace();
				} finally {
					content = null;
				}
				break;

			default:
				break;
			}
			return getResult;

		}
	}

	/**
	 * This method is used to make key/value pair into string and pass to
	 * postserver
	 * 
	 * @param key
	 *            set key[]
	 * @param value
	 *            set value[]
	 * @return string
	 */
	private String encodeUrl(String[] key, String[] value) {
		if (key == null && value == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		int length = key.length;
		if (length != value.length) {
			return "";
		}
		for (int i = 0; i < length; i++) {

			if (!(key instanceof String[])) {
				continue;
			}

			if (first)
				first = false;
			else
				sb.append("&");
			try {
				sb.append(key[i] + "=" + URLEncoder.encode(value[i], CHAR_SET));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
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
				obj.put(key[i], URLEncoder.encode(value[i], CHAR_SET));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return obj.toString();
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
	private String encodeJsonArray(String[] key, String[] value) {
		JSONArray jsonArray = new JSONArray();

		int length = key.length;
		for (int i = 0; i < length; i++) {
			try {
				JSONObject obj = new JSONObject();
				for (int j = 0; j < length; j++) {
					obj.put(key[j], URLEncoder.encode(value[j], CHAR_SET));
					jsonArray.put(obj);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return jsonArray.toString();
	}

	/**
	 * This method is used to detect intenet connection is working or not
	 * 
	 * @param context
	 *            context of your application
	 * @return connected(boolean)
	 */
	public boolean isOnline(Context context) {
		boolean mConnected;
		try {
			Log.i(TAG, "Detect Connection");
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			mConnected = networkInfo != null && networkInfo.isAvailable()
					&& networkInfo.isConnected();

		} catch (Exception e) {

			mConnected = false;
			e.printStackTrace();
		}
		Log.i(TAG, "mConnected = " + mConnected);
		return mConnected;

	}

}