package com.wobgames.whosnext;

import java.io.Serializable;

public class User implements Serializable{

	private int mId;
	private String mName;
	
	public User() {
		
	}
	
	public User(String name) {
		this.mName = name;
	}
	
	public User(int id, String name) {
		this.mId = id;
		this.mName = name;
	}
	
	public void setId(int id) { mId = id; }
	public int id() { return mId; }
	
	public void setName(String name) { mName = name; }
	public String name() { return mName; }
}
