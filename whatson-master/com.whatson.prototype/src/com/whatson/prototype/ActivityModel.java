package com.whatson.prototype;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.whatson.prototype.serverCalls.RemoteAuthentication;

/**
 * Model of the app activities, managing menus in activities
 * 
 *
 */
public class ActivityModel extends ActionBarActivity{

	private Menu myMenu;
	
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		myMenu = menu;
		
		if(MainActivity.connectedToken!=null)
			myMenu.setGroupVisible(R.id.usermenuitem, true);	
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		int id = item.getItemId();
		switch(id){
		case R.id.action_settings:
			goToProviders();
			break;
		case R.id.restart:
			loadImages();
			break;
		case R.id.logout:
			logout();
			break;

		default:return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	
	private void goToProviders() {
		Log.d("menu", "try providers switching");
		Intent intent = new Intent(ActivityModel.this, Providers.class);
		startActivity(intent);
		
	}

	/**
	 * Make toast to inform about errors during runtime
	 * @param string
	 */
	protected void toast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
		Log.d("error",message);
	}

	/*
	 * ACTIONS TO PERFORM ON ACTION BAR ITEM SELECTED
	 */
	
	/**
	 * Load index layout because user is connected and we start a new discovery
	 */
	@SuppressLint("NewApi")
	protected void loadImages() {
		Intent intent = new Intent(ActivityModel.this,LoadImages.class);
		startActivity(intent);
	}
	
	/**
	 * Load the activity with the possible providers
	 */
	protected void loadProviders(){
		Intent intent = new Intent(ActivityModel.this,Providers.class);
		startActivity(intent);
	}

	
	/**
	 * Log out to disconnect from the application
	 */
	@SuppressLint("NewApi")
	protected void logout() {
		RemoteAuthentication ra = new RemoteAuthentication(MainActivity.connectedToken);
		
		try {
			/** wait a second to get response from server */
			Thread.sleep(1000);
			/** Inside the new thread we cannot update the main thread
			 *  So updating the main thread outside the new thread */					
			//if(Integer.valueOf(ra.getResp()) != MainActivity.CONNECTED){
				MainActivity.connectedToken = null;
				Intent intent = new Intent(ActivityModel.this,MainActivity.class);
				startActivity(intent);
//			}
//			toast(ra.getResp());
		} catch (Exception e) {
			toast(e.getMessage());
		}
	}

	
	
	
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		public PlaceholderFragment() {
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,false);
			return rootView;
		}
	}
}
