package com.example.lives;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchLiveActivity extends Activity {
	private Spinner spinner1, spinner2, spinner3;
	private Button button;
	private int myYear, myMonth, myDay;
	private String Year="1900", Month="01", Day="01";

	static final int ID_DATEPICKER = 0;
	// JSON Node names
	private static final String TAG_NOM = "nom";
	private static final String TAG_ID = "id";
	private static final String TAG_lib = "libelle";
	private static final String TAG_sport = "sport";
	JSONArray sport = null;
	JSONArray departement = null;
	JSONArray competition = null;

	List<String> listSportName = new ArrayList<String>();
	List<String> listSportId = new ArrayList<String>();
	List<String> listCompetitionName = new ArrayList<String>();
	List<String> listCompetitionId = new ArrayList<String>();

	List<String> listDepartementName = new ArrayList<String>();
	List<String> listDepartementId = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_live); 
		Button datePickerButton = (Button) findViewById(R.id.datepickerbutton);
		datePickerButton.setOnClickListener(datePickerButtonOnClickListener);
		listCompetitionName.add("indifferent");
		listCompetitionId.add("-1");
		getList();
		getList2();
		getList3();
		addItemsOnSpinner1(listSportName);
		addItemsOnSpinner2(listDepartementName);
		addItemsOnSpinner3(listCompetitionName);

		addListenerOnButton();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_search_live, menu);
		return true;

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
				Intent in = new Intent(getApplicationContext(),
						ResulteSearchActivity.class);
				in.putExtra("sportId",
						listSportId.get(spinner1.getSelectedItemPosition()));
				in.putExtra("depatementId", listDepartementId.get(spinner2
						.getSelectedItemPosition()));
				in.putExtra("competitionId", listCompetitionId.get(spinner3
						.getSelectedItemPosition()));
				in.putExtra("year", Year);
				in.putExtra("month", Month);
				in.putExtra("day", Day);
				startActivity(in);
			}
			/*
			 * Toast.makeText( SearchLiveActivity.this, "Result : " +
			 * "\nSpinner 1 : " + listSportId.get(spinner1
			 * .getSelectedItemPosition()) + "\nSpinner 2 : " +
			 * listDepartementId.get(spinner2 .getSelectedItemPosition()),
			 * Toast.LENGTH_SHORT).show(); }
			 */

		});

	}

	public void buttonExit(View v) {
		Intent in = new Intent(getApplicationContext(),
				ScrenHomeActivity.class);
		startActivity(in);
	}

	private Button.OnClickListener datePickerButtonOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final Calendar c = Calendar.getInstance();
			myYear = c.get(Calendar.YEAR);
			myMonth = c.get(Calendar.MONTH);
			myDay = c.get(Calendar.DAY_OF_MONTH);
			showDialog(ID_DATEPICKER);
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case ID_DATEPICKER:
			
			return new DatePickerDialog(this, myDateSetListener, myYear,
					myMonth, myDay);
		default:
			return null;
		}
	}

	private DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Year = String.valueOf(year);
			Month = String.valueOf(monthOfYear + 1);
			Day = String.valueOf(dayOfMonth);
			// TODO Auto-generated method stub
			String date = "Annee: " + String.valueOf(year) + "\n" + "Mois: "
					+ String.valueOf(monthOfYear + 1) + "\n" + "Jour: "
					+ String.valueOf(dayOfMonth);
			Toast.makeText(SearchLiveActivity.this, date, Toast.LENGTH_LONG)
					.show();
		}
	};
}
