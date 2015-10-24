package com.whatson.prototype.models;

public class Emotion {

	private String emotion;
	private Double intensity;
	
	
	public Emotion(String emotion, Double intensity) {
		super();
		this.emotion = emotion;
		this.intensity = intensity;
	}
	public String getEmotion() {
		return emotion;
	}
	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}
	public Double getIntensity() {
		return intensity;
	}
	public void setIntensity(Double intensity) {
		this.intensity = intensity;
	}
	
	
}
