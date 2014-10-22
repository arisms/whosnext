package com.wobgames.whosnext;

import java.io.Serializable;
import java.util.ArrayList;

import android.util.Log;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mType;
	private User mUser;
	public ArrayList<Answer> clientAnswers;
	
	public Message() {
		Log.i("MESSAGE", "Message class created.");
		clientAnswers = new ArrayList<Answer>();
	}

	public void setType(String type) { mType = type; }
	public String type() { return mType; }
	
	public void setUser(User user) { mUser = user; }
	public User user() { return mUser; }
	
}
