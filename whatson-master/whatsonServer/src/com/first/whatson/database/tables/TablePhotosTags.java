package com.first.whatson.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.first.whatson.database.Database;
import com.first.whatson.database.objects.PhotoTags;
/**
 * Actions in the table of taging images
 * 
 *
 */
public class TablePhotosTags implements Table{

	
	private static final String IDPHOTO = "id_photo";
	private static final String IDEMOTION = "id_emotion";
	private static final String INTENSITY = "intensity";
	private static final String SOURCE = "source";
	private static final String TABLE_PHOTOS_TAGS = "photo_tags";
	private static final String TABLE_PHOTOS = "photos";
	private static final String ID = "id";


	@Override
	public void insert(Object user) throws Exception {
	}

	@Override
	public void update() {
		
	}

	@Override
	public int delete(String column, String value) throws Exception {
		return 0;
	}

	@Override
	public ResultSet get(String object) throws Exception {
		return null;
	}

	@Override
	public boolean exists(String object) throws Exception {
		return false;
	}

	@Override
	public boolean existsObject(Object object) throws Exception {
		return false;
	}

	public static ResultSet getAll() throws Exception {
		Connection conn=Database.getConnection();
		PreparedStatement ps=null;
		ps=conn.prepareStatement("select "+IDPHOTO+","+IDEMOTION+","+INTENSITY+" from "+TABLE_PHOTOS_TAGS);
		ResultSet rset = ps.executeQuery();
		return rset;
	}
	
	public static Map<Integer,PhotoTags> getAllPhotoSpiders() throws Exception{
		Connection conn=Database.getConnection();
		Connection conn2=Database.getConnection();

		PreparedStatement ps=null;
		ps=conn.prepareStatement("select distinct "+ID+","+SOURCE+" from "+TABLE_PHOTOS);
		ResultSet rset = ps.executeQuery();
		
		PreparedStatement ps2=null;
		ResultSet rset2;
		
		Map<Integer,PhotoTags> allPhotoTags = new HashMap<Integer,PhotoTags>();
		PhotoTags photoTags;
		
		int id;
		if(rset.first()){
			do{
				photoTags = new PhotoTags();
				id = rset.getInt(1);
				photoTags.setId(id);
				photoTags.setSource(rset.getString(2));
				//System.out.println(id+" "+rset.getString(2));

				ps2=conn2.prepareStatement("select "+IDEMOTION+","+INTENSITY+" from "+TABLE_PHOTOS_TAGS+" where "+IDPHOTO+" = "+id+" order by "+IDEMOTION);
				rset2 = ps2.executeQuery();
				if(rset2.first()){
					do{
						photoTags.getEmotions().put(rset2.getInt(1), (double) rset2.getFloat(2));
					}while(rset2.next());
				}
				allPhotoTags.put(id,photoTags);
			}while(rset.next());
		}
		conn.close();
		conn2.close();
		return allPhotoTags;
		
	}
}
