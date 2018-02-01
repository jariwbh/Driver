package com.tms.driver.fragments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tms.driver.R;
import com.tms.driver.adapters.TripListAdapter;
import com.tms.driver.base.BaseFragment;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.models.TripListModel;
import com.tms.driver.models.UserLoginModel;
import com.tms.driver.parse.ParseContent;
import com.tms.driver.parse.ParseContent.CONTENT_TYPE;


/**
 * 
 * @author arvind.agarwal
 *this fragment shows list of completed trips
 */
public class MyRecentTripsFragment extends BaseFragment {

	private View mMyRecentTripsView;
	private ListView mMyRecentTripsListView;
	private List<TripListModel> mTripListModelArray;
	private TripListAdapter mTripistAdapter;

	@Override
	public void getServerValues(String response, int id) {
		// TODO Auto-generated method stub

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
	public void onFailure(Throwable e, String error, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		ShowSingleButtonDialog(null, error,
				getString(R.string.alert_ok_button), 0, this, 1);
	}

	
	/**
	 * tackling parsed data
	 */
	@Override
	public void onParsingSuccessful(HashMap<String, List<?>> modelList,
			String response, int callback) {
		// TODO Auto-generated method stub

		switch (callback) {
		case TaxiExConstants.MyTripListParsingResponseId:
			try {
				if (modelList != null) {
					mTripListModelArray.clear();
					mTripListModelArray
							.addAll((Collection<? extends TripListModel>) (modelList
									.get(TaxiExConstants.TripListArrayKey)));
					if (mTripListModelArray.size() > 0) {
						Log.e("Trip List Model Details", mTripListModelArray
								.get(0).getTripStatus());
						mTripistAdapter.notifyDataSetChanged();
					} else {
						ShowSingleButtonDialog(null, response,
								getString(R.string.alert_ok_button), 0, this, 1);

					}
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

			break;

		default:
			break;
		}

	}

	@Override
	public void onPositiveButtonClick(int id) {
		// TODO Auto-generated method stub
		super.onPositiveButtonClick(id);
		switch (id) {
		case 1:

			popFragment(R.id.acitivity_duty_status_frag, getActivity());
			break;

		default:
			break;
		}
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

	
	/**
	 * method to handle webservice response
	 */
	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		super.onSuccess(response, UNIQUE_ID);

		switch (UNIQUE_ID) {
		case TaxiExConstants.MyTipListWebServiceResponse:

			Log.e("My Trip List details", response);
			if (!checkIfStringIsNullOrEmpty(response)) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					ParseContent.getInstance().parseContentFromjson(
							CONTENT_TYPE.TRIPLIST, jsonObject, this,
							TaxiExConstants.MyTripListParsingResponseId);

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			break;

		default:
			break;
		}
	}

	@Override
	public View setContentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

			mMyRecentTripsView = inflater.inflate(
					R.layout.frag_vehicles_registered, null);
		// initializing views
			initializeComponents();
			// getting recent trip list
			postXMl(TaxiExConstants.MyTripListUrl,
					createXml(TaxiExConstants.MyTripListXMLKeys, new String[] {
							"", UserLoginModel.getInstance().getId() }), true,
					TaxiExConstants.MyTipListWebServiceResponse, this);



		return mMyRecentTripsView;
	}

	
	/**
	 * initializing View
	 */
	private void initializeComponents() {

		mMyRecentTripsListView = (ListView) mMyRecentTripsView
				.findViewById(R.id.frag_vehicle_registeres_list);
		mTripListModelArray = new ArrayList<TripListModel>();
		mTripistAdapter = new TripListAdapter(getActivity());
		mTripistAdapter.setList(mTripListModelArray);
		mMyRecentTripsListView.setAdapter(mTripistAdapter);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		//setting header title
		setHeaderTitle(getString(R.string.my_recent_trip_title));
		// hiding left icon
		setLeftIconVisibility(View.GONE);
		// hiding right icon
		setRightIconVisibility(View.GONE);

	}
}
