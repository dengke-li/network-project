package com.first.whatson.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.first.whatson.database.Database;
import com.first.whatson.database.objects.EmotionSpider;
import com.first.whatson.database.objects.FeltEmotion;
import com.first.whatson.database.objects.User;
/**
 * Actions in the table of users
 * 
 *
 */
public class TableUsers implements Table{

	private final static String TABLE_USERS = "users";
	private final static String TABLE_USERSHISTO = "users_historics";
	private final static String USERNAME = "username";
	private final static String EMAIL = "email";
	private final static String PASSWORD = "password";
	
	@Override
	public void insert(Object user) throws Exception {
		User u = (User) user;
		if(!existsObject(u)){
			Connection conn=Database.getConnection();
			PreparedStatement ps=null;
			ps=conn.prepareStatement("insert into "+TABLE_USERS+" ("+USERNAME+","+EMAIL+","+PASSWORD+") values(?,?,?);");
			ps.setObject(1, u.getUsername());
			ps.setObject(2, u.getEmail());
			ps.setObject(3, u.getPassword());
			ps.executeUpdate();
		}
		else{
			System.out.println("=== This user already exists in the database ("+u.getUsername()+","+u.getEmail()+")");
		}
	}

	/**
	 * Get all external contents stored
	 */
	public static ArrayList<EmotionSpider> getUserHistoricEmotions() throws Exception {
		ArrayList<EmotionSpider> feltEmotions = new ArrayList<EmotionSpider>();
		Connection conn=Database.getConnection();
		PreparedStatement ps=null;
		ps=conn.prepareStatement("select id_emotion,intensity,id_felt_emotion from "+TABLE_USERSHISTO);
		ResultSet rset = ps.executeQuery();

		if(rset.first()){
			Map<Integer,Double> emotionSpider;
			EmotionSpider e;
			do{
				emotionSpider=new HashMap<Integer,Double>();
				do{
					emotionSpider.put(rset.getInt(1), (double) rset.getLong(2));
				}while(rset.next());
				e = new FeltEmotion(rset.getInt(3),emotionSpider);
				feltEmotions.add(e);
			}while(rset.next());
			conn.close();
			return feltEmotions;
		}
		return null;
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
		Connection conn=Database.getConnection();
		PreparedStatement ps=null;
		ps=conn.prepareStatement("select ? from "+TABLE_USERS+" where source = ?");
		ps.setObject(1, "emotion");
		ps.setObject(2, object);
		ResultSet rset = ps.executeQuery();
		return rset.first();
	}

	@Override
	public boolean existsObject(Object object) throws Exception {
		User user = (User) object;
		Connection conn=Database.getConnection();
		PreparedStatement ps=null;
		ps=conn.prepareStatement("select ? from "+TABLE_USERS+" where username = ? and email = ?");
		ps.setObject(1, USERNAME);
		ps.setObject(2, user.getUsername());
		ps.setObject(3, user.getEmail());
		ResultSet rset = ps.executeQuery();
		return rset.first();
	}
}
