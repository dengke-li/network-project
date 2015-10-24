package com.first.whatson.algorithms;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Compute the euclidiean distance between two images.
 * Each image owns a vector of emotions with their intensity.
 * 
 *
 */
public class EuclideanDistance {

	
	/**
	 * Compute the euclidean distance between two vectors of emotions
	 * @param emotions
	 * @param emotions2
	 * @return
	 */
	public static Double distance (Map<Integer, Double> emotions1, Map<Integer, Double> emotions2)   {
		double deltas = 0.0;
		Set<Integer> set = emotions1.keySet();
		Iterator<Integer> iter = set.iterator();
		while (iter.hasNext()){
			Integer key = iter.next();
			deltas += Math.pow(emotions1.get(key) - emotions2.get(key),2);
			//System.out.println("premier: "+emotions1.get(key)+" second: "+emotions2.get(key));
		}
		
		return Math.sqrt(deltas) ;
	}
}
