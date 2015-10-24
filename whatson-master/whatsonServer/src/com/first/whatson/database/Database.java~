package com.first.whatson.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.first.whatson.database.objects.Emotion;
import com.first.whatson.database.objects.User;
import com.first.whatson.database.tables.TableEmotions;
import com.first.whatson.database.tables.TablePhotos;
import com.first.whatson.database.tables.TableUsers;
import com.ibatis.common.jdbc.ScriptRunner;

/**
 * Database manages connections to the MySQL database that stores users' data, suggestion images and external contents
 * <br>Manage the whole database
 * <br><ul><li>INIT : to create and fill the database (create some users, all the emotions for the profiles, all the suggestions images, statistifcs from the survey to tag images)</li>
 * <li>DROP : drop the database</li>
 * <li>SYNCHRONIZE : synchronize the database, currently this option merge suggestion images placed in the WebContent/images in the database. Doesn't really work anymore due to sub-folder in the images folder</li>
 * </ul>
 * <br>Allow to connect to the database, thanks to credentials to MySQL database on the server
 * 
 *
 */
public class Database {

	//	private static final String host = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
	//	private static final String port = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
	//	private static final String USERNAME = "adminftYZEbg";
	//	private static final String PASSWORD = "8F9ArjXmnVLy";

	private static final String host = "localhost";
	private static final String port = "3306";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "";


	private static final String PROJECTPATH = "WebContent/";
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static String DBADDRESS = "jdbc:mysql://";
	private static final String DBNAME = "whatson";
	private static String URL = DBADDRESS + host + ":" + port + "/" + DBNAME;
	private static final String IMAGESFOLDER = PROJECTPATH + "images";
	private static final String DATABASESQL = PROJECTPATH + "scripts/database.sql";
	private static final String USERSFILE = PROJECTPATH + "scripts/users.txt";
	private static final String OUTFILE = PROJECTPATH + "scripts/out.csv";
	private static final String EMOTIONSFILE = PROJECTPATH + "scripts/emotions.txt";

	private static TablePhotos tp = new TablePhotos();
	private static TableUsers tu = new TableUsers();
	private static TableEmotions te = new TableEmotions();


	public static String getEmotionsfile() {
		return EMOTIONSFILE;
	}

	/**
	 * Make the connection to the database
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		Class.forName(DRIVER).newInstance();
		Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		return conn;
	}

	/**
	 * To create & initialize and drop the database
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{				
		//create the database from scratch
		if(args[0].equals("INIT"))
			applicationInitialisation();
		//Drop the database
		else if(args[0].equals("DROP"))
			dropDatabase();
		//Synchronize
		else if(args[0].equals("SYNCHRONIZE"))
			synchronizeDatabase();
		else if(args[0].equals("NONE"))
			tagContents();
			

	}

	private static void synchronizeDatabase() {
		tp.merge(IMAGESFOLDER);
	}

	private static void applicationInitialisation() throws Exception {
		//create the database from scratch
		createDatabaseArchitecture();

		//insert photos in database (already done in initialisation in createDatabaseArchitecture())
		//tp.insertAllPhotos(IMAGESFOLDER);
		//tp.merge(IMAGESFOLDER);

		//insert users in database
		insertUsers(USERSFILE);

		//insert emotions in database
		insertEmotions(EMOTIONSFILE);

		//insert all the photos sources
		for(int i=1;i<9;i++){
			tp.insertAllPhotos(IMAGESFOLDER,"lot"+i);
		}

		//tag images		
		insertTags();		
		
		//insert external content images
		tagContents();
	}

	/**
	 * Insert intensties for each emotion per image
	 * @throws SQLException
	 * @throws IOException 
	 */
	private static void insertTags() throws SQLException, IOException {

		//get list des id_photo
		//		String idPhotos;
		Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		Statement stmt = con.createStatement();
		Statement stmt2 = con.createStatement();

		ResultSet rs = stmt.executeQuery("select id,source from photos");
		ResultSet rs2 = stmt2.executeQuery("select id,emotion from emotions");

		//		ArrayList<String> listIdEmotions = new ArrayList<String>();
		Map<String,Integer> mapIdEmotions = new HashMap<String,Integer>();
		if (!rs2.next() ) {System.out.println("no emotions");}
		else {
			do {
				//				listIdEmotions.add(rs2.getString(1));
				mapIdEmotions.put(rs2.getString(2), rs2.getInt(1));
			} while (rs2.next());
		}

		Map<String,Integer> mapIdPhotos = new HashMap<String,Integer>();
		if (!rs.next() ) {System.out.println("no photos");}
		else {
			do {
				System.out.println(rs.getString(2)+" ___photo___ "+ rs.getString(1));
				mapIdPhotos.put(rs.getString(2), rs.getInt(1));
			} while (rs.next());
		}

		//Read csv file to get the photos intensities
		File file = new File(OUTFILE);
		Reader in = new FileReader(file);
		BufferedReader br = new BufferedReader(in);
		String line;


		PreparedStatement stmt3 = con.prepareStatement("insert into photo_tags (id_emotion,id_photo,intensity) VALUES(?,?,?)");
		while( (line = br.readLine()) !=null){
			String[] split = line.split(",");
			//System.out.println(split[0]+" "+split[1]+" "+split[4]);
			
			if(mapIdEmotions.get(split[1])!= null && mapIdPhotos.get(split[0])!=null){
				int idEmotion = mapIdEmotions.get(split[1]);
				int idPhoto = mapIdPhotos.get(split[0]);
				stmt3.setInt(1,idEmotion);
				stmt3.setInt(2,idPhoto);
				stmt3.setDouble(3,Double.valueOf(split[4]));
				stmt3.executeUpdate();
			}
		}
		
		
		//set intensity null
		
		//to loop on photos
		Set<String> setPhotos = mapIdPhotos.keySet();
		Iterator<String> iterPhotos = setPhotos.iterator();
		int idPhoto;
		

		PreparedStatement stmt4 = con.prepareStatement("select intensity from photo_tags where id_photo = ? and id_emotion = ?");
		ResultSet rs4;

		while (iterPhotos.hasNext()){
			idPhoto = mapIdPhotos.get(iterPhotos.next());
			//to loop on emotions
			Set<String> setEmotions = mapIdEmotions.keySet();
			Iterator<String> iterEmotions = setEmotions.iterator();
			int idEmotion;
			while(iterEmotions.hasNext()){
				idEmotion = mapIdEmotions.get(iterEmotions.next());
				stmt4.setInt(1, idPhoto);
				stmt4.setInt(2, idEmotion);
				rs4 = stmt4.executeQuery();
				if(!rs4.next()){
					//System.out.println("insertion: "+idEmotion+" "+idPhoto);

					stmt3.setInt(1,idEmotion);
					stmt3.setInt(2,idPhoto);
					stmt3.setDouble(3,0.0);
					stmt3.executeUpdate();
				}
			}
		}

		//insert tags for photos
		//		PreparedStatement stmt3 = con.prepareStatement("insert into photo_tags (id_emotion,id_photo,intensity) VALUES(?,?,?)");
		//		if (!rs.next() ) {}
		//		else {
		//			do {
		//				idPhotos = rs.getString(1);
		//				for(String idEmotion : listIdEmotions){
		//					stmt3.setString(1,idEmotion);
		//					stmt3.setString(2,idPhotos);
		//					stmt3.setFloat(3,random(0,100));
		//					stmt3.executeUpdate();
		//				}
		//
		//			} while (rs.next());
		//		}
		con.close();	
		System.out.println("TAGS INSERTED");
	}

	/**
	 * Generate a random float between min and max value
	 * @param min
	 * @param max
	 * @return
	 */
	public static float random(int min,int max) {
		//		Random generator = new Random();
		//		double number = generator.nextDouble() * max;
		return (float) (min + Math.random() * ((max - min) + 1));
	}

	public Database(){	}

	/**
	 * insert the emotion of the emotions file in the database (init process)
	 * @param emotionsFile
	 * @throws SQLException
	 */
	private static void insertEmotions(String emotionsFile) throws SQLException {
		Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(emotionsFile)));
			String e;
			Emotion emotion = new Emotion();
			while((e = br.readLine())!=null){
				String[] em = e.split(" ");
				emotion.setEmotion(em[0]);
				te.insert(emotion);
			}
		} catch (Exception e) {
			System.err.println("=== Failed to insert emotions with " + emotionsFile + " The error is " + e.getMessage());
			e.printStackTrace();
		}
		con.close();
		System.out.println("EMOTIONS INSERTED");
	}

	/**
	 * insert the users of the users file in the database (init process)
	 * @param usersql
	 * @throws SQLException
	 */
	private static void insertUsers(String usersql) throws SQLException {
		Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(USERSFILE)));
			String u;
			User user = new User();
			while((u = br.readLine())!=null){
				String[] us = u.split(",");
				if(us.length==3){
					user.setUsername(us[0]);
					user.setEmail(us[1]);
					user.setPassword(us[2]);
					tu.insert(user);
				}
			}
		} catch (Exception e) {
			System.err.println("=== Failed to insert users with " + USERSFILE + " The error is " + e.getMessage());
			e.printStackTrace();
		}
		con.close();
		System.out.println("USERS INSERTED");
	}
	
	/**
	 * Create the architecture of the database with a sql script
	 * @throws Exception
	 */
	private static void createDatabaseArchitecture() throws Exception {
		//create database
		Connection con = DriverManager.getConnection(DBADDRESS, USERNAME, PASSWORD);
		try{
			Statement stmt = con.createStatement();
			stmt.executeUpdate("CREATE DATABASE " + DBNAME );
			stmt.close();
		}
		catch (Exception e) {
			System.out.println("=== Tables have already been created.");
			return;
		}
		con.close();

		//architecture
		con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		try {
			ScriptRunner sr = new ScriptRunner(con, false, false); 
			Reader reader = new BufferedReader(new FileReader(DATABASESQL));
			sr.runScript(reader);
			System.out.println("=== Tables created successfully : Database.createDatabaseArchitecture()");
		} catch (Exception e) {
			System.err.println("=== Failed to Execute" + DATABASESQL + " The error is " + e.getMessage());
		}
		con.close();

	}

	/**
	 * Drop the whole database of the application
	 * @throws Exception
	 */
	private static void dropDatabase() throws Exception{
		Connection con = getConnection();
		Statement stmt = con.createStatement();
		stmt.executeUpdate("DROP DATABASE " + DBNAME );
		stmt.close();
		con.close();
		System.out.println("=== Database dropped successfully : Database.dropDatabase()");
	}
	
	
	/**
	 * Add tags to external contents
	 * @throws SQLException 
	 */
	private static void tagContents() throws SQLException{
		Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		Connection con3 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		Statement stmt = con.createStatement();
		Statement stmt2 = con2.createStatement();
		
		ResultSet rs = stmt.executeQuery("select id from externe_text_content");
		ResultSet rs2 = stmt2.executeQuery("select id from emotions");

		PreparedStatement stmt3;
		String idContent;		
		if (!rs.next() ) {System.out.println("no external content");}
		else {
			
			do {
				if (!rs2.next() ) {System.out.println("no emotions");}
				else {
//					do {
//						idEmotion = rs2.getString(1);
//						idContent = rs.getString(1);
//						//insertion with intensity
//						stmt3 = con3.prepareStatement("insert into externe_tags (id_external_content,id_emotion,intensity) VALUES(?,?,?)");
//						stmt3.setString(1,idContent);
//						stmt3.setString(2,idEmotion);
//						stmt3.setFloat(3,random(0,100));
//						stmt3.executeUpdate();
//					} while (rs2.next());
					for(int i=1;i<26;i++){
						idContent = rs.getString(1);
						//insertion with intensity
						stmt3 = con3.prepareStatement("insert into externe_tags (id_external_content,id_emotion,intensity) VALUES(?,?,?)");
						stmt3.setString(1,idContent);
						stmt3.setString(2,i+"");
						stmt3.setFloat(3,random(0,100));
						stmt3.executeUpdate();
					}
				}
			} while (rs.next());
		}
		con.close();
		con2.close();
		con3.close();

	}
}
