package com.wobgames.whosnext;

import com.wobgames.whosnext.ButtonsFragment.ButtonsFragmentListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class GameOverFragment extends Fragment{
	
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
    long updatedTime = 0L;
    TextView timerValue;
    Button mQuitButton;
    Button mRestartButton;
    GameOverFragmentListener mListener;
    TextView header;
    TextView text1;
    TextView text2;
    TextView text3;
    MainActivity mActivity;
    boolean clientTextsSet = false;


	private static final String TAG = "GameOverFragment";
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		
		mActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.game_over_fragment, container, false);

        header = (TextView) view.findViewById(R.id.game_over_header);
        header.setTypeface(mActivity.exoregular);
        
        text1 = (TextView) view.findViewById(R.id.game_over_text1);
        text2 = (TextView) view.findViewById(R.id.game_over_text2);
        text3 = (TextView) view.findViewById(R.id.game_over_text3);
        
        // If the current device is the Server, display game-over text
        if(mActivity.mGameDevice.isGroupOwner()) {
        	text1.setText("Wrong Answers: " + mActivity.wrongAnswersNumber);
        	text2.setText("Rounds Completed: " + (mActivity.sHelper.turnCounter-1));
        	int score = (mActivity.sHelper.turnCounter-1)*100-mActivity.wrongAnswersNumber*50;
        	text3.setText("Total Score: " + '\n' + (mActivity.sHelper.turnCounter-1) + "x100 - "
        			+ mActivity.wrongAnswersNumber + "x50 = " + score);
        	
        	if(mActivity.timeUp)
            	header.setText("Time up!");
            else
            	header.setText("Game completed!");
        }
        else if(!clientTextsSet) {
        	if(mActivity.timeUp)
            	header.setText("Time up!");
            else
            	header.setText("Game completed!");
        	
        	text1.setText("Wrong Answers: " + mActivity.wrongAnswersNumber);
        	text2.setText("Rounds Completed: " + (mActivity.roundsCompleted));
        	int score = (mActivity.roundsCompleted)*100-mActivity.wrongAnswersNumber*50;
        	text3.setText("Total Score: " + '\n' + (mActivity.roundsCompleted) + "x100 - "
        			+ mActivity.wrongAnswersNumber + "x50 = " + score);
        	clientTextsSet = true;
        }
        
        mRestartButton = (Button) view.findViewById(R.id.restart_game_button);
        if(!(mActivity.mGameDevice.isGroupOwner()))
        	mRestartButton.setVisibility(android.view.View.INVISIBLE);
        else {
	        mRestartButton.setOnClickListener(new View.OnClickListener() {
	    		@Override
	    		public void onClick(View v) {
	    			mListener.onRestartButton();
	    		}
	    	});
        }
        
        mQuitButton = (Button) view.findViewById(R.id.quit_game_button);
        mQuitButton.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			mListener.onQuitButton();
    		}
    	});
        
        return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		Log.d(TAG, "onAttach()");
	    super.onAttach(activity);
	    if (activity instanceof ButtonsFragmentListener) {
	    	mListener = (GameOverFragmentListener) activity;
	    } else {
	      throw new ClassCastException(activity.toString()
	          + " must implemenet GameOverFragment.GameOverFragmentListener");
	    }
	  }
	
	public interface GameOverFragmentListener {
		
		public void onRestartButton();
		
		public void onQuitButton();
	}

	public void updateText() {
		if(!clientTextsSet) {
			text1.setText("Wrong Answers: " + mActivity.wrongAnswersNumber);
	    	text2.setText("Rounds Completed: " + (mActivity.roundsCompleted));
	    	int score = (mActivity.roundsCompleted)*100-mActivity.wrongAnswersNumber*50;
	    	text3.setText("Total Score: " + '\n' + (mActivity.roundsCompleted) + "x100 - "
	    			+ mActivity.wrongAnswersNumber + "x50 = " + score);
	    	
	    	if(mActivity.timeUp)
	        	header.setText("Time up!");
	        else
	        	header.setText("Game completed!");
		}
	}
}
