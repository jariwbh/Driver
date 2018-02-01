package com.tms.driver.utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 
 * @author siddharth.brahmi
 * 
 */

public class ImageUtilsClass {

	public static ImageUtilsClass mImageUtilsInstance;

	private ImageUtilsClass() {

	}

	public static ImageUtilsClass getImageUtilsInstance(Context context) {

		if (mImageUtilsInstance == null) {
			mImageUtilsInstance = new ImageUtilsClass();
		}
		return mImageUtilsInstance;
	}

	/**
	 * This method is used to fetch the large image from gallery and camera.
	 * 
	 * @param imageUri
	 *            Uri of the image

	 * @param context
	 *            of the activity from which it is used
	 * @return bitmap
	 * @throws java.io.IOException
	 * @throws OutOfMemoryError
	 * @throws Exception
	 */
	public Bitmap getBitmapFromGalleryandCamera(Uri imageUri, int width,
			int height, Context context) throws IOException, OutOfMemoryError,
			Exception

	{
		InputStream is = context.getContentResolver().openInputStream(imageUri);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, options);
		is.close();
		options.inSampleSize = calculateInSampleSize(options, width, height);
		options.inJustDecodeBounds = false;
		options.inPurgeable = true;

		AssetFileDescriptor fileDescriptor = null;
		try {
			fileDescriptor = ((Activity) context).getContentResolver()
					.openAssetFileDescriptor(imageUri, "r");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int rotate = 90;
		switch (handleBitmapOrientation(imageUri, context)) {
		case 270:
			rotate = 270;
			break;
		case 180:
			rotate = 180;
			break;
		case 90:
			rotate = 90;
			break;
		default:
			rotate = 0;
		}

		Matrix matrix = new Matrix();
		matrix.postRotate(rotate);

		Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
				fileDescriptor.getFileDescriptor(), null, options);
		fileDescriptor.close();
		if (rotate != 0) {
			actuallyUsableBitmap = Bitmap.createBitmap(actuallyUsableBitmap, 0,
					0, actuallyUsableBitmap.getWidth(),
					actuallyUsableBitmap.getHeight(), matrix, true);
		}
		return actuallyUsableBitmap;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	private int handleBitmapOrientation(Uri imageUri, Context context) {
		Cursor cursor = context.getContentResolver().query(imageUri,
				new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
				null, null, null);

		try {
			if (cursor.moveToFirst()) {
				return cursor.getInt(0);
			} else {
				return -1;
			}
		} finally {
			cursor.close();
		}
	}

	public Bitmap resizeIfBiggerThan(Bitmap bitmap, int maxWidth, int maxHeight) {
		int originalWidth = bitmap.getWidth();
		int originalHeight = bitmap.getHeight();
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
			bitmap = Bitmap.createScaledBitmap(bitmap, maxWidth, maxHeight,
					true);
		}
		return bitmap;

	}
}
