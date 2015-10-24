package com.whatson.prototype;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whatson.prototype.models.Image;
import com.whatson.prototype.resultDisplayers.ResultListDisplayer;
import com.whatson.prototype.serverCalls.SimpleHttpClient;

/**
 * Activity that loads images
 * 
 *
 */
public class LoadImages extends ActivityModel {

	public static LinkedList<Image> currentImages;
	private ImageView picture1, picture2;
	private TextView caption;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//to force connexion
		//connectionToken = null;

		if(MainActivity.connectedToken == null){
			Intent intent = new Intent(LoadImages.this,MainActivity.class);
			startActivity(intent);
		}
		else{
			setContentView(R.layout.index);
			startDiscovery();

			//declare new fields layout fields
			picture1 = (ImageView) findViewById(R.id.picture1);
			picture2 = (ImageView) findViewById(R.id.picture2);
			caption = (TextView)findViewById(R.id.caption);		
			caption.setText("Select the image reflecting your current emotion:");
		}
	}



	/**
	 * Load the first images to start the discovery with a first call to the API
	 * @param v
	 */
	private void startDiscovery() {
		loadImagesWithRequest("0","0");
	}



	/**
	 * Load next images on index layout (current discovery)
	 */
	public void nextImages(View v){
		ImageView imageView = (ImageView) v;
		String id = imageView.getContentDescription().toString();
		//Log.d("Image tag",id);
		//Log.d("Image tag",imageView.getId()+" "+R.id.picture1+" "+R.id.picture2);

		String otherId = null;
		if(imageView.getId() == R.id.picture1)
			otherId = picture2.getContentDescription().toString();
		else
			otherId = picture1.getContentDescription().toString();

		//get the 2 next images based on this id (Rest call)
		loadImagesWithRequest(id,otherId);
	}

	/**
	 * Compute the next images
	 * Construct the API request to get JSON and parse it 
	 * @param id1
	 * @param id2
	 */
	private void loadImagesWithRequest(String id1, String id2) {
		try{
			new LongRunningGetIO().execute(SimpleHttpClient.SERVERADDRESS+"rest/WhatsonServicesProvider/next/json/"+MainActivity.connectedToken+"/"+id1+"/"+id2);
		}
		catch (Exception e) {
			toast("Couldn't parse image description to integer ID");
		}		
	}



	/**
	 * REST Calling processus for next images : 
	 * - get JSON,
	 * - extract next images id and source,
	 * - download remote images,
	 * - display images
	 **/ 
	private class LongRunningGetIO extends AsyncTask <String, String, String> {
		protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
			InputStream in = entity.getContent();
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n>0) {
				byte[] b = new byte[4096];
				n =  in.read(b);
				if (n>0) out.append(new String(b, 0, n));
			}
			return out.toString();
		}

		@Override
		protected String doInBackground(String... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet(params[0]);
			String text = null;
			try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
				HttpEntity entity = response.getEntity();
				text = getASCIIContentFromEntity(entity);
			} catch (Exception e) {
				return e.getLocalizedMessage();
			}
			return text;
		}

		protected void onPostExecute(String results) {
			if (results!=null) {
				//caption.setText(results);
				//toast(results);

				//extract Images Object from json result
				try {
					currentImages = extractImagesFromJSON(results);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				//display current images
				if(currentImages!=null){
					if(currentImages.size()==2)
						displayCurrentImages();
				}
				else{
					displayResult();
					Log.e("Couldn't extract Java Object", "Go to result layout");
				}
			}
			else{
				caption.setText("No images to load, you might have been disconnected from the server, please re-connect you.");
				logout();
			}
		}

		private void displayResult() {
			Intent intent = new Intent(LoadImages.this,ResultListDisplayer.class);
			startActivity(intent);		
		}

		/**
		 * Extract java Image from JSON answer to next images REST call
		 * @param results
		 * @return
		 * @throws JSONException
		 */
		@SuppressLint("NewApi")
		private LinkedList<Image> extractImagesFromJSON(String results) throws JSONException {
			LinkedList<Image> imagesList = new LinkedList<Image>();
			JSONObject jobj = new JSONObject(results);
			Integer iteration =   (Integer) jobj.getJSONArray("iteration").get(0);
			if(iteration>=3)
				return null;
			JSONArray jarray = jobj.getJSONArray("nextImages").getJSONArray(0);
			int id;
			String source;
			for(int i=0;i<2;i++){
				JSONObject image = jarray.getJSONObject(i);
				id = Integer.valueOf((String) image.get("id"));
				source = (String) image.get("source");
				imagesList.add(new Image(id,source));
			}
			return imagesList;
		}

		/**
		 * Display remote next images with their id in ContentDescription in the application
		 */
		private void displayCurrentImages() {
			loadRemoteImage(picture1,currentImages.get(0));
			loadRemoteImage(picture2,currentImages.get(1));
		}

		/**
		 * Load remote image (from remote server) into an image view in the layout
		 * @param imageView
		 * @param imageURL
		 */
		private void loadRemoteImage(ImageView imageView,Image image){
			try {
				imageView.setImageBitmap(getBitmapFile(image.getSource()));
				imageView.setContentDescription(String.valueOf(image.getId()));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * Download a remote image
		 * @param url
		 * @return
		 */
		@SuppressLint("NewApi")
		public Bitmap getBitmapFile(String url)
		{
			url = SimpleHttpClient.SERVERIMAGES+url;
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = 
						new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
			Bitmap bmImg=null;
			URL myFileUrl;
			try {
				myFileUrl = new URL(url);
				HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
				bmImg = BitmapFactory.decodeStream(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bmImg;
		}
	}

}



