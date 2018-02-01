package com.tms.driver.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tms.driver.R;
import com.tms.driver.base.CustomBaseAdapter;
import com.tms.driver.models.TripListModel;

public class TripListAdapter extends CustomBaseAdapter<TripListModel> {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<TripListModel> mTripListModelArray;
	private ViewHolder mViewHolder;

	public TripListAdapter(Context context) {
		super(context);
		mContext = context;
		mInflater = LayoutInflater.from(context);

	}

	@Override
	public void setList(List<TripListModel> items) {
		// TODO Auto-generated method stub
		mTripListModelArray = items;
	}

	@Override
	public List<TripListModel> getList() {
		// TODO Auto-generated method stub
		return mTripListModelArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mTripListModelArray != null) {
			return mTripListModelArray.size();
		}
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.frag_recent_trips_inflated_view, null);
			mViewHolder = new ViewHolder();
			mViewHolder.dateandTimeTextView = (TextView) convertView
					.findViewById(R.id.frag_recent_trip_dateandtime);
			mViewHolder.recieptNumnerTextView = (TextView) convertView
					.findViewById(R.id.frag_recent_trip_recent_trip);

			mViewHolder.statusTextView = (TextView) convertView
					.findViewById(R.id.frag_recent_trip_status);
			mViewHolder.totalFareTextView = (TextView) convertView
					.findViewById(R.id.frag_recent_trip_total_fare);
			mViewHolder.tipTextView = (TextView) convertView
					.findViewById(R.id.frag_recent_trip_tip);

			convertView.setTag(mViewHolder);

		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		mViewHolder.dateandTimeTextView.setText(mTripListModelArray.get(
				position).getCompletedDate());
		mViewHolder.recieptNumnerTextView.setText(mTripListModelArray.get(
				position).getReceiptNumber());
		mViewHolder.statusTextView.setText(mTripListModelArray.get(position)
				.getTripStatus());
		mViewHolder.totalFareTextView.setText(mTripListModelArray.get(position)
				.getTripAmount());
		mViewHolder.tipTextView.setText(mTripListModelArray.get(position)
				.getTip());
		return convertView;
	}

	private class ViewHolder {
		TextView dateandTimeTextView, totalFareTextView, statusTextView,
				recieptNumnerTextView, tipTextView;
	}

}
