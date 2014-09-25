package com.wobgames.whosnext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.media.MediaPlayer;
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
	List<User> mUsersList;
	List<Answer> mAnswersList;
	List<Question> mQuestionsList;
	DatabaseHelper mDBHelper;
	List<String> mUsersStrings;
	TextView questionAnswerTv;
	Random rand = new Random();
	Answer mRandomAnswer = new Answer();
	Question mRandomQuestion = new Question();
	final CharSequence emptyAnswerToast = "Wrong player! Try again...";
	
	Button button;
	
	// onCreateView
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.game_main_fragment, container, false);
	
		playSound();
		
		// Hide keyboard
//		final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//	    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
		
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

		QuestionsAnswersListAdapter mAdapter = new QuestionsAnswersListAdapter(getActivity(), mUsersStrings, this, mUsersList);

		mListView.setAdapter(mAdapter);
		
		// Get a list of all the Answers and Questions in the database
		mAnswersList = mDBHelper.getAnswers();
		mQuestionsList = mDBHelper.getQuestions();
		
		// Choose a random answer from the list
		getRandomAnswer(mAnswersList);
		
		// Find the question with the same id
		mRandomQuestion = mDBHelper.getQuestionById(mRandomAnswer.questionId());
		
		questionAnswerTv = (TextView) view.findViewById(R.id.random_question_answer);
		questionAnswerTv.setText("Q: " + mRandomQuestion.text() + '\n' 
				+ "A: " + mRandomAnswer.text());
		
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
	
	public void getRandomAnswer(List<Answer> mAnswersList) {
		
		int randomId = randInt(1, mAnswersList.size());
		
		// Get function?
		mRandomAnswer = mAnswersList.get(randomId);
		//Log.i(TAG, "randomId = " + randomId);
		//return mRandomAnswer;
	}
	
	public int randInt(int min, int max) {
	    
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
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
	public void onSelectPlayer(int id) {
		final int position = id - 1;
		
		Toast toast = Toast.makeText(getActivity(), "" + mUsersList.get(position).name(), Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		
		
		
		// If wrong player
		//wrongPlayer();
	}
}
