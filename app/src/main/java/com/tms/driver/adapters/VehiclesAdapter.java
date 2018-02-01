package com.tms.driver.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tms.driver.R;
import com.tms.driver.base.CustomBaseAdapter;
import com.tms.driver.models.VehicleListModel;

public class VehiclesAdapter extends CustomBaseAdapter<VehicleListModel>

{
	private List<VehicleListModel> mVehicleModelListArray;
	private Context mContext;
	private LayoutInflater mInflater;
	private ViewHolder mHolder;

	public VehiclesAdapter(Context context) {
		super(context);
		mContext = context;
		mInflater = LayoutInflater.from(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setList(List<VehicleListModel> items) {
		mVehicleModelListArray = items;

	}

	@Override
	public List<VehicleListModel> getList() {
		// TODO Auto-generated method stub
		return mVehicleModelListArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mVehicleModelListArray != null) {
			return mVehicleModelListArray.size();

		}
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.frag_vehicles_registered_inflated_view, null);
			mHolder = new ViewHolder();

			mHolder.vehicleImage = (ImageView) convertView
					.findViewById(R.id.frag_vehicle_registered_inflated_imageview);
			mHolder.mVehicleNameTextView = (TextView) convertView
					.findViewById(R.id.frag_vehicle_registered_inflated_vehicle_name);
			mHolder.mVehiclemodelTextView = (TextView) convertView
					.findViewById(R.id.frag_vehicle_registered_inflated_vehicle_model);

			convertView.setTag(mHolder);

		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		if (mVehicleModelListArray.get(position).getTaxiTypeId()
				.equalsIgnoreCase("1")) {
			mHolder.vehicleImage.setImageDrawable(getContext().getResources()
					.getDrawable(R.drawable.car_13));
		}

		else if (mVehicleModelListArray.get(position).getTaxiTypeId()
				.equalsIgnoreCase("2")) {
			mHolder.vehicleImage.setImageDrawable(getContext().getResources()
					.getDrawable(R.drawable.car_12));
		}

		else if (mVehicleModelListArray.get(position).getTaxiTypeId()
				.equalsIgnoreCase("3")) {
			mHolder.vehicleImage.setImageDrawable(getContext().getResources()
					.getDrawable(R.drawable.car_11));
		}
		else if (mVehicleModelListArray.get(position).getTaxiTypeId()
				.equalsIgnoreCase("4")) {
			mHolder.vehicleImage.setImageDrawable(getContext().getResources()
					.getDrawable(R.drawable.car_12));
		}if (mVehicleModelListArray.get(position).getTaxiTypeId()
                .equalsIgnoreCase("5")) {
            mHolder.vehicleImage.setImageDrawable(getContext().getResources()
                    .getDrawable(R.drawable.car_11));
        }
		

		mHolder.mVehiclemodelTextView.setText(mVehicleModelListArray.get(
				position).getTaxiModel()
				+ "\n" + mVehicleModelListArray.get(position).getTaxiNumber());
		mHolder.mVehicleNameTextView.setText(mVehicleModelListArray.get(
				position).getTaxiType());
		return convertView;
	}

	class ViewHolder {
		ImageView vehicleImage;
		TextView mVehicleNameTextView, mVehiclemodelTextView;

	}

}
