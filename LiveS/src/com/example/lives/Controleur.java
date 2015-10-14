package com.example.lives;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Controleur {

	private JSONArray sports;
	private JSONArray lives;
	private JSONArray departements;
	private JSONArray competitions;

	public Controleur() {
		sports = null;
		lives = null;
		departements = null;
		competitions = null;
	}

	public JSONObject addLive(String nomLive, String equipe1, String equipe2,
			String longitude, String altitude, String competitionId,
			String departementId, String sportId, String date,
			String shortdesc, String longdesc, String nomPerson)
			throws JSONException, UnsupportedEncodingException {
		HttpClient httpclient = new DefaultHttpClient();
		JSONObject object = new JSONObject();

		try {
			HttpPost httppost = new HttpPost(
					"http://live-score.sqli.cloudbees.net/livescore/live");
			ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
			String reponse = null;
			object.put("commentateur", nomPerson);
			object.put("nom", nomLive);
			object.put("equipe1", equipe1);
			object.put("equipe2", equipe2);
			object.put("competitionId", competitionId);
			object.put("departementId", departementId);
			object.put("latitude", altitude);
			object.put("longitude", longitude);
			object.put("shortDescription", shortdesc);
			object.put("longDescription", longdesc);
			object.put("debut", date);
			object.put("sportId", sportId);

			// UrlEncodedFormEntity entite = new UrlEncodedFormEntity(
			// parametres, "UTF-8");
			StringEntity stringEntity = new StringEntity(object.toString());
			httppost.setEntity(stringEntity);

			try {
				reponse = httpclient.execute(httppost, gestionnaire_reponse);
				object = new JSONObject(reponse);

			} catch (ClientProtocolException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

		return object;

	}

	public JSONArray allSports() throws JSONException {
		JSONObject jObject;
		JSONArray ja = null;
		HttpClient httpclient = new DefaultHttpClient();

		try {
			HttpGet httpget = new HttpGet(
					"http://live-score.sqli.cloudbees.net/livescore/sports");
			ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
			String reponse = null;

			try {
				reponse = httpclient.execute(httpget, gestionnaire_reponse);
				ja = new JSONArray(reponse);
			} catch (ClientProtocolException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

		return ja;

	}

	public JSONArray allCompetition() throws JSONException {
		JSONObject jObject;
		JSONArray ja = null;
		HttpClient httpclient = new DefaultHttpClient();

		try {
			HttpGet httpget = new HttpGet(
					"http://live-score.sqli.cloudbees.net/livescore/competitions");
			ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
			String reponse = null;

			try {
				reponse = httpclient.execute(httpget, gestionnaire_reponse);
				ja = new JSONArray(reponse);
			} catch (ClientProtocolException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

		return ja;

	}

	public JSONArray allLivesSportDepartement(String idSport,
			String idDepartement) throws JSONException {
		JSONObject jObject;
		JSONArray ja = null;
		HttpClient httpclient = new DefaultHttpClient();
		String Url = "http://live-score.sqli.cloudbees.net/livescore/livesByDepartementAndSport/"
				+ idDepartement + "/" + idSport;

		try {
			HttpGet httpget = new HttpGet(Url);
			ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
			String reponse = null;

			try {
				reponse = httpclient.execute(httpget, gestionnaire_reponse);
				ja = new JSONArray(reponse);
			} catch (ClientProtocolException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

		return ja;

	}

	public void updateScore(String score1, String score2, String id)
			throws JSONException, UnsupportedEncodingException {

		HttpClient httpclient = new DefaultHttpClient();
		String Url = "http://live-score.sqli.cloudbees.net/livescore/live/"
				+ id + "/score";

		try {
			HttpPut put = new HttpPut(Url);
			ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
			String reponse = null;
			JSONObject object = new JSONObject();
			object.put("scoreEquipe1", score1);
			object.put("scoreEquipe2", score2);
			StringEntity stringEntity = new StringEntity(object.toString());
			put.setEntity(stringEntity);
			try {
				reponse = httpclient.execute(put, gestionnaire_reponse);
				System.out.println(reponse);
			} catch (ClientProtocolException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

	public void updateCommentaire(String commentaire, String id)
			throws JSONException, UnsupportedEncodingException {

		HttpClient httpclient = new DefaultHttpClient();
		String Url = "http://live-score.sqli.cloudbees.net/livescore/live/"
				+ id + "/evenement";

		try {
			HttpPut put = new HttpPut(Url);
			ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
			String reponse = null;
			JSONObject object = new JSONObject();
			object.put("commentaire", commentaire);

			StringEntity stringEntity = new StringEntity(object.toString());
			put.setEntity(stringEntity);
			try {
				reponse = httpclient.execute(put, gestionnaire_reponse);
				System.out.println(reponse);
			} catch (ClientProtocolException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

	public JSONObject detailLive(String idlive) throws JSONException {
		JSONObject jObject = null;
		JSONArray ja = null;
		HttpClient httpclient = new DefaultHttpClient();
		String Url = "http://live-score.sqli.cloudbees.net/livescore/live/"
				+ idlive;

		try {
			HttpGet httpget = new HttpGet(Url);
			ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
			String reponse = null;

			try {
				reponse = httpclient.execute(httpget, gestionnaire_reponse);
				jObject = new JSONObject(reponse);
			} catch (ClientProtocolException e) {
				System.err.println(e);
				return jObject;
			} catch (IOException e) {
				
				System.err.println(e);
				return jObject;
			}
		} finally {
			httpclient.getConnectionManager().shutdown();
			return jObject;
		}

	}

	public JSONArray allLives() throws JSONException {
		JSONObject jObject;
		JSONArray ja = null;
		HttpClient httpclient = new DefaultHttpClient();

		try {
			HttpGet httpget = new HttpGet(
					"http://live-score.sqli.cloudbees.net/livescore/lives");
			ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
			String reponse = null;

			try {
				reponse = httpclient.execute(httpget, gestionnaire_reponse);
				ja = new JSONArray(reponse);
			} catch (ClientProtocolException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

		return ja;

	}

	public JSONArray allDepartements() throws JSONException {
		JSONArray ja = null;
		HttpClient httpclient = new DefaultHttpClient();

		try {
			HttpGet httpget = new HttpGet(
					"http://live-score.sqli.cloudbees.net/livescore/departements");
			ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
			String reponse = null;

			try {
				reponse = httpclient.execute(httpget, gestionnaire_reponse);
				ja = new JSONArray(reponse);
			} catch (ClientProtocolException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

		return ja;

	}

	public void DeleteLive(String id) throws JSONException {
		HttpClient httpclient = new DefaultHttpClient();
		String Url = "http://live-score.sqli.cloudbees.net/livescore/live/"
				+ id;

		try {
			HttpDelete del = new HttpDelete(Url);
			ResponseHandler<String> gestionnaire_reponse = new BasicResponseHandler();
			String reponse = null;
			try {
				reponse = httpclient.execute(del, gestionnaire_reponse);
				System.out.println(reponse);
			} catch (ClientProtocolException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

	public String toString() {
		String s = "les sports sont :" + sports;
		return s;

	}

}
