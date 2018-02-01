package com.tms.driver.fragments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

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


/**
 * 
 * @author arvind.agarwal
 *this fragment shows reservation list for confirmed and pending(has to be taken)
 */
public class ReservationListing extends BaseFragment {

	private View mReservationListng;
	private ListView mReservetionListView;
	private List<ReservationList> mReservationListModelArray;

	private ImageButton btnPending, btnConfirmed, btnCancelled;

	private ReservationListAdapter adapter;

	public static int TYPE = 0;

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

	
	/**
	 * tackles parsed data
	 */
	@Override
	public void onParsingSuccessful(HashMap<String, List<?>> modelList,
			String response, int callback) {
		// TODO Auto-generated method stub

		switch (callback) {
		case TaxiExConstants.ReservationListResponse:
// reservation list parsed response 
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

	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		super.onSuccess(response, UNIQUE_ID);

		switch (UNIQUE_ID) {

		case TaxiExConstants.ReservationListResponse:

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
		if (mReservationListng == null) {
			mReservationListng = inflater.inflate(
					R.layout.frag_reservation_listing, null);
			initializecomponents();

		}
		return mReservationListng;
	}

	private void initializecomponents() {

		btnCancelled = (ImageButton) mReservationListng
				.findViewById(R.id.btnCancelled);

		btnConfirmed = (ImageButton) mReservationListng
				.findViewById(R.id.btnConfirmed);
		btnPending = (ImageButton) mReservationListng
				.findViewById(R.id.btnPending);

		mReservetionListView = (ListView) mReservationListng
				.findViewById(R.id.frag_reservation_list);
		mReservationListModelArray = new ArrayList<ReservationList>();

		adapter = new ReservationListAdapter(getActivity());
		adapter.setList(mReservationListModelArray);
		mReservetionListView.setAdapter(adapter);

		btnConfirmed.setOnClickListener(this);
		btnPending.setOnClickListener(this);
		btnCancelled.setVisibility(View.GONE);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		// type=0 for pending
		TYPE = 0;

		btnConfirmed.setImageDrawable(getResources().getDrawable(
				R.drawable.subbar_btn_confirmed));
		
		
		btnPending.setImageDrawable(getResources().getDrawable(
				R.drawable.subbar_btn_pending_hr));

		mReservationListModelArray.clear();

		adapter.notifyDataSetChanged();

		
		// getting reservation list
		postXMl(TaxiExConstants.ReservationsList,
				createXml(TaxiExConstants.ReservationListXMLKeys, new String[] {
						"", UserLoginModel.getInstance().getId(),
						"" + ReservationListing.TYPE }), true,
				TaxiExConstants.ReservationListResponse, this);

		
		// setting header title
		setHeaderTitle(getString(R.string.reservation_list_title));
		// left icon visibilty gone
		setLeftIconVisibility(View.GONE);
		//right icon visibility visible
		
		setRightIconVisibility(View.VISIBLE);
		
		// setting right icon background 
		setRightIcon((R.drawable.selector_history_button));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {
	

		case R.id.btnConfirmed:

			
			// type =1 for confirmed
			TYPE = 1;

			btnConfirmed.setImageDrawable(getResources().getDrawable(
					R.drawable.subbar_btn_confirmed_hr));
			btnPending.setImageDrawable(getResources().getDrawable(
					R.drawable.subbar_btn_pending));

			mReservationListModelArray.clear();

			adapter.notifyDataSetChanged();

			postXMl(TaxiExConstants.ReservationsList,
					createXml(TaxiExConstants.ReservationListXMLKeys,
							new String[] { "",
									UserLoginModel.getInstance().getId(), "1" }),
					true, TaxiExConstants.ReservationListResponse, this);

			break;

		case R.id.btnPending:

			
			// type =0 for pending
			TYPE = 0;

			btnConfirmed.setImageDrawable(getResources().getDrawable(
					R.drawable.subbar_btn_confirmed));
			btnPending.setImageDrawable(getResources().getDrawable(
					R.drawable.subbar_btn_pending_hr));

			mReservationListModelArray.clear();

			adapter.notifyDataSetChanged();
			postXMl(TaxiExConstants.ReservationsList,
					createXml(TaxiExConstants.ReservationListXMLKeys,
							new String[] { "",
									UserLoginModel.getInstance().getId(), "0" }),
					true, TaxiExConstants.ReservationListResponse, this);
			break;

		default:
			break;
		}
	}

}
