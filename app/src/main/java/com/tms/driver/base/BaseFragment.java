package com.tms.driver.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.tms.driver.R;
import com.tms.driver.async.AndroidWebResponseTask;
import com.tms.driver.async.AndroidWebResponseTask.WebResponse;
import com.tms.driver.async.WebServiceAsync.OnWebServiceProcess;
import com.tms.driver.fragments.HeaderFragment;
import com.tms.driver.interfaces.OnAlertDialogFragmentListener;
import com.tms.driver.parse.ParseContent.onParsingContent;
import com.tms.driver.utilities.Utilities;
import com.tms.driver.utilities.Utilities.DatePickerdialogListener;
import com.tms.driver.utilities.Utilities.OnSpinnerDialogListener;
import com.tms.driver.utilities.Utilities.OnTimePickerDialogListener;

public abstract class BaseFragment extends Fragment implements
		OnWebServiceProcess, onParsingContent, OnAlertDialogFragmentListener,
		OnClickListener, WebResponse, DatePickerdialogListener,
		OnSpinnerDialogListener, OnTimePickerDialogListener, OnTouchListener {
	private AndroidWebResponseTask mWebserviceAsnycInstance;
	private Utilities mABCUtilitiesInstance;
	private HeaderFragment mheaderFragment;
	public static boolean isFromBackPressedBaseFragment = false;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor prefseditor;

	@Override
	public void onBackPress(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		sharedPreferences = getActivity().getSharedPreferences("abcshared", 0);
		prefseditor = sharedPreferences.edit();
		super.onAttach(activity);

	}

	public void setRightIconVisibility(int visibility) {
		mheaderFragment.setRightIconVisibility(visibility);
	}

	public void setLeftIconVisibility(int visibility) {
		mheaderFragment.setLeftIconVisibility(visibility);

	}

	public void setSaveButtonVisibility(int visibility) {
		mheaderFragment.setSaveButtonVisibility(visibility);
	}

	public void setCancelButtonisibility(int visibility) {
		mheaderFragment.setCancelButtonisibility(visibility);

	}

	public void setRightIcon(int drawable) {
		mheaderFragment.setRightIcon(drawable);
	}

	public void setLeftIcon(int drawable) {
		mheaderFragment.setLeftIcon(drawable);

	}

	public String createXml(String[] keys, String[] values) {
		return mABCUtilitiesInstance.createXML(keys, values);
	}

	@Override
	public void onTimeChanged(int hourOfDay, int minute, String formattedValue,
			int callBackId) {
		// TODO Auto-generated method stub

	}

	public SpannableString underLineText(String textToUnderLine) {
		SpannableString spanStr = new SpannableString(textToUnderLine);
		spanStr.setSpan(new UnderlineSpan(), 0, spanStr.length(), 0);
		return spanStr;
	}

	public String showFirstNameandLastName(String firstName, String LastName) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(LastName);
		if (!checkIfStringIsNullOrEmpty(firstName)) {
			stringBuilder.append(", ");
			stringBuilder.append(firstName);
		}
		return stringBuilder.toString();
	}


	@Override
	public void onStart() {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		super.onStart();
		// boolean isLoginActivityCall = sharedPreferences.getBoolean(
		// "start Login Activity", false);
		// if (isLoginActivityCall) {
		// getActivity().finishAffinity();
		// startActivity(new Intent(getActivity(), LoginActivity.class));
		// }

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			mABCUtilitiesInstance.showHideKeyboard(false);
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Utilities.getInstance(getActivity()).showHideKeyboard(false);

		super.onResume();

		// Log.v("Base Fragment ", "On Resume");
		// boolean isLoginActivityCall = sharedPreferences.getBoolean(
		// "start Login Activity", false);
		// if (isLoginActivityCall) {
		// getActivity().finishAffinity();
		// startActivity(new Intent(getActivity(), LoginActivity.class));
		// }
		// getActivity().startService(
		// new Intent(getActivity(), ProcessService.class));

	}

	public String getDateandTime(String dateTimeString) {
		if (dateTimeString != null) {
			return mABCUtilitiesInstance.getDateAndTime(dateTimeString);

		}
		return null;
	}

	public String getCurrentDateAndTime(boolean isDateOnly) {
		return mABCUtilitiesInstance.getCurrentDateAndTime(false);
	}

	public String set15MinutesPrior(String dateandTime) {
		return mABCUtilitiesInstance.set15MinutePrior(dateandTime);
	}

	public boolean isEmailValid(String emailAddress) {
		return mABCUtilitiesInstance.isEmailValid(emailAddress);
	}

	// public void setisSessionClear(boolean setSessionStatus) {
	//
	// mABCUtilitiesInstance.setisSessionClear(setSessionStatus);
	//
	// }
	//
	// public boolean getSessionStatus() {
	// return mABCUtilitiesInstance.getSessionStatus();
	// }

	public boolean checkDateContraints(String startDate, String endDate) {
		return mABCUtilitiesInstance
				.checkEndDateConstraints(startDate, endDate);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);

		mABCUtilitiesInstance = Utilities.getInstance(getActivity());
		mWebserviceAsnycInstance = AndroidWebResponseTask
				.getInstance(getActivity());
		mWebserviceAsnycInstance.setCallback(this);
		mheaderFragment = getHeaderFragment();
		return setContentView(inflater, container, savedInstanceState);
	}

	private HeaderFragment getHeaderFragment() {
		try {
			mheaderFragment = (HeaderFragment) getFragmentManager()
					.findFragmentById(R.id.activity_duty_status_header_fragment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mheaderFragment;
	}

	// public void setRightHeaderButtonVisib(boolean isVisible) {
	// if (mheaderFragment != null) {
	// mheaderFragment.setRightImageViewVisibitity(isVisible);
	// }
	// }

	// public void setRighImageViewIcon(int drawable) {
	// mheaderFragment.setRightImae(drawable);
	// }

	// public void setLeftheaderButtonVisibility(boolean isVisible) {
	// if (mheaderFragment != null) {
	// mheaderFragment.setLeftImageViewVisibitity(isVisible);
	// }
	// }

	public void setHeaderTitle(String headerTitle) {
		if (mheaderFragment != null) {
			mheaderFragment.setHeaderFragmentTitle(headerTitle);
		}
	}
	
	public void setTitleBackGround(int drawable){
		
		if (mheaderFragment != null) {
			mheaderFragment.setTitleBackGround(drawable);
		}
		
	}

	public abstract View setContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void postJsonObjectWebserviceHit(String url, String[] key,
			String[] value, int id, boolean isDialog, boolean isSendingSessionId) {
		mABCUtilitiesInstance.showHideKeyboard(false);
		mABCUtilitiesInstance.postJsonObjectWebserviceHit(url, key, value, id,
				isDialog, isSendingSessionId, mWebserviceAsnycInstance);

	}

	public void postJsonObjectWebserviceHit(String url, String[] key,
			String[] value, int id, boolean isDialog,
			boolean isSendingSessionId, AndroidWebResponseTask mWebServiceAsync) {
		mABCUtilitiesInstance.showHideKeyboard(false);
		mABCUtilitiesInstance.postJsonObjectWebserviceHit(url, key, value, id,
				isDialog, isSendingSessionId, mWebServiceAsync);

	}

	public void postXMl(String Url, String data, boolean isDialog, int Id,
			WebResponse context) {
		mABCUtilitiesInstance.showHideKeyboard(false);
		mWebserviceAsnycInstance.setCallback(context);
		mABCUtilitiesInstance.postXml(Url, data, isDialog, Id,
				mWebserviceAsnycInstance);
	}

	public void postJsonArraytWebserviceHit(String url, String jsonArray,
			int id, boolean isDialog) {
		mABCUtilitiesInstance.showHideKeyboard(false);
		mABCUtilitiesInstance.postJsonArraytWebserviceHit(url, jsonArray, id,
				isDialog, mWebserviceAsnycInstance);
	}

	public void getUrlHit(String Url, int CallBackId) {
		// TODO Auto-generated method stub
		mABCUtilitiesInstance.showHideKeyboard(false);
		mABCUtilitiesInstance.getUrlHit(Url, mWebserviceAsnycInstance,
				CallBackId);
	}

	public void showToast(String message, int duration) {
		mABCUtilitiesInstance.showToast(message, duration);
	}

	public void ShowSingleButtonDialog(String title, String msg,
			String button1, int iconResId,
			OnAlertDialogFragmentListener listener, int activityId) {
		mABCUtilitiesInstance.showSingleOption_AlertDialog(title, msg, button1,
				iconResId, listener, activityId);
	}

	public void ShowDoubleButtonDialog(String title, String msg,
			String button1, String button2, int iconResId,
			OnAlertDialogFragmentListener listener, int activityId) {
		mABCUtilitiesInstance.showDoubleOption_AlertDialog(title, msg, button1,
				button2, iconResId, listener, activityId);
	}

	public void ShowThreeButtonDialog(String title, String msg, String button1,
			String button2, String button3, int iconResId,
			OnAlertDialogFragmentListener listener, int activityId) {
		mABCUtilitiesInstance.showThreeOption_AlertDialog(title, msg, button1,
				button2, button3, iconResId, listener, activityId);
	}

	public void replaceFragment(int replaceId, Fragment fragment,
			String fragmentTag, boolean isBackStack, Context activityContext) {

		mABCUtilitiesInstance.replaceFragment(replaceId, fragment, fragmentTag,
				isBackStack, activityContext);
	}

	public void addFragment(int replaceId, Fragment fragment,
			String fragmentTag, boolean isBackStack, Context activityContext) {
		mABCUtilitiesInstance.addFragment(replaceId, fragment, fragmentTag,
				isBackStack, activityContext);
	}

	public void popFragment(int replaceId, Context activityContext) {
		mABCUtilitiesInstance.popFragment(replaceId, activityContext);
	}

	public void clearBackStack(Context activityContext) {
		mABCUtilitiesInstance.clearBackStack(activityContext);
	}

	public void showDatePicker(int callBackId) {
		mABCUtilitiesInstance.showDatePickerDialog(this, callBackId);
	}

	public void showTimePicker(int callBackId) {
		mABCUtilitiesInstance.ShowTimePicker(this, callBackId);
	}

	public boolean checkIfStringIsNullOrEmpty(String value) {
		return mABCUtilitiesInstance.checkIfStringIsNullOrEmpty(value);
	}

	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		switch (UNIQUE_ID) {
		// case UrlConstants.LogoutUserWebserviceResponseId:
		// Log.v("Logout Url response", response);
		//
		// if (!checkIfStringIsNullOrEmpty(response)) {
		// try {
		//
		// JSONObject jsonObject = new JSONObject(response);
		// boolean isSucess = jsonObject.getBoolean("IsSuccess");
		// if (isSucess) {
		// if (((BaseActivityClass) getActivity()).isFromBackPressed) {
		// getActivity().finishAffinity();
		// } else {
		// getActivity().finishAffinity();
		// startActivity(new Intent(getActivity(),
		// LoginActivity.class));
		// }
		//
		// } else {
		//
		// getActivity().finishAffinity();
		// startActivity(new Intent(getActivity(),
		// LoginActivity.class));
		// }
		// } catch (Exception e) {
		//
		// e.printStackTrace();
		// }
		//
		// }
		//
		// break;
		//
		// default:
		// break;
		}// TODO Auto-generated method stub

	}

	public void showSpinnerDialog(ArrayAdapter<String> listAdapter,
			String title, int callBackId) {
		mABCUtilitiesInstance.showSpinnerDialog(listAdapter, title, this,
				callBackId);
	}

	public void showCustomSpinner(ArrayAdapter<String> listAdapter,
			String title, int callBackId) {
		mABCUtilitiesInstance.showSpinnerDialogCustom(listAdapter, title, this,
				callBackId);
	}

	public void showSpinnerDialog1(ArrayAdapter<?> listAdapter, String title,
			int callBackId) {
		mABCUtilitiesInstance.showSpinnerDialog1(listAdapter, title, this,
				callBackId);
	}

	@Override
	public void onFailure(Throwable e, String error, int UNIQUE_ID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPositiveButtonClick(int id) {
		// TODO Auto-generated method stub
		
		

	}

	@Override
	public void onNegativeButtonClick(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNeutralizeButtonClick(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpinnerDialogClick(int position, int callBackId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void interfaceAttachError(int fragmentId, String errorResponse) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDateChanged(int year, int monthOfYear, int dayOfMonth,
			int callBackId) {
		// TODO Auto-generated method stub

	}

}
