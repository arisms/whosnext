package com.wobgames.whosnext;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ImageFragment extends Fragment{
	
	private long startTime = 0L;
	private Handler customHandler = new Handler();
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
    long updatedTime = 0L;
    TextView timerValue;


	private static final String TAG = "ImageFragment";
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		
        View view = inflater.inflate(R.layout.image_fragment, container, false);
        
        /****************** TIMER ******************/

//		timerValue = (TextView) view.findViewById(R.id.timerValue);
//		startTime = SystemClock.uptimeMillis();
//		customHandler.postDelayed(updateTimerThread, 0);

		/******************************************/

        return view;
	}
	
	
	private Runnable updateTimerThread = new Runnable() {
		public void run() {
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			updatedTime = timeSwapBuff + timeInMilliseconds;

			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			int milliseconds = (int) (updatedTime % 1000);
			timerValue.setText("" + mins + ":"
				+ String.format("%02d", secs) + ":"
				+ String.format("%03d", milliseconds));
				customHandler.postDelayed(this, 0);
		}
	};
}
