package com.wobgames.whosnext;

import java.util.HashMap;
import java.util.Map;

import android.app.IntentService;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class ServerService extends IntentService {

	private final String TAG = "ServerService";
	public int SERVER_PORT = 8080;
	//private 
	
	public ServerService() {
	      super("ServerService");
	  }
	
	@Override
	protected void onHandleIntent(Intent intent) {
		//String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		Log.d(TAG, "onHandleIntent");

	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    //Toast.makeText(this, "Server service started", Toast.LENGTH_SHORT).show();
	    
	    return super.onStartCommand(intent,flags,startId);
	}
		
	private void showToast(String toast) {
		
		final String message = toast;
		
		Handler mHandler = new Handler(getMainLooper());
	    mHandler.post(new Runnable() {
	        @Override
	        public void run() {
	            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	        }
	    });
	}
	
}















