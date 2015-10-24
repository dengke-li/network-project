package com.whatson.prototype;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.whatson.prototype.serverCalls.RemoteAuthentication;

/**
 * Connection activity (formular)
 * 
 *
 */
public class MainActivity extends ActivityModel {

	public static final int CONNECTED = 1;
	public static String connectedToken = null;
	
	EditText email, password;
	TextView error;
	Button login;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//to force connexion
		//connectionToken = null;
		
		if(connectedToken == null){
			setContentView(R.layout.activity_main);
			email = (EditText) findViewById(R.id.emailEdit);
			password = (EditText) findViewById(R.id.passwordEdit);
			login = (Button) findViewById(R.id.login);
			error = (TextView) findViewById(R.id.error);

			/**
			 * Listener to handle the connection (login) in the application
			 */
			login.setOnClickListener(new View.OnClickListener() {
				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {
					final String userEmail = email.getText().toString();
					final String pw = password.getText().toString();
					
					RemoteAuthentication ra = new RemoteAuthentication(userEmail,pw);
					
					//treat server response for authentication
					Log.d("before try to connect","poo");
					try {
						/** wait a second to get response from server */
						Thread.sleep(1000);
						/** Inside the new thread we cannot update the main thread
						 *  So updating the main thread outside the new thread */
						if(Integer.valueOf(ra.getResp().charAt(0))-48 == CONNECTED){

//						if(Integer.valueOf(ra.getResp()) == CONNECTED){
							connectedToken = userEmail;
							//loadImages();
							loadProviders();
						}
						//error.setText(ra.getResp());
						if (null != ra.getErrorMsg() && !ra.getErrorMsg().isEmpty()) {
							error.setText(ra.getErrorMsg());
						}
					} catch (Exception e) {
						error.setText(e.getMessage());
					}
				}
			});

		}
		//if the user is connected:load index main menu layout to start discovery
		else{
			//loadImages();
			loadProviders();
		}
	}
}



