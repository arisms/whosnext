package com.wobgames.whosnext;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GameOverFragment extends Fragment{
	
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
    long updatedTime = 0L;
    TextView timerValue;


	private static final String TAG = "ImageFragment";
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		
		MainActivity mActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.game_over_fragment, container, false);

        TextView text = (TextView) view.findViewById(R.id.game_over_text);
        
        text.setText("Wrong Answers: " + mActivity.wrongAnswersNumber);
        
        return view;
	}
	
	

}
