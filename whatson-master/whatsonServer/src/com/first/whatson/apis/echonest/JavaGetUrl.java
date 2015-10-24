package com.first.whatson.apis.echonest;

//------------------------------------------------------------//
//JavaGetUrl.java:                                          //
//------------------------------------------------------------//
//A Java program that demonstrates a procedure that can be  //
//used to download the contents of a specified URL.         //
//------------------------------------------------------------//
//Code created by Developer's Daily                         //
//http://www.DevDaily.com                                   //
//------------------------------------------------------------//

import java.io.*;
import java.net.*;

public class JavaGetUrl {

	public String result="";
	
	public JavaGetUrl(String url){
		URL u;
		InputStream is = null;
		DataInputStream dis;
		String s;
		try {
			u = new URL(url);
			is = u.openStream();
			dis = new DataInputStream(new BufferedInputStream(is));
			while ((s = dis.readLine()) != null) {
				result+=s;
			}
		} catch (MalformedURLException mue) {
			System.out.println("Ouch - a MalformedURLException happened.");
			mue.printStackTrace();
			System.exit(1);
		} catch (IOException ioe) {
			System.out.println("Oops- an IOException happened.");
			ioe.printStackTrace();
			System.exit(1);
		} finally {
			try {
				is.close();
			} catch (IOException ioe) {			}
		} 
	} 
}