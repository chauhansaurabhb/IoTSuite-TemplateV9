package iotsuite.android.sensingframework;

import iotsuite.android.sensingframework.ProbeKeys.BluetoothKeys;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.JsonObject;

/*
 * This probe continously discovers Bluetooth devices within range.
 */

public class BluetoothProbe extends Service implements BluetoothKeys {

	private BluetoothAdapter adapter;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// This assumes that bluetooth is enabled

		// Get local BluetoothAdapter
		adapter = BluetoothAdapter.getDefaultAdapter();

		// Register the BroadcastReceiver
		IntentFilter intentFilter = new IntentFilter(
				BluetoothDevice.ACTION_FOUND);

		// The local Bluetooth adapter has finished the device discovery
		// process.
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

		registerReceiver(receiver, intentFilter);

	}

	// This will receive messages regarding Bluetooth actions

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			// When discovery finds a device.
			if (action.equals(BluetoothDevice.ACTION_FOUND)) {

				Log.i("state", "I am in BluetoothDevice.ACTION_FOUND");

				JsonObject bluetoothDevice = new JsonObject();

				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				bluetoothDevice.addProperty(NAME, device.getName());
				bluetoothDevice.addProperty(ADDRESS, device.getAddress());

				sendData(bluetoothDevice);

			} else if (action
					.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {

				Log.i("state",
						"I am in BluetoothAdapter.ACTION_DISCOVERY_FINISHED");

				adapter.startDiscovery();

			}
		}
	};

	@Override
	public void onDestroy() {

	}

	private void sendData(final JsonObject data) {

		if (data == null) {
			return;
		}

		// Update here generated Sensor-specific java file

		// MainActivity.onDataReceived(data);

	}

}
