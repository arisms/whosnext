package com.wobgames.whosnext;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.net.wifi.p2p.WifiP2pInfo;
import android.util.Log;

public class ClientSocketHelper {

	private MainActivity mActivity;
	private GameDevice mGameDevice;
	private Socket clientSocket = new Socket();
	byte buf[]  = new byte[1024];
	
	public ClientSocketHelper(MainActivity activity) {
		mActivity = activity;
		mGameDevice = activity.mGameDevice;
	}
	
	
	public void connect() {
		final WifiP2pInfo info = mGameDevice.info();
		
		Thread clientThread = new Thread(new Runnable() {

			@Override
			public void run() {   
				Log.d(mActivity.TAG, "CLIENT THREAD STARTED, info.groupOwnerAddress: " + info.groupOwnerAddress.getHostAddress());
				try {
					clientSocket.bind(null);
					InetSocketAddress serverAddr = new InetSocketAddress(info.groupOwnerAddress.getHostAddress(), mActivity.SERVER_PORT);
					clientSocket.connect(serverAddr, 1000);
					
					OutputStream outputStream = clientSocket.getOutputStream();
					
					String msg = "ASDF";
					buf = msg.getBytes();
					
					outputStream.write(buf, 0, 4);
					Log.d("Output Stream", "" + buf.toString());
					
				} catch (Exception e) {
			    	e.printStackTrace();
			    	Log.d(mActivity.TAG, "CLIENT EXCEPTION");
			   	}
			}
	    });
		clientThread.start();
		
	}
	
	
	
	
//	Socket  clientSocket = new Socket(ServerIP,ServerPort);
//	outputStream = clientSocket.getOutputStream();
//	bufferedReader=newBufferedReader(new 
//	InputStreamReader(clientSocket.getInputStream()));
}
