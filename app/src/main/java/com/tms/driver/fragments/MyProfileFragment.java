package com.tms.driver.fragments;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tms.driver.GCMDialog_Activity;
import com.tms.driver.R;
import com.tms.driver.base.BaseFragment;
import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.constant.TaxiExConstants.FRAGMENT_TAGS;
import com.tms.driver.customviews.CustomImageView;
import com.tms.driver.fragments.DutyStatusFragment;
import com.tms.driver.fragments.MyRecentTripsFragment;
import com.tms.driver.fragments.ReservationListing;
import com.tms.driver.models.MyProfileModel;
import com.tms.driver.models.UserLoginModel;
import com.tms.driver.parse.ParseContent;
import com.tms.driver.parse.ParseContent.CONTENT_TYPE;
import com.tms.driver.utilities.ImageUtilsClass;
import com.tms.driver.utilities.UplaodImageUtil;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

/**
 * 
 * @author arvind.agarwal this fragment is used to show driver's profile
 */
public class MyProfileFragment extends BaseFragment implements
		ImageChooserListener {

	private View mMyProfileView;
	private RatingBar mDriverRatingBar;
	private TextView mDriverNameTextView, mDriverRatingTextView;
	private Button mMyRecentTripsButton, mReservations;
	private MyProfileModel mMyProfileDetailsModel;
	private CustomImageView mProfileImageView;
	private ImageView mChangeProfileImage;
	public static ImageView mDefaultImage;
	private static final int IMAGE_PICK = 1;
	private static final int IMAGE_CAPTURE = 2;
	private Bitmap image;
	public String mImageBase64;
	private ImageChooserManager imageChooserManager;

	@Override
	public void onFailure(Throwable e, String error, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		ShowSingleButtonDialog(null, error,
				getString(R.string.alert_ok_button), 0, this, 10);
	}

	@Override
	public View setContentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


			// inflating view on fragment
			mMyProfileView = inflater.inflate(R.layout.frag_my_profile, null);
			initializecomponents();

			// webservice hit for MyProfile
			postXMl(TaxiExConstants.MyProfileDetailsUrl,
					createXml(TaxiExConstants.MyProfileXMLKeys, new String[] {
							"", UserLoginModel.getInstance().getId() }), true,
					TaxiExConstants.MyProfileWebserviceResponse, this);


		return mMyProfileView;
	}

	/**
	 * initializing view
	 */
	private void initializecomponents() {
		mDriverNameTextView = (TextView) mMyProfileView
				.findViewById(R.id.frag_my_profile_name_textview);
		mDriverRatingTextView = (TextView) mMyProfileView
				.findViewById(R.id.frag_my_profile_rating_textview);

		mDriverRatingBar = (RatingBar) mMyProfileView
				.findViewById(R.id.frag_my_profile_rating_bar);
		mMyRecentTripsButton = (Button) mMyProfileView
				.findViewById(R.id.frag_myprofile_recenttrips_button);

		mReservations = (Button) mMyProfileView
				.findViewById(R.id.frag_myprofile_reservations_button);
		mProfileImageView = (CustomImageView) mMyProfileView
				.findViewById(R.id.frag_my_profile_image);
		mChangeProfileImage = (ImageView) mMyProfileView
				.findViewById(R.id.frag_my_profile_image_selector);
		mDefaultImage = (ImageView) mProfileImageView
				.findViewById(R.id.frag_my_profile_image_default);
		mChangeProfileImage.setOnClickListener(this);
		mMyRecentTripsButton.setOnClickListener(this);

		mReservations.setOnClickListener(this);

		if (TaxiExConstants.IS_RESERVATION_AVAILABLE) {
			mReservations.setVisibility(View.VISIBLE);
		} else {
			mReservations.setVisibility(View.GONE);
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// setting header title
		setHeaderTitle(getString(R.string.my_profile_title));

		// set right icon Visibility
		setRightIconVisibility(View.VISIBLE);

		// setting left icon visibility
		setLeftIconVisibility(View.GONE);
		// setting right button icon
		setRightIcon((R.drawable.selector_logout_button));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * int)
	 * 
	 * this method is used to tackle response from server and further move data
	 * for parsing
	 */

	@Override
	public void onSuccess(String response, int UNIQUE_ID) {
		// TODO Auto-generated method stub
		super.onSuccess(response, UNIQUE_ID);

		switch (UNIQUE_ID) {
		case TaxiExConstants.MyProfileWebserviceResponse:

			Log.e("My Profile Details", response);
			try {
				if (response != null) {
					JSONObject jsonObject = new JSONObject(response);
					ParseContent.getInstance().parseContentFromjson(
							CONTENT_TYPE.MYPROFILE, jsonObject, this,
							TaxiExConstants.MyProfileParsingResponseId);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case TaxiExConstants.UpdateProfileImageWebserviceResponse:

			
			JSONObject jsonObject2;
			try {
				jsonObject2 = new JSONObject(response);
				int statusCode = jsonObject2
						.getInt("editImage");
				String message = jsonObject2.getString("message");

				
				if(statusCode == 1){
					Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
				}else if(statusCode == -4){
					Intent i = new Intent(getActivity(),
							GCMDialog_Activity.class);
					i.putExtra("messageType", "cancel");
					i.putExtra("message", message);

					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TASK);

					startActivity(i);
				}else {
					Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
				}
				
				
			} catch (JSONException e) {
			
				e.printStackTrace();
			}
			
			
			
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {

		case R.id.frag_myprofile_reservations_button:

			// startActivity(new Intent(getActivity(),

			if (DutyStatusFragment.mVehicleListModel == null) {// if vehicle
																// model is null
																// then show
																// prompt
				ShowSingleButtonDialog(null, "Please first choose vehicle.",
						getString(R.string.alert_ok_button), 0, this, 9999);
			} else {

				// move to reservation listing screen
				replaceFragment(R.id.acitivity_duty_status_frag,
						new ReservationListing(),
						FRAGMENT_TAGS.RESERVATION_LISTING.name(), true,
						getActivity());

			}
			break;

		case R.id.frag_myprofile_recenttrips_button:

			replaceFragment(R.id.acitivity_duty_status_frag,
					new MyRecentTripsFragment(),
					FRAGMENT_TAGS.MYRECENTTRIPS.name(), true, getActivity());

			break;
		case R.id.frag_my_profile_image_selector:

			// showing dialog for selecting image from camera or Gallery

			ShowDoubleButtonDialog(null, "Select Option:", "Camera", "Gallery",
					0, this, 1);

			break;
		default:
			break;
		}
	}

	@Override
	public void onPositiveButtonClick(int id) {
		// TODO Auto-generated method stub
		super.onPositiveButtonClick(id);

		switch (id) {
		case 1:

			// if camera is selected
			imageChooserManager = new ImageChooserManager(
					MyProfileFragment.this, ChooserType.REQUEST_CAPTURE_PICTURE);
			imageChooserManager.setImageChooserListener(MyProfileFragment.this);
			try {
				imageChooserManager.choose();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * Intent intent1 = new Intent(
			 * android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			 * 
			 * startActivityForResult(intent1, IMAGE_CAPTURE);
			 */
			break;
		case 2:

			// updating image on server
			updateProfileImage(image);
			break;
		case 10:
			popFragment(R.id.acitivity_duty_status_frag, getActivity());
			break;
		default:
			break;
		}
	}

	@Override
	public void onNegativeButtonClick(int id) {
		// TODO Auto-generated method stub
		super.onNegativeButtonClick(id);
		switch (id) {
		case 1:
			// if gallery is selected
			imageChooserManager = new ImageChooserManager(
					MyProfileFragment.this, ChooserType.REQUEST_PICK_PICTURE);
			imageChooserManager.setImageChooserListener(MyProfileFragment.this);

			try {
				imageChooserManager.choose();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * Intent intent = new Intent( Intent.ACTION_PICK,
			 * android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			 * intent.setType("image/*");
			 * startActivityForResult(Intent.createChooser(intent, ""),
			 * IMAGE_PICK);
			 */
			break;
		case 2:

			// reseting to previous image
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mProfileImageView.setImageUrl(
							mMyProfileDetailsModel.getDriverImage(), 0);

				}
			}, 1000);
			// mProfileImageView.requestLayout();
			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK
				&& (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
			imageChooserManager.submit(requestCode, data);

		}
		/*
		 * if (resultCode == Activity.RESULT_OK) {
		 * 
		 * saveImageAlert(); switch (requestCode) {
		 * 
		 * case IMAGE_PICK: this.imageFromGallery(resultCode, data); break; case
		 * IMAGE_CAPTURE: this.imageFromCamera(resultCode, data); break;
		 * default: break; } }
		 */
	}

	// prompt for save image confirmation
	private void saveImageAlert() {
		ShowDoubleButtonDialog(null, "Do you want to save image?",
				getString(R.string.alert_ok_button),
				getString(R.string.alert_cancel_button), 0, this, 2);
	}

	/**
	 * Image result from camera
	 * 
	 * @param resultCode
	 * @param data
	 */
	@SuppressWarnings("deprecation")
	private void imageFromCamera(int resultCode, Intent data) {
		Uri selectedImageUri = data.getData();
		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inSampleSize = 4;
		//
		// AssetFileDescriptor fileDescriptor = null;
		// try {
		// fileDescriptor = getActivity().getContentResolver()
		// .openAssetFileDescriptor(selectedImageUri, "r");
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
		// fileDescriptor.getFileDescriptor(), null, options);
		// ImageUtilsClass.getImageUtilsInstance(getActivity()).getBitmapFromGalleryandCamera(selectedImageUri,
		// 4, getActivity());
		try {
			this.updateImageView(ImageUtilsClass.getImageUtilsInstance(
					getActivity()).getBitmapFromGalleryandCamera(
					selectedImageUri, 500, 500, getActivity()));
		} catch (OutOfMemoryError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// handleOrientation(Uri.parse(data.toURI()).toString());
		// mProfileImageView.setImageUri(Uri.parse(data.toURI()));

	}

	/**
	 * Image result from gallery
	 * 
	 * @param resultCode
	 * @param data
	 */
	private void imageFromGallery(int resultCode, Intent data) {
		Uri selectedImageUri = data.getData();

		// Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr,
		// url)(getActivity()
		// .getContentResolver(), selectedImage);
		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inSampleSize = 4;
		//
		// AssetFileDescriptor fileDescriptor = null;
		// fileDescriptor = getActivity().getContentResolver()
		// .openAssetFileDescriptor(selectedImageUri, "r");
		//
		// Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
		// fileDescriptor.getFileDescriptor(), null, options);
		//
		// // Utils.Log("'4-sample' method bitmap ... "
		// // +actuallyUsableBitmap.getWidth() +" "
		// // +actuallyUsableBitmap.getHeight() );
		// updateImageView(actuallyUsableBitmap);
		try {
			this.updateImageView(ImageUtilsClass.getImageUtilsInstance(
					getActivity()).getBitmapFromGalleryandCamera(
					selectedImageUri, 500, 500, getActivity()));
		} catch (OutOfMemoryError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity()
		// .getContentResolver(), selectedImage);
		// bitmap = new BitmapProcessor(bitmap).resizeIfBiggerThan(1000,
		// 1000);
		// mProfileImageView.setImageBitmap(bitmap);
		// mImageBase64 = UplaodImageUtil.getBase64String(UplaodImageUtil
		// .getBitmapByteArray(bitmap));

	}

	public void updateProfileImage(Bitmap bitmap) {
		if (bitmap != null) {
			
			SharedPreferences mSharedprefrences=getActivity().getSharedPreferences(
					TaxiExConstants.SharedPreferenceName, 0);
		
		
		String deviceToken=mSharedprefrences.getString(
				TaxiExConstants.Pref_GCM_key, "");
			
			mImageBase64 = UplaodImageUtil.getBase64String(UplaodImageUtil
					.getBitmapByteArray(bitmap));
			if (!checkIfStringIsNullOrEmpty(mImageBase64)) {
				postXMl(TaxiExConstants.UpdateProfileImageUrl,
						createXml(TaxiExConstants.UpdateProfileImageXmlKeys,
								new String[] { "",
										UserLoginModel.getInstance().getId(),
										mImageBase64, deviceToken }), true,
						TaxiExConstants.UpdateProfileImageWebserviceResponse,
						this);
			}
		}
	}

	/**
	 * Update the imageView with new bitmap
	 * 
	 * @param newImage
	 */
	private void updateImageView(Bitmap newImage) {
		// Resources r = getResources();
		// float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
		// 200,
		// r.getDisplayMetrics());
		// BitmapProcessor bitmapProcessor = new BitmapProcessor(newImage,
		// (int) px, (int) px, 90);
		//
		image = newImage;
		mProfileImageView.setImageBitmap(newImage);
		// mImageBase64 = UplaodImageUtil.getBase64String(UplaodImageUtil
		// .getBitmapByteArray(newImage));
		// updateProfileImage(UplaodImageUtil.getBase64String(UplaodImageUtil
		// .getBitmapByteArray(this.image)));
	}

	@Override
	public void onSpinnerDialogClick(int position, int callBackId) {
		// TODO Auto-generated method stub
		super.onSpinnerDialogClick(position, callBackId);
		switch (position) {
		case 0:

			Intent intent = new Intent(
					Intent.ACTION_PICK,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");
			startActivityForResult(Intent.createChooser(intent, ""), IMAGE_PICK);

			break;
		case 1:
			Intent intent1 = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			intent1.putExtra(
					MediaStore.EXTRA_OUTPUT,
					(new File(Environment.getExternalStorageDirectory(), String
							.valueOf(System.currentTimeMillis()) + ".jpg")));

			startActivityForResult(intent1, IMAGE_CAPTURE);
			break;
		default:
			break;
		}
	}

	@Override
	public void getServerValues(String response, int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setServerError(int id, String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onParsingSuccessful(String response, int callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onParsingSuccessful(HashMap<String, List<?>> modelList,
			String response, int callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onParsingError(String errorMessages, int callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onParsingSuccessful(Object modelClass, int callback) {
		// TODO Auto-generated method stub
		try {
			mMyProfileDetailsModel = (MyProfileModel) modelClass;
			mDriverRatingTextView.setText(mMyProfileDetailsModel.getRating());
			mDriverRatingBar.setRating(Float.valueOf(mMyProfileDetailsModel
					.getRating()));
			mDriverNameTextView.setText(mMyProfileDetailsModel.getFirstName()
					+ " " + mMyProfileDetailsModel.getLastName());
			mProfileImageView.setImageUrl(
					mMyProfileDetailsModel.getDriverImage(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSuccess(String[] response, int UNIQUE_ID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(String arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kbeanie.imagechooser.api.ImageChooserListener#onImageChosen(com.kbeanie
	 * .imagechooser.api.ChosenImage)this method is used when image is selected
	 * from camera or gallery
	 */

	@Override
	public void onImageChosen(final ChosenImage arg0) {
		// TODO Auto-generated method stub

		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {

					// getting selected image into Bitmap
					Bitmap bmp = BitmapFactory.decodeFile(arg0
							.getFileThumbnail());
					MyProfileFragment.this.updateImageView(bmp);

					// save image alert message
					saveImageAlert();
				} catch (OutOfMemoryError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

}
