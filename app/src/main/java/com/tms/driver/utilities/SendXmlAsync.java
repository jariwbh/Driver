package com.tms.driver.utilities;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tms.driver.utilities.*;

/**
 * SendXmlAsync is Asynctask class to send xml data to web service
 * 
 * @author rishab.bhardwaj
 * 
 */
public class SendXmlAsync extends AsyncTask<Void, Void, Void> {

	String strUrl;
	String strEntity;
	com.tms.driver.utilities.XmlListener mListener;
	String response = "";
	ProgressDialog mDialog;
	Context mContext;
	boolean isDialog;

	/**
	 * constructor of class
	 * 
	 * @param strUrl
	 *            url of web service
	 * @param strEntity
	 *            xml string to send data
	 * @param mListener
	 *            listner to catch response
	 * 
	 * @param mContext
	 *            context of class
	 */
	public SendXmlAsync(String strUrl, String strEntity, com.tms.driver.utilities.XmlListener mListener,
			Context mContext, boolean isDialog) {
		// TODO Auto-generated constructor stub
		this.strEntity = strEntity;
		this.mListener = mListener;
		this.strUrl = strUrl;
		this.mContext = mContext;
		this.isDialog = isDialog;

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub

		super.onPreExecute();
		if (isDialog == true) {
			mDialog =  new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
			mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDialog.setMessage("Loading...");
			mDialog.setCancelable(false);
			mDialog.show();

		}

	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (!response.equalsIgnoreCase("") && response != null) {
			mListener.onResponse(response);
			if (isDialog==true) {
				mDialog.dismiss();
			}
		
		} else {
			Log.e("-----", "Error");
			mListener.onResponse(response);
			if (isDialog==true) {
				mDialog.dismiss();
			}
		}
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(strUrl);
			StringEntity stringEntity = new StringEntity(strEntity);

			stringEntity.setContentType("application/atom+xml");
			httpPost.setEntity(stringEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();

			String Response = EntityUtils.toString(httpEntity);

			response = Response;
			/*
			 * mListener.onResponse(Response); mDialog.dismiss();
			 */
		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;
	}

}
