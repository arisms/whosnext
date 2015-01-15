package com.wobgames.whosnext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wobgames.whosnext.QuestionsAnswersListAdapter.OnSelectPlayerListener;

public class GameMainFragment extends ListFragment implements OnSelectPlayerListener{
	private static final String TAG = "GameMainFragment";

	// Member data
	ListView mListView;
	//List<Answer> mAnswersList;
	//List<Question> mQuestionsList;
	DatabaseHelper mDBHelper;
	List<String> mUsersStrings;
	TextView headerTv;
	TextView questionTv;
	TextView answerTv;
	Random rand = new Random();
	Question mRandomQuestion;
	MainActivity mActivity;
	final CharSequence emptyAnswerToast = "Wrong player! Try again...";
	
	Button button;
	
	// onCreateView
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.game_main_fragment, container, false);
		
		
		mActivity = (MainActivity) getActivity();
		mDBHelper = mActivity.mDBHelper;
		
		if(!mActivity.mGameDevice.isGroupOwner())
			mActivity.wrongAnswersNumber = 0;
		
		// Wait for timer to be created in main Activity
		while(!mActivity.timerCreated) {
			// wait...
		}
		mActivity.textViewTime = (TextView) view.findViewById(R.id.mainTimer);
		mActivity.showTimer();
		
		// Get users from the database
        //mUsersList = mDBHelper.getUsers();
        
        /*****/
//		for(int i=0; i<mActivity.currentUsers.size(); i++)
//			Log.i(TAG, "In fragment currentUsers (global variable): " + i + ". " + mActivity.currentUsers.get(i)); //------------
		
		Log.d(TAG, "onCreateView() - mActivity.currentUsers.size()" + mActivity.currentUsers.size());
        mUsersStrings = new ArrayList<String>();
        for(int i=0; i<mActivity.currentUsers.size(); i++)
        	mUsersStrings.add(mActivity.currentUsers.get(i).name());
        
        
		//Link ListView to the data
		mListView = (ListView) view.findViewById(android.R.id.list);

		QuestionsAnswersListAdapter mAdapter = new QuestionsAnswersListAdapter(getActivity(), 
				mUsersStrings, this, mActivity.currentUsers);

		mListView.setAdapter(mAdapter);
		
		// Find the question with the same id as the one in currentAnswer
		mRandomQuestion = mDBHelper.getQuestionById(mActivity.currentAnswer.questionId());
		
		headerTv = (TextView) view.findViewById(R.id.main_game_header);
		headerTv.setTypeface(mActivity.exoregular);
		
		questionTv = (TextView) view.findViewById(R.id.random_question);
		questionTv.setTypeface(mActivity.exoregular);
		questionTv.setText(mRandomQuestion.text());
		
		answerTv = (TextView) view.findViewById(R.id.random_answer);
		answerTv.setTypeface(mActivity.exoregular);
		answerTv.setText(mActivity.currentAnswer.text());
		
		questionTv.requestFocus();
		
		return view;
	}
	
	public void wrongPlayer() {
		Toast toast = Toast.makeText(getActivity(), emptyAnswerToast, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 0);
		toast.show();
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		//mActivity.showToast("Position: " + position);
		onSelectPlayer(position);
    }

	@Override
	public void onSelectPlayer(int pos) {
		//final int position = id - 1;
		final int position = pos;

		// If the correct player is chosen, send dialog
		if(mActivity.currentAnswer.userId() == mActivity.currentUsers.get(position).id()) {
			mActivity.myRounds++;
			mActivity.roundsCompleted++;
			mActivity.nextTurn();
		}
		else {
			// If wrong player
			mActivity.v.vibrate(600);
			mActivity.myErrors++;
			mActivity.wrongAnswersNumber++;
			Toast toast = Toast.makeText(getActivity(), "Wrong player! Please try again...", Toast.LENGTH_SHORT);
//			Toast toast = Toast.makeText(getActivity(), "Wrong player! Please try again..." + '\n' 
//					+ mActivity.currentAnswer.userId() + " - " + mActivity.currentUsers.get(position).id(), Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 120);
			toast.show();
			mActivity.soundpool.play(mActivity.soundIds[5], (float)0.3, (float)0.3, 1, 0, 1);
		}
	}
	
	
	
	
	
}
