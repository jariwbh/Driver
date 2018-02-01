package com.tms.driver.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

import com.tms.driver.R;
import com.tms.driver.customviews.RoundedImageView;
import com.tms.driver.imageloader.DecorderImage;
import com.tms.driver.imageloader.ImageLoaderAsync;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.io.IOException;

/**
 * This class is used as widget instead to using FrameLayout with ProgressBar
 * and ImageView. This is used like a ImageView. This is used when images are
 * fetched from server and show on ImageView. Till the images are loader from
 * server progress bar is visible on image view. After that Images are loaded
 * and set on imageView. This is also done with the help of Universal Image
 * Loader.
 * 
 */
public class CustomImageView extends FrameLayout {
	private RoundedImageView mAsyncImageView;
	private ProgressBar mProgressBar;
	private ImageLoaderAsync mImageLoaderAsync;

	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView();
		if (attrs != null) {
			TypedArray array = context.obtainStyledAttributes(attrs,
					R.styleable.CustomImageView, 0, 0);
			String url = array.getString(R.styleable.CustomImageView_url);
			int defaultImage = array.getResourceId(
					R.styleable.CustomImageView_defaultImage, 0);
			int height = array.getInt(R.styleable.CustomImageView_height_stylable, 0);
			int width = array.getInt(R.styleable.CustomImageView_width_stylable, 0);
			if (!isEmptyOrNull(url)) {
				setImageUrl(url, defaultImage);
			}
			if (defaultImage > 0) {
				setDefaultImage(defaultImage);
			}
			if (width > 0 && height > 0) {
				LayoutParams param = new LayoutParams(width, height);
				mAsyncImageView.setLayoutParams(param);

			}
			array.recycle();
		}
	}

	public CustomImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}

	public void setImageUri(Uri uri) {
		// TODO Auto-generated method stub

		mAsyncImageView.setImageURI(uri);

	}

	private void initView() {
		LayoutInflater inflator = LayoutInflater.from(getContext());
		View v = inflator.inflate(R.layout.framelayout_image, this);
		mProgressBar = (ProgressBar) v
				.findViewById(R.id.action_refresh_progress);
		mAsyncImageView = (RoundedImageView) v.findViewById(R.id.imageView);

		mAsyncImageView.setScaleType(ScaleType.CENTER_CROP);

		mImageLoaderAsync = ImageLoaderAsync.getInstance(getContext());
	}

	public final void setDefaultImage(int resId) {
		mAsyncImageView.setScaleType(ScaleType.CENTER_CROP);
		mAsyncImageView.setImageResource(resId);
	}

	/**
	 * This method is used to setImage on ImageView.
	 * 
	 * @param url
	 *            set url of image
	 */
	public final void setImageUrl(String url, int defaultImage) {
		setImage(url, defaultImage);
	}

	/**
	 * This method is used to setImage from the sdcard path. only path has to
	 * given file:/// is automatically set
	 * 
	 * @param filePath
	 *            set path of sdcard
	 */
	public final void setImageFile(String filePath, int defaultImage) {
		if (!filePath.contains(ImageLoaderAsync.IMAGERUI_SDCARD)) {
			filePath = ImageLoaderAsync.IMAGERUI_SDCARD + filePath;
		}
		setImage(filePath, defaultImage);
	}

	/**
	 * This method is used to set contact image. path of contact image has to
	 * given. content:// auto set
	 * 
	 * @param contactPath
	 *            set path of contact image
	 */
	public final void setImageContact(String contactPath, int defaultImage) {
		if (!contactPath.contains(ImageLoaderAsync.IMAGERUI_CONTACT)) {
			contactPath = ImageLoaderAsync.IMAGERUI_CONTACT + contactPath;
		}
		setImage(contactPath, defaultImage);
	}

	/**
	 * This method is used to set image from resources drawable folder.
	 * 
	 * @param res
	 *            set res path
	 */
	public final void setImageDrawable(int res, int defaultImage) {
		String path = null;
		if (res > 0) {
			path = ImageLoaderAsync.IMAGERUI_DRAWABLE + res;
		}
		setImage(path, defaultImage);
	}

	public final void setImageBitmap(Bitmap bitmap) {

		mAsyncImageView.setImageBitmap(bitmap);
	}

	/**
	 * This method is used to make bitmap from filepath
	 * 
	 * @param imageUri
	 *            uri of image
	 * @param width
	 *            set new width
	 * @param height
	 *            set new height
	 * @return bitmap
	 * @throws java.io.IOException
	 */
	public final Bitmap decode(Uri imageUri, int width, int height)
			throws IOException {
		DecorderImage image = DecorderImage.getInstance(getContext());
		return image.decode(imageUri, width, height);
	}

	/**
	 * This method is used to setImage on AsyncImageView
	 * 
	 * @param url
	 *            set the url to pass
	 */
	private void setImage(String url, final int defaultImage) {
		mImageLoaderAsync.getImageLoader().displayImage(url, mAsyncImageView,
				mImageLoaderAsync.getOption(), new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						setDefaultImage(defaultImage);
						mProgressBar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						// TODO Auto-generated method stub
						mProgressBar.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							final Bitmap bitmap) {
						// TODO Auto-generated method stub
						mProgressBar.setVisibility(View.INVISIBLE);
						if (bitmap != null) {
							// try {
							// new Handler().post(new Runnable() {
							//
							// @Override
							// public void run() {
							// // TODO Auto-generated method stub
							// MyProfileFragment.mDefaultImage
							// .setVisibility(View.GONE);
							//
							// }
							// });
							// } catch (Exception e) {
							// e.printStackTrace();
							// }
							new Handler().postDelayed(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									mAsyncImageView
											.setScaleType(ScaleType.CENTER_CROP);
									mAsyncImageView.setImageBitmap(bitmap);

								}
							}, 200);
						} else {
							mAsyncImageView.setScaleType(ScaleType.CENTER_CROP);
							setDefaultImage(defaultImage);

						}
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub

					}
				});
	}

	private boolean isEmptyOrNull(String str) {
		return !(!TextUtils.isEmpty(str) && !str.equals("null"));
	}

	public void setResource(int resId) {
		mAsyncImageView.setImageResource(resId);
	}
}
