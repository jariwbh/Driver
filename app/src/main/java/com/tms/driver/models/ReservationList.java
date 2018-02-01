package com.tms.driver.models;

public class ReservationList {
	
	// {"driverReservations":[{"id":"2","riderId":"12","name":"Arvind","picUpLoc":"Chandigarh IT Park, Chandigarh, India",
	//"dropOffLoc":"Kharar, Punjab, India","picUpTime":"January 15, 2015","
	//picUpDate":"February 12, 2015","phone":"7696209733",
	//"fareQuote":"20","carType":"3","invoiceId":"54b7a92eedde6"}],"message":"Driver reservations."}

	
	private String id="",riderId="",name="",pickUpLoc="",dropOffLoc="",picUpTime="",picUpDate=""
			,phone="",fareQuote="",carType="",invoiceId="";
	
	private int type=1;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRiderId() {
		return riderId;
	}

	public void setRiderId(String riderId) {
		this.riderId = riderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPickUpLoc() {
		return pickUpLoc;
	}

	public void setPickUpLoc(String pickUpLoc) {
		this.pickUpLoc = pickUpLoc;
	}

	public String getDropOffLoc() {
		return dropOffLoc;
	}

	public void setDropOffLoc(String dropOffLoc) {
		this.dropOffLoc = dropOffLoc;
	}

	public String getPicUpTime() {
		return picUpTime;
	}

	public void setPicUpTime(String picUpTime) {
		this.picUpTime = picUpTime;
	}

	public String getPicUpDate() {
		return picUpDate;
	}

	public void setPicUpDate(String picUpDate) {
		this.picUpDate = picUpDate;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFareQuote() {
		return fareQuote;
	}

	public void setFareQuote(String fareQuote) {
		this.fareQuote = fareQuote;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
}
