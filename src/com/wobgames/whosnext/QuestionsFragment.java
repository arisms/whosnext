package com.wobgames.whosnext;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	TextView mQuestionTv;
	EditText mAnswerEt;
	int mQuestionCounter = 0;
	int mTotalQuestions;
	final CharSequence emptyAnswerToast = "Answer cannot be empty!";
	boolean NAME = true;
	OnStartGameSelectedListener mListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
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
        
        mQuestionTv = (TextView) view.findViewById(R.id.question_text);
        mAnswerEt = (EditText) view.findViewById(R.id.answer_text);
        
        button = (Button) view.findViewById(R.id.submit_question_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// If the answer text is empty
            	
            	nextQuestion();
            }
        });
        
        // Insert user name
        nextQuestion();
        
        return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		Log.d(TAG, "onAttach()");
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
		// Set user name
		if(NAME)
		{
			mQuestionTv.setText("What is your name?");
			
			// Clear answer text
			mAnswerEt.setText("");
	        
			button.setText(getResources().getString(R.string.next_button));
	        NAME = false;
	        
	        return;
		}
		
		// Empty answer
//		if(mAnswerEt.getText().toString().trim().length() == 0)
//    	{
//    		Toast toast = Toast.makeText(getActivity(), emptyAnswerToast, Toast.LENGTH_SHORT);
//    		toast.setGravity(Gravity.CENTER, 0, 0);
//    		toast.show();
//    		
//    		return;
//    	}
		
		// If Start Game is selected replace the fragment
		if(mQuestionCounter == mTotalQuestions)
		{
			// 
			mAnswerEt.clearFocus();
			button.requestFocus();
			
			// Commit answer to database
			// ...
			
			// Replace the fragment
			mListener.onStartGame();
			return;
		}
		
		// Clear answer text
		mAnswerEt.setText("");
		
		// Set question text
		mQuestionTv.setText(mQuestionsStrings.get(mQuestionCounter));
		mQuestionTv.append(" (" + (mQuestionCounter+1) + "/" + mTotalQuestions + ")");
		
		// For last question, change the button text
		if(mQuestionCounter == mTotalQuestions-1)
		{
			button.setText(getResources().getString(R.string.start_button));
		}
		
		// Commit
		
		mQuestionCounter++;
		
	}
	
	public interface OnStartGameSelectedListener {
		
		public void onStartGame();
		
	}
}