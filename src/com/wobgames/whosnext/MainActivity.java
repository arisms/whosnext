package com.wobgames.whosnext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.wobgames.whosnext.ButtonsFragment.OnButtonSelectedListener;
import com.wobgames.whosnext.DeviceListFragment.OnCreateGroupListener;
import com.wobgames.whosnext.QuestionsFragment.OnStartGameSelectedListener;

public class MainActivity extends FragmentActivity implements OnButtonSelectedListener, OnStartGameSelectedListener, PeerListListener, OnCreateGroupListener {
	// Debug
	public static final String TAG = "MainActivity";
	public final static String EXTRA_MESSAGE = "com.wobgames.whosnext.MESSAGE";
	
	/********** Member data **********/
	// Fragments
	ButtonsFragment mButtonsFragment;
	QuestionsFragment mQuestionsFragment;
	GameMainFragment mGameMainFragment;
	ImageFragment mImageFragment;
	DeviceListFragment mDeviceListFragment;
	
	// Objects
	GameDevice mGameDevice;
	
	// WiFi p2p
	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver = null;
	private IntentFilter mIntentFilter;
	private boolean isWifiP2pEnabled = false;
	WifiP2pDevice mDevice;
	/*********************************/
	
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
        mDeviceListFragment = new DeviceListFragment();
        
        // Create the Database
        DatabaseHelper mDBHelper = new DatabaseHelper(getApplication());
        mDBHelper.init();
        
        /** WiFiDirect **/
        
        // WiFi p2p Initial Setup
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        //mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
        
        
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
    
    /* ************ WiFiP2p Functions ************ */
    
    /**
     * @param isWifiP2pEnabled the isWifiP2pEnabled to set
     */
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }
    
    public void updateDevice (WifiP2pDevice device) {
    	Log.d(TAG, "updateThisDevice()");
    	
    	mDevice = device;
    	mGameDevice = new GameDevice(device);
    }
    
    /**
     * Remove all peers and clear all fields. This is called on
     * BroadcastReceiver receiving a state change event.
     */
    public void resetData() {
        if (mDeviceListFragment != null) {
            mDeviceListFragment.clearPeers();
        }
    }
        
    /** PeerListListener **/
    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {
    	Log.d(TAG, "onPeersAvailable()");

    	if(mDeviceListFragment != null && mDeviceListFragment.isAdded()) {
    		mDeviceListFragment.updatePeerList(peerList);
    		mDeviceListFragment.updateThisDevice(mDevice);
    	}
    }
    
    /* ************ Fragment Interfaces ************ */
    
    /**
     * onCreateGame() - ButtonsFragment
     */
    @Override
    public void onCreateGame() {
    	// When the user taps the Create Game button
    	Log.d(TAG, "onCreateGame()");
    	
    	// Check if WiFiP2p is enabled
    	if (!isWifiP2pEnabled) {
            Toast.makeText(MainActivity.this, "Wi-Fi Direct is not enabled.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
    	
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
        
        mGameDevice.setIsGroupOwner(true);

    	// Load DeviceList Fragment
    	getSupportFragmentManager().beginTransaction()
    		.replace(R.id.rootlayout, mDeviceListFragment).addToBackStack(null).commit();

    }
    
    /**
     * onJoinGame() - ButtonsFragment
     */
    @Override
    public void onJoinGame() {
    	
    	// When the user taps the Join Game button
    	Log.d(TAG, "onJoinGame()");
    	
    	// Check if WiFiP2p is enabled
    	if (!isWifiP2pEnabled) {
            Toast.makeText(MainActivity.this, "Wi-Fi Direct is not enabled.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
    	
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
//    	getSupportFragmentManager().beginTransaction()
//			.replace(R.id.rootlayout, mImageFragment).addToBackStack(null).commit();
    }
    
    /**
     * onStartGame() - GameMainFragment
     */
    @Override
    public void onStartGame() {
    	// When the user taps the Start Game button
    	Log.d(TAG, "onStartGame()");
    	
    	// Load GameMain Fragment
    	getSupportFragmentManager().beginTransaction()
			.replace(R.id.rootlayout, mGameMainFragment).commit();
    }
    
    /**
     * onCreateGroup - DeviceListFragment
     */
    public void onCreateGroup() {
    	Toast.makeText(MainActivity.this, "onCreateGroup",
                Toast.LENGTH_SHORT).show();
    }
}
