package com.tms.driver.fragments;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.tms.driver.R;
import com.tms.driver.base.BaseFragment;
import com.tms.driver.constant.TaxiExConstants;

/**
 * 
 * @author arvind.agarwal
 *This fragment represents Terms and conditions View
 *User can see its company's policies and terms and conditions
 */
public class TermsConditionsWebFragment extends BaseFragment {
	
	
	

	@Override
	public void getServerValues(String response, int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(Throwable e, String error, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		
		ShowSingleButtonDialog(null, error,
				getString(R.string.alert_ok_button), 0, this, 1);
	}

	@Override
	public void setServerError(int id, String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onParsingSuccessful(String response, int callback) {
		// TODO Auto-generated method stub

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

	@Override
	public void onSuccess(String[] response, int UNIQUE_ID) {
		// TODO Auto-generated method stub

	}

	private View mTermsConditiosnWeb;
	private WebView mTermsConditionsWebView;

	@Override
	public View setContentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (mTermsConditiosnWeb == null) {
			mTermsConditiosnWeb = inflater.inflate(
					R.layout.frag_terms_conditions_web, null);
			initializeComponents();
		}

		return mTermsConditiosnWeb;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (getArguments() != null)
			updateUI(getArguments().getInt(
					TaxiExConstants.TermsConditionsBundleKey));
	}
	
	/**
	 * Initializing components
	 */

	public void initializeComponents() {
		mTermsConditionsWebView = (WebView) mTermsConditiosnWeb
				.findViewById(R.id.frag_terms_conditions_web);
		mTermsConditionsWebView.getSettings().setJavaScriptEnabled(true);
		mTermsConditionsWebView.getSettings().setDisplayZoomControls(false);
		mTermsConditionsWebView.getSettings().setBuiltInZoomControls(true);

	}

	public static final int PRIVACY_POLICY = 1, RULES_REGULATIONS = 2,
			INDEPENDENT_CONTRACTORS = 3;

	private void updateUI(int termsType) {

		switch (termsType) {
		case PRIVACY_POLICY:

			// webview.loadUrl("http://docs.google.com/gview?embedded=true&url="
			// + pdf);
			mTermsConditionsWebView
					.loadUrl(TaxiExConstants.PrivacyPolicyURL);
			break;
		case RULES_REGULATIONS:
			mTermsConditionsWebView
					.loadUrl(TaxiExConstants.RulesandRegulationsURL);
			break;
		case INDEPENDENT_CONTRACTORS:
			mTermsConditionsWebView
					.loadUrl( TaxiExConstants.ContractorServiceURL);
			break;
		default:
			break;
		}

	}
}
