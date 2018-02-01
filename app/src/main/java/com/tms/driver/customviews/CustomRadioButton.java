package com.tms.driver.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.tms.driver.R;



public class CustomRadioButton extends RadioButton {

	public CustomRadioButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		if (attrs != null) {
			if (!isInEditMode()) {
				TypedArray ta = context.obtainStyledAttributes(attrs,
						R.styleable.CustomTextView);
				String typeface = ta
						.getString(R.styleable.CustomTextView_customtypeface);
				String typeSize = ta
						.getString(R.styleable.CustomTextView_customtextsize);
				if (!TextUtils.isEmpty(typeSize)) {
					setTextViewSize(Float.valueOf(typeSize));
				}
				ta.recycle();
				if (!TextUtils.isEmpty(typeface)) {
					Typeface tf = Typeface.createFromAsset(context.getAssets(),
							typeface);
					setTypeface(tf);
				}
			}
		}
	}

	/**
	 * This method is used to setTextSize. In this float value is changed to
	 * scaledDenisty and then set to the editText
	 * 
	 * @param px
	 *            float size of editText
	 */
	public void setTextViewSize(float px) {
		float scaledDensity = getResources().getDisplayMetrics().scaledDensity;
		int sp = (int) (px / scaledDensity);
		setTextSize(sp);
	}

}
