package com.whatson.prototype.models;

/**
 * Image model in database
 * 
 *
 */
public class Image {

	private String source;
	private int id;
	
	public Image(int id,String source) {
		super();
		this.source = source;
		this.id = id;
	}
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
	
	
	
}
