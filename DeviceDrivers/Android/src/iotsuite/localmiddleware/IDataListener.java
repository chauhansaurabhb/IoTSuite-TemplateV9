package iotsuite.localmiddleware;

import com.google.gson.JsonObject;

public interface IDataListener {
	// public abstract void onDataReceived(String eventName, JsonObject
	// dataEvent);
	public abstract void onDataReceived(String eventName, JsonObject data);

}
