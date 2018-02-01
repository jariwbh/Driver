package com.tms.driver.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.Window;

import com.tms.driver.interfaces.OnAlertDialogFragmentListener;

/*
 * This Class is used to create custom alert dialog. You can create single option, double option and three options alert dialog.
 */
class AlertDialogUtil {

	private OnAlertDialogFragmentListener alertDialogListener;
	private Context context;


	protected AlertDialogUtil() {

	}
	
	
	public AlertDialog alertDialog;
	
	public void hideAlertBox(){
		
		if(alertDialog!=null){
			
			alertDialog.dismiss();
		}
	}

	public AlertDialog singleOptionAlertDialog(Context ctx, String title,
			String msg, String button1, int iconResId,
			OnAlertDialogFragmentListener listener, final int activityId) {
		this.context = ctx;
		 alertDialog = null;
		try {
			this.alertDialogListener = listener;
			alertDialog = new AlertDialog.Builder(ctx).setMessage(msg)
					.setPositiveButton(button1, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (alertDialogListener != null) {
								alertDialogListener
										.onPositiveButtonClick(activityId);
							}// TODO Auto-generated method stub

						}
					}).create();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null == title) {
				alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			} else {
				alertDialog.setTitle(title);
			}
			if (iconResId != 0) {
				alertDialog.setIcon(iconResId);
			}
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.show();
			alertDialog.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {

					// TODO Auto-generated method stub
					if (alertDialogListener != null) {
						alertDialogListener.onBackPress(activityId);
					}
					return true;
				}
			});
		}
		return alertDialog;

	}

	/**
	 * Get new instance of Double Option alert dialog **Note - User can pass
	 * null for String value and 0 for integer value for unwanted parameters.
	 * 
	 * @param ctx
	 *            of Activity to which dialog is related
	 * @param title
	 *            for dialog
	 * @param msg
	 *            to print
	 * @param button1
	 *            name of first(Positive) button
	 * @param button2
	 *            name of second(Negative) button
	 * @param iconResId
	 *            for Icon to set on Dialog
	 * @param listener
	 *            OnALertFragmentListener to perform action on button clicked.
	 * @param activityId
	 *            to identify functionality of first button
	 * 
	 * @return alertDialog
	 */

	public AlertDialog doubleOptionAlertDialog(Context ctx, String title,
			String msg, String button1, String button2, int iconResId,
			OnAlertDialogFragmentListener listener, final int activityId) {
		 alertDialog = null;
		this.context = ctx;
		try {
			this.alertDialogListener = listener;
			alertDialog = new AlertDialog.Builder(ctx)
					.setMessage(msg)
					.setPositiveButton(button1,
							new OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									if (alertDialogListener != null) {
										alertDialogListener
												.onPositiveButtonClick(activityId);
									}

								}
							})
					.setNegativeButton(button2,
							new OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (alertDialogListener != null) {
										alertDialogListener
												.onNegativeButtonClick(activityId);
									}
								}
							}).create();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null == title) {
				alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			} else {
				alertDialog.setTitle(title);
			}
			if (iconResId != 0) {
				alertDialog.setIcon(iconResId);
			}
			alertDialog.setCanceledOnTouchOutside(false);

			alertDialog.show();
		}
		return alertDialog;

	}

	/**
	 * Get new instance of Double Option alert dialog **Note - User can pass
	 * null for String value and 0 for integer value for unwanted parameters.
	 * 
	 * @param ctx
	 *            of Activity to which dialog is related
	 * @param title
	 *            for dialog
	 * @param msg
	 *            to print
	 * @param button1
	 *            name of first(Positive) button
	 * @param button2
	 *            name of second(Negative) button
	 * @param button3
	 *            name of third(Neutral) button
	 * @param iconResId
	 *            for Icon to set on Dialog
	 * @param listener
	 *            OnALertFragmentListener to perform action on button clicked.
	 * @param activityId
	 *            to identify functionality of first button
	 * 
	 * @return alertDialog
	 */
	public AlertDialog threeOptionAlertDialog(Context ctx, String title,
			String msg, String button1, String button2, String button3,
			int iconResId, OnAlertDialogFragmentListener listener,
			final int activityId) {
		AlertDialog alertDialog = null;
		this.context = ctx;
		try {
			this.alertDialogListener = listener;
			alertDialog = new AlertDialog.Builder(ctx)
					.setMessage(msg)
					.setPositiveButton(button1,
							new OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									if (alertDialogListener != null) {
										alertDialogListener
												.onPositiveButtonClick(activityId);
									}

								}
							})
					.setNegativeButton(button2,
							new OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (alertDialogListener != null) {
										alertDialogListener
												.onNegativeButtonClick(activityId);
									}
								}
							})
					.setNeutralButton(button3,
							new OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									if (alertDialogListener != null) {
										alertDialogListener
												.onNeutralizeButtonClick(activityId);
									}
								}
							}).create();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null == title) {
				alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			} else {
				alertDialog.setTitle(title);
			}
			if (iconResId != 0) {
				alertDialog.setIcon(iconResId);
			}
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.show();
		}
		return alertDialog;

	}

}