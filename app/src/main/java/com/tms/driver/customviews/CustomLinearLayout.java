package com.tms.driver.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class CustomLinearLayout extends LinearLayout {

	public static boolean isIntercepting = false;

	public CustomLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		// return super.onInterceptTouchEvent(ev);

		// In general, we don't want to intercept touch events. They should be
		// handled by the child view.
		return isIntercepting;
	}

}
