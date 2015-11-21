package iotsuite.android.sensingframework;

import java.util.Locale;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings.Secure;


import com.google.gson.JsonObject;


/*
 * This class gives the Operating System information.
 * 
 */

public class AndroidInfoProbe extends Service implements iotsuite.android.sensingframework.ProbeKeys.AndroidInfoKeys,
		iotsuite.android.sensingframework.ProbeKeys.HardwareInfoKeys {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		sendData(getData()); // Send one time data and then stop the service
		stopService(new Intent(this, AndroidInfoProbe.class));

	}

	private JsonObject getData() {
		JsonObject data = new JsonObject();

		data.addProperty(FIRMWARE_VERSION, Build.VERSION.RELEASE);
		data.addProperty(BUILD_NUMBER, Build.PRODUCT + "-" + Build.TYPE + " "
				+ Build.VERSION.RELEASE + " " + Build.ID + " "
				+ Build.VERSION.INCREMENTAL + " " + Build.TAGS);
		data.addProperty(SDK, Integer.parseInt(Build.VERSION.SDK, 10));
		data.addProperty("LANGUAGE", getCurrentLanguage());
		data.addProperty(OS_VERSION, System.getProperty("os.version"));
		data.addProperty(MODE, getSoundProfile());

		return data;
	}

	private void sendData(final JsonObject data) {

		if (data == null) {
			return;
		}

		// Update here generated Sensor-specific java file
		//MainActivity.onDataReceived(data);

	}

	/**
	 * returns language set for the device in settings
	 * 
	 * @return String
	 */
	public String getCurrentLanguage() {

		Locale locale = Locale.getDefault();

		String currentLang = "";

		if (locale != null)
			currentLang = locale.getDisplayLanguage();

		return currentLang;
	}

	/**
	 * Method to get the currrent profile set in Settings get the Sound Profile
	 * set in Settings
	 * 
	 * @return
	 */
	public String getSoundProfile() {

		try {

			final AudioManager audioManager = (AudioManager) this
					.getSystemService(Context.AUDIO_SERVICE);
			int profileMode = audioManager.getRingerMode();

			if (profileMode == AudioManager.RINGER_MODE_NORMAL)
				return MODE_NORMAL;
			else if (profileMode == AudioManager.RINGER_MODE_SILENT)
				return MODE_SILENT;
			else if (profileMode == AudioManager.RINGER_MODE_VIBRATE)
				return MODE_VIBRATE;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

}
