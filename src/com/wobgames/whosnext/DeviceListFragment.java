package com.wobgames.whosnext;

import java.util.ArrayList;
import java.util.List;

import com.wobgames.whosnext.ButtonsFragment.OnButtonSelectedListener;

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
import android.widget.TextView;

public class DeviceListFragment extends ListFragment {

	public static final String TAG = "DeviceListFragment";
	
	private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
	private WifiP2pDevice device;
	private View mView = null;
	private Boolean thisDeviceCalled = false;
	OnCreateGroupListener mListener;
	
	Button button;

	
	// onCreateView
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		mView = inflater.inflate(R.layout.device_list_fragment, container, false);
		
		this.setListAdapter(new WiFiPeerListAdapter(getActivity(), R.layout.row_devices, peers));
		
		button = (Button) mView.findViewById(R.id.create_group_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	// If the answer text is empty
            	mListener.onCreateGroup();
            }
        });
		
		
		return mView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		Log.d(TAG, "onAttach()");
	    super.onAttach(activity);
	    if (activity instanceof OnButtonSelectedListener) {
	    	mListener = (OnCreateGroupListener) activity;
	    } else {
	      throw new ClassCastException(activity.toString()
	          + " must implemenet ButtonsFragment.OnButtonSelectedListener");
	    }
	  }
	
	/**
     * @return this device
     */
    public WifiP2pDevice getDevice() {
        return device;
    }
	
	private static String getDeviceStatus(int deviceStatus) {
        Log.d(MainActivity.TAG, "Peer status :" + deviceStatus);
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
                TextView bottom = (TextView) v.findViewById(R.id.device_details);
                if (top != null) {
                    top.setText(device.deviceName);
                }
                if (bottom != null) {
                    bottom.setText(getDeviceStatus(device.status));
                }
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
	

    /**
     * Update UI for this device.
     * 
     * @param device WifiP2pDevice object
     */
    public void updateThisDevice(WifiP2pDevice device) {
    	Log.d(TAG, "updateThisDevice()");
    	if(!thisDeviceCalled){
	        this.device = device;
	        TextView header = (TextView) mView.findViewById(R.id.this_device);
	        header.append(device.deviceName);
	        header.append(" - " + getDeviceStatus(device.status));
	        thisDeviceCalled = true;
    	}
    }

    public void clearPeers() {
        peers.clear();
        ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
    }

    
    public interface OnCreateGroupListener {
		
		public void onCreateGroup();
		
	}
    
}
