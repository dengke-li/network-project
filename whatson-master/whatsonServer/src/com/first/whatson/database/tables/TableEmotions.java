package com.first.whatson.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.first.whatson.database.Database;
import com.first.whatson.database.objects.Emotion;

/**
 * Actions in the table of emotions
 * 
 *
 */
public class TableEmotions implements Table {

	private final static String TABLE_EMOTIONS = "emotions";
	private final static String EMOTION = "emotion";
	private final static String ID = "id";
	
	public void insert(Object object) throws Exception {
		Emotion e = (Emotion) object;
		if(!existsObject(e)){
			Connection conn=Database.getConnection();
			PreparedStatement ps=null;
			ps=conn.prepareStatement("insert into "+TABLE_EMOTIONS+" ("+EMOTION+") values(?);");
			ps.setObject(1, e.getEmotion());
			ps.executeUpdate();
		}
		else{
			System.out.println("=== This emotion already exists in the database ("+e.getEmotion()+")");
		}
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
		Connection conn=Database.getConnection();
		PreparedStatement ps=null;
		ps=conn.prepareStatement("select ? from "+TABLE_EMOTIONS+" where source = ?");
		ps.setObject(1, EMOTION);
		ps.setObject(2, object);
		ResultSet rset = ps.executeQuery();
		return rset.first();
	}

	@Override
	public boolean existsObject(Object object) throws Exception {
		Emotion emotion = (Emotion) object;
		Connection conn=Database.getConnection();
		PreparedStatement ps=null;
		ps=conn.prepareStatement("select ? from "+TABLE_EMOTIONS+" where emotion = ?");
		ps.setObject(1, ID);
		ps.setObject(2, emotion.getEmotion());
		ResultSet rset = ps.executeQuery();
		return rset.first();
	}


	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	public static String getEmotionWithId(Integer id) throws Exception{
		Connection conn=Database.getConnection();
		PreparedStatement ps=null;
		ps=conn.prepareStatement("select "+EMOTION+" from "+TABLE_EMOTIONS+" where "+ID+" = ?");
		ps.setObject(1, id);
		ResultSet rset = ps.executeQuery();
		String emotion = null;
		if(rset.first())
			emotion = rset.getString(1);
		conn.close();
		return emotion;
	}
	
	public static ArrayList<String> getEmotionsList() throws Exception{
		Connection conn=Database.getConnection();
		PreparedStatement ps=null;
		ps=conn.prepareStatement("select "+EMOTION+" from "+TABLE_EMOTIONS);
		ResultSet rset = ps.executeQuery();
		if(rset.first()){
			ArrayList<String> list = new ArrayList<String>();
			do{
				list.add(rset.getString(1));
			}while(rset.next());
			return list;
		}
		conn.close();
		return null;
		
		
	
	}
	

	
}
