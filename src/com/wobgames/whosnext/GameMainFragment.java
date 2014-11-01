package com.wobgames.whosnext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
	TextView questionAnswerTv;
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
		//playSound();
		
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
		
		questionAnswerTv = (TextView) view.findViewById(R.id.random_question_answer);
		questionAnswerTv.setText("Q: " + mRandomQuestion.text() + '\n' 
				+ "A: " + mActivity.currentAnswer.text());
		
		questionAnswerTv.requestFocus();
		
		
//		button = (Button) view.findViewById(R.id.select_player_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//            	// If the answer text is empty
//            	
//            	wrongPlayer();
//            }
//        });
		
		
		return view;
	}
	
	public void wrongPlayer() {
		Toast toast = Toast.makeText(getActivity(), emptyAnswerToast, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	public void playSound() {
		
		MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.bell);
		mediaPlayer.start();
		
		/************/
		// MediaPlayer.setOnCompletionListener() <------!!!!!
		
	}

	@Override
	public void onSelectPlayer(int pos) {
		//final int position = id - 1;
		final int position = pos;
		
		Toast toast1 = Toast.makeText(getActivity(), "Position: " + position + " Name: " 
				+ mActivity.currentUsers.get(position).name() + " Id: " 
				+ mActivity.currentUsers.get(position).id() + " Answer-UserId: "
				+ mActivity.currentAnswer.userId(), Toast.LENGTH_SHORT);
		toast1.show();

		// If the correct player is chosen, send dialog
		if(mActivity.currentAnswer.userId() == mActivity.currentUsers.get(position).id())
			mActivity.nextTurn();
		else {
			// If wrong player
			mActivity.wrongAnswersNumber++;
			Toast toast = Toast.makeText(getActivity(), "Wrong player! Please try again..." + '\n' 
			+ mActivity.currentAnswer.userId() + " - " + mActivity.currentUsers.get(position).id(), Toast.LENGTH_SHORT);
			toast.show();
		}
		//wrongPlayer();
	}
	
	
	
	
	
}
