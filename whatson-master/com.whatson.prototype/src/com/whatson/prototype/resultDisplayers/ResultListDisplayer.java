package com.whatson.prototype.resultDisplayers;

import java.io.IOException;
import java.io.InputStream;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.whatson.prototype.ActivityModel;
import com.whatson.prototype.MainActivity;
import com.whatson.prototype.Providers;
import com.whatson.prototype.R;
import com.whatson.prototype.R.drawable;
import com.whatson.prototype.R.id;
import com.whatson.prototype.R.layout;
import com.whatson.prototype.resultTypes.Content;
import com.whatson.prototype.resultTypes.TextContent;
import com.whatson.prototype.serverCalls.SimpleHttpClient;

/**
 * Activity that displays the list of results of matching external contents
 * 
 *
 */
public class ResultListDisplayer extends ActivityModel {
	
	private TextView caption;
	private ListView list;
	
	String[] contentsArray;
	Integer[] imageId;
	public static LinkedList<Content> contents = null;
	public static final String CONTENT = "content";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		caption = (TextView) findViewById(R.id.caption);
		//fill contents list with good matching content and display the matching list
		loadResultMatchingContent();
		
	}
	
	/**
	 * Compute and match the current user mood with extern content
	 * Construct the API request to get JSON and parse it 
	 */
	private void loadResultMatchingContent() {
		try{
			new LongRunningGetIO().execute(SimpleHttpClient.SERVERADDRESS+"rest/WhatsonServicesProvider/matchingContent/"+MainActivity.connectedToken+"/"+Providers.selectedProvider);
		}
		catch (Exception e) {
			toast("Couldn't get the user current emotional JSON data.");
		}		
	}


	/**
	 * REST Calling processus for user current emotional state : 
	 * - get JSON,
	 * - extract emotions vector
	 * - display emotions with their intensity
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
				caption.setText("Suggestions list:");
				//toast(results);
				
				//extract Images Object from json result
				try {
					contents = extractContentsFromJSON(results);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				//display current images
				if(contents!=null){
						displayCurrentEmotions();
				}
				else{
					Log.e("Display result list", "Couldn't get user current emotions");
				}
			}
			else{
				caption.setText("No emotions to load, you might no longer be connected.");
				logout();
			}
		}

		/**
		 * Extract java Emotions from JSON answer to current user data REST call
		 * @param results
		 * @return
		 * @throws JSONException
		 */
		@SuppressLint("NewApi")
		private LinkedList<Content> extractContentsFromJSON(String results) throws JSONException {
			
			
			Log.d("json contents",results);
			LinkedList<Content> contentsList = new LinkedList<Content>();
			//Log.d("json result",results);
			JSONArray jarray = new JSONArray(results);
			contentsList = new LinkedList<Content>();
			JSONObject jobj;
			String type;
			int id;
			if(jarray.length()>0){
				for(int i=0;i<jarray.length();i++){
					jobj = jarray.getJSONObject(i);
					type = (String) jobj.get("type");
					id = jobj.getInt("id");
					
					//case external content is text article
					if(type.equals("text")){
						String url = (String) jobj.get("url");
						String title = (String) jobj.get("title");
						String source = (String) jobj.get("source");
						String author = (String) jobj.get("author");
						contentsList.add(new TextContent(id,title,url,source,author));
					}
				}
			}
			return contentsList;
		}

		/**
		 * Display remote emotions with their id in the listresult
		 */
		private void displayCurrentEmotions() {
			int length = contents.size();
			//toast("length : "+length);
			contentsArray = new String[length];
			imageId = new Integer[length];
			for(int i=0;i<length;i++){
				TextContent tc = (TextContent) contents.get(i);
				contentsArray[i] = tc.getTitle()+"\n("+tc.getAuthor()+")";//+"\n("+tc.getSource()+")";//.get(i).get.getcurrentEmotions.get(i).getEmotion() + " " + String.valueOf(currentEmotions.get(i).getIntensity());
				imageId[i] = Providers.providerId[Providers.selectedProvider];//R.drawable.newspaper;
				//Log.d("emotion",contentsArray[i]);
			}
			

			//display the custom list
			CustomList adapter = new CustomList(ResultListDisplayer.this, contentsArray, imageId);
			list=(ListView)findViewById(R.id.listresult);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					//Toast.makeText(ResultDisplayer.this, "You Clicked at " +contentsArray[position], Toast.LENGTH_SHORT).show();
					Intent intent = null;
					switch(Providers.selectedProvider){
					case 0: intent = new Intent(ResultListDisplayer.this,TextContentDisplayer.class);
					break;
					case 1: intent = new Intent(ResultListDisplayer.this,VideoContentDisplayer.class);
					break;
					case 2: intent = new Intent(ResultListDisplayer.this,VideoContentDisplayer.class);
					break;
					}
					
					intent.putExtra(CONTENT, position+1);
					startActivity(intent);
				}
			});
		}
	}
}
