package com.tms.driver.interfaces;

/*
 *
 * YOu have to implement this interface whenever want to use AlertDialogFragments. 
 * This will provide methods to perform actions on specific button click. 
 */

public interface OnAlertDialogFragmentListener {

	/**
	 * perform some action when positive button clicked (Button 1)
	 */
	void onPositiveButtonClick(int id);

	/**
	 * perform some action when negative button clicked (Button 2)
	 */
	void onNegativeButtonClick(int id);

	/**
	 * perform some action when negative button clicked (Button 3)
	 */
	void onNeutralizeButtonClick(int id);

	/**
	 * Show error if the interface is not attached properly.
	 */
	void interfaceAttachError(int fragmentId, String errorResponse);

	void onBackPress(int id);

}
