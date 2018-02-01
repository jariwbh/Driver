package com.tms.driver.customviews;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.Button;

import com.tms.driver.R;



public class CustomButton extends Button {

	public CustomButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomButton(Context context, AttributeSet attrs) {
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
				String typeWidth = ta
						.getString(R.styleable.CustomTextView_customwidth);
				String typeHeight = ta
						.getString(R.styleable.CustomTextView_customheight);
				if (!TextUtils.isEmpty(typeSize)) {
					setTextViewSize(Float.valueOf(typeSize));
				}
				if (!TextUtils.isEmpty(typeWidth)
						&& !TextUtils.isEmpty(typeHeight)) {
					setTextFieldSize(Float.valueOf(typeWidth),
							Float.valueOf(typeHeight));
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

	public void setTextFieldSize(float width, float height) {
		float scaledDensity = getResources().getDisplayMetrics().densityDpi;
		Resources resources = getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float widthDp = width / (metrics.densityDpi / 160f);
		float heightDp = height / (metrics.densityDpi / 160f);

		// int widthDp = (int) (width / scaledDensity);
		// int heightDp = (int) (height / scaledDensity);
		setWidth((int) widthDp);
		setHeight((int) heightDp);
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
