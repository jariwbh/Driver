package com.tms.driver.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tms.driver.R;
import com.tms.driver.base.CustomBaseAdapter;
import com.tms.driver.models.ReservationList;

public class ReservationListAdapter extends CustomBaseAdapter<ReservationList> {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<ReservationList> mTripListModelArray;
	private ViewHolder mViewHolder;
	private int type;

	public ReservationListAdapter(Context context) {
		super(context);
		mContext = context;
		type=type;
		mInflater = LayoutInflater.from(context);

	}

	@Override
	public void setList(List<ReservationList> items) {
		// TODO Auto-generated method stub
		mTripListModelArray = items;
	}

	@Override
	public List<ReservationList> getList() {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.frag_reservations_inflated_view, null);
			mViewHolder = new ViewHolder();
		mViewHolder.txtView1=(TextView)convertView.findViewById(R.id.txtLine1);
		mViewHolder.txtView2=(TextView)convertView.findViewById(R.id.txtLine2);

			convertView.setTag(mViewHolder);

		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		mViewHolder.txtView1.setText("Location :"+mTripListModelArray.get(
				position).getPickUpLoc());
		mViewHolder.txtView2.setText("PickUp Date :"+mTripListModelArray.get(
				position).getPicUpDate());
	
		
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				Intent intent=new Intent(mContext, com.tms.driver.ReservationDetails.class);
				
				intent.putExtra("RESERVATION_ID", mTripListModelArray.get(position).getId());
				intent.putExtra("TYPE", mTripListModelArray.get(position).getType());

				
				mContext.startActivity(intent);
				
			
				
			}
		});
		return convertView;
	}

	private class ViewHolder {
		TextView txtView1,txtView2;
	}

}
