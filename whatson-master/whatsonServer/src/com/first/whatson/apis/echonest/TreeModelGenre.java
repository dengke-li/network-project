package com.first.whatson.apis.echonest;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Model to extract a list of music genres in EchoNest list of genre (from a json api response to java list of strings)
 * @author alice
 *
 */
public class TreeModelGenre {
	
	private List<String> genres = new ArrayList<String>();
	
   public List<String> getGenres() {
		return genres;
	}


	public void setGenres(List<String> genres) {
		this.genres = genres;
	}


public TreeModelGenre() throws IOException, JSONException {
	   String url = "http://developer.echonest.com/api/v4/genre/list?api_key=NG6LOLINXBHFJY9J6&format=json";
	   JavaGetUrl j = new JavaGetUrl(url);
       String genreJson = j.result;
       //System.out.println(genreJson);

       ObjectMapper mapper = new ObjectMapper();
       JsonNode node = mapper.readTree(genreJson);
       JsonNode subNodes = mapper.readTree(node.getFields().next().getValue().toString());
       //System.out.println(subNodes.get("genres"));
       
       String json = mapper.writeValueAsString(subNodes.get("genres"));
       //System.out.println(json);
       
       this.genres = arrayToList(json);
   }

   
	private static List<String> arrayToList(String jsonString) throws JSONException {
		List<String> list = new ArrayList<String>();
		JSONArray array = new JSONArray(jsonString);
	    JSONObject jobj = new JSONObject();

		for (int i=0;i<array.length();i++){
			jobj = (JSONObject) array.get(i);
			list.add(jobj.getString("name").toString());
		}
			
		return list;
	}
}