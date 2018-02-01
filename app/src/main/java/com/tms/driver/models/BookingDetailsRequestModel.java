package com.tms.driver.models;

import java.io.Serializable;

public class BookingDetailsRequestModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2738337251082608331L;
	String bookingId, riderId, firstName, lastName, phone, shortAddress,
			rating, fullAddress, pickUpShortAddress, pickUpFullAddress,
			latitude, longitude, rest, ETA, userImage, reqestedVeh, cabType,
			payvia, message,dropOffShortAddress,dropOffFullAddress,dropOffLat,dropOffLong,pickUpAddressvalue,dropOffAddressvalue;

	public String getPickUpAddressvalue() {
		return pickUpAddressvalue;
	}

	public void setPickUpAddressvalue(String pickUpAddressvalue) {
		this.pickUpAddressvalue = pickUpAddressvalue;
	}

	public String getDropOffAddressvalue() {
		return dropOffAddressvalue;
	}

	public void setDropOffAddressvalue(String dropOffAddressvalue) {
		this.dropOffAddressvalue = dropOffAddressvalue;
	}

	public String getDropOffLat() {
		return dropOffLat;
	}

	public void setDropOffLat(String dropOffLat) {
		this.dropOffLat = dropOffLat;
	}

	public String getDropOffLong() {
		return dropOffLong;
	}

	public void setDropOffLong(String dropOffLong) {
		this.dropOffLong = dropOffLong;
	}

	public String getDropOffShortAddress() {
		return dropOffShortAddress;
	}

	public void setDropOffShortAddress(String dropOffShortAddress) {
		this.dropOffShortAddress = dropOffShortAddress;
	}

	public String getDropOffFullAddress() {
		return dropOffFullAddress;
	}

	public void setDropOffFullAddress(String dropOffFullAddress) {
		this.dropOffFullAddress = dropOffFullAddress;
	}

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public String getRiderId() {
		return riderId;
	}

	public void setRiderId(String riderId) {
		this.riderId = riderId;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getShortAddress() {
		return shortAddress;
	}

	public void setShortAddress(String shortAddress) {
		this.shortAddress = shortAddress;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public String getPickUpShortAddress() {
		return pickUpShortAddress;
	}

	public void setPickUpShortAddress(String pickUpShortAddress) {
		this.pickUpShortAddress = pickUpShortAddress;
	}

	public String getPickUpFullAddress() {
		return pickUpFullAddress;
	}

	public void setPickUpFullAddress(String pickUpFullAddress) {
		this.pickUpFullAddress = pickUpFullAddress;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getRest() {
		return rest;
	}

	public void setRest(String rest) {
		this.rest = rest;
	}

	public String getETA() {
		return ETA;
	}

	public void setETA(String eTA) {
		ETA = eTA;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public String getReqestedVeh() {
		return reqestedVeh;
	}

	public void setReqestedVeh(String reqestedVeh) {
		this.reqestedVeh = reqestedVeh;
	}

	public String getCabType() {
		return cabType;
	}

	public void setCabType(String cabType) {
		this.cabType = cabType;
	}
	
	

	public String getPayvia() {
		return payvia;
	}

	public void setPayvia(String payvia) {
		this.payvia = payvia;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
