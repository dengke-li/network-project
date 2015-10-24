package com.first.whatson.database.tables;

import java.sql.ResultSet;

/**
 * Some methods to use while manipulating database object models to interact between the java code and the database
 * 
 *
 */
public interface Table {

	public void insert(Object object) throws Exception;
	public void update();
	public int delete(String column, String value) throws Exception;
	public ResultSet get(String object) throws Exception;
	public boolean exists(String object) throws Exception;
	public boolean existsObject(Object object) throws Exception;
}
