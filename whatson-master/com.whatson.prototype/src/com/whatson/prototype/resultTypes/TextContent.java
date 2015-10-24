package com.whatson.prototype.resultTypes;

/**
 * TextContent for text external contents object
 * 
 *
 */
public class TextContent extends Content{

	private String title;
	private String author;
	private String url;
	private String source;


	
	public TextContent(int id,String title,String url,String source,String author) {
		super(id);
		this.title = title;
		this.url = url;
		this.source = source;
		this.author = author;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	
	
}
