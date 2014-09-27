package com.wobgames.whosnext;

import android.net.wifi.p2p.WifiP2pDevice;

public class GameDevice {
	
	private WifiP2pDevice mDevice;
	private User mUser;
	private Boolean isGroupOwner;
	
	public GameDevice(WifiP2pDevice device) {
		this.mDevice = device;
		this.isGroupOwner = false;
	}
	
	public GameDevice(WifiP2pDevice device, String username) {
		this.mDevice = device;
		this.mUser = new User(username);
		this.isGroupOwner = false;
	}
	
	public void setDevice(WifiP2pDevice device) { this.mDevice = device; }
	public WifiP2pDevice device() { return mDevice; }
	
	public void setUserName(String name) { mUser.setName(name); }
	public User user() { return mUser; }
	
	public void setIsGroupOwner(Boolean value) { isGroupOwner = value; }
	public Boolean isGroupOwner() { return isGroupOwner; }

}
