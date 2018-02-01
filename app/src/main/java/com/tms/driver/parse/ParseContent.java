package com.tms.driver.parse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Base64OutputStream;

import com.tms.driver.constant.TaxiExConstants;
import com.tms.driver.fragments.ReservationListing;
import com.tms.driver.models.BookingDetailsRequestModel;
import com.tms.driver.models.MyProfileModel;
import com.tms.driver.models.ReservationList;
import com.tms.driver.models.TripListModel;
import com.tms.driver.models.UserLoginModel;
import com.tms.driver.models.VehicleListModel;
import com.tms.driver.models.WayBillModel;

public class ParseContent {

	private static ParseContent singleInstance;

	/**
	 * interface to check if the content is successfully parsed.
	 * 
	 * @author siddharth.brahmi
	 * 
	 */
	public interface onParsingContent {
		void onParsingSuccessful(String response, int callback);

		void onParsingSuccessful(HashMap<String, List<?>> modelList,
                                 String response, int callback);

		void onParsingError(String errorMessages, int callback);

		void onParsingSuccessful(Object modelClass, int callback);
	}

	public enum CONTENT_TYPE {
		LOGIN, FORGORPASSWORD, VEHICLELIST, MYPROFILE, TRIPLIST, WAYBILL, BOOKINGDETAILS, RESERVATION_LIST
	};

	/*
	 * Private Constructor
	 */
	private ParseContent() {

	}

	public static ParseContent getInstance() {
		if (singleInstance == null) {
			singleInstance = new ParseContent();

		}
		return singleInstance;
	}

	public void parseContentFromjson(CONTENT_TYPE contentType,
			Object jsonObject, onParsingContent onParsingListener,
			int callBackListener) throws NullPointerException {
		switch (contentType) {
		case LOGIN:
			String message = parseLoginContent((JSONObject) jsonObject);
			onParsingListener.onParsingSuccessful(message, callBackListener);
			break;

		case FORGORPASSWORD:
			String forgotpswd_message = parseForgotPasswordContent((JSONObject) jsonObject);
			onParsingListener.onParsingSuccessful(forgotpswd_message,
					callBackListener);
			break;

		case VEHICLELIST:

			List<VehicleListModel> vehicleListModelArray = parseVehicleListModel((JSONObject) jsonObject);
			HashMap<String, List<?>> vehiclListHashMap = new HashMap<String, List<?>>();

			vehiclListHashMap.put(TaxiExConstants.VehicleListArrayKey,
					vehicleListModelArray);
			onParsingListener.onParsingSuccessful(vehiclListHashMap, "Success",
					callBackListener);
			break;
		case MYPROFILE:

			MyProfileModel myPofileModel = parseMyProfileContent((JSONObject) jsonObject);
			onParsingListener.onParsingSuccessful(myPofileModel,
					callBackListener);
			break;
		case TRIPLIST:
			String message1 = "";
			List<TripListModel> tripListModelArray = parseRecentTripContent((JSONObject) jsonObject);
			HashMap<String, List<?>> tripListHashMap = new HashMap<String, List<?>>();
			if (tripListModelArray.size() == 0) {

				try {
					message1 = ((JSONObject) jsonObject).getString("message");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			tripListHashMap.put(TaxiExConstants.TripListArrayKey,
					tripListModelArray);
			onParsingListener.onParsingSuccessful(tripListHashMap, message1,
					callBackListener);
			break;
		case WAYBILL:

			WayBillModel wayBillModel = parseWayBillContent((JSONObject) jsonObject);
			onParsingListener.onParsingSuccessful(wayBillModel,
					callBackListener);
			break;
		case BOOKINGDETAILS:

			BookingDetailsRequestModel bookingDetailsRequestModel = parseBookingDetailsContent((JSONObject) jsonObject);
			onParsingListener.onParsingSuccessful(bookingDetailsRequestModel,
					callBackListener);
			break;

		case RESERVATION_LIST:
			String message3001 = "";

			List<ReservationList> reservationListArray = parseReservationListModel((JSONObject) jsonObject);
			HashMap<String, List<?>> reservationListHashMap = new HashMap<String, List<?>>();
			if (reservationListArray.size() == 0) {

				try {
					message3001 = ((JSONObject) jsonObject)
							.getString("message");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			reservationListHashMap.put(TaxiExConstants.ReservationListArrayKey,
					reservationListArray);
			onParsingListener.onParsingSuccessful(reservationListHashMap,
					message3001, callBackListener);

			break;
		default:
			break;
		}

	}

	private BookingDetailsRequestModel parseBookingDetailsContent(
			JSONObject jsonObject) {
		BookingDetailsRequestModel bookingDetailsRequestModel = new BookingDetailsRequestModel();
		if (jsonObject != null) {
			
			
	        /*ETA = "14 min";
	        bookingId = 194;
	        cabType = 2;
	        dropOffAddressValue = "";
	        dropoffLat = "";
	        dropoffLong = "";
	        firstName = efbgrtdb;
	        fullAddress = "";
	        lastName = "";
	        latitude = "30.673697";
	        longitude = "76.789970";
	        payvia = 0;
	        phone = "+16483637373";
	        pickUpAddressValue = "144, Behlana Road, Behlana, Chandigarh Chandigarh 160003, India";
	        pickUpFullAddress = "144, Behlana Road, Behlana, Chandigarh";
	        pickUpShortAddress = "Chandigarh 160003, India";
	        rating = "4.3";
	        reqestedVeh = 2;
	        rest = 14;
	        riderId = 11;
	        shortAddress = "Drop as directed";
	        userImage = "";*/
			try {
				JSONObject jsonObject2 = jsonObject
						.getJSONObject("BookingRequestDetail");
				bookingDetailsRequestModel.setBookingId(jsonObject2
						.getString("bookingId"));
				bookingDetailsRequestModel.setCabType(jsonObject2
						.getString("cabType"));

				bookingDetailsRequestModel.setETA(jsonObject2.getString("ETA"));

				bookingDetailsRequestModel.setFirstName(jsonObject2
						.getString("firstName"));

				bookingDetailsRequestModel.setFullAddress(jsonObject2
						.getString("fullAddress"));

				bookingDetailsRequestModel.setLastName(jsonObject2
						.getString("lastName"));
				bookingDetailsRequestModel.setLatitude(jsonObject2
						.getString("latitude"));
				bookingDetailsRequestModel.setLongitude(jsonObject2
						.getString("longitude"));
				// bookingDetailsRequestModel
				// .setMessage(jsonObject2.getString(""));
				bookingDetailsRequestModel.setPayvia(jsonObject2
						.getString("payvia"));
				bookingDetailsRequestModel.setPhone(jsonObject2
						.getString("phone"));
				bookingDetailsRequestModel.setPickUpFullAddress(jsonObject2
						.getString("pickUpFullAddress"));
				bookingDetailsRequestModel.setPickUpShortAddress(jsonObject2
						.getString("pickUpShortAddress"));

				bookingDetailsRequestModel.setDropOffShortAddress(jsonObject2
						.getString("shortAddress"));
				bookingDetailsRequestModel.setDropOffFullAddress(jsonObject2
						.getString("fullAddress")
						);
				bookingDetailsRequestModel.setRating(jsonObject2
						.getString("rating"));
				bookingDetailsRequestModel.setReqestedVeh(jsonObject2
						.getString("reqestedVeh"));

				bookingDetailsRequestModel.setRest(jsonObject2
						.getString("rest"));

				bookingDetailsRequestModel.setRiderId(jsonObject2
						.getString("riderId"));
				bookingDetailsRequestModel.setShortAddress(jsonObject2
						.getString("shortAddress"));
				bookingDetailsRequestModel.setUserImage(jsonObject2
						.getString("userImage"));
				bookingDetailsRequestModel.setDropOffLat(jsonObject2.getString("dropoffLat"));
				bookingDetailsRequestModel.setDropOffLong(jsonObject2.getString("dropoffLong"));
				bookingDetailsRequestModel.setPickUpAddressvalue(jsonObject2.getString("pickUpAddressValue"));
				bookingDetailsRequestModel.setDropOffAddressvalue(jsonObject2.getString("dropOffAddressValue"));

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return bookingDetailsRequestModel;

	}

	private WayBillModel parseWayBillContent(JSONObject jsonObject) {
		WayBillModel wayBillModel = new WayBillModel();
		if (jsonObject != null) {
			try {
				JSONObject jsonObject2 = jsonObject.getJSONObject("driverInfo");
				wayBillModel.setAbove(jsonObject2.getString("above"));
				wayBillModel.setBaseFare(jsonObject2.getString("baseFare"));
				wayBillModel.setBelow(jsonObject2.getString("below"));
				wayBillModel.setCancelFare(jsonObject2.getString("cancelFare"));
				wayBillModel.setCompanyName(jsonObject2
						.getString("companyName"));
				wayBillModel.setDriverLicence(jsonObject2
						.getString("driverLicence"));
				wayBillModel.setDriverName(jsonObject2.getString("driverName"));
				wayBillModel.setDriverStatus(jsonObject2
						.getString("driverStatus"));
				wayBillModel.setMinFare(jsonObject2.getString("minFare"));
				wayBillModel.setPersons(jsonObject2.getString("persons"));
				wayBillModel.setRiderName(jsonObject2.getString("riderName"));
				wayBillModel.setTaxiModel(jsonObject2.getString("taxiModel"));
				wayBillModel.setTaxiName(jsonObject2.getString("taxiName"));
				wayBillModel.setTaxiNumber(jsonObject2.getString("taxiNumber"));
				wayBillModel.setTaxiType(jsonObject2.getString("taxiType"));
				wayBillModel.setTripDate(jsonObject2.getString("tripDate"));
				wayBillModel.setTripFrom(jsonObject2.getString("tripFrom"));
				wayBillModel.setTripTo(jsonObject2.getString("tripTo"));

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return wayBillModel;

	}

	private MyProfileModel parseMyProfileContent(JSONObject jsonObject) {
		String message = null;
		MyProfileModel myProfileModel = new MyProfileModel();
		if (jsonObject != null) {
			try {
				message = jsonObject.getString("message");
				JSONObject driverDetailsJsonObject = jsonObject
						.getJSONObject("driverDetail");
				myProfileModel.setDriverImage(driverDetailsJsonObject
						.getString("driverImage"));
				myProfileModel.setFirstName(driverDetailsJsonObject
						.getString("firstName"));
				myProfileModel.setLastName(driverDetailsJsonObject
						.getString("lastName"));
				myProfileModel.setPhone(driverDetailsJsonObject
						.getString("phone"));
				myProfileModel.setRating(driverDetailsJsonObject
						.getString("rating"));
				myProfileModel.setTaxiModel(driverDetailsJsonObject
						.getString("taxiModel"));
				myProfileModel.setTaxiNumber(driverDetailsJsonObject
						.getString("taxiNumber"));
				myProfileModel.setTaxiType(driverDetailsJsonObject
						.getString("taxiModel"));

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return myProfileModel;
	}

	private String parseLoginContent(JSONObject jsonObject) {
		String message = null;
		if (jsonObject != null) {
			try {
				message = jsonObject.getString("message");
				try {

					String userLoginString = jsonObject.getString("userLogin");
					if (userLoginString.length() > 10) {
						JSONObject userLoginObject = new JSONObject(
								userLoginString);
						UserLoginModel userLoginModel = UserLoginModel
								.getInstance();
						userLoginModel.setDriverStatus(userLoginObject
								.getString("driverStatus"));
						userLoginModel.setEmailAddress(userLoginObject
								.getString("emailAddress"));
						userLoginModel.setFirstName(userLoginObject
								.getString("firstName"));
						userLoginModel.setId(userLoginObject.getString("id"));
						userLoginModel.setLastName(userLoginObject
								.getString("lastName"));
						userLoginModel.setUsername(userLoginObject
								.getString("username"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
//				{"taxiDetail":{"taxiId":"11","taxiModel":"Lexus RX 400","taxiNumber":"","taxiType":"Standard","taxiTypeId":"1"},"appVer":"1"}
				
				try{
					
					String taxiDetails=jsonObject.getString("taxiDetail");
					
					if(taxiDetails.length()>10){
						
						
						
						JSONObject taxiDetailsObject = new JSONObject(
								taxiDetails);	
						
						if(!taxiDetailsObject.getString("taxiId").equals("")){
						VehicleListModel vehicleListModel=VehicleListModel.getInstance();
						
						vehicleListModel.setId(taxiDetailsObject.getString("taxiId"));
						vehicleListModel.setTaxiModel(taxiDetailsObject.getString("taxiModel"));
						vehicleListModel.setTaxiNumber(taxiDetailsObject.getString("taxiNumber"));
						vehicleListModel.setTaxiType(taxiDetailsObject.getString("taxiType"));
						vehicleListModel.setTaxiTypeId(taxiDetailsObject.getString("taxiTypeId"));
						}
			
					}
						
						
						
					
					
				}catch(Exception e){
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return message;
	}

	private String parseForgotPasswordContent(JSONObject jsonObject) {
		String message = null;
		if (jsonObject != null) {
			try {
				message = jsonObject.getString("message");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return message;
	}

	private List<VehicleListModel> parseVehicleListModel(JSONObject jsonObject)

	{
		List<VehicleListModel> vehicleListModelArray = new ArrayList<VehicleListModel>();
		try {
			JSONArray vehicleJsonArray = jsonObject.getJSONArray("getTaxiList");
			for (int count = 0; count < vehicleJsonArray.length(); count++) {
				VehicleListModel vehicleListModel = new VehicleListModel();
				JSONObject jsonObject1 = vehicleJsonArray.getJSONObject(count);
				vehicleListModel.setId(jsonObject1.getString("id"));
				vehicleListModel.setMake(jsonObject1.getString("make"));
				vehicleListModel.setTaxiModel(jsonObject1
						.getString("taxiModel"));
				vehicleListModel.setTaxiNumber(jsonObject1
						.getString("taxiNumber"));
				vehicleListModel.setTaxiType(jsonObject1.getString("taxiType"));
				vehicleListModel.setTaxiTypeId(jsonObject1
						.getString("taxiTypeId"));
				vehicleListModelArray.add(vehicleListModel);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vehicleListModelArray;
	}

	private List<ReservationList> parseReservationListModel(
			JSONObject jsonObject) {

		List<ReservationList> reservationModelArray = new ArrayList<ReservationList>();

		try {
			JSONArray reservationJsonArray = jsonObject
					.getJSONArray("driverReservations");

			for (int count = 0; count < reservationJsonArray.length(); count++) {
				ReservationList reservation = new ReservationList();
				JSONObject jsonObject1 = reservationJsonArray
						.getJSONObject(count);
				// {"driverReservations":[{"id":"2","riderId":"12","name":"Arvind","picUpLoc":"Chandigarh
				// IT Park,
				// Chandigarh, India","dropOffLoc":"Kharar, Punjab,
				// India","picUpTime":"January 15, 2015",
				// "picUpDate":"February 12, 2015","phone":"7696209733","fareQuote":"20","carType":"3",
				// "invoiceId":"54b7a92eedde6"}],"message":"Driver reservations."}

				reservation.setId(jsonObject1.getString("id"));
				reservation.setRiderId(jsonObject1.getString("riderId"));
				reservation.setName(jsonObject1.getString("name"));
				reservation.setPickUpLoc(jsonObject1.getString("picUpLoc"));
				reservation.setDropOffLoc(jsonObject1.getString("dropOffLoc"));
				reservation.setPicUpTime(jsonObject1.getString("picUpTime"));
				reservation.setPicUpDate(jsonObject1.getString("picUpDate"));
				reservation.setPhone(jsonObject1.getString("phone"));
				reservation.setFareQuote(jsonObject1.getString("fareQuote"));
				reservation.setCarType(jsonObject1.getString("carType"));
				reservation.setInvoiceId(jsonObject1.getString("invoiceId"));

				reservation.setType(ReservationListing.TYPE);

				reservationModelArray.add(reservation);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reservationModelArray;

	}

	private List<TripListModel> parseRecentTripContent(JSONObject jsonObject)

	{
		List<TripListModel> recentTripArray = new ArrayList<TripListModel>();
		try {
			JSONArray tripListJsonArray = jsonObject.getJSONArray("tripList");
			for (int count = 0; count < tripListJsonArray.length(); count++) {
				TripListModel tripListModel = new TripListModel();
				JSONObject jsonObject1 = tripListJsonArray.getJSONObject(count);
				tripListModel.setCompletedDate(jsonObject1
						.getString("completedDate"));
				tripListModel.setReceiptNumber(jsonObject1
						.getString("receiptNumber"));
				tripListModel
						.setTripAmount(jsonObject1.getString("tripAmount"));
				tripListModel.setTripId(jsonObject1.getString("tripId"));
				tripListModel
						.setTripStatus(jsonObject1.getString("tripStatus"));
				tripListModel.setTip(jsonObject1.getString("tip"));

				recentTripArray.add(tripListModel);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recentTripArray;
	}
}
