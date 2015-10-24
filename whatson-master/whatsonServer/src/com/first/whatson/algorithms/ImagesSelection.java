package com.first.whatson.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.first.whatson.database.Database;
import com.first.whatson.database.objects.PhotoTags;
import com.first.whatson.database.tables.TablePhotosTags;

/**
 * Selection of next images to display to the user 
 * 
 *
 */
public class ImagesSelection {

	private static Map<Integer,Double> distances;
	private static Map<Integer, PhotoTags> imagesSpiders = new HashMap<Integer, PhotoTags>();

	/**
	 * Get all the images with their data and the spider of the emotions and their intensity
	 * @return
	 */
	public static Map<Integer, PhotoTags> getImagesSpiders() {
		return imagesSpiders;
	}
	public static void setImagesSpiders(Map<Integer, PhotoTags> imagesSpiders) {
		ImagesSelection.imagesSpiders = imagesSpiders;
	}

	/**
	 * List of already seen images by the user during this session
	 */
	private static ArrayList<Integer> seenImages = new ArrayList<Integer>();
	
	/**
	 * starting images selection
	 * @throws Exception
	 */
	public static ArrayList<Map<String, String>> ImagesSelection(int lastImage,int otherImage) throws Exception{
		//if there is at least 2 more images, we can select next images, if not return null
//		if(imagesSpiders.size()<2)
//			return null;
		
		Database.getConnection().close();
		imagesSpiders = TablePhotosTags.getAllPhotoSpiders();
		
		//add seen images not to show them again
		if(lastImage>0)
			seenImages.add(lastImage);
		if(otherImage>0)
			seenImages.add(otherImage);
//		for(int idseen : seenImages)
//			System.out.println("SEEN : "+idseen);
		
		/**
		 * parcours des images pour établir les distances
		 * avec le dernier idImage choisi (cas du next)
		 * ou la première image (cas du start)
		 */
		
		//START : select first image in database
		if(lastImage == 0 && otherImage == 0){
			//init for a new search
			seenImages = new ArrayList<Integer>();
			int imagesSpidersLength = imagesSpiders.size();
			if(imagesSpiders != null && imagesSpidersLength>0){
				int randomStart = (int) Database.random(1,imagesSpidersLength);
				//randomStart = 20;
				System.out.println("LENGTH "+imagesSpidersLength);
				Set<Integer> keys = imagesSpiders.keySet();
				Iterator<Integer> it = keys.iterator();
				if (it.hasNext())
					for(int i=0;i<randomStart;i++)
						lastImage = it.next();		
				System.out.println("Start images :: idimage1 :: "+lastImage);
			}
			else
				System.err.println("No images to display in database (empty table)");
		}
		
		
		//A ce stade : case start: lastImage sera la première image, case next : on possède les 2 denières images,
		//en particulier la dernière qui a été sélectionnée
		//réaliser l'algorithme pour déterminer les 2 prochaines images à montrer
		Set<Integer> keys = imagesSpiders.keySet();
		Iterator<Integer> it = keys.iterator();
		Double distance;
		distances = new HashMap<Integer, Double>();
		System.out.println("LAST IMAGE "+lastImage);
		while (it.hasNext()){
			Integer key = it.next();
			PhotoTags value = imagesSpiders.get(key);
			if(key != lastImage && key != otherImage && !seenImages.contains(key)){
				//System.out.println(imagesSpiders.get(lastImage).getEmotions()+" "+value.getEmotions());
				distance = EuclideanDistance.distance(imagesSpiders.get(lastImage).getEmotions(),value.getEmotions());
				distances.put(key, distance);
				//System.out.println(key+ " :: "+distance);
			}
		}
		
		//remove already seen images from the imagesSpider map (lastimage and otherimage)
		for(Integer seenIdImage : seenImages){
			if(imagesSpiders.containsKey(seenIdImage))
				imagesSpiders.remove(seenIdImage);
		}
		
		//sort diffScalars map
		Map<Integer, Double> sortedDiffMap = sortByComparator(distances);
		//printMap(sortedDiffMap);

		int idImage1 = 0;
		int idImage2 = 0;
		
		//case Start
		if(lastImage == 0 && otherImage == 0){
			//get idImage2
			idImage1 = lastImage;
			keys = sortedDiffMap.keySet();
			it = keys.iterator();
			while (it.hasNext())
				idImage2 = it.next();
			System.out.println("START :: idImage1: "+idImage1+" idImage2: "+idImage2);
		}
		
		//case Next
		else{
			//get next images id
			keys = sortedDiffMap.keySet();
			it = keys.iterator();
			int quart = (int)(keys.size()/4);
			int trquart = (int)(3*keys.size()/4);
			int i=0;
			while (it.hasNext()){
				int index = it.next();
				//System.out.println(i+" "+index+" "+quart+" "+trquart);
				if(i == quart)
					idImage1 = index;
				else if(i == trquart)
					idImage2 = index;
				i++;
			}
			System.out.println("NEXT :: idImage1: "+idImage1+" idImage2: "+idImage2);
		}

		//make array to post response in REST service
//		ArrayList<String> nextImages = new ArrayList<String>();
//		nextImages.add(idImage1+"");
//		nextImages.add(idImage2+"");
		
		if(imagesSpiders.containsKey(idImage1) && imagesSpiders.containsKey(idImage2)){
			ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();
			Map<String,String> map = new HashMap<String, String>();
			map.put(idImage1+"", imagesSpiders.get(idImage1).getSource());
			map.put(idImage2+"",imagesSpiders.get(idImage2).getSource());
			list.add(map);
			return list;
		}
		return null;
	}

	/**
	 * Sort the map of the differnet emotions, by value
	 * @param diffScalars
	 * @return
	 */
	public static Map sortByComparator(Map diffScalars) {
		List list = new LinkedList(diffScalars.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		// put sorted list into map again
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	/**
	 * Display sorted map by increasing euclidean distance
	 * @param map
	 */
	public static void printMap(Map<Integer,Double> map){
		for (Map.Entry entry : map.entrySet())
			System.out.println("Key : " + entry.getKey()+ " Value : " + entry.getValue());
	}
}
