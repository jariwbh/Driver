package com.tms.driver.customviews;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.tms.driver.async.AndroidWebResponseTask;
import com.tms.driver.utilities.Utilities;

public class PaginatedListView extends ListView {
	public static int PAGESIZE = 100;

	public boolean loading = false;
	public View footerView;
	public Utilities mUtilities;
	public Context mContext;

	public interface onPageLoadingListener {
		void onPageLoadingFinished(List<?> newContentList);
	}

	public PaginatedListView(Context context) {
		super(context);
		mUtilities = Utilities.getInstance(context);

		// TODO Auto-generated constructor stub
	}

	public PaginatedListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void loadNextPageContent(String url, String jsonArray, int id,
			boolean isDialog, AndroidWebResponseTask mWebserviceAsnycInstance) {
		mUtilities.postJsonArraytWebserviceHit(url, jsonArray, id, isDialog,
				mWebserviceAsnycInstance);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
	}

	public boolean load(int firstVisibleItem, int visibleItemCount,
			int totalItemCount, int totalValuesToLoad) {
		boolean lastItem = firstVisibleItem + visibleItemCount == totalItemCount
				&& getChildAt(visibleItemCount - 1) != null
				&& getChildAt(visibleItemCount - 1).getBottom() <= getHeight();
		try {
			boolean moreRows = getCount() < totalValuesToLoad;
			return moreRows && lastItem && !loading;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;

	}

	public List<?> getData(int offset, int limit, List<?> customArrayList) {
		List<Object> newList = new ArrayList<Object>();
		int end = offset + limit;
		if (end > customArrayList.size()) {
			end = customArrayList.size();
		}
		newList.addAll((Collection<?>) customArrayList.subList(offset, end));
		return newList;
	}

}
