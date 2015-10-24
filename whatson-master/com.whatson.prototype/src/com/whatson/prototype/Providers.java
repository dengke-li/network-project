package com.whatson.prototype;

import java.util.LinkedList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.whatson.prototype.resultTypes.Content;
import com.whatson.prototype.resultTypes.TextContent;

public class Providers extends ActivityModel {

	private TextView caption;
	private ListView list;
	
	public static int selectedProvider;

	
	public static String[] contentsArray = {"Le Monde","Spotify","Netflix"};
	public static Integer[] providerId = {R.drawable.newspaper0, R.drawable.spotify,R.drawable.playpause};
	
	public static LinkedList<Content> contents = null;
	public static final String CONTENT = "content";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(MainActivity.connectedToken == null){
			Intent intent = new Intent(Providers.this,MainActivity.class);
			startActivity(intent);
		}
		else{
			setContentView(R.layout.providers);
			caption = (TextView) findViewById(R.id.caption);
			caption.setText("Choose the external data provider");
			
			//display the custom list
			ProviderCustomList adapter = new ProviderCustomList(Providers.this, contentsArray, providerId);
			list=(ListView)findViewById(R.id.listproviders);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					//TODO ajouter dans une variable statique le choix du provider qui a été fait
					selectedProvider = position;

					loadImages();
				}
			});
		}
	}
	
}
