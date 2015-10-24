package com.first.whatson.apis.echonest;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.echonest.api.v4.Song;

/**
 * Model to extract a list of songs in EchoNest to get spotify id of each track in a playlist (from a json api response to java list of strings)
 * @author alice
 *
 */
public class TreeModelPlaylist {
	
	private List<String> idTracks = new ArrayList<String>();
	
   public List<String> getIdTracks() {
		return idTracks;
	}
	public void setIdTracks(List<String> idTracks) {
		this.idTracks = idTracks;
	}


public TreeModelPlaylist(String url) throws IOException, JSONException {
	   JavaGetUrl j = new JavaGetUrl(url);
       String songsJson = j.result;
       //System.out.println(songsJson);
       
       ObjectMapper mapper = new ObjectMapper();
       JsonNode node = mapper.readTree(songsJson);
       //System.out.println(node.get("response").get("songs"));
       JsonNode songs = node.get("response").get("songs");
       int i = 0;
       try{
	       do{
			   String[] t = mapper.writeValueAsString(songs.get(i).get("tracks").get(0).get("foreign_id")).split(":track:");
			   //System.out.println("song : "+t[1]);
			   String[] tt = t[1].split("\"");
			   idTracks.add(tt[0]);
			   
			   i++;
	       	}while(songs.get(i)!=null);
       }
       catch (Exception e) {
    	   
       }
       
       //JsonNode songs = mapper.readTree(subNodes.get("songs").get.next().getValue().toString());
       
       //String json = mapper.writeValueAsString(subNodes.get("songs"));
       //System.out.println(json);
       
       //this.idTracks = arrayToList(json);
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