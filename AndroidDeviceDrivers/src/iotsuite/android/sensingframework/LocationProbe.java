package iotsuite.android.sensingframework;

import iotsuite.android.sensingframework.ProbeKeys.LocationKeys;
import iotsuite.android.sensingmiddleware.PubSubsSensingFramework;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.google.gson.JsonObject;

import edu.mit.media.funf.time.DecimalTimeUnit;

public class LocationProbe extends Service implements LocationListener,
		LocationKeys {

	private LocationManager locationManager;

	private boolean useGps = true;

	private boolean useNetwork = true;

	private boolean useCache = true;
	private PubSubsSensingFramework pubSubSensingFramework = null;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {

		pubSubSensingFramework = PubSubsSensingFramework.getInstance();

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// if (useGps) {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);
		// }

		/*
		 * if (useNetwork) { locationManager.requestLocationUpdates(
		 * LocationManager.NETWORK_PROVIDER, 0, 0, this); } if (useCache) {
		 * 
		 * this.onLocationChanged(locationManager
		 * .getLastKnownLocation(LocationManager.GPS_PROVIDER));
		 * this.onLocationChanged(locationManager
		 * .getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
		 * 
		 * }
		 */

	}

	@Override
	public void onDestroy() {

		locationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {

		JsonObject locationData = new JsonObject();

		// if (location != null) {
		// String provider = location.getProvider();
		/*
		 * if (provider == null || (useGps &&
		 * LocationManager.GPS_PROVIDER.equals(provider)) || (useNetwork &&
		 * LocationManager.NETWORK_PROVIDER .equals(provider))) {
		 */
		locationData.addProperty(LONGITUDE, location.getLongitude());
		locationData.addProperty(LATITUDE, location.getLatitude());
		locationData.addProperty(TIMESTAMP, DecimalTimeUnit.MILLISECONDS
				.toSeconds(locationData.get("mTime").getAsBigDecimal()));
		sendData(locationData);
		// }
		// }

	}

	private void sendData(final JsonObject data) {

		if (data == null) {
			return;
		}

		pubSubSensingFramework.publish(LocationEvent, data);

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}
