package iotsuite.android.sensingframework;

import iotsuite.android.sensingframework.ProbeKeys.BatteryKeys;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;

import com.google.gson.JsonObject;

/* 
 * Information about the type and current state of the battery in the device.
 */

public class BatteryProbe extends Service implements BatteryKeys {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		this.registerReceiver(receiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
	}

	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			JsonObject batteryData = new JsonObject();

			batteryData.addProperty(HEALTH,
					intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0));
			batteryData.addProperty(LEVEL,
					intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0));
			batteryData.addProperty(PRESENT,
					intent.getIntExtra(BatteryManager.EXTRA_PRESENT, 0));
			batteryData.addProperty(TECHNOLOGY,
					intent.getIntExtra(BatteryManager.EXTRA_TECHNOLOGY, 0));
			batteryData.addProperty(TEMPERATURE,
					intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0));
			batteryData.addProperty(SCALE,
					intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0));
			batteryData.addProperty(ICON_SMALL,
					intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, 0));
			batteryData.addProperty(STATUS,
					intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0));
			batteryData.addProperty(VOLTAGE,
					intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0));
			batteryData.addProperty(PLUGGED,
					intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0));

			int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,
					-1);
			int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			int remainingLevel = -1;
			if (currentLevel >= 0 && scale > 0) {
				remainingLevel = (currentLevel * 100) / scale;
			}

			batteryData.addProperty(BATTERY_PERCENTAGE, remainingLevel);

			sendData(batteryData);

		}
	};

	private void sendData(final JsonObject data) {

		if (data == null) {
			return;
		}

		// Update here generated Sensor-specific java file
		// MainActivity.onDataReceived(data);

	}

	@Override
	public void onDestroy() {
		this.unregisterReceiver(receiver);

	}
}
