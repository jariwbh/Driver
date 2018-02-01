package com.tms.driver.models;

import java.io.Serializable;

public class UserLoginModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -989771977874945718L;
	private static UserLoginModel mUserLoginModelInstance;

	private UserLoginModel() {
		// TODO Auto-generated constructor stub
	}

	public static UserLoginModel getInstance() {
		if (mUserLoginModelInstance == null) {
			mUserLoginModelInstance = new UserLoginModel();
		}
		return mUserLoginModelInstance;
	}

	public static void setInstance(UserLoginModel userLoginModel) {
		mUserLoginModelInstance = userLoginModel;
	}

	String id, firstName, lastName, username, emailAddress, driverStatus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

public void setUsername(String username) {
		this.username = username;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getDriverStatus() {
		return driverStatus;
	}

	public void setDriverStatus(String driverStatus) {
		this.driverStatus = driverStatus;
	}

}
