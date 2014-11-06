package com.wobgames.whosnext;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ButtonsFragment extends Fragment {
	private static final String TAG = "ButtonsFragment";

	// Member Data
	Button mCreateGame;
	Button mJoinGame;
	ButtonsFragmentListener mListener;
	
	TextView mainHeader;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView()");
		// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.buttons_fragment, container, false);
        MainActivity mActivity = (MainActivity) getActivity();
        
        
        // Text
        mainHeader = (TextView) view.findViewById(R.id.main_header);
        mainHeader.setTypeface(mActivity.exoregular);
        mainHeader.setText("Who's next?");
        
        // Buttons
        mCreateGame = (Button) view.findViewById(R.id.creategame);
        //mCreateGame.setTypeface(mActivity.exoregular);
        mCreateGame.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			mListener.onCreateGame();
    		}
    	});
        
        mJoinGame = (Button) view.findViewById(R.id.joingame);
        //mJoinGame.setTypeface(mActivity.exoregular);
        mJoinGame.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			mListener.onJoinGame();
    		}
    	});
        
        return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		Log.d(TAG, "onAttach()");
	    super.onAttach(activity);
	    if (activity instanceof ButtonsFragmentListener) {
	    	mListener = (ButtonsFragmentListener) activity;
	    } else {
	      throw new ClassCastException(activity.toString()
	          + " must implemenet ButtonsFragment.OnButtonSelectedListener");
	    }
	  }
	
	
	public interface ButtonsFragmentListener {
		
		public void onCreateGame();
		
		public void onJoinGame();
	}
}
