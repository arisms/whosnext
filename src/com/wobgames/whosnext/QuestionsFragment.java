package com.wobgames.whosnext;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionsFragment extends Fragment{
	
	private static final String TAG = "QuestionsFragment";
	
	Button button;
	List<Question> questions_list;
	DatabaseHelper mDBHelper;
	List<String> mQuestionsStrings;
	TextView mHeaderTv;
	TextView mQuestionTv;
	EditText mAnswerEt;
	int mQuestionCounter = 0;
	int mTotalQuestions;
	final CharSequence emptyAnswerToast = "Answer cannot be empty!";
	boolean NAME = true;
	OnStartGameSelectedListener mListener;
	MainActivity mActivity;
	private ArrayList<Answer> clientAnswers;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		mActivity = (MainActivity) getActivity();
		clientAnswers = new ArrayList<Answer>();
		
		// PROGRESS BAR
		mDBHelper = new DatabaseHelper(getActivity());
        questions_list = mDBHelper.getQuestions();
        mTotalQuestions = questions_list.size();
	
        /*****/
        mQuestionsStrings = new ArrayList<String>();
        for(int i=0; i<questions_list.size(); i++)
        	mQuestionsStrings.add(questions_list.get(i).text());
        
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.questions_fragment, container, false);
        
        mHeaderTv = (TextView) view.findViewById(R.id.questions_header);
        mHeaderTv.setTypeface(mActivity.exoregular);
        mQuestionTv = (TextView) view.findViewById(R.id.question_text);
        mQuestionTv.setTypeface(mActivity.exoregular);
        mAnswerEt = (EditText) view.findViewById(R.id.answer_text);
        mAnswerEt.setTypeface(mActivity.exoregular);
        
        button = (Button) view.findViewById(R.id.submit_question_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	nextQuestion();
            }
        });
        
        // Insert user name
        nextQuestion();
        
        return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		//Log.d(TAG, "onAttach()");
	    super.onAttach(activity);
	    if (activity instanceof OnStartGameSelectedListener) {
	    	mListener = (OnStartGameSelectedListener) activity;
	    } else {
	      throw new ClassCastException(activity.toString()
	          + " must implemenet QuestionsFragment.OnStartGameSelectedListener");
	    }
	  }
        
	public void nextQuestion()
	{
		Log.d(TAG, "nextQuestion()");
		// Set user name
		if(NAME)
		{
			Log.d(TAG, "nextQuestion() - NAME");
			// Create currentUser object
			mActivity.currentUser = new User("");
			
			mQuestionTv.setText("What is your name?");
			
			// Clear answer text
			mAnswerEt.setText("");
	        
			//button.setText(getResources().getString(R.string.next_button));
	        NAME = false;
	        
	        return;
		}
		else
		{
			// If name in currentUser is empty, send user-name to server
			if(mActivity.currentUser.name().equals(""))
			{
				Log.d(TAG, "nextQuestion() - currentUser.name is empty");
				mActivity.currentUser.setName(mAnswerEt.getText().toString());
				
				Message userMessage = new Message();
				
				// If the current device is the server device
				if(mActivity.mGameDevice.isGroupOwner())
				{
					//message.setType(mAnswerEt.getText().toString() + " - G.O.");
					mActivity.sHelper.addOwnUser();
					
					
				}
				else
				{
					mActivity.currentUser.setName(mAnswerEt.getText().toString());

					userMessage.setType("USER");
					userMessage.setUser(mActivity.currentUser);
					
					// Send the User info to the server (user-name) and wait to receive the id
					mActivity.cHelper.sendToServer(userMessage);
					//mActivity.cHelper.receiveFromServer();
					
				}
				
			}
			// Else store answer to clientAnswers list
			else
			{
				Log.d(TAG, "nextQuestion() - currentUser.name NOT empty, adding answer to list");
				Answer answer = new Answer(mAnswerEt.getText().toString(), 
						mActivity.currentUser.id(), questions_list.get(mQuestionCounter-1).id());
				clientAnswers.add(answer);
			}
		}
		
		// Answer cannot be empty
		if(mAnswerEt.getText().toString().trim().length() == 0)
    	{
    		Toast toast = Toast.makeText(getActivity(), emptyAnswerToast, Toast.LENGTH_SHORT);
    		toast.setGravity(Gravity.CENTER, 0, 0);
    		toast.show();
    		
    		return;
    	}
		
		// If Start Game is selected, replace the fragment
		if(mQuestionCounter == mTotalQuestions)
		{
			Log.d(TAG, "nextQuestion() - start game selected");
			Message answersMessage = new Message();
			answersMessage.setType("ANSWERS");
			answersMessage.setUser(mActivity.currentUser);
			
			// Update userId in answers
			for(int i=0; i<clientAnswers.size(); i++)
				clientAnswers.get(i).setUserId(mActivity.currentUser.id());
							
			for(int i=0; i<clientAnswers.size(); i++) {
				answersMessage.answers_list.add(clientAnswers.get(i));
			}
			
			// If the current device is the server
			if(mActivity.mGameDevice.isGroupOwner())
				mActivity.sHelper.addOwnAnswers(answersMessage);
			else {
				// Send the list of answers to the server
				mActivity.cHelper.sendToServer(answersMessage);
			}
			
			// Hide the soft keyboard
			InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mAnswerEt.getWindowToken(), 0);
			
			mAnswerEt.clearFocus();
			button.requestFocus();
			
//			if(mActivity.mGameDevice.isGroupOwner())
//				mActivity.getSupportFragmentManager().beginTransaction()
//					.replace(R.id.rootlayout, mActivity.mImageFragment).addToBackStack(null).commit();
			
			// Start the game!
			mListener.onStartGame();
			
			return;
		}
		
		// Clear answer text
		mAnswerEt.setText("");
		
		// Set question text
		mQuestionTv.setText(mQuestionsStrings.get(mQuestionCounter));
		mQuestionTv.append(" (" + (mQuestionCounter+1) + "/" + mTotalQuestions + ")");
		
		// For last question, change the button
		if(mQuestionCounter == mTotalQuestions-1)
		{
			//button.setText(getResources().getString(R.string.start_button));
			button.setBackground(getResources().getDrawable(R.drawable.start_button));
		}
		
		mQuestionCounter++;
		
	}
	
	public interface OnStartGameSelectedListener {
		
		public void onStartGame();
		
	}
}