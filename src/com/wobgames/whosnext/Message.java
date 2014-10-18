package com.wobgames.whosnext;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mType;
	private User mUser;
	
	public Message() {
		
	}

	public void setType(String type) { mType = type; }
	public String type() { return mType; }
	
	public void setUser(User user) { mUser = user; }
	public User user() { return mUser; }
	
}
