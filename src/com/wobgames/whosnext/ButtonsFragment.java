package com.wobgames.whosnext;

import com.wobgames.whosnext.R;
import com.wobgames.whosnext.R.id;
import com.wobgames.whosnext.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ButtonsFragment extends Fragment {
	private static final String TAG = "ButtonsFragment";

	// Member Data
	Button mCreateGame;
	Button mJoinGame;
	OnButtonSelectedListener mListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView()");
		// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.buttons_fragment, container, false);
        
        // Buttons
        mCreateGame = (Button) view.findViewById(R.id.creategame);
        mCreateGame.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			mListener.onCreateGame();
    		}
    	});
        
        mJoinGame = (Button) view.findViewById(R.id.joingame);
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
	    if (activity instanceof OnButtonSelectedListener) {
	    	mListener = (OnButtonSelectedListener) activity;
	    } else {
	      throw new ClassCastException(activity.toString()
	          + " must implemenet ButtonsFragment.OnButtonSelectedListener");
	    }
	  }
	
	
	public interface OnButtonSelectedListener {
		
		public void onCreateGame();
		
		public void onJoinGame();
	}
}
