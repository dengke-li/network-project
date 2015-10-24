package com.first.whatson.algorithms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.first.whatson.database.Database;
import com.first.whatson.database.objects.EmotionSpider;
import com.first.whatson.database.objects.TextExternalContent;

/**
 * Matching processus between the user profile and the whole emotional profile of digital external contents
 * 
 *
 */
public class Matchinfo {

	/**
	 * Get all the images with their data and the spider of the emotions and their intensity
	 * return the list of external content, sorted by increasing euclidean distance with the new emotions spider created with the current search
	 * @param felt_emotion
	 * @param emotion_history
	 * @param info_external
	 * @return
	 * @throws Exception 
	 */	
	public static LinkedList<TextExternalContent> match(Map<Integer, Double> felt_emotion,ArrayList<EmotionSpider> emotion_history,LinkedList<EmotionSpider> info_external) throws Exception{
		  LinkedList<TextExternalContent> listInstancestrie = new LinkedList<TextExternalContent>();
		LinkedList<TextExternalContent> listInstances = new LinkedList<TextExternalContent>();

		//connection to db
		Connection conn=Database.getConnection();
		TextExternalContent content;
		
		PreparedStatement ps=null;
		ps=conn.prepareStatement("select id,source,url,author,title from externe_text_content");
		ResultSet rset = ps.executeQuery();
		if(rset.first()){
			do{
				int id = rset.getInt(1);
				for(EmotionSpider spider : info_external){
					if(id == spider.getId()){
						content = new TextExternalContent(id, spider.getEmotionSpider()	);
						content.setAuthor(rset.getString(4));
						content.setSource(rset.getString(2));
						content.setTitle(rset.getString(5));
						content.setUrl(rset.getString(3));
						listInstances.add(content);
						break;
					}				
				}
			}while(rset.next());
		}
		 int Index;
         int c=listInstances.size();
         for(int i=0;i<c;i++){
                 System.out.println(i+" ");
                 Index=mindistance(felt_emotion,listInstances);
                 System.out.println(Index+"haha");
                 listInstancestrie.add((TextExternalContent) listInstances.get(Index));
                 listInstances.remove(Index);
         }
		conn.close();

		 return listInstancestrie;
		
		//}
		
		
//		LinkedList<EmotionSpider> list=new LinkedList<EmotionSpider>();
//		int Index=mindistance(felt_emotion,emotion_history);
//		Map<Integer, Double> new_emotion;
//		if(emotion_history!=null)
//			new_emotion=middle(felt_emotion,emotion_history.get(Index).getEmotionSpider());
//		else{
//			//TODO 	insert emotion as historic in database
//
//			new_emotion = felt_emotion;
//		}
//		System.out.println("external info size "+info_external.size());
//		int c=info_external.size();
//		for(int i=0;i<c;i++){
//			System.out.println(i+" ");
//			Index=mindistance(new_emotion,info_external);
//			System.out.println(Index+"");
//			list.add(info_external.get(Index));
//			info_external.remove(Index);
//		}
//		return list;
		//return listInstances;
	}
	
	
	/**
	 * Get the euclidean distance between the current user emotion spider and the emotions spider of a photo
	 * @param felt_emotion
	 * @param list
	 * @return
	 */
    private static int mindistance(Map<Integer, Double> felt_emotion,LinkedList<TextExternalContent> listInstances){
//        int Index = 0;
//        if(listInstances!=null){
//                //System.out.println(list.size()+"");
//                Double min=EuclideanDistance.distance(felt_emotion,listInstances.get(0).getEmotionSpider());
//                for(int i=1;i<listInstances.size();i++){
//                        Double p=EuclideanDistance.distance(felt_emotion,listInstances.get(i).getEmotionSpider());
//                        if(p<min){
//                                min=p;
//                                Index=i;
//                        }
//                }
//        }
//        return Index;
    	return (int) Database.random(0,listInstances.size()-1);

}
//	private static int mindistance(Map<Integer, Double> felt_emotion,ArrayList<EmotionSpider> list){
//		int Index = 0;
//		if(list!=null){
//			//System.out.println(list.size()+"");
//			Double min=EuclideanDistance.distance(felt_emotion,list.get(0).getEmotionSpider());
//			for(int i=1;i<list.size();i++){
//				Double p=EuclideanDistance.distance(felt_emotion,list.get(i).getEmotionSpider());
//				if(p<min){
//					min=p;
//					Index=i;
//				}
//			}
//		}
//		return Index;
//		
//	}
//	
	/**
	 * Create a new emotion spider, it is the middle between the matching photo spider and the current emotion spider
	 * We can store it for next research with this user
	 * @param emotions1
	 * @param emotions2
	 * @return
	 */
	private static Map<Integer, Double> middle(Map<Integer, Double> emotions1,Map<Integer, Double> emotions2){
		Map<Integer, Double> emotions3=new HashMap<Integer, Double>();
		Set<Integer> set = emotions1.keySet();
		Iterator<Integer> iter = set.iterator();
		while (iter.hasNext()){
			Integer key = iter.next();
			Double value= (emotions1.get(key) + emotions2.get(key))/2;	
			emotions3.put(key,value);
			
		}
		return emotions3;
		
	}
	
}
