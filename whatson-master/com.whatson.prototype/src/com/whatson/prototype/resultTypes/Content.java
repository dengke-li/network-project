package com.whatson.prototype.resultTypes;


/**
 * Abstract class to use external content
 *
 *
 */
abstract public class  Content {

	private int id;
	
	public Content(int id) {
		super();
		this.id = id;
	}
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	
}
