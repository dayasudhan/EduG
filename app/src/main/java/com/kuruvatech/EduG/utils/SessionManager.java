package com.kuruvatech.EduG.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;
	
	// Editor for Shared preferences
	Editor editor;
	
	// Context
	Context _context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;
	//ArrayList<FeedItem> feedList;
	// Sharedpref file name
	public static final String PREF_NAME = "ElectionCandidate";
	
	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";
	
	// User name (make variable public to access from outside)
	public static final String KEY_NAME = "name";
	
	// Email address (make variable public to access from outside)
	public static final String KEY_EMAIL = "email";

	public static final String KEY_PHONE = "phone";
	public static final String KEY_AREANAME = "areaname";
	public static final String KEY_LANDMARK = "landmark	";
	public static final String KEY_ADDRESSLINE1 = "addressline1";
	public static final String KEY_ADDRESSLINE2 = "addressline2";
	public static final String KEY_CITY = "city";

	public static final String KEY_FAVOURITE_ADDRESS = "favourite_address";
	//To store the firebase id in shared preferences
	public static final String KEY_ORDER_ID = "orderid";

	//To store the firebase id in shared preferences
	public static final String KEY_ORDER_ID_LIST = "orderidlist";

	//To store the firebase id in shared preferences
	public static final String KEY_LAST_AREA_SERCHED = "lastareasearched";

	public static final String KEY_SLIDER_LOGO = "slider";
	public static final String KEY_LAST_PN = "last_pn";


	public static final String KEY_FEED_NEWS = "NEWS_FEED";
	public static final String KEY_FEED_IMAGES = "IMAGES_FEED";
	public static final String KEY_FEED_VIDEOS = "VIDEOS_FEED";
	public static final String KEY_FEED_MANIFESTO = "MANIFESTO_FEED";

	// Constructor
	public  boolean hasAddress=false;

	public boolean isHasAddress() {
		return hasAddress;
	}
	public void setHasAddress(Boolean hasAddress){
		this.hasAddress=hasAddress;
	}

	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

//	public List<String> getSlider()
//	{
//		String orders = pref.getString(KEY_SLIDER_LOGO, null);
//		List<String> list = null;
//		if(orders != null) {
//			Gson gson = new Gson();
//			list = (List<String>) gson.fromJson(orders, Object.class);
//		}
//		else
//		{
//			list = new ArrayList<String>();
//			list.add(Constants.SLIDER_URL1);
//			list.add(Constants.SLIDER_URL2);
//			list.add(Constants.SLIDER_URL3);
//			list.add(Constants.SLIDER_URL4);
//			list.add(Constants.SLIDER_URL5);
//			list.add(Constants.SLIDER_URL6);
//		}
//		return list;
//	}
	/**
	 * Create login session
	 * */
	public void createLoginSession(String name, String phone, String email){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_PHONE,phone);
//		// Storing name in pref
		editor.putString(KEY_NAME, name);
//
//		// Storing email in pref
		editor.putString(KEY_EMAIL, email);


//
//		//Storing the unique id
//		editor.putString(Constants.UNIQUE_ID, uniqueId);
		// commit changes
		editor.commit();

	}
	public void setlastareasearched(String orderId)
	{
		editor.putString(KEY_LAST_AREA_SERCHED,orderId);
		editor.commit();
	}
	public String getlastareasearched()
	{
		String id = pref.getString(KEY_LAST_AREA_SERCHED, "");
		return id;
	}
	public void setlastpn(String msg)
	{
		editor.putString(KEY_LAST_PN,msg);
		editor.commit();
	}
	public String getlastpn()
	{
		String id = pref.getString(KEY_LAST_PN, "");
		return id;
	}
	public void setKeyPhone(String orderId)
	{
		editor.putString(KEY_PHONE,orderId);
		editor.commit();
	}
	public String getKeyPhone()
	{
		String id = pref.getString(KEY_PHONE, null);
		return id;
	}
//gagan
	public void setEmail(String orderId)
	{
		editor.putString(KEY_EMAIL,orderId);
		editor.commit();
	}

	public String getEmail()
	{
		String id = pref.getString(KEY_EMAIL, null);
		return id;
	}

	public void setAddress(String areaname, String landmark, String addressline1 , String addressline2, String city)
	{
		editor.putString(KEY_AREANAME,areaname);
		editor.putString(KEY_LANDMARK,landmark);
		editor.putString(KEY_ADDRESSLINE1,addressline1);
		editor.putString(KEY_ADDRESSLINE2,addressline2);
		editor.putString(KEY_CITY,city);
		editor.commit();
	}

	public void setCurrentOrderId(String orderId)
	{
		editor.putString(KEY_ORDER_ID,orderId);
		editor.commit();
		addOrderId(orderId);
	}
	public String getCurrentOrderId()
	{
		String id = pref.getString(KEY_ORDER_ID, null);
		return id;

	}
	public void setOrderIdList(List<String> list)
	{
		Gson gson = new Gson();
		String json = gson.toJson(list);
		editor.putString(KEY_ORDER_ID_LIST,json);
		editor.commit();
	}

	public void addOrderId(String orderid)
	{
		String orders = pref.getString(KEY_ORDER_ID_LIST, null);

		Gson gson = new Gson();
		List<String> list = null;
		if(orders != null) {
			list = (List<String>) gson.fromJson(orders, Object.class);
		}
		else
		{
			list = new ArrayList<String>();
		}

		list.add(0,orderid);
		if(list.size()> 10)
		{
			list.remove(10);
		}
		setOrderIdList(list);
	}
	public List<String> getOrderIdList()
	{
		String orders = pref.getString(KEY_ORDER_ID_LIST, null);
		List<String> list = null;
		if(orders != null) {
			Gson gson = new Gson();
			list = (List<String>) gson.fromJson(orders, Object.class);
		}
		return list;
	}
	public void setSlider(List<String> list)
	{
		Gson gson = new Gson();
		String json = gson.toJson(list);
		editor.putString(KEY_SLIDER_LOGO,json);
		editor.commit();
	}


	public void commiting(){
		editor.commit();
	}



	public void clearAddress()
	{
		editor.putString(KEY_FAVOURITE_ADDRESS, null);
		editor.commit();
	}




	public void setLastNewsFeed(String feedList)
	{
		editor.putString(KEY_FEED_NEWS,null);
		editor.putString(KEY_FEED_NEWS,feedList);
		editor.commit();
	}
	public String getLastNewsFeed()
	{
		String list = pref.getString(KEY_FEED_NEWS, null);
		return list;
	}

	public void setLastImagesFeed(String feedList)
	{
		editor.putString(KEY_FEED_IMAGES,null);
		editor.putString(KEY_FEED_IMAGES,feedList);
		editor.commit();
	}
	public String getLastImagesFeed()
	{
		String list = pref.getString(KEY_FEED_IMAGES, null);
		return list;
	}
	public void setLastVideoFeed(String feedList)
	{
		editor.putString(KEY_FEED_VIDEOS,null);
		editor.putString(KEY_FEED_VIDEOS,feedList);
		editor.commit();
	}
	public String getLastVideoFeed()
	{
		String list = pref.getString(KEY_FEED_VIDEOS, null);
		return list;
	}
	public void setManifestFeed(String feedList)
	{
		editor.putString(KEY_FEED_MANIFESTO,null);
		editor.putString(KEY_FEED_MANIFESTO,feedList);
		editor.commit();
	}
	public String getManifestoFeed()
	{
		String list = pref.getString(KEY_FEED_MANIFESTO, null);
		return list;
	}
	public void setName(String orderId)
	{
		editor.putString(KEY_NAME,orderId);
		editor.commit();

	}
	public String getName()
	{
		String id = pref.getString(KEY_NAME, null);
		return id;

	}
	public String getCustomer()
	{
		String id = pref.getString(KEY_PHONE, null);
		return id;
	}

	
	
	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));
		
		// user email id
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		
		// return user
		return user;
	}

	
	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}
}
