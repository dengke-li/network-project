package com.first.whatson.database.objects;

import java.util.Map;

/**
 * TextExternalContent model in the database
 * 
 *
 */
public class TextExternalContent extends EmotionSpider{

	private String url;
	private String source;
	private String author;
	private String title;
	
	public TextExternalContent(int id,Map<Integer,Double> imageSpider,String url, String source, String author,String title) {
		super(id,imageSpider);
		setFinalAttributes(url,source,author,title);

	}
	
	public TextExternalContent(int id,Map<Integer,Double> imageSpider) {
		super(id,imageSpider);
	}
	
	public TextExternalContent(String url, String source, String author,String title) {
		super();
		setFinalAttributes(url,source,author,title);
	}
	
	
	private void setFinalAttributes(String url, String source, String author,String title) {
		this.url = url;
		this.source = source;
		this.author = author;
		this.title = title;		
	}

	public String getUrl() {
		return url;
	}
	public String getSource() {
		return source;
	}
	public String getAuthor() {
		return author;
	}
	public String getTitle() {
		return title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	

	/**
	 * Get all the external text contents with their data and the spider of the emotions and their intensity
	 * @return
	 */

	
}
