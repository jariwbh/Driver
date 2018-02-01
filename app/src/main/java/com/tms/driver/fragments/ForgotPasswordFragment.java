package com.tms.driver.fragments;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tms.driver.R;
import com.tms.driver.base.BaseFragment;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.customviews.CustomTextView;
import com.tms.driver.parse.ParseContent;
import com.tms.driver.parse.ParseContent.CONTENT_TYPE;
import com.tms.driver.utilities.FONTS;
import com.tms.driver.utilities.Utilities;


/**
 * 
 * 
 * @author arvind.agarwal
 *This fragment represents Forgot password view
 *
 *Fields required  Email address
 */
public class ForgotPasswordFragment extends BaseFragment {
	private View mForgotPasswordView;
	private EditText mForgotPasswordEditText;
	private Button mSendMePassword;
	private TextView mBackTextView;
	private Typeface typeFace, typeFaceForgot;
	@Override
	public View setContentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

			mForgotPasswordView = inflater.inflate(
					R.layout.frag_forgot_password, null);
			initializeComponents();


		return mForgotPasswordView;
	}
	
	/**
	 * Initializing components
	 */

	private void initializeComponents() {
		// getting tyeface
		typeFace = Typeface.createFromAsset(getActivity().getAssets(), FONTS.TYPE_FACE);
		typeFaceForgot = Typeface.createFromAsset(getActivity().getAssets(),
				FONTS.TYPE_FACE_FORGOT);
		((CustomTextView) mForgotPasswordView.findViewById(R.id.title)).setTypeface(typeFace);
		mForgotPasswordEditText = (EditText) mForgotPasswordView
				.findViewById(R.id.forgot_password_screen_email_edittext);
		mForgotPasswordEditText.setTypeface(typeFace);
		((CustomTextView)mForgotPasswordView. findViewById(R.id.txtInfo)).setTypeface(typeFaceForgot);
		mForgotPasswordEditText.requestFocus();
		mSendMePassword = (Button) mForgotPasswordView
				.findViewById(R.id.forgot_pswd_screen_send_me_button);
		mBackTextView = (TextView) mForgotPasswordView
				.findViewById(R.id.forgot_password_back_textview);
		mSendMePassword.setOnClickListener(this);
		mBackTextView.setOnClickListener(this);

	}

	@Override
	public void onFailure(Throwable e, String error, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		ShowSingleButtonDialog(null, error,
				getString(R.string.alert_ok_button), 0, this, 1);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mForgotPasswordEditText.requestFocus();
		Utilities.getInstance(getActivity()).showHideKeyboard(true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {
		case R.id.forgot_pswd_screen_send_me_button:
			sendEmail();
			break;
		case R.id.forgot_password_back_textview:
			popFragment(R.id.acitivity_main_fragment, getActivity());
		default:
			break;
		}
	}

	
	/**
	 * this method requests for password and send an email to user where they get Recovered password 
	 */
	private void sendEmail() {

		String emailAddress = mForgotPasswordEditText.getText().toString()
				.trim();

		if (!checkIfStringIsNullOrEmpty(emailAddress)) {
			if (isEmailValid(emailAddress)) {

				String[] keys = new String[] { "forgotPassword", "email" };
				String[] values = new String[] { "", emailAddress };
				// ShowSingleButtonDialog(null, createXml(keys, values),
				// getString(R.string.alert_ok_button), 0, this,
				// ALERT_PASSWORD_BLANK);

				
				// webservice hit for forgot password
				postXMl(TaxiExConstants.ForgotPasswordURL,
						createXml(keys, values), true, 1, this);

			} else {
				ShowSingleButtonDialog(null,
						getString(R.string.alert_email_not_valid),
						getString(R.string.alert_ok_button), 0, this, 2);
			}
		} else {
			ShowSingleButtonDialog(null,
					getString(R.string.alert_email_left_blank),
					getString(R.string.alert_ok_button), 0, this, 3);

		}

	}

	@Override
	public void getServerValues(String response, int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setServerError(int id, String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPositiveButtonClick(int id) {
		// TODO Auto-generated method stub
		super.onPositiveButtonClick(id);
		switch (id) {
		case 4:
			popFragment(R.id.acitivity_main_fragment, getActivity());

			break;

		default:
			break;
		}
	}

	
	/**
	 * 
	 * This interface collects the parsed data from forgot password webservice
	 */
	
	@Override
	public void onParsingSuccessful(String response, int callback) {
		ShowSingleButtonDialog(null, response,
				getString(R.string.alert_ok_button), 0, this, 4);

	}

	@Override
	public void onParsingSuccessful(HashMap<String, List<?>> modelList,
			String response, int callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onParsingError(String errorMessages, int callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onParsingSuccessful(Object modelClass, int callback) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * This interface catches response from forgot password webservices
	 * and send it to get parse
	 */

	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		super.onSuccess(response, UNIQUE_ID);

		try {
			ParseContent.getInstance().parseContentFromjson(
					CONTENT_TYPE.FORGORPASSWORD, new JSONObject(response),
					this, 1);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onSuccess(String[] response, int UNIQUE_ID) {
		// TODO Auto-generated method stub

	}

}
