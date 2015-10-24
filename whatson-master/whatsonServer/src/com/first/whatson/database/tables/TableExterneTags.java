package com.first.whatson.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.first.whatson.database.Database;
import com.first.whatson.database.objects.EmotionSpider;
import com.first.whatson.database.objects.TextExternalContent;
/**
 * Actions in the table of externe contents tags
 * 
 *
 */
public class TableExterneTags implements Table {

	private final static String TABLE_EXTERNETAGS = "externe_tags";
	private final static String EMOTION = "emotion";
	private final static String ID_EXTERNAL_CONTENT = "id_external_content";

	public static EmotionSpider getExternalContentsWithId(Integer id) throws Exception{
		Connection conn=Database.getConnection();
		PreparedStatement ps=null;
		ps=conn.prepareStatement("select id_emotion,intensity"+" from "+TABLE_EXTERNETAGS+" where "+ID_EXTERNAL_CONTENT+" = ?");
		ps.setObject(1, id);
		ResultSet rset = ps.executeQuery();

		Map<Integer,Double> emotionSpider=new HashMap<Integer,Double>();
		if(rset.first()){
			do{
				emotionSpider.put(rset.getInt(1), (double) rset.getLong(2));
			}while(rset.next());
			EmotionSpider e = new TextExternalContent(id,emotionSpider);
			conn.close();
			return e;	
		}
		return null;
	}
	@Override
	public void insert(Object object) throws Exception {
		// TODO Auto-generated method stub

	}
	@Override
	public void update() {
		// TODO Auto-generated method stub

	}
	@Override
	public int delete(String column, String value) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Get all external contents stored
	 */
	public static LinkedList<EmotionSpider> getExternalContents(int type) throws Exception {
		System.out.println("getting external info spiders");
		LinkedList<EmotionSpider> externalContentsList = new LinkedList<EmotionSpider>();
		Connection conn=Database.getConnection();
		Connection conn2=Database.getConnection();
		PreparedStatement ps=null;
		PreparedStatement ps2=null;

		ps=conn.prepareStatement("select id_emotion,intensity,"+ID_EXTERNAL_CONTENT+" from "+TABLE_EXTERNETAGS);
		ps2=conn.prepareStatement("select id from externe_text_content where typeContent="+type);
		
		ResultSet rset = ps.executeQuery();
		ResultSet rset2 = ps2.executeQuery();
		int idContent;
		Map<Integer,Double> emotionSpider;
		EmotionSpider e;
		if(rset2.first()){
			
			do{
				idContent = rset2.getInt(1);
				emotionSpider=new HashMap<Integer,Double>();
				if(rset.first()){
					do{
						if(idContent == rset.getInt(3))
							emotionSpider.put(rset.getInt(1), (double) rset.getLong(2));
					}while(rset.next());
					e = new TextExternalContent(idContent,emotionSpider);
					externalContentsList.add(e);
				}
			}while(rset2.next());
			System.out.println("external cntents list "+externalContentsList.size());
			conn.close();
			conn2.close();
			return externalContentsList;
		}
		conn.close();
		return null;
	}
	
	@Override
	public boolean exists(String object) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean existsObject(Object object) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public ResultSet get(String object) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
