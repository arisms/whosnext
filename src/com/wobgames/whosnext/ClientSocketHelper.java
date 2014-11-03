package com.wobgames.whosnext;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import com.wobgames.whosnext.MainActivity.CounterClass;

import android.net.wifi.p2p.WifiP2pInfo;
import android.util.Log;

public class ClientSocketHelper {
	private String TAG = "CientSocketHelper";
	private MainActivity mActivity;
	private GameDevice mGameDevice;
	private Socket connectionSocket = new Socket();
	private boolean userIdReceived = false;
	//byte buf[]  = new byte[1024];
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
				Log.d(TAG, "connectToServerThread - run(), info.groupOwnerAddress: " + info.groupOwnerAddress.getHostAddress());
				try {
					connectionSocket.bind(null);
					InetSocketAddress serverAddr = new InetSocketAddress(info.groupOwnerAddress.getHostAddress(), mActivity.SERVER_PORT);
					//InetSocketAddress serverAddr = new InetSocketAddress(mActivity.SERVER_PORT);
					connectionSocket.connect(serverAddr, 500);
					mActivity.peersRemaining = false;
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
			Log.d(TAG, "SendMessageThread - run()");
			while(mMessage.type().equals("ANSWERS") && !userIdReceived) {
				// Wait...
			}
				
			byte buf[]  = new byte[1024];
			
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
			Log.d(TAG, "ReceiveMessageThread - run()");
			//ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int bytes;
			boolean dataAvailable = false;
			
			while(true) {
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte buf[]  = new byte[1024];
					InputStream inputStream = connectionSocket.getInputStream();
					
					// Wait for data from server
					while(inputStream.available() > 0 && (bytes = inputStream.read(buf, 0, buf.length)) > -1)
					{
						baos.write(buf, 0, bytes);
						dataAvailable = true;
					}
				   	
				   	if(dataAvailable)
				   	{
				   		final Message message = (Message) Serializer.deserialize(buf);
				   		dataAvailable = false;
				   		baos.flush();
				   		
				   		Log.i(TAG, "Received message from server: " + message.type());
				   		
				   		// Read message type
				   		if(message.type().equals("USER"))
				   		{
				   			//Log.i(TAG, "Received message from server: " + message.user().id() + " " + message.user().name());
				   			
				   			// Get userId from server, and add it to mainActivity's currentUser
				   			mActivity.currentUser.setId(message.user().id());
				   			userIdReceived = true;
				   			//mActivity.currentUser.setName(message.user().name());
				   		}
				   		else if(message.type().equals("START")) {
				   			mActivity.currentUsers = new ArrayList<User>(message.users_list);
				   			
				   			for(int i=0; i<mActivity.currentUsers.size(); i++) 
				   				Log.d(TAG, "currentUsers" + i + " Name: " + mActivity.currentUsers.get(i).name()
				   						+ " Id: " + mActivity.currentUsers.get(i).id());
				   			
				   			mActivity.runOnUiThread(new Runnable() {
								  public void run() {
									  //mActivity.currentUsers = new ArrayList<User>(message.users_list);
									  mActivity.startTimer();
									  mActivity.showToast(message.toast());
								  }
								});
						}
				   		else if(message.type().equals("PLAY")) {
				   			Log.d(TAG, "message.type().equals(PLAY)");
				   			
				   			mActivity.runOnUiThread(new Runnable() {
								  public void run() {
									  mActivity.showToast("Your turn to play!");
								  }
								});
				   			
				   			Log.d(TAG, "Current Answer, Text: " + message.currentAnswer().text()
				   					+ " UserId: " + message.currentAnswer().userId());
				   			
				   			mActivity.startTurn(message.currentAnswer());
				   			
				   		}
				   		else if(message.type().equals("GAME OVER")) {
				   			Log.d(TAG, "message.type().equals(GAME OVER)");
				   			mActivity.runOnUiThread(new Runnable() {
								  public void run() {
									  
						   			mActivity.gameOver(message);
								  }
								});
				   		}
				   		else if(message.type().equals("WRONG ANSWERS")) {
				   			Log.d(TAG, "WRONG ANSWERS");
				   			// Send wrongAnswersNumber to server
				   			Message msg = new Message();
				   			msg.setType("WRONG ANSWERS");
				   			msg.setWrongAnswers(mActivity.wrongAnswersNumber);
				   			sendToServer(msg);
				   		}
				   		else
				   		{
				   			Log.d(TAG, "Else");
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
