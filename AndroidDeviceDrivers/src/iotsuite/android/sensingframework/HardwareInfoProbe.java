package iotsuite.android.sensingframework;

import iotsuite.android.sensingframework.ProbeKeys.HardwareInfoKeys;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.google.gson.JsonObject;

/*
 * This class provides Hardware information about device. 
 */

public class HardwareInfoProbe extends Service implements HardwareInfoKeys {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		sendData(getData()); // Send one time data and then stop the service
		stopService(new Intent(this, HardwareInfoProbe.class));

	}

	private JsonObject getData() {
		JsonObject data = new JsonObject();

		// shows error- why ??
		// data.addProperty(WIFI_MAC, ((WifiManager)
		// context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress());

		// Shows - error - why???
		/*
		 * String bluetoothMac = getBluetoothMac(); if (bluetoothMac != null) {
		 * data.addProperty(BLUETOOTH_MAC, bluetoothMac); }
		 */
		data.addProperty(ANDROID_ID,
				Secure.getString(this.getContentResolver(), Secure.ANDROID_ID));
		data.addProperty(BRAND, Build.BRAND);
		data.addProperty(MODEL, Build.MODEL);
		data.addProperty(MANUFACTURER, Build.MANUFACTURER);
		data.addProperty(HOST, Build.HOST);

		data.addProperty("DEVICE_TYPE", getDeviceType());

		// -Shows error - why?
		// data.addProperty(DEVICE_ID, ((TelephonyManager)
		// this.getSystemService(this.TELEPHONY_SERVICE)).getDeviceId());
		return data;
	}

	private String getBluetoothMac() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		return (adapter != null) ? adapter.getAddress() : null;
	}

	private void sendData(final JsonObject data) {

		if (data == null) {
			return;
		}

		// Update here generated Sensor-specific java file
		// MainActivity.onDataReceived(data);

	}

	/**
	 * Method to get the phone type of the android device Get the device type
	 * 
	 * @return String
	 */
	public String getDeviceType() {
		TelephonyManager mTelephonyMgr = null;
		mTelephonyMgr = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);

		if (mTelephonyMgr.getPhoneType() == 0)
			return PHONE_TYPE_NONE;
		if (mTelephonyMgr.getPhoneType() == 1)
			return PHONE_TYPE_GSM;
		if (mTelephonyMgr.getPhoneType() == 2)
			return PHONE_TYPE_CDMA;
		if (mTelephonyMgr.getPhoneType() == 3)
			return PHONE_TYPE_SIP;
		else
			return "no";
	}

}
