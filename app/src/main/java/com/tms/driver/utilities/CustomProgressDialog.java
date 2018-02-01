package com.tms.driver.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

public class CustomProgressDialog {

	private static CustomProgressDialog instance;
	private static Context context;
	private static ProgressDialog progressDialog;

	/**
	 * Private constructor
	 */
	private CustomProgressDialog() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method is used to get Instance of the the CustomProgreesDialog and
	 * new instance of progessDialog.
	 * 
	 * @param ctx
	 *            of calling activity
	 * @return instance of the CustomProgessDialog
	 */
	protected static CustomProgressDialog getInstance(Context ctx) {
		if (instance == null) {
			instance = new CustomProgressDialog();

		}
		context = ctx;
		progressDialog = new ProgressDialog(ctx);
		return instance;

	}

	/**
	 * This method is used to show the progress dialog.
	 * 
	 * @param message
	 *            for the progress dialog to display
	 * @param iconRes
	 *            for icon to be diaplayed in progress dialog.
	 */
	public void showDialog(String message, int iconRes) {
		if (progressDialog != null) {
			// dismissDialog();
			if (!TextUtils.isEmpty(message)
					&& !TextUtils.equals("null", message)) {
				progressDialog.setMessage(message);
			}
			if (iconRes > 0) {
				progressDialog.setIcon(iconRes);
			}

			progressDialog.show();
		}
	}

	/*
	 * public void showCustomDialog(View view) { if (progressDialog != null) {
	 * // dismissDialog();
	 * 
	 * progressDialog.setContentView(view); progressDialog.show(); } }
	 * 
	 * public void showCustomDialog(int layoutResId) { if (progressDialog !=
	 * null) { dismissDialog(); progressDialog.setContentView(layoutResId);
	 * progressDialog.show(); } }
	 */

	public void dismissDialog() {
		if (progressDialog != null) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
				progressDialog = null;
			}

		}
	}
}
