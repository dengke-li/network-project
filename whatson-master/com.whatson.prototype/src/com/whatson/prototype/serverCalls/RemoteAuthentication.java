package com.whatson.prototype.serverCalls;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;


public class RemoteAuthentication {

	private String resp;
	private String errorMsg;
	private static final String CONNECTEDTOKEN = "connectedToken";
		
	public String getResp() {
		return resp;
	}
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * Authentication to log in the application
	 * @param email
	 * @param password
	 */
	public RemoteAuthentication(final String email,final String password){
		/** According with the new StrictGuard policy,
		 * running long tasks on the Main UI thread is not possible
		 * So creating new thread to create and execute http operations */
		new Thread(new Runnable() {
			@Override
			public void run() {
				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("username",email));
				postParameters.add(new BasicNameValuePair("password",password));
				String response = null;
				try {
					response = SimpleHttpClient.executeHttpPost(SimpleHttpClient.SERVERADDRESS+"login", postParameters);
					String res = response.toString();
					Log.d("login", res);
					resp = res.replaceAll("\\s+", "");
				} catch (Exception e) {
					e.printStackTrace();
					errorMsg = e.getMessage();
				}
			}
		}).start();	
	}
	
	
	/**
	 * Authentication to log out the application
	 * @param connectedToken
	 */
	public RemoteAuthentication(final String connectedToken){
		new Thread(new Runnable() {
			@Override
			public void run() {
				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair(CONNECTEDTOKEN,connectedToken));
				String response = null;
				try {
					response = SimpleHttpClient.executeHttpPost(SimpleHttpClient.SERVERADDRESS+"logout", postParameters);
					String res = response.toString();
					resp = res.replaceAll("\\s+", "");
				} catch (Exception e) {
					e.printStackTrace();
					errorMsg = e.getMessage();
				}
			}
		}).start();	
	}
}
