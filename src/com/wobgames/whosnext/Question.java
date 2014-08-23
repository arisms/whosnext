package com.wobgames.whosnext;

public class Question {

	private int mId;
	private String mText;
	private int mRound;
	
	public Question() {
		
	}
	
	public Question(int id, String text, int round) {
		this.mId = id;
		this.mText = text;
		this.mRound = round;
	}
	
	public void setId(int id) { mId = id; }
	public int id() { return mId; }
	
	public void setText(String text) { mText = text; }
	public String text() { return mText; }
	
	public void setRound(int round) { mRound = round; }
	public int round() { return mRound; }
	
}
