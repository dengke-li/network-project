package com.whatson.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Accueil extends ActivityModel{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accueil);

		try {
			Thread.sleep(3000); 
			Intent intent = new Intent(Accueil.this,MainActivity.class);
			startActivity(intent);
			
		}
		catch (InterruptedException e) {
			Log.d("accueil error",e.getMessage());
		}
	}
}
