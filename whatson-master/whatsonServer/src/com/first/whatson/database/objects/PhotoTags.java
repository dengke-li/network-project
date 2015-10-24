package com.first.whatson.database.objects;

import java.util.HashMap;
import java.util.Map;

/**
 * Photo tagged model in database to represent emotional profile of a photo
 * 
 */
public class PhotoTags {

	private Map<Integer,Double> emotions = new HashMap<Integer,Double>();
	private int id;
	private String source;
	
	public PhotoTags(){
		
	}
	
	public PhotoTags(Map<Integer, Double> emotions, int id, String source) {
		super();
		this.emotions = emotions;
		this.id = id;
		this.source = source;
	}
	
	public Map<Integer, Double> getEmotions() {
		return emotions;
	}
	public void setEmotions(Map<Integer, Double> emotions) {
		this.emotions = emotions;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	
	
}
