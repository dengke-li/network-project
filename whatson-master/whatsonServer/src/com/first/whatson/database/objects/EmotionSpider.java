package com.first.whatson.database.objects;

import java.util.Map;

/**
 * Abstract class
 * Emotional profile of something (taggued image, user profile, external content)
 * Main attribute: emotionSpider is a map that binds each emotion with an intensity for the thing
 * 
 *
 */
public abstract class EmotionSpider {


	protected Map<Integer,Double> emotionSpider;
	protected int id;

	public EmotionSpider() {
	}
	
	public EmotionSpider( int id, Map<Integer, Double> emotionSpider) {
		this.emotionSpider = emotionSpider;
		this.id = id;
	}

	public Map<Integer, Double> getEmotionSpider() {
		return this.emotionSpider;
	}
	public void setEmotionSpider(Map<Integer, Double> emotionSpider) {
		this.emotionSpider = emotionSpider;
	}
	public int getId() {
		return this.id;
	}
	public void setId(int id) {
		this.id = id;
	}


}
