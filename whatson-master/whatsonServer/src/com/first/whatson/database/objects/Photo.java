package com.first.whatson.database.objects;

/**
 * Image model in the database
 * 
 *
 */
public class Photo {
	
	private String source;
	private int id;
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Photo(String source, int id) {
		super();
		this.source = source;
		this.id = id;
	}
	public Photo() {
		super();
	}

	
}
