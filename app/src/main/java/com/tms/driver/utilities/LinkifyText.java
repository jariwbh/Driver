package com.tms.driver.utilities;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tms.driver.models.LinkTextModel;


/**
 * This class is used to linkify text to the activities
 * 
 * @author siddharth.brahmi
 * 
 */

public class LinkifyText {

	/*
	 * private static String[] mtextTolinkfyArray; private static Class[]
	 * mlinkedText;
	 */

	/**
	 * Static method to linkyfy text.
	 * 

	 */

	public enum LinkType {
		ACTIVITY, CALL, VIEW
	}

	private LinkifyText() {

	}

	/**
	 * @param ctx
	 *            Context of the calling activity
	 * @param textView
	 *            textview on which to linkify the text
	 * @param textViewText
	 *            text that will be matched with likifying text
	 * @param texttoLinkyfi
	 *            array of specific text that needs linkify

	 * @param linkType
	 *            type of link set from LinkType enum class
	 */
	protected static void linkifyText(Context ctx, TextView textView,
			String textViewText, String[] texttoLinkyfi,
			LinkTextModel[] linkedTextArray, LinkType linkType) {

		SpannableStringBuilder builder = new SpannableStringBuilder(
				textViewText);
		ArrayList<Matcher> matcherArray = new ArrayList<Matcher>();
		String textTo_span = textViewText.toString();

		for (int count = 0; count < texttoLinkyfi.length; count++) {
			Pattern pattern = Pattern.compile(texttoLinkyfi[count]);

			Matcher pattern_matcher = pattern.matcher(textTo_span);
			matcherArray.add(pattern_matcher);

		}

		for (Matcher matcherText : matcherArray) {
			while (matcherText.find()) {
				int start = matcherText.start();
				int end = matcherText.end();

				String text1 = textTo_span.toString().subSequence(start, end)
						.toString();
				ClickableURLSpan url = null;
				if (linkedTextArray != null) {
					url = new ClickableURLSpan(text1,
							texttoLinkyfi[matcherArray.indexOf(matcherText)],
							linkedTextArray[matcherArray.indexOf(matcherText)],
							linkType, ctx, null);
				}

				builder.setSpan(url, start, end, 0);
			}

		}

		textView.setText(builder);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}

	static class ClickableURLSpan extends URLSpan {
		private LinkTextModel mclassforIntent;
		private String mStringToattachIntent;
		private LinkType linkType;
		private Context mctx;

		public ClickableURLSpan(String url, String matchString,
				LinkTextModel intentClass, LinkType linkType, Context ctx,
				String linkedText) {
			super(url);
			this.mStringToattachIntent = matchString;
			this.mclassforIntent = intentClass;
			this.linkType = linkType;
			this.mctx = ctx;
		}

		@Override
		public void onClick(View widget) {
			String clickedText = getURL();
			Log.e("Clicked", "" + clickedText);

			if (TextUtils.equals(getURL().toString(),
					this.mStringToattachIntent.toString())) {

				switch (linkType) {
				case ACTIVITY:
					if (this.mclassforIntent != null) {
						Intent intent = new Intent(mctx,
								(Class<?>) this.mclassforIntent.getT());
						mctx.startActivity(intent);
					}
					break;
				case VIEW:
					if (this.mclassforIntent != null) {
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(this.mclassforIntent.getT()
								.toString()));
						mctx.startActivity(i);
					}
					break;
				case CALL:
					if (this.mclassforIntent != null) {
						Intent i = new Intent(Intent.ACTION_CALL);
						i.setData(Uri.parse(this.mclassforIntent.getT()
								.toString()));
						mctx.startActivity(i);
					}

				default:
					break;
				}

			}
		}
	}

}
