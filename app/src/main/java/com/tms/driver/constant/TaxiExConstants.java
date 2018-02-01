package com.tms.driver.constant;

public class TaxiExConstants {
	public enum FRAGMENT_TAGS {
		LOGINFRAGMENT, FORGOTPASSWORDFRAGMENT, DUTYSTATUSFRAGMENT, CHANGEVEHICLEFRAGMENT, MYPROFILEFRGMENT, MYRECENTTRIPS, WAYBILLFRAGMENT, TERMSANDCONDITION_FRAGMENT, TERMSWEBFRAGMENT, FARESUMMARYFRAGMENT, PAYMENTMODE_FRAGMENT, RESERVATION_LISTING, RESERVATIONDETAILS, RESERVATIONHISTORY
	}

//	public static String  BASEURL="http://demo.taximobilesolutions.com/"; //demo
	public static String  BASEURL="http://taxidispatch.azurewebsites.net/"; //demo

	//public static String BASEURL = "http://skylimo.taximobilesolutions.com/";  // live
		
	//	public static String  BASEURL="http://partner.alphacarlimoinc.com/";

	
	/* changed */
	public static String LoginURl = BASEURL + "driverServices/userLoginAndroid";

	/* changed */
	public static String ForgotPasswordURL = BASEURL
			+ "driverWebservice/forgotPassword";
	/* changed */
	public static String PrivacyPolicyURL = BASEURL + "driverServices/privacy";

	/* changed */
	public static String RulesandRegulationsURL = BASEURL
			+ "driverServices/rulesRegulations";

	/* changed */
	public static String ContractorServiceURL = BASEURL
			+ "driverServices/contract";

	/* changed */
	public static String CheckTripUrl = BASEURL + "driverServices/checkTripV1";

	/* changed */
	public static String BookingIdUrl = BASEURL
			+ "driverServices/driverInfo?driverId=";

	/* changed */
	public static String DriverCurStatusUrl = BASEURL
			+ "driverServices/driverCurStatus";

	/* changed */
	public static String FareReviewURL = BASEURL + "driverServices/fareReview";

	/* changed */
	public static String RiderRatingURL = BASEURL
			+ "driverServices/riderRating";

	/* changed */

	public static String LatLongUpdateWithDistanceURL = BASEURL
			+ "driverServices/latLongUpdateWithDistance";

	/* changed */
	public static String ArrivedUrl = BASEURL + "driverServices/arrived";

	/* changed */
	public static String EndTripUrl = BASEURL + "driverServices/endTripV1";

	/* changed */
	public static String DriverLogoutUrl = BASEURL
			+ "driverServices/driverLogoutV1";
	/* changed */
	public static String ChangeVehicleURl = BASEURL
			+ "driverServices/changeVehicleV1";
	/* changed */
	public static String TaxiListUrl = BASEURL + "driverServices/getTaxiList";
	/* changed */
	public static String MyProfileDetailsUrl = BASEURL
			+ "driverServices/driverDetail";

	/* changed */
	public static String MyTripListUrl = BASEURL + "driverServices/tripList";

	/* changed */
	public static String UpdateProfileImageUrl = BASEURL
			+ "driverServices/editImageV1";

	// public static String DriverStatusOnlineUrl =
	// "http://taxiex.netsmartz.us/testdriverWebservice/driverStatusOnline";

	/* changed */
	public static String DriverStatusOnlineUrl = BASEURL
			+ "driverServices/driverStatusOnline";

	/* changed */
	public static String DriverStatusOfflineUrl = BASEURL
			+ "driverServices/driverStatusOffline";

	/* changed */
	public static String StartTripUrl = BASEURL + "driverServices/startTrip";

	/* changed */
	public static String AcceptTripUrl = BASEURL
			+ "driverServices/acceptBooking";

	/* changed */
	public static String BookingDetails = BASEURL
			+ "driverServices/BookingDetail";

	/* changed */
	/*public static String CancelBookinfUrl = BASEURL
			+ "driverServices/cancelBookingV2";*/

    public static String CancelBookinfUrl = BASEURL
            + "driverServices/cancelBooking";

	public static String ReservationsList = BASEURL
			+ "reservationservices/driverReservations";

	public static String ReservationDetails = BASEURL
			+ "reservationservices/reservationDetail";

	public static String ReservationConfirm = BASEURL
			+ "reservationservices/acceptResBookingV1?reservationId=";

	public static String ReservationMarkAsComplete = BASEURL
			+ "reservationservices/markComplete?reservationId=";

	public static String ReservationOnSite = BASEURL
			+ "reservationservices/reservationOnsiteV1";
	
	
	public static String End_Trip_Fare=BASEURL+"driverServices/endTripFare";

	public static String[] DriverStatusXMLKeys = { "driverStatus", "driverId" };

	public static String[] DriverOnlineStatusXMLKeys = { "driverStatusOnline",
			"driverId", "deviceToken" };
	public static String[] CheckTripXMLKeys = { "checkTrip", "driverId" , "deviceToken" };
	public static String[] DriverCurStatusXMLKeys = { "driverCurStatus",
			"driverId" };
	public static String[] FareReviewXMLKeys = { "fareReview", "bookingId",
			"driverId", "riderId", "type" };
	public static String[] RiderRatinXMLKeys = { "riderRating", "bookingId",
			"driverId", "riderId", "rating", "comment" };
	public static String[] LatLongUpdateWithDistanceXMLKeys = { "driverStatus",
			"driverId", "latitdue", "longitude", "bookingId", "distance","bearingDegree","bearingWR" };
	public static String[] ArrivedXMLKeys = { "arrived", "driverId",
			"bookingId" };
	public static String[] EndTripFareKeys= { "endTrip", "driverId", "riderId",
		"bookingId", "paymentMode","toll","other" };
	public static String[] EndTripXMLKeys = { "endTrip", "driverId", "riderId",
		"bookingId", "paymentMode","toll","other" };
	public static String[] ChangeVehicleXMLKeys = { "changeVehicle",
			"driverId", "vehicleId" ,"deviceToken"};
	public static String[] TaxiListXMLKeys = { "getTaxiList", "driverId" };
	public static String[] LogoutXMlKeys = { "driverLogout", "driverId","deviceToken" };

	public static String[] MyProfileXMLKeys = { "driverDetail", "driverId" };
	public static String[] MyTripListXMLKeys = { "tripList", "driverId" };
	public static String[] UpdateProfileImageXmlKeys = { "editImage", "userId",
			"userImage","deviceToken" };
	public static String[] StartTripXMLKeys = { "startTrip", "driverId",
			"riderId", "latitude", "longitude", "oldLatitude", "oldLongitude",
			"bookingId" };
	public static String[] AcceptBookingXMLKeys = { "acceptBooking",
			"driverId", "bookingId" };
	public static String[] BookingDetailsXMLKeys = { "BookingDetail",
			"driverId", "bookingId" };

	public static String[] CancelBookingXMLKeys = { "cancelBooking",
			"driverId", "bookingId", "afterAccepting", "cancellationType",
			"cancellationBit" };

	public static String[] ReservationDetailsXMLKeys = { "reservationList",
			"reservationId" };
	public static String[] ReservationListXMLKeys = { "reservationList",
			"driverId", "reservationStatus" };
	public static String[] ReservationOnSiteKeys = { "reservationOnsite",
			"reservationId" , "deviceToken" };
	public static final int LoginWebserviceResponse = 1001;
	public static final int ForgotPasswordWebserviceResponse = 1002;
	public static final int CheckTripWebserviceResponse = 1003;
	public static final int DriverCurStatusWebserviceResponse = 1004;
	public static final int FareReviewWebserviceResponse = 1005;
	public static final int RiderRatingWebserviceResponse = 1006;
	public static final int LatLongUpdateWebserviceResponse = 1007;
	public static final int ArrivedWebserviceResponse = 1008;
	public static final int EndTripWebserviceResponse = 1009;
	
	
	public static final int TaxiListWebserviceResponse = 1010;
	public static final int LogoutWebserviceResponse = 1011;
	public static final int ChangeVehicleWebserviceResponse = 1012;
	public static final int MyProfileWebserviceResponse = 1013;
	public static final int MyTipListWebServiceResponse = 1014;
	public static final int BookingIdWebServiceResponse = 1015;
	public static final int UpdateProfileImageWebserviceResponse = 1016;
	public static final int DriverStatusWebserviceResponse = 1017;
	public static final int StarTripWebServiceResponse = 1018;
	public static final int AcceptBookingWebserviceResponse = 1019;
	public static final int BookingDetailsWebserviceResponse = 1020;
	public static final int CancelBookingWebserviceResponse = 1021;
	public static final int CancelBookingHeaderWebserviceRedponse = 1022;
	public static final int EndTripFareWebserviceResponse = 1023;

	public static final int VehicleListParsingResponseId = 2001;
	public static final int MyProfileParsingResponseId = 2002;
	public static final int MyTripListParsingResponseId = 2003;
	public static final int BookingIdParsingid = 2004;
	public static final int UpdateProfileImageParsingResponseId = 2005;
	public static final int DriverStatusParsingResponse = 2006;
	public static final int StartTripParsingResponse = 2007;
	public static final int AcceptBookingParsingResponse = 2008;
	public static final int BookingDetailsParsingResponse = 2009;
	public static final int CancelBookingParsingResponse = 2010;

	public static final int ReservationListResponse = 3001;
	public static final int ReservationDetailsResponse = 3002;
	public static final int ReservationConfirmResponse = 3003;
	public static final int ReservationMarkResponse = 3004;
	public static final int ReservationOnSiteResponse = 3005;
	public static final String VehicleListArrayKey = "Vehicle list";
	public static final String TripListArrayKey = "Trip List";

	public static final String ReservationListArrayKey = "Reservation List";

	public static final String VehicleChangedBundleKey = "Vehicle Changed Bundle Key";
	public static final String FirsTimeLoginBundleKey = "Is First Time Login";
	public static final String TermsConditionsBundleKey = "TermsType";
	public static final String BookingDetailsBundleKey = "bookind Details";
	public static final String BookingTipBundleKey = "booking Tip";
	public static final String BookingDateBundleKey = "booking date";
	public static final String IsFromEndTrip = "isFromEndTrip";
	public static final String PaymentMode = "payment mode";

	public static final String UserDiscount = "userDiscount";
	public static final String PaymentAmount = "paymentAmt";

	public static final String SharedPreferenceName = "TAXIEX";
	public static final String SharedPreferenceName1 = "TAXIEX1";
	public static final String Pref_GCM_key = "TaxiexGCMKey";
	public static final String Pref_selected_vehicle = "selected vehicle";
	public static final String Pref_isOnline = "IsDriverOnline";
	public static final String Pref_Login_Details = "Login Details";
	public static final String pref_Booking_details = "Booking Details";
	public static final String Pref_is_login = "Is Login";
	public static final String Pref_login_username = "Login username";
	public static final String Pref_old_latitude = "latitute";
	public static final String Pref_old_longitude = "longitude";
	public static final String Pref_login_userid = "Login userId";

	public static final String Pref_isUserOnLine = "IsUserOnline";

	public static final String Pref_isFIrstTime = "ISFirstTime";

	/*
	 * public static final String CAR_1="Standard"; public static final String
	 * CAR_2="XL"; public static final String CAR_3="Luxury"; public static
	 * final String CAR_4="MiniMove";
	 */




	public static final String CAR_1 = "X";
	public static final String CAR_2 = "XL";
	public static final String CAR_3 = "Luxury";
	public static final String CAR_4 = "SUV";
	public static final String CAR_5 = "LUX";
	
	
	public static final boolean IS_RESERVATION_AVAILABLE=true;

	public static final String INTENT_TAG = "intent_tag";
	public static final String RESERVATION_TO_ONLINE = "reservation_to_online";
	public static final String BOOKING_ID = "bookingId";
	public static final String PICK_UP_ADDRESS = "pick_up_address";
	public static final String DROPP_OFF_ADDRESS = "drop_off_address";
	public static final String PICK_UP_LAT = "pick_up_lat";
	public static final String PICK_UP_LONG = "pick_up_long";

	public static final String USER_NAME_INTENT = "userNameIntent";
	public static final String USER_PASSWORD_INTENT = "userPasswordIntent";

}
