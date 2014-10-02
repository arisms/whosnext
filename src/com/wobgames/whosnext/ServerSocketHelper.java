package com.wobgames.whosnext;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.hardware.Camera.Size;
import android.util.Log;

public class ServerSocketHelper {

	private ServerSocket serverSocket;
	MainActivity mActivity;
	byte buf[]  = new byte[1024];
	
	public ServerSocketHelper(MainActivity activity) {
		mActivity = activity;
	}
	
	public void connect() {
		
		Thread serverThread = new Thread(new Runnable() {
			
			
	    @Override
	    public void run() {         
	    	Log.d(mActivity.TAG, "SERVER THREAD STARTED, buf length = " + buf.length);
		    try {
		    	serverSocket = new ServerSocket(17777); 
		
		    	 Socket clientSocket = serverSocket.accept();
		
		    	 InputStream inputStream = clientSocket.getInputStream();
		    	 inputStream.read(buf, 0, buf.length);
		    	 Log.d("Client's InetAddress", "" + clientSocket.getInetAddress());
		
		    	 //String temp = new String(buf, "UTF-8");
		    	 
		    	 User user = (User) Serializer.deserialize(buf);
		    	 Log.d("Input Stream", user.name());
		    } catch (Exception e) {
		    	e.printStackTrace();
		
		   		}
			}
	    });
		serverThread.start();
		
//		ServerSocket serverSocket = new ServerSocket(8988);
//		Socket s = serverSocket.accept();  
//		InputStream is = s.getInputStream();  
//		Log.d("Client's InetAddress",""+s.getInetAddress());
//		ObjectInputStream ois = new ObjectInputStream(is);  
//		TestObject to = (TestObject)ois.readObject();  
//		is.close();  
//		s.close();  
//		serverSocket.close();
		
		
	}
	
	public void disconnect() {
		try {
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
