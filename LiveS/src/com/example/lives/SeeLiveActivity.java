package com.example.lives;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Service.Live;
import Service.LivresBDD;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SeeLiveActivity extends ListActivity {
	private TextView coucou = null;

	private static final String TAG_NOM = "nom";
	private static final String TAG_ID = "id";
	List<String> listLiveId = new ArrayList<String>();
	List<String> listLiveBaseId = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_see_live);

		ArrayList<HashMap<String, String>> liveList = new ArrayList<HashMap<String, String>>();

		LivresBDD livreBdd = new LivresBDD(this);
		// On ouvre la base de données pour écrire dedans
		livreBdd.open();
		// On insère le livre que l'on vient de créer

		List<Live> livreFromBdd = livreBdd.getLivreWithTitre();
		if (livreFromBdd != null) {

			for (Live live : livreFromBdd) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(TAG_NOM, live.getTitre());
				map.put(TAG_ID, live.getIsbn());
				listLiveId.add(live.getIsbn());
				listLiveBaseId.add(String.valueOf(live.getId()));
				liveList.add(map);

			}

			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new SimpleAdapter(this, liveList,
					R.layout.main3, new String[] { TAG_NOM },
					new int[] { R.id.name });

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
							DetailLiveUserActivity.class);
					in.putExtra("liveId", listLiveId.get(position));
					in.putExtra("listLiveBaseId", listLiveBaseId.get(position));

					Toast.makeText(SeeLiveActivity.this, "Detail du live",
							Toast.LENGTH_LONG).show();
					startActivity(in);

				}
			});
		} else {
			String comment = "Vous n'avez aucun live";
			Toast.makeText(SeeLiveActivity.this, comment, Toast.LENGTH_LONG)
					.show();

		}
		livreBdd.close();

	}

	public void buttonExit(View v) {
		Intent in = new Intent(getApplicationContext(), ScrenHomeActivity.class);
		startActivity(in);
	}

}
