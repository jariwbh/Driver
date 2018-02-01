package com.tms.driver.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.tms.driver.R;
import com.tms.driver.adapters.ReservationListAdapter;
import com.tms.driver.base.BaseFragment;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.models.ReservationList;
import com.tms.driver.models.UserLoginModel;
import com.tms.driver.parse.ParseContent;
import com.tms.driver.parse.ParseContent.CONTENT_TYPE;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;



/**
 * 
 * @author arvind.agarwal
 *this fragment shows reservation history listing 
 *
 */
public class ReservationHistory extends BaseFragment {
	
	

	private View mReservationListng;
	private ListView mReservetionListView;
	private List<ReservationList> mReservationListModelArray;

	private ImageButton btnPending, btnConfirmed,btnCancelled;

	private ReservationListAdapter adapter;

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
	public void onPositiveButtonClick(int id) {
		// TODO Auto-generated method stub
		super.onPositiveButtonClick(id);
		switch (id) {
		case 1:

			/*
			 * popFragment(R.id.acitivity_duty_status_frag, getActivity());
			 */break;

		default:
			break;
		}
	}

	
	
	/*
	 * (non-Javadoc)
	 *
	 * this method is used to get web service parsed data
	 */
	@Override
	public void onParsingSuccessful(HashMap<String, List<?>> modelList,
			String response, int callback) {
		// TODO Auto-generated method stub

		switch (callback) {
		case TaxiExConstants.ReservationListResponse:

			try {
				if (modelList != null) {
					mReservationListModelArray.clear();
					mReservationListModelArray
							.addAll((Collection<? extends ReservationList>) (modelList
									.get(TaxiExConstants.ReservationListArrayKey)));
					if (mReservationListModelArray.size() > 0) {

						adapter.notifyDataSetChanged();
					} else {
						ShowSingleButtonDialog(null, response,
								getString(R.string.alert_ok_button), 0, this, 1);

					}
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

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

	
	/*
	 * (non-Javadoc)
	 * this method is used to tackle webservice response
	 */
	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		super.onSuccess(response, UNIQUE_ID);

		switch (UNIQUE_ID) {

		case TaxiExConstants.ReservationListResponse:
			
			// reservation listing  response

			if (!checkIfStringIsNullOrEmpty(response)) {
				try {
					JSONObject jsonObject = new JSONObject(response);

					ParseContent.getInstance().parseContentFromjson(
							CONTENT_TYPE.RESERVATION_LIST, jsonObject, this,
							TaxiExConstants.ReservationListResponse);
					// {"driverReservations":"-3","message":"You have no reservations."}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			break;

		}
	}

	@Override
	public View setContentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

			mReservationListng = inflater.inflate(
					R.layout.frag_reservation_listing, null);
			
			
		
			initializecomponents();

			
			
			
			
			



		
		ReservationListing.TYPE=2;
		return mReservationListng;
	}

	private void initializecomponents() {
		
		btnCancelled=(ImageButton)mReservationListng.findViewById(R.id.btnCancelled);
	

		btnConfirmed = (ImageButton) mReservationListng
				.findViewById(R.id.btnConfirmed);
		btnPending = (ImageButton) mReservationListng
				.findViewById(R.id.btnPending);
		
		btnPending.setImageDrawable(getResources().getDrawable(
				R.drawable.subbar_btn_expired));

		mReservetionListView = (ListView) mReservationListng
				.findViewById(R.id.frag_reservation_list);
		mReservationListModelArray = new ArrayList<ReservationList>();

		adapter = new ReservationListAdapter(getActivity());
		adapter.setList(mReservationListModelArray);
		mReservetionListView.setAdapter(adapter);

		btnConfirmed.setOnClickListener(this);
		btnPending.setOnClickListener(this);
		btnCancelled.setOnClickListener(this);		
	

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ReservationListing.TYPE=2;

			
		
		// reservation type=2 for confirmed
		btnConfirmed.setImageDrawable(getResources().getDrawable(
				R.drawable.subbar_btn_confirmed_hr));
		btnPending.setImageDrawable(getResources().getDrawable(
				R.drawable.subbar_btn_expired));
		
		btnCancelled.setImageDrawable(getResources().getDrawable(R.drawable.subbar_btn_cancel));
	
		
			
		

		mReservationListModelArray.clear();
		
		
		// webservices hit to get coni
		postXMl(TaxiExConstants.ReservationsList,
				createXml(TaxiExConstants.ReservationListXMLKeys,
						new String[] { "",
								UserLoginModel.getInstance().getId(), ""+ReservationListing.TYPE }),
				true, TaxiExConstants.ReservationListResponse, this);

		setHeaderTitle(getString(R.string.reservation_history));
		setLeftIconVisibility(View.GONE);
		setRightIconVisibility(View.GONE);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {
		
		case R.id.btnCancelled:

			
			// reservation type =5 for cancelled
			ReservationListing.TYPE = 5;
			btnConfirmed.setImageDrawable(getResources().getDrawable(
					R.drawable.subbar_btn_confirmed));
			btnPending.setImageDrawable(getResources().getDrawable(
					R.drawable.subbar_btn_expired));
			btnCancelled.setImageDrawable(getResources().getDrawable(R.drawable.subbar_btn_cancel_hr));

			mReservationListModelArray.clear();

			adapter.notifyDataSetChanged();
			//getting cancelled reservation list
			postXMl(TaxiExConstants.ReservationsList,
					createXml(TaxiExConstants.ReservationListXMLKeys,
							new String[] { "",
									UserLoginModel.getInstance().getId(), "4" }),
					true, TaxiExConstants.ReservationListResponse, this);

			break;
		
		
		case R.id.btnConfirmed:
			
			// type =2 confirmed
			ReservationListing.TYPE=2;

			btnConfirmed.setImageDrawable(getResources().getDrawable(
					R.drawable.subbar_btn_confirmed_hr));
			btnPending.setImageDrawable(getResources().getDrawable(
					R.drawable.subbar_btn_expired));
			btnCancelled.setImageDrawable(getResources().getDrawable(R.drawable.subbar_btn_cancel));

			mReservationListModelArray.clear();
			
	

			adapter.notifyDataSetChanged();
			// getting confirmed reservation list
			
			postXMl(TaxiExConstants.ReservationsList,
					createXml(TaxiExConstants.ReservationListXMLKeys,
							new String[] { "",
									UserLoginModel.getInstance().getId(), "2" }),
					true, TaxiExConstants.ReservationListResponse, this);

			break;

		case R.id.btnPending:

			// type=3 for pending
			ReservationListing.TYPE=3;
			btnConfirmed.setImageDrawable(getResources().getDrawable(
					R.drawable.subbar_btn_confirmed));
			btnPending.setImageDrawable(getResources().getDrawable(
					R.drawable.subbar_btn_expired_hr));
			btnCancelled.setImageDrawable(getResources().getDrawable(R.drawable.subbar_btn_cancel));
			mReservationListModelArray.clear();
			
			

			adapter.notifyDataSetChanged();
			
			// getting reservation list for pending 
			postXMl(TaxiExConstants.ReservationsList,
					createXml(TaxiExConstants.ReservationListXMLKeys,
							new String[] { "",
									UserLoginModel.getInstance().getId(), "3" }),
					true, TaxiExConstants.ReservationListResponse, this);
			break;

		default:
			break;
		}
	}

}
