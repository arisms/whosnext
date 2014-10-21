package com.wobgames.whosnext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class ServerSocketHelper {

	private String TAG = "ServerSocketHelper";
	private ServerSocket serverSocket;
	private Socket connectionSocket;
	MainActivity mActivity;
	
	
	public ServerSocketHelper(MainActivity activity) {
		mActivity = activity;
	}
	
	public void connect() {
		
		Thread serverThread = new ConnectServerThread();
		serverThread.start();
	}
	
	public void receiveMessage(Socket connectionSocket) {
		Thread receiveThread = new ReceiveFromClientsThread(connectionSocket);
		receiveThread.start();
	}
	
	public void sendMessage(Socket connectionSocket, Message msg) {
		Thread sendThread = new SendToClientThread(connectionSocket, msg);
		sendThread.start();
	}
	
	public void disconnect() {
		try {
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addOwnUser() {
		
		//User user = new User(name);
		int id = (int)mActivity.mDBHelper.addUser(mActivity.currentUser);
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
		    
		    // Accept connections from all the clients
		    while(true) {
		    	try {
		    		connectionSocket = serverSocket.accept();
		    		//Log.d(TAG, "ConnectServerThread - accept() - " + connectionSocket.getInetAddress());
		    		
		    		// Create a ConnectedServerThread for that peer
		    		receiveMessage(connectionSocket);
		    		
		    		counter++;
		    		// If the number of connected devices has 
		    		//reached the number of peers, exit the loop 
		    		if(counter == mActivity.totalPeers)
		    		{
		    			//Log.d(TAG, "ConnectServerThread - returning with counter = " + counter);
		    			mActivity.peersRemaining = false;
		    			return;
		    		}
		    		
		    	} catch (IOException e) {
		    		e.printStackTrace();
		    		Log.d(TAG, "ConnectServerThread - accept() exception");
		    	}
		    }
		 }
	}
	
	/** Thread that receives data from the client **/
	public class ReceiveFromClientsThread extends Thread {
		private Socket clientSocket;
		byte buf[]  = new byte[1024];
		
		public ReceiveFromClientsThread(Socket clientConnectionSocket) {
			clientSocket = clientConnectionSocket;
			Log.d(TAG, "ReceiveFromClientsThread created...");
		}
		
		@Override
		public void run() {
			Log.d(TAG, "ReceiveFromClientsThread running...");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int bytes;
			boolean dataAvailable = false;
			
			while(true) {
				try {
					//Log.i(TAG, "Server receive thread entered try");
					InputStream inputStream = clientSocket.getInputStream();
					
					// Wait for data from client
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
					   	//Log.d("Input Stream ", msg.type());
				   		
				   		// Read message type
				   		if(message.type().equals("USER"))
				   		{
				   			// Add user in database
				   			Log.d("Input Stream - User name: ", message.user().name());
				   			message.user().setId((int)mActivity.mDBHelper.addUser(message.user()));
				   			sendMessage(clientSocket, message);
				   		}
				   		else
				   		{
				   			Log.d(TAG, "It's an answer!");
				   		}
				   			
				   	}
				   	
				}
				catch (Exception e) {
			    	e.printStackTrace();
			   	}
			}
		}
	}
	
	/** Thread that sends data to the client **/
	public class SendToClientThread extends Thread {
		private Socket clientSocket;
		private Message message;
		byte buf[]  = new byte[1024];
		
		public SendToClientThread(Socket clientConnectionSocket, Message msg) {
			clientSocket = clientConnectionSocket;
			message = msg;
		}
		
		@Override
		public void run() {
			
			try {
				OutputStream outputStream = clientSocket.getOutputStream();
				
				buf = Serializer.serialize(message);
				outputStream.write(buf, 0, buf.length);
				
				Log.i(TAG, "Sent message to client: " + message.user().id() + " " + message.user().name());
				
			} catch (Exception e) {
				e.printStackTrace();
		    	Log.d(TAG, "Server exception");
			}
			
			
		}
	}
	
}
