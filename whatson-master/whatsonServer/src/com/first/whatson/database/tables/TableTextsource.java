package com.first.whatson.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

import com.first.whatson.database.Database;
import com.first.whatson.database.objects.Emotion;
import com.first.whatson.database.objects.EmotionSpider;
import com.first.whatson.database.tables.*;
/**
 * Actions in the table of of external text contents
 * 
 *
 */
public class TableTextsource implements Table {

	private final static String TABLE_EXTERNE_text_source = "externe_text_content";
	private final static String EMOTION = "emotion";
	private final static String ID = "id";

	public static ArrayList<EmotionSpider> getEmotionsList() throws Exception{
		Connection conn=Database.getConnection();
		PreparedStatement ps=null;
		ps=conn.prepareStatement("select "+"id_external_content"+" from "+TABLE_EXTERNE_text_source);
		ResultSet rset = ps.executeQuery();
		if(rset.first()){
			ArrayList<EmotionSpider> list = new ArrayList<EmotionSpider>();
			do{	
				EmotionSpider e=TableExterneTags.getExternalContentsWithId(rset.getInt("id_external_content"));
				list.add(e);
			}while(rset.next());
			return list;
		}
		conn.close();
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

	@Override
	public ResultSet get(String object) throws Exception {
		// TODO Auto-generated method stub
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
}
