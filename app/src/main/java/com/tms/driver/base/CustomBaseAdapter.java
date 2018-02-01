package com.tms.driver.base;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Common base class of common implementation for an Adapter that can be used
 * for listView, GridView etc..
 * 
 * @method setList method is used to setList to this adapter
 * @method getList method is used to getList from this adapter
 * 
 * 
 * @param <T>
 *            This class is of generic type. T here describes the getter/setter
 *            class. T should is either String , Integer or any user defined
 *            class
 */
public abstract class CustomBaseAdapter<T> extends BaseAdapter {
	private Context context;

	public CustomBaseAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@SuppressWarnings("unused")
	private CustomBaseAdapter() {

	}

	/**
	 * This method is used to setList containing ListArray<T>
	 * 
	 * @param items
	 */
	public abstract void setList(List<T> items);

	/**
	 * This method is used to get the setListArray
	 * 
	 * @return ListArray<T>
	 */
	public abstract List<T> getList();

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return convertView;
	}

	public Context getContext() {
		return context;
	}

	/**
	 * This method is used to check if the entered string is null, blank, or
	 * "null"
	 * 
	 * @param str
	 *            set String to check
	 * @return true if null else false
	 */
	public boolean isEmptyOrNull(String str) {
		return !(!TextUtils.isEmpty(str) && !str.equals("null"));
	}

	/**
	 * This method is used to replaceFragment with another fragment
	 * 
	 * @param replaceId
	 *            Set id of the view on which fragment is to replaced
	 * @param fragment
	 *            fragment which is to called
	 * @param tag
	 *            Set tag if needed otherwise set null
	 * @param isBackStack
	 *            Set true if need backStack else false
	 */
	protected final void replaceFragment(int replaceId, Fragment fragment,
			String tag, boolean isBackStack, Context contex) {
		FragmentTransaction ft = ((FragmentActivity) contex)
				.getSupportFragmentManager().beginTransaction();
		if (!isEmptyOrNull(tag)) {
			ft.replace(replaceId, fragment, tag);
		} else {
			ft.replace(replaceId, fragment);
		}
		if (isBackStack) {
			ft.addToBackStack(tag);
		}
		// ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.commit();
	}
}
