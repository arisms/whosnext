package com.wobgames.whosnext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;

public class ServerSocketHelper {

	private String TAG = "ServerSocketHelper";
	private ServerSocket serverSocket;
	private Socket connectionSocket;
	MainActivity mActivity;
	private List<Device> mDevices;
	Device lastUsedDevice;
	Random rand;
	public List<Answer> gameAnswers;
	int MAX_TURNS;
	int turnCounter;
	public boolean gameStarted = false;
	private boolean answerReceived;
	private boolean wrongAnswers;
	
	public ServerSocketHelper(MainActivity activity) {
		mActivity = activity;
		mDevices = new ArrayList<Device>();
		gameAnswers = new ArrayList<Answer>();
		lastUsedDevice = null;
		rand = new Random();
		turnCounter = 1;
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
	
	public boolean allDevicesReady() {
		for(int i=0; i<mDevices.size(); i++)
		{
			if(!(mDevices.get(i).isReady()))
				return false;
		}
		return true;
	}
	
	/** Receive wrongAnswerNumber from clients **/
	public void getWrongAnswers() {
		Log.d(TAG, "getWrongAnswers()");
		wrongAnswers = false;
		
		Message message = new Message();
		message.setType("WRONG ANSWERS");
		
		// Send the message to each client device, and wait for the answer
		for(int i=0; i<mDevices.size(); i++) {
			answerReceived = false;
			if(!(mDevices.get(i).isGroupOwner)) {
				sendMessage(mDevices.get(i).clientSocket(), message);
				Log.d(TAG, "Sent message to client");
				
				// Wait for the answer from the client
				while(!answerReceived) {
					// wait...
				}
			}
		}
		wrongAnswers = true;
	}
	
	/** Get user info from same (server) device and store it in the database **/
	public void addOwnUser() {
		Log.d(TAG, "addOwnUser()");
		
		//User user = new User(name);
		int id = (int)mActivity.mDBHelper.addUser(mActivity.currentUser);
		
		// Add server device in list of devices
		Device device = new Device();
		device.setClientSocket(connectionSocket);
		device.setIsGroupOwner(true);
		device.setisReady(false);

		mActivity.currentUser.setId(id);
		device.setUser(mActivity.currentUser);
		device.setClientSocket(null);
		
		mDevices.add(device);
	}
	
	/** Get answers from same (server) device and store them in the database **/
	public void addOwnAnswers(Message message) {
		Log.d(TAG, "addOwnAnswers()");
		
		for(int i=0; i<message.answers_list.size(); i++)
		{
			mActivity.mDBHelper.addAnswer(message.answers_list.get(i));
		}
		
		// Set device as ready to start the game
		for(int i=0; i<mDevices.size(); i++)
			if(mDevices.get(i).isGroupOwner)
			{
				mDevices.get(i).setisReady(true);
				
				break;
			}
	}
	
	/** Send a message to every device **/
	public void broadcastMessage(Message message) {
		Log.d(TAG, "broadcastMessage, mDevices.size(): " + mDevices.size() + ", type: " + message.type());
		// Iterate for every device
		for(int i=0; i<mDevices.size(); i++) {
			// If the target device is a client
			if(!(mDevices.get(i).isGroupOwner())) {
				//Log.d(TAG, "Sending message to device " + mDevices.get(i).clientSocket().toString());
				sendMessage(mDevices.get(i).clientSocket(), message);
			}
			// If the target device is the server
			else
				receiveBroadcastMessage(message);
		}
				
		
	}
	
	/**  Process broadcast message when targeted to server **/
	public void receiveBroadcastMessage(Message message) {
		Log.d(TAG, "receiveBroadcastMessage()");
		
		final Message msg = message;
		
		if(message.type().equals("START")) {
			mActivity.currentUsers = new ArrayList<User>(msg.users_list);
			
			mActivity.runOnUiThread(new Runnable() {
				  public void run() {
					  //mActivity.currentUsers = new ArrayList<User>(msg.users_list);
					  mActivity.startTimer();
					  mActivity.showToast(msg.toast());
				  }
				});
			
			for(int i=0; i<mActivity.currentUsers.size(); i++) 
   				Log.d(TAG, "currentUsers" + i + " Name: " + mActivity.currentUsers.get(i).name()
   						+ " Id: " + mActivity.currentUsers.get(i).id());
		}
		else if(message.type().equals("GAME OVER")) {
			
			// Get wrong answers from the clients
//			getWrongAnswers();
//			while(!wrongAnswers) {
//				// wait...
//			}
//			
			mActivity.runOnUiThread(new Runnable() {
				  public void run() {
					  //mActivity.currentUsers = new ArrayList<User>(msg.users_list);
					  //mActivity.showToast(msg.toast());
					  mActivity.gameOver(msg);
				  }
				});
		}
		else {
			// Ignore...
		}
		
	}
	
	/** Takes care of game set-up procedure **/
	public void startGame() {
		
		Log.d(TAG, "startGame() - size = " + mDevices.size());
		
		// Wait until all the devices have submitted their answers
		if(!allDevicesReady()) {
			mActivity.runOnUiThread(new Runnable() {
				  public void run() {
					  //mActivity.currentUsers = new ArrayList<User>(msg.users_list);
					  mActivity.showToast("Waiting for other devices...");
				  }
				});
		}
		while(!allDevicesReady()) {
			// Wait...
		}

		Log.d(TAG, "Finished waiting...");
		
		Message message = new Message();
		message.setType("START");
		message.setToast("Game started!");
		
		// Add list of users to the message
		for(int i=0; i<mDevices.size(); i++) {
			message.users_list.add(mDevices.get(i).user());
		}
		
		broadcastMessage(message);
		
		// Get all answers from the database
		gameAnswers = mActivity.mDBHelper.getAnswers();
		
		for(int i=0; i<gameAnswers.size(); i++)
			Log.d(TAG, "Answer text: "+ gameAnswers.get(i).text()
					+ "  - userId: " + gameAnswers.get(i).userId());
		
		//MAX_TURNS = gameAnswers.size();
		MAX_TURNS = mActivity.MAX_TURNS;
		gameStarted = true;

		// Wait...
//		while(!gameStarted) {
//		}
		
		mActivity.sHelper.randomize();
	}
	
	public void finishGame() {
		Message message = new Message();
		message.setType("GAME OVER");
		message.setToast("Game over!");
		
		broadcastMessage(message);
	}
	
	public void continueGame() {
		Log.d(TAG, "continueGame() , turnCounter = " + turnCounter);
		
		// If the game is over, broadcast corresponding message
		if(turnCounter > MAX_TURNS) {
			Log.d(TAG, "turnCounter > MAX_TURNS " + turnCounter + " " + MAX_TURNS);
			Message message = new Message();
			message.setType("GAME OVER");
			message.setToast("Game over!");
			
			broadcastMessage(message);
		}
		else {
			mActivity.sHelper.randomize();
		}
		
		
	}
	
	public void randomize() {
		Log.d(TAG, "randomize()");
		Message message = new Message();
		message.setType("PLAY");
		
		// Get a random device from the list, that is not the same as last time	
		int j;
		j = getRandomDevice();
		
		// If all the answers left have the same userId as this device's user, pick another device
		while(!findQuestionsWithDifferentUserId(mDevices.get(j).user().id()))
			j = getRandomDevice();
		
		lastUsedDevice = mDevices.get(j);
		
		// Get a random Answer from the list, that hasn't been used
		int counter = 0;
		int i = randInt(0, gameAnswers.size()-1);
		while(gameAnswers.get(i).used() || (gameAnswers.get(i).userId() == mDevices.get(j).user().id())) {
			// Fix for infinite loop bug
			if(counter > (3*MAX_TURNS)) {
				//Log.d(TAG, "counter > (3*gameAnswers.size())");
				Message msg = new Message();
				msg.setType("GAME OVER");
				msg.setToast("Game over!");
				
				broadcastMessage(msg);
				return;
			}
			//Log.d(TAG, "randomize() - while - answers");
			i = randInt(0, gameAnswers.size()-1);
			counter++;
		}
		gameAnswers.get(i).setUsed(true);
		message.setCurrentAnswer(gameAnswers.get(i));
		
		Log.d(TAG, "randomize(), answer " + i + " out of " + gameAnswers.size());
		
		turnCounter++;
		
		Log.d(TAG, "Current Answer, Text: " + message.currentAnswer().text()
					+ " UserId: " + message.currentAnswer().userId());
		
		// Send message to selected device
		if(mDevices.get(j).isGroupOwner())	{ // If the target device is the server
			mActivity.runOnUiThread(new Runnable() {
				  public void run() {
					  mActivity.showToast("Your turn to play!");
				  }
				});
 			
 			mActivity.startTurn(message.currentAnswer());
		}
		else	// If the target device is a client
			sendMessage(mDevices.get(j).clientSocket(), message);
		
	}
	
	
	// Thread that creates a socket and accepts incoming connections from clients
	public class ConnectServerThread extends Thread {
		int counter = 0;
		
		 @Override
		 public void run() {         
	    	Log.d(TAG, "ConnectServerThread - run");
	    	
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
		    		device.setisReady(false);
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
			//Log.d(TAG, "ReceiveFromClientsThread created...");
		}
		
		@Override
		public void run() {
			Log.d(TAG, "ReceiveFromClientsThread - run...");
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
				   		Log.d(TAG, "Data is available.");
				   		Message message = (Message) Serializer.deserialize(buf);
				   		dataAvailable = false;
				   		baos.flush();
				   		
				   		// Read message type
				   		if(message.type().equals("USER"))	// Get user-name entered by the client device's user
				   		{
				   			// Add user in database
				   			Log.d("Input Stream - User name: ", message.user().name());
				   			message.user().setId((int)mActivity.mDBHelper.addUser(message.user()));
				   			// Update Devices list with user info
				   			for(int i=0; i<mDevices.size(); i++)
				   				if(mDevices.get(i).clientSocket() == clientSocket)
				   				{
				   					mDevices.get(i).setUser(message.user());
				   					
				   					break;
				   				}
				   			
				   			sendMessage(clientSocket, message);
				   			
				   		}
				   		else if(message.type().equals("ANSWERS"))
				   		{
				   			// Add answers in database
				   			for(int i=0; i<message.answers_list.size(); i++)
				   			{
				   				//message.clientAnswers.get(i).setId((int))
				   				mActivity.mDBHelper.addAnswer(message.answers_list.get(i));
				   			}
				   			
				   			// Set device as ready to start the game
				   			for(int i=0; i<mDevices.size(); i++)
				   				if(mDevices.get(i).clientSocket() == clientSocket)
				   				{
				   					mDevices.get(i).setisReady(true);
				   					
				   					break;
				   				}
				   			
				   		}
				   		else if(message.type().equals("CONTINUE")) {
				   			mActivity.wrongAnswersNumber += message.wrongAnswers();
				   			continueGame();
				   		}
				   		else if(message.type().equals("WRONG ANSWERS")) {
				   			Log.d(TAG, "WRONG ANSWERS");
				   			mActivity.wrongAnswersNumber += message.wrongAnswers();
				   			answerReceived = true;
				   		}
				   		else
				   		{
				   			Log.d(TAG, "Unknown message type");
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
			Log.d(TAG, "SendToClientThread - run()");
			
			try {
				OutputStream outputStream = clientSocket.getOutputStream();
				
				buf = Serializer.serialize(message);
				outputStream.write(buf, 0, buf.length);
				
				Log.i(TAG, "Sent message to client: " + message.type());
				
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
		private Boolean isReady;
		
		public void setClientSocket(Socket clientSocket) { mClientSocket = clientSocket; }
		public Socket clientSocket() { return mClientSocket; }
		
		public void setUser(User user) { mUser = user; }
		public User user() { return mUser; }
		
		public void setIsGroupOwner(Boolean value) { isGroupOwner = value; }
		public Boolean isGroupOwner() { return isGroupOwner; }
		
		public void setisReady(Boolean value) { isReady = value; }
		public Boolean isReady() { return isReady; }
	}
	
	/** Return random element from a list of Answers  **/
	public Answer getRandomAnswer(List<Answer> mAnswersList) {
		
		int randomId = randInt(1, mAnswersList.size());
		return mAnswersList.get(randomId);
	}
	
	/** Return random element from a list of Devices  **/
	public Device getRandomDevice(List<Device> mDevicesList) {
		
		int randomId = randInt(1, mDevicesList.size());
		return mDevicesList.get(randomId);
	}
	
	/** Return a random integer from within a certain range **/
	public int randInt(int min, int max) {
	    
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	private int getRandomDevice() {
		int k=0;
		int j;
		if(lastUsedDevice == null) {
			j = 0; //randInt(0, mDevices.size()-1);
			Log.d(TAG, "randomize() - lastUsedDevice == null, j = " + j);
		}
		else {
			// Get last used device's position in the list
			for(k=0; k<mDevices.size(); k++) {
				if(mDevices.get(k).equals(lastUsedDevice))
					break;
			}
			j = k+1;
			Log.d(TAG, "randomize() - else j=k+1 = " + j);
			
			if(j==mDevices.size()) {
				Log.d(TAG, "randomize() - j>mDevices.size() => j=0");
				j=0;
			}
		}
		return j;
	}
	
	private boolean findQuestionsWithDifferentUserId(int userId) {
		
		// Return true if an answer with a different userId is found in the list
		for(int i=0; i<gameAnswers.size(); i++) {
			if(gameAnswers.get(i).userId() != userId)
				return true;
		}
		
		return false;
	}
}










