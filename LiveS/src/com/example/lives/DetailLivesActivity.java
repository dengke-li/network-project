package com.example.lives;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import Service.LivresBDD;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DetailLivesActivity extends ListActivity {
	// live JSONArray
	private static final String TAG_NOM = "nom";
	private static final String TAG_COM = "commentateur";
	private static final String TAG_EQ1 = "equipe1";
	private static final String TAG_EQ2 = "equipe2";
	private static final String TAG_SC1 = "scoreEquipe1";
	private static final String TAG_SC2 = "scoreEquipe2";
	private static final String TAG_DESCRIPTION = "longDescription";
	private static final String TAG_DATE = "dateDebut";
	private static final String TAG_ID = "id";
	private static final String TAG_SCORE = "score";
	private static final String TAG_latitude = "latitude";
	private static final String TAG_longitude = "longitude";
	private static final String TAG_competition = "competition";
	private static final String TAG_departement = "departement";
	private static final String TAG_sport = "sport";
	private static final String TAG_evenements = "evenements";
	private static final String TAG_commentaire = "commentaire";

	// contacts JSONArray
	JSONObject live = null;
	String liveId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_lives);
		// getting intent data
		Intent in = getIntent();

		// Get JSON values from previous intent
		 liveId = in.getStringExtra("liveId");
		// Hashmap for ListView
		ArrayList<HashMap<String, String>> evenementList = new ArrayList<HashMap<String, String>>();

		// Creating JSON Parser instance

		try {
			// Getting Array of Contacts
			Controleur c = new Controleur();

			// Getting Array of Contacts

			live = c.detailLive(liveId);
			if (live != null) {
			String id = live.getString(TAG_ID);
			
			TextView VdName = (TextView) findViewById(R.id.liveName);
			String name = live.getString(TAG_NOM);
			VdName.setText(name);
			
;

			String score1 = live.getString(TAG_SC1);
			String score2 = live.getString(TAG_SC2);
			String equipe1 = live.getString(TAG_EQ1);
			String equipe2 = live.getString(TAG_EQ2);
			String score ="   " +equipe1 + " " + score1 + "-" + score2 + " "
					+ equipe2;
			TextView leScore = (TextView) findViewById(R.id.leScore);
			leScore.setText(score);
			
			String longitude = live.getString(TAG_longitude);
			String latitude = live.getString(TAG_latitude);
			JSONObject departement = new JSONObject(
					live.getString(TAG_departement));
			String ville = " " + departement.getString("nom") + ":"
					+ departement.getString("code") + "(longitude: "
					+ longitude + " , latitude " + latitude + ")";
			String commentateur = live.getString(TAG_COM);
			JSONObject sport = new JSONObject(live.getString(TAG_sport));
			String sprt = sport.getString("nom");
			String deb = live.getString(TAG_DATE);
			String com = getCompetition(live);
			TextView info = (TextView) findViewById(R.id.information);
			String info1=" Match de : "+sprt+" - " +com+ "\n"
			+ville+"\n"+" Publie le:"+deb+", par: "+commentateur; 
			info.setText(info1);
			
			TextView Vdescription = (TextView) findViewById(R.id.longdescription);
			String description = live.getString(TAG_DESCRIPTION);
			Vdescription.setText(description);


			JSONArray evenements = new JSONArray(live.getString(TAG_evenements));
			for (int i = 0; i < evenements.length(); i++) {
				JSONObject l = evenements.optJSONObject(i);
				String commentaire = l.getString(TAG_commentaire);
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				// adding each child node to HashMap key => value
				map.put(TAG_commentaire, commentaire);

				// adding HashList to ArrayList
				evenementList.add(0,map);
			}
			}else {
				

				dialogReturn(DetailLivesActivity.this);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/**
		 * Updating parsed JSON data into ListView
		 * */
		ListAdapter adapter = new SimpleAdapter(this, evenementList,
				R.layout.main2, new String[] { TAG_commentaire },
				new int[] { R.id.evenement, });

		setListAdapter(adapter);
		final ListView lv = getListView();

		// Launching new screen on Selecting Single ListItem
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String text = ((TextView) view.findViewById(R.id.evenement)).getText().toString();

				Toast.makeText(DetailLivesActivity.this,
						text, Toast.LENGTH_LONG).show();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_lives, menu);
		return true;
	}

	public String getCompetition(JSONObject live) {
		String com = null;
		try {
			JSONObject competition = new JSONObject(
					live.getString(TAG_competition));
			com = competition.getString("id")
					+ competition.getString("libelle");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Inconnu";
		}
		return com;
	}
	public void dialogReturn(Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("Probleme de recuperation du live");
		builder.setTitle("Attention");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent in = new Intent(getApplicationContext(),
						ScrenHomeActivity.class);
				startActivity(in);

				// Getting Array of Contacts
			}
		});

		builder.create().show();
	}
	public void buttonMenu(View v) {
		Intent in = new Intent(getApplicationContext(),
				ScrenHomeActivity.class);
		startActivity(in);
	}
	public void buttonRaf(View v) {
		Intent in = new Intent(getApplicationContext(),
				DetailLivesActivity.class);
						
							in.putExtra("liveId", liveId);

							Toast.makeText(DetailLivesActivity.this,
									"Rafraichement en cours", Toast.LENGTH_LONG)
									.show();
							startActivity(in);
	
}
}
