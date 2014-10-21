package com.wobgames.whosnext;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.net.wifi.p2p.WifiP2pInfo;
import android.util.Log;

public class ClientSocketHelper {
	private String TAG = "CientSocketHelper";
	private MainActivity mActivity;
	private GameDevice mGameDevice;
	private Socket connectionSocket = new Socket();
	byte buf[]  = new byte[1024];
	Message mMessage;
	
	public ClientSocketHelper(MainActivity activity) {
		mActivity = activity;
		mGameDevice = activity.mGameDevice;
	}
	
	public void sendToServer(Message message) {
		mMessage = message;
		Thread sendThread = new SendMessageThread();
		sendThread.start();
	}
	
	public void receiveFromServer() {
		Thread receiveThread = new ReceiveMessageThread();
		receiveThread.start();
	}
	
	public void connect() {
		final WifiP2pInfo info = mGameDevice.info();
		
		Thread connectToServerThread = new Thread(new Runnable() {

			@Override
			public void run() {   
				Log.d(TAG, "Client Thread Started, info.groupOwnerAddress: " + info.groupOwnerAddress.getHostAddress());
				try {
					connectionSocket.bind(null);
					InetSocketAddress serverAddr = new InetSocketAddress(info.groupOwnerAddress.getHostAddress(), mActivity.SERVER_PORT);
					//InetSocketAddress serverAddr = new InetSocketAddress(mActivity.SERVER_PORT);
					connectionSocket.connect(serverAddr, 500);
					
				} catch (Exception e) {
			    	e.printStackTrace();
			    	Log.d(TAG, "Client exception");
			   	}
			}
	    });
		connectToServerThread.start();
		
	}
	
	public class SendMessageThread extends Thread {
		
		@Override
		public void run() {
			Log.d(TAG, "SendMessageThread: " + mMessage);
			
			
			try {
				OutputStream outputStream = connectionSocket.getOutputStream();
				
				Message msg = mMessage;
				buf = Serializer.serialize(msg);
				outputStream.write(buf, 0, buf.length);
				
			} catch (Exception e) {
				e.printStackTrace();
		    	Log.d(TAG, "Client exception");
			}
			
			
		}
	}
	
	public class ReceiveMessageThread extends Thread {
		
		@Override
		public void run() {
			Log.d(TAG, "ReceiveMessageThread in client running...");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int bytes;
			boolean dataAvailable = false;
			
			while(true) {
				try {
					InputStream inputStream = connectionSocket.getInputStream();
					
					// Wait for data from server
					while(inputStream.available() > 0 && (bytes = inputStream.read(buf, 0, buf.length)) > -1)
					{
						baos.write(buf, 0, bytes);
						dataAvailable = true;
					}
				   	
				   	if(dataAvailable)
				   	{
				   		Message message = (Message) Serializer.deserialize(buf);
				   		dataAvailable = false;
				   		baos.flush();
				   		
				   		// Read message type
				   		if(message.type().equals("USER"))
				   		{
				   			Log.i(TAG, "Received message from server: " + message.user().id() + " " + message.user().name());
				   		}
				   		else
				   		{
				   			Log.d(TAG, "Not user");
				   		}
				   			
				   	}
				   	
				}
				catch (Exception e) {
			    	e.printStackTrace();
			   	}
			}
		}
		
		
	}

}
