package iotsuite.android.sensingmiddleware;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;

public class PubSubsSensingFramework {

	private Map<String, Set<ISensorListener>> subscriberMap = new Hashtable<String, Set<ISensorListener>>();

	static PubSubsSensingFramework singletonInstance;

	public static PubSubsSensingFramework getInstance() {

		if (singletonInstance == null) {
			singletonInstance = new PubSubsSensingFramework();
		}

		return singletonInstance;

	}

	/*
	 * Data consumer implements this interface.
	 */

	public void registerForSensorData(ISensorListener s, String eventSignature) {

		registerNewSubscriber(s, eventSignature);
	}

	private void registerNewSubscriber(ISensorListener s, String eSig) {

		// create Map by EventName

		if (subscriberMap.containsKey(eSig)) {

			Set<ISensorListener> tempSet = subscriberMap.get(eSig);
			tempSet.add(s);
		} else {
			Set<ISensorListener> newSet = new HashSet<ISensorListener>();
			newSet.add(s);
			subscriberMap.put(eSig, newSet);

		}

		System.out.println(subscriberMap);

	}

	/*
	 * Data producer implements this interface.
	 */

	public void publish(String eventName, JsonObject dataEvent) {

		Set<ISensorListener> subscriberEventSet = getSubscribersForEvent(eventName);

		if (subscriberEventSet != null) {
			for (ISensorListener s : subscriberEventSet) {
				s.onDataReceived(eventName, dataEvent);
			}
		}

	}

	private Set<ISensorListener> getSubscribersForEvent(String eventName) {

		return subscriberMap.get(eventName);
	}

}
