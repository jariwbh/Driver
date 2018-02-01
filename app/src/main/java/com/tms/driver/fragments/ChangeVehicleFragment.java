package com.tms.driver.fragments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tms.driver.GCMDialog_Activity;
import com.tms.driver.R;
import com.tms.driver.adapters.VehiclesAdapter;
import com.tms.driver.base.BaseFragment;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.constant.TaxiExConstants.FRAGMENT_TAGS;
import com.tms.driver.fragments.*;
import com.tms.driver.models.UserLoginModel;
import com.tms.driver.models.VehicleListModel;
import com.tms.driver.parse.ParseContent;
import com.tms.driver.parse.ParseContent.CONTENT_TYPE;



/**
 * 
 * @author arvind.agarwal
 * 
 * this fragment represents the list of vehicle registered with the company
 * 
 * User can pick any available vehicle from the list
 *
 */
public class ChangeVehicleFragment extends BaseFragment implements
		OnItemClickListener {

	private View mChangeVehiclesView;
	
	//listview to show list of vehicles
	private ListView mvehicleListView;
	//Array list of modal type for vehicle information
	private List<VehicleListModel> mVehicleListModelArray;
	//Adapter to show list
	private VehiclesAdapter mVehicleAdapter;
	
	//Model class object that will contains info of vehicle selected
	private VehicleListModel mSelectedVehicleModel;

	@Override
	public View setContentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

			mChangeVehiclesView = inflater.inflate(
					R.layout.frag_vehicles_registered, null);

		initializecomponents();
		return mChangeVehiclesView;
	}
	
	/**
	 * initializing view
	 */

	private void initializecomponents() {

		mvehicleListView = (ListView) mChangeVehiclesView
				.findViewById(R.id.frag_vehicle_registeres_list);
		mvehicleListView.setOnItemClickListener(this);
		mVehicleListModelArray = new ArrayList<VehicleListModel>();
		mVehicleAdapter = new VehiclesAdapter(getActivity());
		mVehicleAdapter.setList(mVehicleListModelArray);
		mvehicleListView.setAdapter(mVehicleAdapter);

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
		// setting header title
		setHeaderTitle(getString(R.string.vehicles_registered_title));
		// hiding right icon
		setRightIconVisibility(View.GONE);
		//hiding left icon
		setLeftIconVisibility(View.GONE);
		
		
		//webservice hit to get list of vehicle
		postXMl(TaxiExConstants.TaxiListUrl,
				createXml(TaxiExConstants.TaxiListXMLKeys, new String[] { "",
						UserLoginModel.getInstance().getId() }), true,
				TaxiExConstants.TaxiListWebserviceResponse, this);
	}

	
	/**
	 * this interface recieves data from webservice (Vehicle list and change vehicle)
	 * 
	 * sends the data to get parse
	 */
	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		super.onSuccess(response, UNIQUE_ID);

		switch (UNIQUE_ID) {
		case TaxiExConstants.TaxiListWebserviceResponse:
			Log.e("Taxi List Response", response);

			JSONObject jsonObject;
			try {

				jsonObject = new JSONObject(response);

				//{"getTaxiList":[],"message":"No Cab is available."}
				String message=jsonObject.getString("message");
				
				
				
				// if list size is zero than will show up a pop up
				if(jsonObject.getJSONArray("getTaxiList").length()==0){
					
					ShowSingleButtonDialog(null, message,
							getString(R.string.alert_ok_button), 0, this, 1);
					
					
					
				}else{
					// sending data to get parse
				ParseContent.getInstance().parseContentFromjson(
						CONTENT_TYPE.VEHICLELIST, jsonObject, this,
						TaxiExConstants.VehicleListParsingResponseId);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case TaxiExConstants.ChangeVehicleWebserviceResponse:
			Log.e("Change Taxi Response", response);
			if (response != null) {
				
				//getting data after selecting a vehicle

				try {
					JSONObject jsonObject2 = new JSONObject(response);

					int changeVehicleStatusCode = jsonObject2
							.getInt("changeVehicle");
					String message = jsonObject2.getString("message");
					if (changeVehicleStatusCode == 1) {
						Bundle bundle = new Bundle();
						bundle.putSerializable(
								TaxiExConstants.VehicleChangedBundleKey,
								mSelectedVehicleModel);
						try {
							Fragment fragment = getActivity()
									.getSupportFragmentManager()
									.findFragmentByTag(
											FRAGMENT_TAGS.DUTYSTATUSFRAGMENT
													.name());
							com.tms.driver.fragments.DutyStatusFragment dutyStatusFragment = (com.tms.driver.fragments.DutyStatusFragment) fragment;
							if (dutyStatusFragment != null) {

								
								//Updating DutyStatus Fragment with new vehicle info
								dutyStatusFragment.updateDutyStatusUI(bundle);
								popFragment(R.id.acitivity_duty_status_frag,
										getActivity());
								// replaceFragment(R.id.acitivity_duty_status_frag,
								// dutyStatusFragment,
								// FRAGMENT_TAGS.DUTYSTATUSFRAGMENT.name(),
								// false,
								// getActivity());

							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if(changeVehicleStatusCode == -5){
						
						
						Intent i = new Intent(getActivity(),
								GCMDialog_Activity.class);
						i.putExtra("messageType", "logoutStatus");
						i.putExtra("message", message);

						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TASK);

						startActivity(i);
						
					}else {
						if (message != null)
							ShowSingleButtonDialog(null, message,
									getString(R.string.alert_ok_button), 0,
									this, 1);

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
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
	public void onParsingSuccessful(String response, int callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onParsingSuccessful(HashMap<String, List<?>> modelList,
			String response, int callback) {

		switch (callback) {
		case TaxiExConstants.VehicleListParsingResponseId:
			try {
				if (modelList != null) {
					
					
					// clear array list
					
					mVehicleListModelArray.clear();
					mVehicleListModelArray
							.addAll((Collection<? extends VehicleListModel>) modelList
									.get(TaxiExConstants.VehicleListArrayKey));
					
					// notifying adapter
					mVehicleAdapter.notifyDataSetChanged();
					
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

	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		SharedPreferences mSharedprefrences=getActivity().getSharedPreferences(
				TaxiExConstants.SharedPreferenceName, 0);
	
	
	String deviceToken=mSharedprefrences.getString(
			TaxiExConstants.Pref_GCM_key, "");
		// selecting vehicle
		postXMl(TaxiExConstants.ChangeVehicleURl,
				createXml(TaxiExConstants.ChangeVehicleXMLKeys, new String[] {
						"", UserLoginModel.getInstance().getId(),
						mVehicleListModelArray.get(position).getId(), deviceToken }), true,
				TaxiExConstants.ChangeVehicleWebserviceResponse, this);
		
		//setting vehicle model
		mSelectedVehicleModel = mVehicleListModelArray.get(position);
	}
	
	@Override
	public void onPositiveButtonClick(int id) {
		// TODO Auto-generated method stub
		super.onPositiveButtonClick(id);
		switch (id) {
		case 1:
			
			
			// calling duty fragment
			replaceFragment(R.id.acitivity_duty_status_frag,
					new com.tms.driver.fragments.DutyStatusFragment(),
					FRAGMENT_TAGS.DUTYSTATUSFRAGMENT.name(), false,
					getActivity());
		
			
			break;

		default:
			break;
		}
	}
}
