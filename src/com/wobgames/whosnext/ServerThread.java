package com.wobgames.whosnext;

import android.widget.Toast;

public class ServerThread {

	private int mId;
	private Thread mThread;
	private final MainActivity mActivity;
	
	public ServerThread(MainActivity activity) {
		
		mActivity = activity;
		
		// Create new thread
		mThread = new Thread() {

			@Override
		    public void run() {
				
				mActivity.runOnUiThread(new Runnable() {
				    public void run() {
				        //Toast.makeText(mActivity, "Hello", Toast.LENGTH_SHORT).show();
				    	netRunner("Android");
				    }
				});
				
			}
			
			
		};
		
		// Start the new thread
		mThread.start();
	}
	
	
	public void netRunner(String in) {
		String out;
		
		out = in + " Netrunner";
		
		Toast.makeText(mActivity, out, Toast.LENGTH_SHORT).show();
	}
	
	
}
