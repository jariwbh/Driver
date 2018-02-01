package com.tms.driver.utilities;

import android.graphics.Bitmap;

public class BitmapProcessor {
	private Bitmap _bitmap;

	public Bitmap getBitmap() {
		return this._bitmap;
	}

	public BitmapProcessor(Bitmap bitmap) {
		this._bitmap = bitmap;
	}

	public BitmapProcessor(Bitmap bitmap, int maxWidth, int maxHeight) {
		this._bitmap = bitmap;

		this.resizeIfBiggerThan(maxWidth, maxHeight);
	}

	public BitmapProcessor(Bitmap bitmap, int maxWidth, int maxHeight, int hue) {
		this._bitmap = bitmap;
		this.resizeIfBiggerThan(maxWidth, maxHeight);
		// this.adjustHue(hue);
	}

	public Bitmap resizeIfBiggerThan(int maxWidth, int maxHeight) {
		int originalWidth = this._bitmap.getWidth();
		int originalHeight = this._bitmap.getHeight();
		int newWidth = originalWidth;
		int newHeight = originalHeight;
		float ratio = 1.0f;

		if (originalWidth >= maxWidth || originalHeight >= maxHeight) {
			if (originalWidth >= maxWidth) {
				ratio = originalWidth / (float) maxWidth;
				newWidth = maxWidth;
				newHeight = (int) (originalHeight / ratio);
			}

			if (newHeight >= maxHeight) {
				ratio = newHeight / (float) maxHeight;
				newHeight = maxHeight;
				newWidth = (int) (newWidth / ratio);
				if (newWidth >= maxWidth) {
					ratio = newWidth / (float) maxWidth;
					newWidth = maxWidth;
					newHeight = (int) (newHeight / ratio);
				}
			}
			this._bitmap = Bitmap.createScaledBitmap(this._bitmap, maxWidth,
					maxHeight, true);
		}
		return this._bitmap;

	}

}
