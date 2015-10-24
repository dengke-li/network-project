package com.first.whatson.database.objects;

import java.util.Map;


/**
 * FeltEmotion model in the database
 * 
 *
 */
public class FeltEmotion extends EmotionSpider{
	
	public FeltEmotion(int id, Map<Integer, Double> emotionSpider) {
		super(id,emotionSpider);
	}
	
	
}
