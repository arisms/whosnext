package com.wobgames.whosnext;

public class Answer {

	private int mId;
	private String mText;
	private int mUserId;
	private int mQuestionId;
	
	public Answer() { 
		
	}
	
	public Answer(int id, String text, int userId, int questionId) {
		this.mId = id;
		this.mText = text;
		this.mUserId = userId;
		this.mQuestionId = questionId;
	}
	
	public void setId(int id) { mId = id; }
	public int id() { return mId; }
	
	public void setText(String text) { mText = text; }
	public String text() { return mText; }
	
	public void setUserId(int userId) { mUserId = userId; }
	public int userId() { return mUserId; }
	
	public void setQuestionId(int questionId) { mQuestionId = questionId; }
	public int questionId() { return mQuestionId; }
	
}
