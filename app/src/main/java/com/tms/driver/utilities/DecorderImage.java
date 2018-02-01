package com.tms.driver.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import org.apache.commons.io.FileUtils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.tms.driver.async.AndroidWebResponseTask;
import com.tms.driver.imageloader.ImageLoaderAsync;
import com.tms.driver.utilities.*;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.utils.L;

/**
 * Decodes images to {@link android.graphics.Bitmap}, scales them to needed size
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * 
 * @see com.nostra13.universalimageloader.core.assist.ImageScaleType
 * @see com.nostra13.universalimageloader.core.assist.ViewScaleType
 * @see com.nostra13.universalimageloader.core.download.ImageDownloader
 * @see com.nostra13.universalimageloader.core.DisplayImageOptions
 */
@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.KITKAT)
public class DecorderImage {

	private static final String LOG_IMAGE_SUBSAMPLED = "Original image (%1$dx%2$d) is going to be subsampled to %3$dx%4$d view. Computed scale size - %5$d";
	private static final String LOG_IMAGE_SCALED = "Subsampled image (%1$dx%2$d) was scaled to %3$dx%4$d";
	private Matrix m;
	// private final Uri imageUri;
	private int orientation;
	private ImageLoaderAsync mImageLoaderAsync;
	private static DecorderImage instance;
	private boolean loggingEnabled;
	private String file;
	private static Context mContext;

	/**

	 * 
	 */
	public static DecorderImage getInstance(Context context) {
		if (instance == null) {
			instance = new DecorderImage(context);
		}
		mContext = context;
		return instance;
	}

	private DecorderImage(Context context) {
		mImageLoaderAsync = ImageLoaderAsync.getInstance(context);
	}

	private DecorderImage() {

	}

	public int defineExifOrientation(String imageUri) {
		int rotation = 0;

		try {
			ExifInterface exif = new ExifInterface(imageUri);
			int exifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (exifOrientation) {

			case ExifInterface.ORIENTATION_NORMAL:
				rotation = 0;

				break;

			case ExifInterface.ORIENTATION_ROTATE_90:
				rotation = 90;

				break;

			case ExifInterface.ORIENTATION_ROTATE_180:
				rotation = 180;

				break;

			case ExifInterface.ORIENTATION_ROTATE_270:
				rotation = 270;

				break;
			}
		} catch (IOException e) {
			L.w("Can't read EXIF tags from file [%s]", imageUri);
		}

		return rotation;
	}

	/**
	 * This method is used to decode image
	 * 
	 * @param imageUri
	 *            set uri of sdcard
	 * @param width
	 *            set width of image
	 * @param height
	 *            set height of image
	 * @return bitmap
	 * @throws java.io.IOException
	 */
	public Bitmap decode(Uri imageUri, int width, int height)
			throws IOException {
		orientation = defineExifOrientation(imageUri.getPath());
		ImageSize targetSize = new ImageSize(width, height, orientation);
		return decode(imageUri, targetSize, ImageScaleType.EXACTLY,
				ViewScaleType.FIT_INSIDE);
	}

	/**
	 * This method is used to decode image
	 * 
	 * @param imageUri
	 *            set uri of gallery like (content://)
	 * @param width
	 *            set width of image
	 * @param height
	 *            set height of image
	 * @param context
	 *            context to pass
	 * @return bitmap
	 * @throws java.io.IOException
	 */
	public Bitmap decodeGalleryUriContent(Uri imageUri, int width, int height,
			Context context) throws IOException {
		orientation = defineExifOrientation(imageUri.getPath());
		Bitmap bitmap = Media.getBitmap(context.getContentResolver(), imageUri);
		ImageSize targetSize = new ImageSize(width, height, orientation);
		return scaleImageExactly(bitmap, targetSize, ImageScaleType.EXACTLY,
				ViewScaleType.FIT_INSIDE);
	}

	public String getGalleryFilePath(Context context, Uri imageUri) {
		// String[] filePathColumn = { MediaStore.Images.Media.DATA };
		// if (Build.VERSION.SDK_INT >= 19) {
		// String wholeID = DocumentsContract.getDocumentId(imageUri);
		// String id = wholeID.split(":")[1];
		// String sel = MediaStore.Images.Media._ID + "=?";
		// Cursor cursor = context.getContentResolver().query(
		// MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
		// filePathColumn, sel, new String[] { id }, null);
		// cursor.moveToFirst();
		// int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		// String filePath = cursor.getString(columnIndex);
		// cursor.close();
		// return filePath;
		// } else {
		//
		// Cursor cursor = context.getContentResolver().query(imageUri,
		// filePathColumn, null, null, null);
		// cursor.moveToFirst();
		// int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		// String picturePath = cursor.getString(columnIndex);
		// return picturePath;
		// }
		return getPath(context, imageUri);
	}

	/**
	 * This method is used decodeImage according to aspect ratio
	 * 
	 * @param bitmap
	 *            set bitmap
	 * @param imageView
	 *            set imageview
	 * @return bitmap
	 */
	public void decodeAspectRatio(Bitmap bitmap, ImageView imageView) {
		SetAspectRatio(bitmap, imageView);
	}

	/**
	 * This method is used to decode bitmap
	 * 
	 * @param bitmap
	 *            set bitmap
	 * @param width
	 *            set width
	 * @param height
	 *            set height
	 * @return bitmap
	 */
	public Bitmap decodeAspectRatio(Bitmap bitmap, int width, int height) {
		ImageSize targetSize = new ImageSize(width, height);
		return scaleImageExactly(bitmap, targetSize, ImageScaleType.EXACTLY,
				ViewScaleType.FIT_INSIDE);
	}

	@SuppressWarnings("unused")
	private ImageSize getImageSizeScaleTo(ImageView imageView, int width,
			int height) {
		DisplayMetrics displayMetrics = imageView.getContext().getResources()
				.getDisplayMetrics();

		LayoutParams params = imageView.getLayoutParams();
		int width1 = params.width; // Get layout width parameter
		if (width1 <= 0)
			width1 = getFieldValue(imageView, "mMaxWidth"); // Check maxWidth
															// parameter
		if (width1 <= 0)
			width1 = width;
		if (width1 <= 0)
			width1 = displayMetrics.widthPixels;

		int height1 = params.height; // Get layout height parameter
		if (height1 <= 0)
			height1 = getFieldValue(imageView, "mMaxHeight"); // Check maxHeight
																// parameter
		if (height1 <= 0)
			height1 = height;
		if (height1 <= 0)
			height1 = displayMetrics.heightPixels;

		return new ImageSize(width1, height1);
	}

	private void SetAspectRatio(Bitmap img, ImageView myView) {
		try {
			int height, width, w;
			int layoutHeight = myView.getDrawable().getIntrinsicHeight();
			int layoutWidth = myView.getDrawable().getIntrinsicWidth();
			if (img != null) {
				height = img.getHeight() > layoutHeight ? layoutHeight : img
						.getHeight();
				int ratio = ((img.getHeight() - height) * 100 / img.getHeight());
				int wi = (img.getWidth() * ratio) / 100;
				width = img.getWidth() - wi;
				w = width;
				int width1 = width > layoutWidth ? layoutWidth : width;
				if (width > 0) {
					ratio = ((width - width1) * 100 / width);
					int hi = (height * ratio) / 100;
					height = height - hi;
					w = width1;
				}

				LayoutParams params = myView.getLayoutParams();
				params.height = height;
				params.width = w;
				myView.setLayoutParams(params);

				myView.setImageBitmap(img);
				myView.setMaxHeight(img.getHeight());
				img = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private int getFieldValue(Object object, String fieldName) {
		int value = 0;
		try {
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer) field.get(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
				value = fieldValue;
			}
		} catch (Exception e) {
			L.e(e);
		}
		return value;
	}

	/**
	 * Decodes image from URI into {@link android.graphics.Bitmap}. Image is scaled close to
	 * incoming {@link com.nostra13.universalimageloader.core.assist.ImageSize image size} during decoding (depend on incoming
	 * image scale type).
	 * 
	 * @param targetSize
	 *            Image size to scale to during decoding
	 * @param scaleType
	 *            {@link com.nostra13.universalimageloader.core.assist.ImageScaleType Image scale type}
	 * 
	 * @return Decoded bitmap
	 * @throws java.io.IOException
	 */
	public Bitmap decode(Uri imageUri, ImageSize targetSize,
			ImageScaleType scaleType) throws IOException {
		return decode(imageUri, targetSize, scaleType, ViewScaleType.FIT_INSIDE);
	}

	/**
	 * Decodes image from URI into {@link android.graphics.Bitmap}. Image is scaled close to
	 * incoming {@link com.nostra13.universalimageloader.core.assist.ImageSize image size} during decoding (depend on incoming
	 * image scale type).
	 * 

	 * @return Decoded bitmap
	 * @throws java.io.IOException
	 */
	public void decodeAutoSize(Uri imageUri, int id, Context context)
			throws IOException {
		Bitmap bitmap;
		if (imageUri.toString().startsWith("content")
				|| imageUri.toString().startsWith("com")) {
			id = 0;
		} else {
			id = 1;
		}
		if (id == 0) {
			String file = getGalleryFilePath(context, imageUri);
			bitmap = decode(Uri.parse(file), getImageSize(Uri.parse(file)),
					ImageScaleType.EXACTLY, ViewScaleType.FIT_INSIDE);
		} else {
			bitmap = decode(imageUri, getImageSize(imageUri),
					ImageScaleType.EXACTLY, ViewScaleType.FIT_INSIDE);
		}
		com.tms.driver.utilities.Utilities utilities = com.tms.driver.utilities.Utilities.getInstance(mContext);
		FileUtils.writeByteArrayToFile(
				new File(Environment.getExternalStorageDirectory(),
						"photo1.png"), UplaodImageUtil
						.getBitmapByteArray(bitmap));

	}

	/**
	 * Decodes image from URI into {@link android.graphics.Bitmap}. Image is scaled close to
	 * incoming {@link com.nostra13.universalimageloader.core.assist.ImageSize image size} during decoding (depend on incoming
	 * image scale type).
	 * 

	 * @return Decoded bitmap
	 * @throws java.io.IOException
	 */
	public String decodeAutoSize(final String imageUri) throws IOException {
		final AndroidWebResponseTask task = AndroidWebResponseTask
				.getInstance(mContext);
		// file = task.getImage(imageUri);
		return file;

	}

	/**
	 * Decodes image from URI into {@link android.graphics.Bitmap}. Image is scaled close to
	 * incoming {@link com.nostra13.universalimageloader.core.assist.ImageSize image size} during decoding (depend on incoming
	 * image scale type).
	 * 
	 * @param targetSize
	 *            Image size to scale to during decoding
	 * @param scaleType
	 *            {@link com.nostra13.universalimageloader.core.assist.ImageScaleType Image scale type}
	 * @param viewScaleType
	 *            {@link com.nostra13.universalimageloader.core.assist.ViewScaleType View scale type}
	 * 
	 * @return Decoded bitmap
	 * @throws java.io.IOException
	 */
	public Bitmap decode(Uri imageUri, ImageSize targetSize,
			ImageScaleType scaleType, ViewScaleType viewScaleType)
			throws IOException {

		Options decodeOptions = getBitmapOptionsForImageDecoding(imageUri,
				targetSize, scaleType, viewScaleType);
		InputStream imageStream = new FileInputStream(imageUri.getPath());
		Bitmap subsampledBitmap;
		try {
			subsampledBitmap = BitmapFactory.decodeStream(imageStream, null,
					decodeOptions);
		} finally {
			imageStream.close();
		}
		if (subsampledBitmap == null) {
			return null;
		}

		// Scale to exact size if need
		if (scaleType == ImageScaleType.EXACTLY
				|| scaleType == ImageScaleType.EXACTLY_STRETCHED) {
			subsampledBitmap = scaleImageExactly(subsampledBitmap, targetSize,
					scaleType, viewScaleType);
		}

		return subsampledBitmap;
	}

	private Options getBitmapOptionsForImageDecoding(Uri imageUri,
			ImageSize targetSize, ImageScaleType scaleType,
			ViewScaleType viewScaleType) throws IOException {
		Options decodeOptions = new Options();
		decodeOptions.inSampleSize = computeImageScale(imageUri, targetSize,
				scaleType, viewScaleType);
		decodeOptions.inPreferredConfig = mImageLoaderAsync.getOption()
				.getDecodingOptions().inPreferredConfig;
		return decodeOptions;
	}

	private int computeImageScale(Uri imageUri, ImageSize targetSize,
			ImageScaleType scaleType, ViewScaleType viewScaleType)
			throws IOException {
		int targetWidth = targetSize.getWidth();
		int targetHeight = targetSize.getHeight();
		// decode image size
		Options options = new Options();
		options.inJustDecodeBounds = true;
		InputStream imageStream = new FileInputStream(imageUri.getPath());
		try {
			BitmapFactory.decodeStream(imageStream, null, options);
		} finally {
			imageStream.close();
		}

		int scale = 1;
		int imageWidth = options.outWidth;
		int imageHeight = options.outHeight;
		int widthScale = imageWidth / targetWidth;
		int heightScale = imageHeight / targetHeight;

		if (viewScaleType == ViewScaleType.FIT_INSIDE) {
			if (scaleType == ImageScaleType.IN_SAMPLE_POWER_OF_2
					|| scaleType == ImageScaleType.IN_SAMPLE_INT) {
				while (imageWidth / 2 >= targetWidth
						|| imageHeight / 2 >= targetHeight) { // ||
					imageWidth /= 2;
					imageHeight /= 2;
					scale *= 2;
				}
			} else {
				scale = Math.max(widthScale, heightScale); // max
			}
		} else { // ViewScaleType.CROP
			if (scaleType == ImageScaleType.IN_SAMPLE_POWER_OF_2
					|| scaleType == ImageScaleType.IN_SAMPLE_INT) {
				while (imageWidth / 2 >= targetWidth
						&& imageHeight / 2 >= targetHeight) { // &&
					imageWidth /= 2;
					imageHeight /= 2;
					scale *= 2;
				}
			} else {
				scale = Math.min(widthScale, heightScale); // min
			}
		}

		if (scale < 1) {
			scale = 1;
		}

		if (loggingEnabled)
			L.d(LOG_IMAGE_SUBSAMPLED, imageWidth, imageHeight, targetWidth,
					targetHeight, scale);
		return scale;
	}

	private Bitmap scaleImageExactly(Bitmap subsampledBitmap,
			ImageSize targetSize, ImageScaleType scaleType,
			ViewScaleType viewScaleType) {
		float srcWidth = subsampledBitmap.getWidth();
		float srcHeight = subsampledBitmap.getHeight();

		float widthScale = srcWidth / targetSize.getWidth();
		float heightScale = srcHeight / targetSize.getHeight();

		int destWidth;
		int destHeight;
		if ((viewScaleType == ViewScaleType.FIT_INSIDE && widthScale >= heightScale)
				|| (viewScaleType == ViewScaleType.CROP && widthScale < heightScale)) {
			destWidth = targetSize.getWidth();
			destHeight = (int) (srcHeight / widthScale);
		} else {
			destWidth = (int) (srcWidth / heightScale);
			destHeight = targetSize.getHeight();
		}

		Bitmap scaledBitmap;
		if ((scaleType == ImageScaleType.EXACTLY && destWidth < srcWidth && destHeight < srcHeight)
				|| (scaleType == ImageScaleType.EXACTLY_STRETCHED
						&& destWidth != srcWidth && destHeight != srcHeight)) {
			if (orientation != 0) {
				m = new Matrix();
				m.postRotate(orientation);
				scaledBitmap = Bitmap.createBitmap(subsampledBitmap, 0, 0,
						subsampledBitmap.getWidth(),
						subsampledBitmap.getHeight(), m, true);
			} else {
				scaledBitmap = Bitmap.createScaledBitmap(subsampledBitmap,
						destWidth, destHeight, true);
			}

			if (loggingEnabled)
				L.d(LOG_IMAGE_SCALED, (int) srcWidth, (int) srcHeight,
						destWidth, destHeight);
		} else {
			scaledBitmap = subsampledBitmap;

		}
		if (scaledBitmap != subsampledBitmap) {
			subsampledBitmap.recycle();
		}
		return scaledBitmap;
	}

	void setLoggingEnabled(boolean loggingEnabled) {
		this.loggingEnabled = loggingEnabled;
	}

	public ImageSize getImageSize(Uri uri) {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(uri.getPath(), options);
		int imageHeight = options.outHeight;
		int imageWidth = options.outWidth;
		return new ImageSize(imageWidth, imageHeight,
				defineExifOrientation(uri.toString()));
	}

	/*************************************************************************************************************************/
	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @author paulburke
	 */
	@SuppressLint("NewApi")
	private String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri)) {
				try {
					return decodeAutoSize(uri.getLastPathSegment());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return uri.getLastPathSegment();
				}
			} else if ("com.sec.android.gallery3d.provider".equals(uri
					.getAuthority())) {
				try {
					return decodeAutoSize(getDataColumn(context, uri, null,
							null));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				return getDataColumn(context, uri, null, null);
			}

		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	private String getDataColumn(Context context, Uri uri, String selection,
			String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	private boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	private boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	private boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	private boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	/*************************************************************************************************************************/
	private class CompressImage extends AsyncTask<Void, Void, String> {
		ProgressDialog mProgressDialog;
		File cameraFile = new File(Environment.getExternalStorageDirectory(),
				"photo.png");
		DecorderImage image;
		DisplayMetrics metrics;
		com.tms.driver.utilities.Utilities utilities;

		public CompressImage(Context context, ProgressDialog mProgressDialog) {
			this.mProgressDialog = mProgressDialog;
			image = DecorderImage.getInstance(context);
			metrics = context.getResources().getDisplayMetrics();
			utilities = com.tms.driver.utilities.Utilities.getInstance(mContext);

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Bitmap bitmap = image.decode(Uri.parse(cameraFile.toString()),
						metrics.widthPixels, metrics.heightPixels);
				if (bitmap != null) {
					FileUtils.writeByteArrayToFile(cameraFile,
							UplaodImageUtil.getBitmapByteArray(bitmap));
					return cameraFile.toString();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}

		}
	}
}