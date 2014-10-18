/** ConnectionInfoListener **/
    @Override
    public void onConnectionInfoAvailable(final WifiP2pInfo info) {
    	//Log.d(TAG, "onConnectionInfoAvailable");
    	
        // Group Owner (Server)
        if (info.groupFormed && info.isGroupOwner) {
            // Do whatever tasks are specific to the group owner.
            // One common case is creating a server thread and accepting
            // incoming connections.
        	
        	Toast.makeText(MainActivity.this, "Connected as group owner.",
                    Toast.LENGTH_SHORT).show();
        	mGameDevice.setIsGroupOwner(true);
        	// If the list of connected devices is empty, add the current device (group owner)
        	if(connectedDevices.isEmpty())
        	{
        		mGameDevice.setInfo(info);
        		connectedDevices.add(mGameDevice);
        	}
        	
        	// Add the peer device to the list of connected devices
        	GameDevice peerDevice = new GameDevice(mPeers.get(peersCounter-1));
        	peerDevice.setInfo(info);
        	connectedDevices.add(peerDevice);
        	
//        	ServerSocketHelper sHelper = new ServerSocketHelper(this); 		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        	sHelper.connect();
        	
        }
        // Client (not group owner)
        else if (info.groupFormed) {
        	
        	// If the list of connected devices is empty, add the current device (not group owner)
        	if(connectedDevices.isEmpty())
        	{
        		mGameDevice.setIsGroupOwner(false);
        		mGameDevice.setInfo(info);
        		connectedDevices.add(mGameDevice);
        	}
        	else
        	{
        		// Add the peer device to the list of connected devices
            	GameDevice peerDevice = new GameDevice(mPeers.get(peersCounter-1));
            	peerDevice.setInfo(info);
            	connectedDevices.add(peerDevice);
        	}
        	
        	if(!threadStarted) {
	        	Log.d(TAG, "CLIENT CONNECT");
	        	// Connect to the server socket
	        	cHelper = new ClientSocketHelper(this);
	        	cHelper.connect();
	        	threadStarted = true;
        	}
        	
        	getSupportFragmentManager().beginTransaction()
				.replace(R.id.rootlayout, mQuestionsFragment).commit();
        	//peersCounter++;
        }
    }














/********** DATABASE INIT *************/
// USERS table initialization (to be removed)
        if(IsTableEmpty(TABLE_USERS)) {
        	
        	BufferedReader reader = null;
        	try {
        		// Open txt file in Assets folder, for reading
        	    reader = new BufferedReader(new InputStreamReader(context.getAssets().open("default_users.txt"), "UTF-8")); 

        	    // Parse each line of the text file
        	    String name;
        	    String delim = "_";
        	    String mLine = reader.readLine();
        	    while (mLine != null) {
        	       
        	       String[] tokens = mLine.split(delim);
        	       name = tokens[0];
        	       
        	       User user = new User();
        	       user.setName(name);
        	       addUser(user);
        	       
        	       mLine = reader.readLine();
        	    }
        	} catch (IOException e) {
        		Log.e("DatabaseHelper", "open txt file error 1" + e);
        	}
        	finally {
        	    if (reader != null) {
        	         try {
        	             reader.close();
        	         } catch (IOException e) {
        	        	 Log.e("DatabaseHelper", "open txt file error 2" + e);
        	         }
        	    }
        	}
        }
        
     // ANSWERS table initialization (to be removed)
        if(IsTableEmpty(TABLE_ANSWERS)) {
        	
        	BufferedReader reader = null;
        	try {
        		// Open txt file in Assets folder, for reading
        	    reader = new BufferedReader(new InputStreamReader(context.getAssets().open("default_answers.txt"), "UTF-8")); 

        	    // Parse each line of the text file
        	    String text;
        	    int userId;
        	    int questionId;
        	    String delim = "_";
        	    String mLine = reader.readLine();
        	    while (mLine != null) {
        	       
        	       String[] tokens = mLine.split(delim);
        	       text = tokens[0];
        	       userId = Integer.parseInt(tokens[1]);
        	       questionId = Integer.parseInt(tokens[2]);
        	       
        	       Answer answer = new Answer(text, userId, questionId);
        	       addAnswer(answer);
        	       
        	       mLine = reader.readLine();
        	    }
        	} catch (IOException e) {
        		Log.e("DatabaseHelper", "open txt file error 1" + e);
        	}
        	finally {
        	    if (reader != null) {
        	         try {
        	             reader.close();
        	         } catch (IOException e) {
        	        	 Log.e("DatabaseHelper", "open txt file error 2" + e);
        	         }
        	    }
        	}
        }









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




OutputStream outputStream = clientSocket.getOutputStream();
					
					//String msg = "ASDF";
					 User msg = new User(5, "Vikas");
					
					
					buf = Serializer.serialize(msg);
					
					outputStream.write(buf, 0, buf.length);
					
					String temp = new String(buf, "UTF-8");
					Log.d("Output Stream", temp);

import com.wobgames.whosnext.Serializer;
import com.wobgames.whosnext.User;
public class SendMessageThread extends Thread {
		
		@Override
		public void run() {
			try{
				
			} catch (Exception e) {
				e.printStackTrace();
		    	Log.d(TAG, "Client exception - SendMessageThread");
			}
		}
	}



/****  ON LIST START GAME ******/

    	// Connect current device to all peers in the list, 
    	// and set it as group owner
    	//Log.d(TAG, "onListStartGame()");
    	
//    	getSupportFragmentManager().beginTransaction()
//			.replace(R.id.rootlayout, mQuestionsFragment).commit();
    	
//    	ServerSocketHelper sHelper = new ServerSocketHelper(this);
//    	sHelper.connect();
//    	Intent intent = new Intent(this, ServerService.class);
//    	intent.putExtra(EXTRA_MESSAGE, "Server");
//    	startService(intent);

	
	
	// Client socket helper
//	Socket  clientSocket = new Socket(ServerIP,ServerPort);
//	outputStream = clientSocket.getOutputStream();
//	bufferedReader=newBufferedReader(new 
//	InputStreamReader(clientSocket.getInputStream()));





/****  SERVER SOCKET HELPER ******/
public class ConnectServerThread extends Thread {
		
		 @Override
		 public void run() {         
	    	Log.d(TAG, "SERVER THREAD STARTED");
		    try {
		    	serverSocket = new ServerSocket(mActivity.SERVER_PORT); 
		    	connectionSocket = serverSocket.accept();
		    	Log.d(TAG, "SERVER THREAD CONNECTED");
		    } 
		    catch (Exception e) {
		    	e.printStackTrace();
		   	}
		 }
	}
}

/*****************************/
package com.wobgames.whosnext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

import android.app.IntentService;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class ServerService extends IntentService {

	private final String TAG = "ServerService";
	//public int SERVER_PORT = 8080;
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


<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="fill_parent"
    android:layout_height="60dp"
    android:minHeight="60dp"
    android:orientation="horizontal"
    android:gravity="center"
    >
    
        
    <TextView
        android:id="@+id/list_question" 
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="24sp"
        android:gravity="left|center_vertical"
        android:layout_gravity="left"
        android:paddingLeft="20dp"
        />
    
    <Button 
        	android:layout_width="80dp"
        	android:layout_height="40dp"
        	android:layout_marginTop="5dp"
        	android:background="@drawable/backrepeat_button"
        	android:layout_gravity="right"
        	android:text="@string/next_button"
        	android:textSize="22sp" />
</RelativeLayout>




// questions_fragment.xml

<ListView
        	android:id="@id/android:list"
        	android:layout_height="0dp"
        	android:layout_weight="1"
        	android:layout_width="match_parent"
        	android:background="@color/grey1" >
</ListView>
		

// QuestionsFragment.java

//Link ListView to the data
mListView = (ListView) view.findViewById(android.R.id.list);

QuestionsAnswersListAdapter mAdapter = new QuestionsAnswersListAdapter(getActivity(), questions_strings);

mListView.setAdapter(mAdapter);



/**QUESTIONS ACTIVITY**/
public class QuestionsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questions);
		
		Intent intent = getIntent();
	    String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
	    TextView temp = (TextView) findViewById(R.id.temp_text);
	    temp.setText(message);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.questions, menu);
		return true;
	}
}






// Member Data
	Button mCreateGame;
	Button mJoinGame;
	
	
	
	// Buttons
    mCreateGame = (Button) findViewById(R.id.creategame);
    mCreateGame.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			createGame();				
		}
	});
    mJoinGame = (Button) findViewById(R.id.joingame);
    mJoinGame.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			joinGame();				
		}
	});





import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.wobgames.whosnext.Answer;
import com.wobgames.whosnext.ClientSocketHelper;
import com.wobgames.whosnext.GameDevice;
import com.wobgames.whosnext.MainActivity;
import com.wobgames.whosnext.QuestionsAnswersListAdapter;
import com.wobgames.whosnext.QuestionsFragment;
import com.wobgames.whosnext.R;

        DatabaseHelper mDBHelper = new DatabaseHelper(getApplication());
        
        mDBHelper.init();
        
        List<Question> questions_list = mDBHelper.getQuestions();
        
        Log.i("Question.text: ", Integer.toString(questions_list.size()));
        
        for(int i=0; i<questions_list.size(); i++)
        	Log.i("Question.text: ", questions_list.get(i).text());
        
        
        
        
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {
        	
        	// If being restored from a previous state, return
            if (savedInstanceState != null) {
                return;
            }
            
            // Create a new Fragment to be placed in the activity layout
            MainFragment mainFragment = new MainFragment();
            
            // Pass the Intent's extras to the fragment as arguments
            mainFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mainFragment).commit();


        }
        
        
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }