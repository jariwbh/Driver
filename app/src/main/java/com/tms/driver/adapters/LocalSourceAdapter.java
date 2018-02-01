package com.tms.driver.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class LocalSourceAdapter extends ArrayAdapter implements SpinnerAdapter {

	public LocalSourceAdapter(Context context, int resource,
			List<String> objects) {
		super(context, resource, objects);

		mLocalSourceArrayList = (List<String>) objects;
		mLayoutInflater = LayoutInflater.from(context);
		// TODO Auto-generated constructor stub
	}

	private List<String> mLocalSourceArrayList;
	Context mContext;
	LayoutInflater mLayoutInflater;
	LocalSourceModelViewHolder mLocalSourceViewHolderClass;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mLocalSourceArrayList.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {

			convertView = mLayoutInflater.inflate(
					android.R.layout.simple_spinner_dropdown_item, null);
			mLocalSourceViewHolderClass = new LocalSourceModelViewHolder();
			mLocalSourceViewHolderClass.localSourceTextView = (TextView) convertView
					.findViewById(android.R.id.text1);
			convertView.setTag(mLocalSourceViewHolderClass);

		} else {
			mLocalSourceViewHolderClass = (LocalSourceModelViewHolder) convertView
					.getTag();
		}

		
		mLocalSourceViewHolderClass.localSourceTextView
				.setText(mLocalSourceArrayList.get(position).toString());

		return convertView;

	}

	public class LocalSourceModelViewHolder {
		public TextView localSourceTextView;
	}
}
