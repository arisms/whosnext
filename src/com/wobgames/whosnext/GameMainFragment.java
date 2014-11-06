package com.wobgames.whosnext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pDevice;
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
		
		questionTv = (TextView) view.findViewById(R.id.random_question);
		answerTv = (TextView) view.findViewById(R.id.random_answer);
		questionTv.setText(mRandomQuestion.text());
		answerTv.setText(mActivity.currentAnswer.text());
		
		questionTv.requestFocus();
		
		
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
		if(mActivity.currentAnswer.userId() == mActivity.currentUsers.get(position).id())
			mActivity.nextTurn();
		else {
			// If wrong player
			mActivity.wrongAnswersNumber++;
			Toast toast = Toast.makeText(getActivity(), "Wrong player! Please try again...", Toast.LENGTH_SHORT);
//			Toast toast = Toast.makeText(getActivity(), "Wrong player! Please try again..." + '\n' 
//					+ mActivity.currentAnswer.userId() + " - " + mActivity.currentUsers.get(position).id(), Toast.LENGTH_SHORT);
			toast.show();
			mActivity.soundpool.play(mActivity.soundIds[5], (float)0.3, (float)0.3, 1, 0, 1);
		}
	}
	
	
	
	
	
}
