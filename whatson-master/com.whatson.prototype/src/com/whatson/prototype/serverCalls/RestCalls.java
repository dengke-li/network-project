package com.whatson.prototype.serverCalls;

import android.util.Log;


public class RestCalls {

	public static final String RESTPROVIDERPATTERN = SimpleHttpClient.SERVERADDRESS+"rest/WhatsonServicesProvider";
	
	private static String resp;
	private static String errorMsg;
	
	/**
	 * Get ids and images url of the next images to display in the discovery
	 * @param lastId
	 */
	public static void getNextImages(int lastId, int otherId){
		String requestURL = RESTPROVIDERPATTERN+"/next/json/"+lastId+"/"+otherId;
		String response = null;
		try {
			response = SimpleHttpClient.executeHttpGet(requestURL);
			String res = response.toString();
			resp = res.replaceAll("\\s+", "");
			Log.d("Rest next images",resp);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg = e.getMessage();
		}
		
	}
	
	/**
	 * Get current emotionnal profile of the user (vector of emotions intensites)
	 */
	public static void getEmotionnalProfile(){
		
	}
}
