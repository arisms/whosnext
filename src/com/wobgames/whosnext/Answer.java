package com.wobgames.whosnext;

import java.io.Serializable;

public class Answer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int mId;
	private String mText;
	private int mUserId;
	private int mQuestionId;
	private boolean used;
	
	public Answer() { 
		
	}
	
	public Answer(String text, int userId, int questionId) {
		this.mText = text;
		this.mUserId = userId;
		this.mQuestionId = questionId;
		this.used = false;
	}
	
	public void setId(int id) { mId = id; }
	public int id() { return mId; }
	
	public void setText(String text) { mText = text; }
	public String text() { return mText; }
	
	public void setUserId(int userId) { mUserId = userId; }
	public int userId() { return mUserId; }
	
	public void setQuestionId(int questionId) { mQuestionId = questionId; }
	public int questionId() { return mQuestionId; }
	
	public void setUsed(boolean value) { used = value; }
	public boolean used() { return used; }
}
