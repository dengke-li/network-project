package com.first.whatson.database.tables;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.first.whatson.database.Database;
import com.first.whatson.database.objects.Photo;
/**
 * Actions in the table of photos
 * 
 *
 */
public class TablePhotos implements Table{

	private final static String TABLE_PHOTOS = "photos";
	private final static String SOURCE = "source";
	private final static String ID = "id";

	public void insertAllPhotos(String folder,String lot){
		//System.out.println("================== LOT: "+lot);
		try{
			File[] files = new File(folder+"/"+lot).listFiles();
			
			for (File file : files) {
				if (file.isFile()){
					//System.out.println(folder+"/"+lot+"    "+file);
					Photo photo = new Photo();
					photo.setSource(lot+"/"+file.getName());	
					insert(photo);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void insert(Object source) throws Exception{
		Photo photo = (Photo) source;
		if(!existsObject(photo)){
			Connection conn=Database.getConnection();
			PreparedStatement ps=null;
			ps=conn.prepareStatement("insert into "+TABLE_PHOTOS+" ("+SOURCE+") values(?)");
			ps.setObject(1, photo.getSource());
			ps.executeUpdate();
			conn.close();
		}
		else{
			System.out.println("=== This emotion already exists in the database ("+photo.getSource()+")");
		}
		
	}
	
	public void merge(String folder){
		//folder insert into db
		try{
			File[] files = new File(folder).listFiles();
			String filename;
			for (File file : files) {
				filename = file.getName();
				if (file.isFile()) 
					if(!exists(filename)){
						insert(filename);
						System.out.println(filename+" has been added in the database.");
					}
//					else
//						System.out.println(filename+" exists");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		//db sources deleted if not into folder
		try {
			ResultSet rset = getAll();
			while (rset.next()) {
			    String source = rset.getString(SOURCE);
			    File file = new File(folder +"/"+ source);
			    if(!file.exists()){
			    	deletePhoto(source);
			    	System.out.println(source + " has been deleted from the database.");
			    }
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private int deletePhoto(String source) throws Exception{
		return delete(SOURCE,source);
	}
	
	private int deletePhoto(int id) throws Exception{
		return delete(ID,String.valueOf(id));
	}
	
	@Override
	public int delete(String column, String value) throws Exception {
		Connection conn=Database.getConnection();
		PreparedStatement ps=null;
		ps=conn.prepareStatement("delete from "+TABLE_PHOTOS+" where "+column+" = ?");
		ps.setObject(1, value);
		return ps.executeUpdate();
	}
	
	@Override
	public boolean exists(String filename) throws Exception {
		Connection conn=Database.getConnection();
		PreparedStatement ps=null;
		ps=conn.prepareStatement("select ? from "+TABLE_PHOTOS+" where source = ?");
		ps.setObject(1, SOURCE);
		ps.setObject(2, filename);
		ResultSet rset = ps.executeQuery();
		return rset.first();
	}
	
	public ResultSet get(String source) throws Exception {
		Connection conn=Database.getConnection();
		PreparedStatement ps=null;
		ps=conn.prepareStatement("select "+SOURCE+" from "+TABLE_PHOTOS+" where source = "+source);
		ResultSet rset = ps.executeQuery();
		return rset;
	}
	
	public static ResultSet getAll() throws Exception {
		Connection conn=Database.getConnection();
		PreparedStatement ps=null;
		ps=conn.prepareStatement("select "+ID+","+SOURCE+" from "+TABLE_PHOTOS);
		ResultSet rset = ps.executeQuery();
		return rset;
	}


	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean existsObject(Object object) throws Exception {
		Photo photo = (Photo) object;
		Connection conn=Database.getConnection();
		PreparedStatement ps=null;
		ps=conn.prepareStatement("select ? from "+TABLE_PHOTOS+" where ? = ?");
		ps.setObject(1, ID);
		ps.setObject(2, SOURCE);
		ps.setObject(3, photo.getSource());
		ResultSet rset = ps.executeQuery();
		return rset.first();
	}




}
