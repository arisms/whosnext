package com.wobgames.whosnext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class GameMainFragment extends ListFragment{
	private static final String TAG = "ButtonsFragment";

	// Member data
	ListView mListView;
	List<User> mUsersList;
	List<Answer> mAnswersList;
	List<Question> mQuestionsList;
	DatabaseHelper mDBHelper;
	List<String> mUsersStrings;
	TextView questionAnswerTv;
	Random rand = new Random();
	Answer mRandomAnswer = new Answer();
	Question mRandomQuestion = new Question();
	
	// onCreateView
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.game_main_fragment, container, false);
	
		// Get data from the database
		mDBHelper = new DatabaseHelper(getActivity());
        mDBHelper.init();
        
        mUsersList = mDBHelper.getUsers();
        /*****/
        mUsersStrings = new ArrayList<String>();
        for(int i=0; i<mUsersList.size(); i++)
        	mUsersStrings.add(mUsersList.get(i).name());
        
        
		//Link ListView to the data
		mListView = (ListView) view.findViewById(android.R.id.list);

		QuestionsAnswersListAdapter mAdapter = new QuestionsAnswersListAdapter(getActivity(), mUsersStrings);

		mListView.setAdapter(mAdapter);
		
		// Get a list of all the Answers and Questions in the database
		mAnswersList = mDBHelper.getAnswers();
		mQuestionsList = mDBHelper.getQuestions();
		
		// Choose a random answer from the list
//		getRandomAnswer(mAnswersList);
//		
//		// Find the question with the same id
//		for(int i=0; i<mQuestionsList.size(); i++)
//		{
//			if(mQuestionsList.get(i).id() == mRandomAnswer.questionId())
//			{
//				mRandomQuestion = mQuestionsList.get(i);
//				break;
//			}
//		}
		
		//mRandomQuestion = mDBHelper.getQuestionById(10);
		
		questionAnswerTv = (TextView) view.findViewById(R.id.random_question_answer);
		questionAnswerTv.setText("Q: " + mQuestionsList.get(6).text() + '\n' 
				+ "A: " + mAnswersList.get(6).text());
		
		return view;
	}
	
	public void getRandomAnswer(List<Answer> mAnswersList) {
		
		int randomId = randInt(1, mAnswersList.size());
		
		/******** !!!!!!!! *******/
		// REPLACE WITH SELECT WHERE ID = RANDOMID
		mRandomAnswer = mAnswersList.get(randomId);
		
		//return mRandomAnswer;
	}
	
	public int randInt(int min, int max) {
	    
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
}
