package com.wobgames.whosnext;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.wobgames.whosnext.ButtonsFragment.ButtonsFragmentListener;

@SuppressLint("InflateParams") public class DeviceListFragment extends ListFragment {

	public static final String TAG = "DeviceListFragment";
	
	private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
	private WifiP2pDevice device;
	private View mView = null;
	OnCreateGroupListener mListener;
	
	Button buttonConnect;
	Button buttonStart;
	MainActivity mActivity;
	
	// onCreateView
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		mView = inflater.inflate(R.layout.device_list_fragment, container, false);
		mActivity = (MainActivity) getActivity();
		
		// Play sound effect
        //mActivity.soundpool.play(mActivity.soundIds[2], 1, 1, 1, 0, 1);
		
		peers.clear();
		this.setListAdapter(new WiFiPeerListAdapter(getActivity(), R.layout.row_devices, peers));
		
		
		
//		// Connect button
//		buttonConnect = (Button) mView.findViewById(R.id.connect_button);
//        buttonConnect.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//            	mListener.onConnect();
//            }
//        });
        
        // Start Game button
        buttonStart = (Button) mView.findViewById(R.id.server_start_button);
        buttonStart.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		mListener.onListStartGame();
        	}
        });
		
		return mView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		Log.d(TAG, "onAttach()");
	    super.onAttach(activity);
	    if (activity instanceof ButtonsFragmentListener) {
	    	mListener = (OnCreateGroupListener) activity;
	    } else {
	      throw new ClassCastException(activity.toString()
	          + " must implemenet ButtonsFragment.OnButtonSelectedListener");
	    }
	  }
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		WifiP2pDevice device = (WifiP2pDevice) l.getItemAtPosition(position);
		Log.d(TAG, "onListItemClick " + device.deviceName);
		if(getDeviceStatus(device.status).equals("Available") && 
				(device.deviceName.equals("Whosnext1") ||
				 device.deviceName.equals("Whosnext2") ||
				 device.deviceName.equals("Whosnext3") ||
				 device.deviceName.equals("Whosnext4") ||
				 device.deviceName.equals("Whosnext5") ||
				 device.deviceName.equals("Whosnext6") ||
				 device.deviceName.equals("Whosnext7"))
				) {
			mActivity.onConnect(device);
		}
    }
	
	/**
     * @return this device
     */
    public WifiP2pDevice getDevice() {
        return device;
    }
	
	public String getDeviceStatus(int deviceStatus) {
        //Log.d(TAG, "Peer status :" + deviceStatus);
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknown";
        }
    }
	
	
	/**
     * Array adapter for ListFragment that maintains WifiP2pDevice list.
     */
    private class WiFiPeerListAdapter extends ArrayAdapter<WifiP2pDevice> {
    	private List<WifiP2pDevice> items;
        
    	/**
         * @param context
         * @param textViewResourceId
         * @param objects
         */
        public WiFiPeerListAdapter(Context context, int textViewResourceId,
                List<WifiP2pDevice> objects) {
            super(context, textViewResourceId, objects);
            items = objects;
            
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row_devices, null);
                
            }
            WifiP2pDevice device = items.get(position);
            if (device != null) {
                TextView top = (TextView) v.findViewById(R.id.device_name);
                top.setTypeface(mActivity.exoregular);
                TextView bottom = (TextView) v.findViewById(R.id.device_details);
                bottom.setTypeface(mActivity.exoregular);
                if (top != null) {
                    top.setText(device.deviceName);
                }
                if (bottom != null) {
                    bottom.setText(getDeviceStatus(device.status));
                }
                if(getDeviceStatus(device.status).equals("Available"))
                	bottom.append(" - Tap to connect");
            }
            
            return v;
        }
    }
    
    public void updatePeerList(WifiP2pDeviceList peerList) {
    	
    	peers.clear();
    	peers.addAll(peerList.getDeviceList());
    	((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
    	if (peers.size() == 0) {
            Log.d(TAG, "No devices found");
            return;
        }
    }
	
    
    public List<WifiP2pDevice> getPeersList() {
    	return this.peers;
    }
    

    /**
     * Update UI for this device.
     * 
     * @param device WifiP2pDevice object
     */
    public void updateThisDevice(WifiP2pDevice device) {
    	//Log.d(TAG, "updateThisDevice()");
        this.device = device;
        TextView header = (TextView) mView.findViewById(R.id.this_device);
        header.setTypeface(mActivity.exoregular);
        header.setText(device.deviceName);
        header.append(" - " + getDeviceStatus(device.status));
}

    public void clearPeers() {
        peers.clear();
        ((WiFiPeerListAdapter) getListAdapter()).clear();
        ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
    }

    /**
     * Public interface for Create Group button in DeviceListFragment 
     */
    public interface OnCreateGroupListener {
		
		public void onConnect(WifiP2pDevice device);
		
		public void onListStartGame();
	}
}
