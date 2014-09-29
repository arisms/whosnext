package com.wobgames.whosnext;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

public class ServerService extends IntentService{

	public ServerService() {
	      super("ServerService");
	  }
	
	@Override
	protected void onHandleIntent(Intent intent) {
		
		// Normally we would do some work here, like download a file.
		// For our sample, we just sleep for 5 seconds.
	      
		long endTime = System.currentTimeMillis() + 5*1000;
	    while (System.currentTimeMillis() < endTime) {
	    	synchronized (this) {
	    		try {
	    			wait(endTime - System.currentTimeMillis());
	    		} catch (Exception e) {
	    		}
	    	}
    	}
	    Toast.makeText(this, "Server service started", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    Toast.makeText(this, "Server service started", Toast.LENGTH_SHORT).show();
	    return super.onStartCommand(intent,flags,startId);
	}
	
}
