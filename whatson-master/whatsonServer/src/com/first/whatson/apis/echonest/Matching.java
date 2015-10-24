package com.first.whatson.apis.echonest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.first.whatson.database.Database;
import com.first.whatson.database.tables.TableEmotions;

public class Matching {

	private Map<String,String> matchingFeelingGenre = new HashMap<String,String>();
	private List<String> emotions = new ArrayList<String>();
	
	
	public Map<String, String> getMatchingFeelingGenre() {
		return matchingFeelingGenre;
	}
	public void setMatchingFeelingGenre(
			Map<String, String> matchingFeelingGenre) {
		this.matchingFeelingGenre = matchingFeelingGenre;
	}
	public List<String> getEmotions() {
		return emotions;
	}
	public void setEmotions(List<String> emotions) {
		this.emotions = emotions;
	}

	public Matching() throws Exception{
		//get all emotions
		emotions = TableEmotions.getEmotionsList();
		
		//get all genres
		TreeModelGenre tm = new TreeModelGenre();
		List<String> genres = tm.getGenres();
		
		//read matching file in txt
		File file = new File("/home/alice/JEE/first/whatson/"+Database.getEmotionsfile());
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while((line = br.readLine())!=null){
			String [] matcher = line.split(" ");
			String emotion = matcher[0];
			String musicGenre = stringJoin(matcher," ");
			if(musicGenre != null){
				//System.out.println(emotion+ " ====> " + musicGenre);
				matchingFeelingGenre.put(emotion,musicGenre);
			}
		}
		
		Set<String> keys = matchingFeelingGenre.keySet();
		for(String key : keys)
			System.out.println(key + " ===> " + matchingFeelingGenre.get(key));
		
		
//		for(String genre : genres)
//			System.out.println(genre);
		
//		for(String emotion:emotions){
//			if(emotion.equals("Excité") && genres.contains("Rock"))
//				matchingFeelingGenre.put(emotion,"rock");
//		}
	}


	private static String stringJoin(String[] matcher, String delimiter) {
		int length = matcher.length;
		if(length>1){
			String joined = matcher[1];
			for(int i=2;i<length;i++)
				joined += delimiter + matcher[i];
			return joined;
		}
		return null;
	}
	
}
