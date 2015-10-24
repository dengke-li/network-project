package com.first.whatson.database.objects;

/**
 * Database model of the bind between a specific emotion and a photo (its intensity)
 * 
 *
 */

public class PhotoTag {

	private int id_emotion;
	private int id_photo;
	private float intensity;
	
	
	
	public PhotoTag(int id_emotion, int id_photo, float intensity) {
		super();
		this.id_emotion = id_emotion;
		this.id_photo = id_photo;
		this.intensity = intensity;
	}
	public int getId_emotion() {
		return id_emotion;
	}
	public void setId_emotion(int id_emotion) {
		this.id_emotion = id_emotion;
	}
	public int getId_photo() {
		return id_photo;
	}
	public void setId_photo(int id_photo) {
		this.id_photo = id_photo;
	}
	public float getIntensity() {
		return intensity;
	}
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
	

}

