package com.first.whatson.database.objects;

/**
 * Emotion model in the database
 * 
 *
 */
public class Emotion {

	private String emotion;
	private int id;
	
	public String getEmotion() {
		return emotion;
	}
	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Emotion(String emotion, int id) {
		super();
		this.emotion = emotion;
		this.id = id;
	}
	public Emotion() {
		super();
	}
	
	
}
