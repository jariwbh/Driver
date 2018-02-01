package com.tms.driver.models;

import java.io.Serializable;

public class VehicleListModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2209586951724855418L;
	String id, make, taxiModel, taxiNumber, taxiType,taxiTypeId;
	
	public static VehicleListModel vehicleListModel;
	
	public static VehicleListModel getInstance(){
		if(vehicleListModel==null){
			
			
			vehicleListModel=new VehicleListModel();
		}
		
		return vehicleListModel;
	}

	public String getTaxiTypeId() {
		return taxiTypeId;
	}

	public void setTaxiTypeId(String taxiTypeId) {
		this.taxiTypeId = taxiTypeId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getTaxiModel() {
		return taxiModel;
	}

	public void setTaxiModel(String taxiModel) {
		this.taxiModel = taxiModel;
	}

	public String getTaxiNumber() {
		return taxiNumber;
	}

	public void setTaxiNumber(String taxiNumber) {
		this.taxiNumber = taxiNumber;
	}

	public String getTaxiType() {
		return taxiType;
	}

	public void setTaxiType(String taxiType) {
		this.taxiType = taxiType;
	}

}
