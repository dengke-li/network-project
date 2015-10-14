package com.example.lives;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import android.app.ListActivity;
import android.content.Intent;
import android.util.MonthDisplayHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import android.widget.AdapterView.OnItemClickListener;

public class ResulteSearchActivity extends ListActivity {

	// contacts JSONArray
	private static final String TAG_NOM = "nom";
	private static final String TAG_COM = "commentateur";
	private static final String TAG_EQ1 = "equipe1";
	private static final String TAG_EQ2 = "equipe2";
	private static final String TAG_SC1 = "scoreEquipe1";
	private static final String TAG_SC2 = "scoreEquipe2";
	private static final String TAG_DESCRIPTION = "shortDescription";
	private static final String TAG_DATE = "dateDebut";
	private static final String TAG_ID = "id";
	private static final String TAG_SCORE = "score";
	private static final String TAG_competition = "competition";

	// contacts JSONArray
	JSONArray live = null;
	List<String> listLiveId = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resulte_search);

		Intent in = getIntent();

		String sportId = in.getStringExtra("sportId");
		String depatementId = in.getStringExtra("depatementId");
		String competitionId = in.getStringExtra("competitionId");
		String year = in.getStringExtra("year");
		String month = in.getStringExtra("month");
		String day = in.getStringExtra("day");
		String dat =year+"/"+month+"/"+day;
		Date tDate =new Date(dat);
		// Hashmap for ListView
		ArrayList<HashMap<String, String>> liveList = new ArrayList<HashMap<String, String>>();

		// Creating JSON Parser instance

		try {
			// Getting Array of Contacts
			Controleur c = new Controleur();

			// Getting Array of Contacts
			live = c.allLivesSportDepartement(sportId, depatementId);

			// looping through All Contacts
			for (int i = 0; i < live.length(); i++) {
				JSONObject l = live.optJSONObject(i);

				// Storing each json item in variable
				String id = l.getString(TAG_ID);
				String name = l.getString(TAG_NOM);
				String score1 = l.getString(TAG_SC1);
				String score2 = l.getString(TAG_SC2);

				String equipe1 = l.getString(TAG_EQ1);
				String equipe2 = l.getString(TAG_EQ2);

				String commentateur = l.getString(TAG_COM);
				String date = "debut : " + l.getString(TAG_DATE);
				Date tLiveDate =new Date(l.getString(TAG_DATE));
				String description = l.getString(TAG_DESCRIPTION);
				String score = equipe1 + " " + score1 + "-" + score2 + " "
						+ equipe2;
			
				String com =getCompetition(l);
				// creating new HashMap

				HashMap<String, String> map = new HashMap<String, String>();

				// adding each child node to HashMap key => value
				map.put(TAG_ID, id);
				listLiveId.add(id);

				map.put(TAG_NOM, name);
				map.put(TAG_SCORE, score);
				map.put(TAG_COM, commentateur);
				map.put(TAG_DESCRIPTION, description);
				map.put(TAG_DATE, date);
				// adding HashList to ArrayList
				if(! tDate.after(tLiveDate)){
				if (competitionId.equals("-1") || competitionId.equals(com)) {
					liveList.add(map);
				}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		/**
		 * Updating parsed JSON data into ListView
		 * */
		ListAdapter adapter = new SimpleAdapter(this, liveList, R.layout.main,
				new String[] { TAG_NOM, TAG_SCORE, TAG_DATE, TAG_DESCRIPTION },
				new int[] { R.id.name, R.id.score, R.id.dateDebut,
						R.id.description });

		setListAdapter(adapter);

		// selecting single ListView item
		final ListView lv = getListView();

		// Launching new screen on Selecting Single ListItem
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						DetailLivesActivity.class);
				in.putExtra("liveId", listLiveId.get(position));
				startActivity(in);

			}
		});

	}
	public 	String getCompetition(JSONObject live){
		String com=null;
		try {
			JSONObject competition = new JSONObject(
					live.getString(TAG_competition));
			 com = competition.getString("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		return com;
	}
}