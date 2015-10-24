package com.first.whatson.database.objects;

/**
 * User model in the database
 * 
 *
 */
public class User {
	
	private String username;
	private String email;
	private String password;
	private int id;
	
	public String getUsername() {
		return username;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public int getId() {
		return id;
	}
	public User(String username, String email, String password, int id) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.id = id;
	}
	public User() {
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setId(int id) {
		this.id = id;
	}
	

}
