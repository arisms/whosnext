package com.wobgames.whosnext;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String mType;
	private User mUser;
	public ArrayList<Answer> answers_list;
	public ArrayList<User> users_list;
	private String mToast;
	private Answer currentAnswer;
	private int wrongAnswers;
	private int roundsCompleted;
	public int level;
	public int timerDuration;
	
	public Message() {
		answers_list = new ArrayList<Answer>();
		users_list = new ArrayList<User>();
	}

	public void setType(String type) { mType = type; }
	public String type() { return mType; }
	
	public void setUser(User user) { mUser = user; }
	public User user() { return mUser; }
	
	public void setToast(String toast) {mToast = toast; }
	public String toast() { return mToast; }
	
	public void setCurrentAnswer(Answer answer) { currentAnswer = answer; }
	public Answer currentAnswer() { return currentAnswer; }
	
	public void setWrongAnswers(int number) { wrongAnswers = number; }
	public int wrongAnswers() { return wrongAnswers; }
	
	public void setRoundsCompleted(int number) { roundsCompleted = number; }
	public int roundsCompleted() { return roundsCompleted; }
}
