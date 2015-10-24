package com.first.whatson.connection;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.first.whatson.database.objects.EmotionSpider;
import com.first.whatson.database.objects.TextExternalContent;

/**
 * User: Bean, data of the user during a session
 * By this we can follow his choices during a suggestion process, and create/access to his current emotional profile
 * 
 *
 */
public class User {

	private String email;
	private LinkedList<Integer> selectedPhotoIds = new LinkedList<Integer>();
	private Map<Integer,Double> currentEmotionsIntensities = null;	
	private List<String> biggestEmotions = new LinkedList<String>();
	private LinkedList<TextExternalContent> sortedEmotionSpiderList = new LinkedList<TextExternalContent>();
	
	
	public User(String email){
		this.email=email;
	}
	
	/**
	 * Get the current emotionnal profile of the user, sorted by increasing emotions
	 * @return
	 */
	public LinkedList<TextExternalContent> getSortedEmotionSpiderList() {
		return sortedEmotionSpiderList;
	}
	
	/**
	 * set the increasing sorted emotional profile of the user
	 * @param sortedEmotionSpiderList
	 */
	public void setSortedEmotionSpiderList(
			LinkedList<TextExternalContent> sortedEmotionSpiderList) {
		this.sortedEmotionSpiderList = sortedEmotionSpiderList;
	}

	/**
	 * Intensity of each emotion of the user (not sorted!)
	 * @return
	 */
	public Map<Integer, Double> getCurrentEmotionsIntensities() {
		return currentEmotionsIntensities;
	}
	
	/**
	 * Set the current emotional profile of the user
	 * @param currentEmotionsIntensities
	 */
	public void setCurrentEmotionsIntensities(
			Map<Integer, Double> currentEmotionsIntensities) {
		this.currentEmotionsIntensities = currentEmotionsIntensities;
	}
	
	/**
	 * get the user email
	 * @return
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * Set the user email
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * Get the id list of the images the user has selected during the current suggestion process
	 * @return
	 */
	public LinkedList<Integer> getSelectedPhotoIds() {
		return selectedPhotoIds;
	}
	/**
	 * Set the id list of the images the user has selected during the current suggestion process
	 * @param selectedPhotoIds
	 */
	public void setSelectedPhotoIds(LinkedList<Integer> selectedPhotoIds) {
		this.selectedPhotoIds = selectedPhotoIds;
	}
	/**
	 * Set the biggest emotions felt by the user
	 * @param biggestEmotions
	 */
	public void setBiggestEmotions(List<String> biggestEmotions) {
		this.biggestEmotions = biggestEmotions;
	}
	/**
	 * Get the biggest emotions felt by the user
	 * @return
	 */
	public List<String> getBiggestEmotions() {
		return biggestEmotions;
	}
	

	

	

}
