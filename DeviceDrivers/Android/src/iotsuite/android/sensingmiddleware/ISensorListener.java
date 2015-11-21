package iotsuite.android.sensingmiddleware;

import com.google.gson.JsonObject;

/*
 * This interface is responsible for defining interface.
 *  The data listner has to implement this interface
 * 
 * @Author: Pankesh 
 * @Date : 1st May, 2014
 */

public interface ISensorListener {
	public abstract void onDataReceived(String eventName, JsonObject dataEvent);

}
