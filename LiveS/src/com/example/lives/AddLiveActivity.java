package com.example.lives;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Service.Live;
import Service.LivresBDD;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("NewApi")
public class AddLiveActivity extends Activity {
	private Spinner spinner1, spinner2, spinner3;
	private Button button;
	private int myYear, myMonth, myDay;
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
	JSONObject live = null;

	// JSON Node names

	private static final String TAG_lib = "libelle";
	JSONArray sport = null;
	JSONArray departement = null;
	JSONArray competition = null;
	String longitude, laltitude;
	LivresBDD liveBdd = new LivresBDD(this);
	List<String> listSportName = new ArrayList<String>();
	List<String> listSportId = new ArrayList<String>();
	List<String> listCompetitionName = new ArrayList<String>();
	List<String> listCompetitionId = new ArrayList<String>();

	List<String> listDepartementName = new ArrayList<String>();
	List<String> listDepartementId = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_live);
		

		listCompetitionName.add("Autres");
		listCompetitionId.add("-1");
		getList();
		getList2();
		getList3();
		addItemsOnSpinner1(listSportName);
		addItemsOnSpinner2(listDepartementName);
		addItemsOnSpinner3(listCompetitionName);
		addListenerOnButton();

	}

	public void getList2() {

		Controleur c = new Controleur();
		try {
			// Getting Array of Contacts
			departement = c.allDepartements();

			// looping through All Contacts
			for (int i = 0; i < departement.length(); i++) {
				JSONObject l = departement.optJSONObject(i);

				// Storing each json item in variable
				String id = l.getString(TAG_ID);
				String name = l.getString(TAG_NOM);

				// adding each child node to HashMap key => value

				listDepartementName.add(name);
				listDepartementId.add(id);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void getList3() {

		Controleur c = new Controleur();
		try {
			// Getting Array of Contacts
			competition = c.allCompetition();

			// looping through All Contacts
			for (int i = 0; i < competition.length(); i++) {
				JSONObject l = competition.optJSONObject(i);

				// Storing each json item in variable
				String id = l.getString(TAG_ID);
				String name = l.getString(TAG_lib);
				JSONObject sport = new JSONObject(l.getString(TAG_sport));
				String sprt = sport.getString("nom");
				name = name + " ( Sport: " + sprt + ")";
				// adding each child node to HashMap key => value

				listCompetitionId.add(id);
				listCompetitionName.add(name);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void getList() {

		Controleur c = new Controleur();
		try {
			// Getting Array of Contacts
			sport = c.allSports();

			// looping through All Contacts
			for (int i = 0; i < sport.length(); i++) {
				JSONObject l = sport.optJSONObject(i);

				// Storing each json item in variable
				String id = l.getString(TAG_ID);
				String name = l.getString(TAG_NOM);

				// adding each child node to HashMap key => value

				listSportId.add(id);
				listSportName.add(name);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// add items into spinner dynamically
	public void addItemsOnSpinner2(List<String> s) {

		spinner2 = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, s);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dataAdapter);
	}

	// add items into spinner dynamically
	public void addItemsOnSpinner1(List<String> s) {

		spinner1 = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, s);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(dataAdapter);
	}

	public void addItemsOnSpinner3(List<String> s) {

		spinner3 = (Spinner) findViewById(R.id.Spinner3);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, s);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner3.setAdapter(dataAdapter);
	}

	public void addListenerOnButton() {

		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		spinner3 = (Spinner) findViewById(R.id.Spinner3);

		button = (Button) findViewById(R.id.button);

		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				EditText e1 = (EditText) findViewById(R.id.NomLive);
				EditText e2 = (EditText) findViewById(R.id.equipe1);
				EditText e3 = (EditText) findViewById(R.id.equipe2);

				EditText e6 = (EditText) findViewById(R.id.longitude);
				EditText e7 = (EditText) findViewById(R.id.altitude);
				EditText e8 = (EditText) findViewById(R.id.shortDescription);
				EditText e9 = (EditText) findViewById(R.id.longDescription);
				EditText e10 = (EditText) findViewById(R.id.NomPerson);

				String nomLive = e1.getText().toString();
				String equipe1 = e2.getText().toString();
				String equipe2 = e3.getText().toString();

				longitude = e6.getText().toString();
				if (longitude.length() == 0) {
					longitude = "0";
				}
				laltitude = e7.getText().toString();
				if (laltitude.length() == 0) {
					laltitude = "0";
				}
				String shortdesc = e8.getText().toString();
				String longdesc = e9.getText().toString();
				String nomPerson = e10.getText().toString();
				final Calendar c = Calendar.getInstance();
				myYear = c.get(Calendar.YEAR);
				myMonth = c.get(Calendar.MONTH) + 1;
				myDay = c.get(Calendar.DAY_OF_MONTH);
				String date = myYear + "-" + myMonth + "-" + myDay;
				String sportId = listSportId.get(spinner1
						.getSelectedItemPosition());
				String depatementId = listDepartementId.get(spinner2
						.getSelectedItemPosition());
				String competitionId = listCompetitionId.get(spinner3
						.getSelectedItemPosition());
				if ((nomLive.length() > 0) && (equipe1.length() > 0)
						&& (equipe2.length() > 0) && (nomPerson.length() > 0)) {
					try {
						// Getting Array of Contacts
						Controleur c1 = new Controleur();

						// Getting Array of Contacts

						live = c1.addLive(nomLive, equipe1, equipe2, longitude,
								laltitude, competitionId, depatementId,
								sportId, date, shortdesc,longdesc , nomPerson);
						String id = live.getString(TAG_ID);
						if (Integer.parseInt(id) != -1) {
							String s = "Ajout du Live effectué avec succés ";
							Toast.makeText(AddLiveActivity.this, s,
									Toast.LENGTH_LONG).show();
							Live live = new Live(id, nomLive);

							// On ouvre la base de données pour écrire dedans
							liveBdd.open();
							// On insère le livre que l'on vient de créer
							liveBdd.insertLivre(live);
							liveBdd.close();
							Intent in = new Intent(getApplicationContext(),
									SeeLiveActivity.class);
							startActivity(in);
						} else {
							String s = "Echec de creation de live, verifiez vos entrees(deminuer la taille de vos texts)";
							Toast.makeText(AddLiveActivity.this, s,
									Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {

					String comment = "les champs nom  du Live,votre nom, equipe1 et equipe2"
							+ "sont obligatoires";
					Toast.makeText(AddLiveActivity.this, comment,
							Toast.LENGTH_LONG).show();

				}
				/*
				 * Toast.makeText( SearchLiveActivity.this, "Result : " +
				 * "\nSpinner 1 : " + listSportId.get(spinner1
				 * .getSelectedItemPosition()) + "\nSpinner 2 : " +
				 * listDepartementId.get(spinner2 .getSelectedItemPosition()),
				 * Toast.LENGTH_SHORT).show(); }
				 */

			}
		});

	}

	public void buttonExit(View v) {
		Intent in = new Intent(getApplicationContext(),
				ScrenHomeActivity.class);
		startActivity(in);
	}

}
