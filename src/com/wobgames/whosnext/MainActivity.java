package com.wobgames.whosnext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.wobgames.whosnext.ButtonsFragment.OnButtonSelectedListener;
import com.wobgames.whosnext.QuestionsFragment.OnStartGameSelectedListener;

public class MainActivity extends FragmentActivity implements OnButtonSelectedListener, OnStartGameSelectedListener {
	// Debug
	private static final String TAG = "MainActivity";
	public final static String EXTRA_MESSAGE = "com.wobgames.whosnext.MESSAGE";
	
	/** Members **/
	
	// Fragments
	ButtonsFragment mButtonsFragment;
	QuestionsFragment mQuestionsFragment;
	GameMainFragment mGameMainFragment;
	ImageFragment mImageFragment;
	// WiFi p2p
	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;
	// Used for WiFip2p
	private Context mContext;
	private IntentFilter mIntentFilter;
	
	/** On Create() **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
        	mButtonsFragment = new ButtonsFragment();
			getSupportFragmentManager().beginTransaction().add(R.id.rootlayout, mButtonsFragment).commit();
		} else {
			// Or set the fragment from restored state info
			mButtonsFragment = (ButtonsFragment) getSupportFragmentManager().findFragmentById(R.id.rootlayout);
		}
        
        // Fragments
        mQuestionsFragment = new QuestionsFragment();
        mGameMainFragment = new GameMainFragment();
        mImageFragment = new ImageFragment();
        
        // Create the Database
        DatabaseHelper mDBHelper = new DatabaseHelper(getApplication());
        mDBHelper.init();
        
        /** WiFiDirect **/
        
        // WiFi p2p Initial Setup
        mContext = getApplicationContext();
        mManager = (WifiP2pManager) getSystemService(mContext.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
        
        // Intent filter & intents
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        
    }
    
    /** onResume() **/
    @Override
    public void onResume() {
        super.onResume();
        
        // Register the broadcast receiver with same intent values
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
        registerReceiver(mReceiver, mIntentFilter);
    }

    /** onPause() **/
    @Override
    public void onPause() {
        super.onPause();
        
        // Unregister the broadcast receiver
        unregisterReceiver(mReceiver);
    }
    

    /**** FRAGMENT INTERFACES ****/
    @Override
    public void onCreateGame() {
    	// When the user taps the Create Game button
    	Log.d(TAG, "onCreateGame()");
    	
    	// Discover Peers
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "Peers Discovery Initiated",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(MainActivity.this, "Peers Discovery Failed : " + reasonCode,
                        Toast.LENGTH_SHORT).show();
            }
        });
    	
    	// Load Questions Fragment
    	getSupportFragmentManager().beginTransaction()
    		.replace(R.id.rootlayout, mQuestionsFragment).addToBackStack(null).commit();
    }
    
    @Override
    public void onJoinGame() {
    	// When the user taps the Join Game button
    	Log.d(TAG, "onJoinGame()");
    	
    	// Load Questions Fragment
//    	getSupportFragmentManager().beginTransaction()
//			.replace(R.id.rootlayout, mQuestionsFragment).addToBackStack(null).commit();
    	getSupportFragmentManager().beginTransaction()
			.replace(R.id.rootlayout, mImageFragment).addToBackStack(null).commit();
    }
    
    @Override
    public void onStartGame() {
    	// When the user taps the Start Game button
    	Log.d(TAG, "onStartGame()");
    	
    	// Load GameMain Fragment
    	getSupportFragmentManager().beginTransaction()
			.replace(R.id.rootlayout, mGameMainFragment).commit();
    }
}
