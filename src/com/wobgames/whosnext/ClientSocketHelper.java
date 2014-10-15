package com.wobgames.whosnext;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.net.wifi.p2p.WifiP2pInfo;
import android.util.Log;

public class ClientSocketHelper {
	private String TAG = "CientSocketHelper";
	private MainActivity mActivity;
	private GameDevice mGameDevice;
	private Socket clientSocket = new Socket();
	byte buf[]  = new byte[1024];
	String mMessage;
	
	public ClientSocketHelper(MainActivity activity) {
		mActivity = activity;
		mGameDevice = activity.mGameDevice;
	}
	
	public void sendToServer(String message) {
		mMessage = message;
		Thread msgThread = new SendMessageThread();
		msgThread.start();
	}
	
	
	public void connect() {
		final WifiP2pInfo info = mGameDevice.info();
		
		Thread clientThread = new Thread(new Runnable() {

			@Override
			public void run() {   
				Log.d(TAG, "Client Thread Started, info.groupOwnerAddress: " + info.groupOwnerAddress.getHostAddress());
				try {
					clientSocket.bind(null);
					InetSocketAddress serverAddr = new InetSocketAddress(info.groupOwnerAddress.getHostAddress(), mActivity.SERVER_PORT);
					//InetSocketAddress serverAddr = new InetSocketAddress(mActivity.SERVER_PORT);
					clientSocket.connect(serverAddr, 500);
					
				} catch (Exception e) {
			    	e.printStackTrace();
			    	Log.d(TAG, "Client exception");
			   	}
			}
	    });
		clientThread.start();
		
	}
	
	public class SendMessageThread extends Thread {
		
		@Override
		public void run() {
			Log.d(TAG, "SendMessageThread: " + mMessage);
			
			
			try {
				OutputStream outputStream = clientSocket.getOutputStream();
				
				String msg = mMessage;
				buf = Serializer.serialize(msg);
				outputStream.write(buf, 0, buf.length);
				
				String temp = new String(buf, "UTF-8");
				Log.d("Output Stream", temp);
				
			} catch (Exception e) {
				e.printStackTrace();
		    	Log.d(TAG, "Client exception");
			}
			
			
		}
	}

}
