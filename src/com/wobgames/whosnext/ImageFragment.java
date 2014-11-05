package com.wobgames.whosnext;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ImageFragment extends Fragment{
	
	TextView wait1;
	
	private static final String TAG = "ImageFragment";
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		
        View view = inflater.inflate(R.layout.image_fragment, container, false);
        MainActivity mActivity = (MainActivity) getActivity();
        
        wait1 = (TextView) view.findViewById(R.id.wait1);
        wait1.setTypeface(mActivity.exoregular);
        
        //MainActivity mActivity = (MainActivity) getActivity();
        /****************** TIMER ******************/

		/******************************************/

        return view;
	}
	
}
