package com.wobgames.whosnext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class ServerSocketHelper {

	private String TAG = "ServerSocketHelper";
	private ServerSocket serverSocket;
	private Socket connectionSocket;
	MainActivity mActivity;
	private List<Device> mDevices;
	
	
	public ServerSocketHelper(MainActivity activity) {
		mActivity = activity;
		mDevices = new ArrayList<Device>();
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
	
	/** Get user info from same (server) device and store it in the database **/
	public void addOwnUser() {
		
		//User user = new User(name);
		int id = (int)mActivity.mDBHelper.addUser(mActivity.currentUser);
		
		// Add server device in list of devices
		Device device = new Device();
		device.setClientSocket(connectionSocket);
		device.setIsGroupOwner(true);

		mActivity.currentUser.setId(id);
		device.setUser(mActivity.currentUser);
		device.setClientSocket(null);
		
		mDevices.add(device);
		
//		for(int i=0; i<mDevices.size(); i++)
//	        	Log.i("Device: ", "" + mDevices.get(i).user().id() + ". " + mDevices.get(i).user().name()
//	        			+ " - " + mDevices.get(i).isGroupOwner() + " - " + mDevices.get(i).clientSocket().toString());
	}
	
	/** Get answers from same (server) device and store them in the database **/
	public void addOwnAnswers(Message message) {
		
		for(int i=0; i<message.clientAnswers.size(); i++)
		{
			int id = (int) mActivity.mDBHelper.addAnswer(message.clientAnswers.get(i));
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
		    
		    // Accept connections from all the clients
		    while(true) {
		    	try {
		    		connectionSocket = serverSocket.accept();
		    		//Log.d(TAG, "ConnectServerThread - accept() - " + connectionSocket.getInetAddress());
		    		
		    		// Create an entry in the Devices list, for the specific client
		    		Device device = new Device();
		    		device.setClientSocket(connectionSocket);
		    		device.setIsGroupOwner(false);
		    		mDevices.add(device);
		    		
		    		// Create a ConnectedServerThread for that peer
		    		receiveMessage(connectionSocket);
		    		
		    		counter++;
		    		// If the number of connected devices has 
		    		//reached the number of peers, exit the loop 
		    		if(counter == mActivity.totalPeers)
		    		{
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
				   		
				   		// Read message type
				   		if(message.type().equals("USER"))	// Get user-name entered by the client device's user
				   		{
				   			// Add user in database
				   			Log.d("Input Stream - User name: ", message.user().name());
				   			message.user().setId((int)mActivity.mDBHelper.addUser(message.user()));
				   			sendMessage(clientSocket, message);
				   			
				   			// Update Devices list with user info
				   			for(int i=0; i<mDevices.size(); i++)
				   				if(mDevices.get(i).clientSocket() == clientSocket)
				   				{
//				   					Log.i(TAG, "Adding user in list: " + message.user().id() 
//				   							+ ". " + message.user().name());
				   					//User user = new User();
				   					//user.setId(message.user().id());
				   					//user.setName(message.user().name());
				   					mDevices.get(i).setUser(message.user());
				   					
				   					break;
				   				}
				   		}
				   		else if(message.type().equals("ANSWERS"))
				   		{
//				   			Log.i(TAG, "Entered else if with list size: " + message.clientAnswers.size());
//				   			for(int i=0; i<message.clientAnswers.size(); i++)
//				   				Log.i("Answer: ", "" + message.clientAnswers.get(i).text() 
//				   						+ " - " + message.clientAnswers.get(i).userId() 
//				   						+ " - " + message.clientAnswers.get(i).questionId());
				   			
				   			// Add answers in database
				   			for(int i=0; i<message.clientAnswers.size(); i++)
				   			{
				   				//message.clientAnswers.get(i).setId((int))
				   				int id = (int) mActivity.mDBHelper.addAnswer(message.clientAnswers.get(i));
				   			}
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
				
				//Log.i(TAG, "Sent message to client: " + message.user().id() + " " + message.user().name());
				
			} catch (Exception e) {
				e.printStackTrace();
		    	Log.d(TAG, "Server exception");
			}
			
			
		}
	}
	
	
	/** Class to handle in-game devices **/
	public class Device {
		private Socket mClientSocket;
		public User mUser;
		private Boolean isGroupOwner;
		
		public void setClientSocket(Socket clientSocket) { mClientSocket = clientSocket; }
		public Socket clientSocket() { return mClientSocket; }
		
		public void setUser(User user) { mUser = user; }
		public User user() { return mUser; }
		
		public void setIsGroupOwner(Boolean value) { isGroupOwner = value; }
		public Boolean isGroupOwner() { return isGroupOwner; }
	}
	
}
