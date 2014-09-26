package com.wobgames.whosnext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

	private static final String TAG = "WiFiDirectBroadcastReceiver";
	private WifiP2pManager mManager;
    private Channel mChannel;
    private MainActivity mActivity;
    //private PeerListListener mPeerListListener;

    /** Constructor **/
    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
            MainActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        // State changed
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
        	
            // Check to see if WiFi is enabled and notify appropriate activity
        	int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // WiFi P2P is enabled
            	mActivity.setIsWifiP2pEnabled(true);
            } else {
                // WiFi P2P is not enabled
            	mActivity.setIsWifiP2pEnabled(false);
                mActivity.resetData();
            }
            Log.d(TAG, "P2P state changed - " + state);
        } 
        // Peers changed
        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
        	
        	// Request available peers from the WiFiP2pManager
            if (mManager != null) {
            	//mManager.requestPeers(mChannel, myPeerListListener);
                mManager.requestPeers(mChannel, (PeerListListener) mActivity);
                
            }
            
            Log.d(TAG, "P2P peers changed");
        	
        }
        // Connection changed
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        	Log.d(TAG, "P2P connection changed");
        	
        	// IF CONNECTED
        	// ELSE
        		//mActivity.resetData();
        	
        }
        // This device's WiFi changed
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's Wi-Fi state changing
        	Log.d(TAG, "P2P this device changed");
        	//DeviceListFragment fragment = (DeviceListFragment) activity.getFragmentManager().findFragmentById(R.id.frag_list);
            //fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
        	mActivity.updateDevice((WifiP2pDevice) intent.
        			getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
        }
    }
}




