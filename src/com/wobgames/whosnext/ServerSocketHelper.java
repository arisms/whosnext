package com.wobgames.whosnext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class ServerSocketHelper {

	private String TAG = "ServerSocketHelper";
	private ServerSocket serverSocket;
	private Socket connectionSocket;
	MainActivity mActivity;
	byte buf[]  = new byte[1024];
	
	
	public ServerSocketHelper(MainActivity activity) {
		mActivity = activity;
	}
	
	public void connect() {
		
		Thread serverThread = new ConnectServerThread();
		serverThread.start();
	}
	
	public void receiveMessage() {
		Thread msgThread = new ConnectedServerThread();
		msgThread.start();
	}
	
	public void disconnect() {
		try {
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Thread that creates a socket and accepts incoming connections from clients
	public class ConnectServerThread extends Thread {
		int counter = 0;
		
		 @Override
		 public void run() {         
	    	Log.d(TAG, "SERVER THREAD STARTED");
	    	
	    	// Create new Socket
		    try {
		    	serverSocket = new ServerSocket(mActivity.SERVER_PORT);
		    	Log.d(TAG, "ConnectServerThread - new socket");
		    } 
		    catch (Exception e) {
		    	e.printStackTrace();
		    	Log.d(TAG, "ConnectServerThread - new socket exception");
		   	}
		    
		    // Accept connections from all the clients _o/
		    while(true) {
		    	try {
		    		connectionSocket = serverSocket.accept();
		    		Log.d(TAG, "ConnectServerThread - accept() - " + connectionSocket.getInetAddress());
		    		
		    		counter++;
		    		// If the number of connected devices has reached the number of peers
		    		// exit the loop and create a ConnectedServerThread
		    		if(counter == mActivity.totalPeers)
		    		{
		    			receiveMessage();
		    			Log.d(TAG, "ConnectServerThread - returning with counter = " + counter);
		    			return;
		    		}
		    		
		    	} catch (IOException e) {
		    		e.printStackTrace();
		    		Log.d(TAG, "ConnectServerThread - accept() exception");
		    	}
		    }
		 }
	}
	
	// Thread that performs data exchange between the server and the clients
	public class ConnectedServerThread extends Thread {
	
		@Override
		public void run() {
			//Log.d(TAG, "ConnectedServerThread running...");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int bytes;
			boolean dataAvailable = false;
			//User user;
			
			while(true) {
				try {
					InputStream inputStream = connectionSocket.getInputStream();
					
					// Wait for data from client
					while(inputStream.available() > 0 && (bytes = inputStream.read(buf, 0, buf.length)) > -1)
					{
						baos.write(buf, 0, bytes);
						dataAvailable = true;
					}
					//baos.flush();
				   	//Log.d("Client's InetAddress", "" + connectionSocket.getInetAddress());
				   	
				   	if(dataAvailable)
				   	{
				   		String msg = (String) Serializer.deserialize(buf);
				   		dataAvailable = false;
				   		baos.flush();
					   	Log.d("Input Stream ", msg);
				   	}
				   	
				}
				catch (Exception e) {
			    	e.printStackTrace();
			   	}
			}
		}
	}
}
