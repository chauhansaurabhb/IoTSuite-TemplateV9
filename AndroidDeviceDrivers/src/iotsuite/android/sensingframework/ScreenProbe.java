package iotsuite.android.sensingframework;

import iotsuite.android.sensingframework.ProbeKeys.ScreenKeys;
import iotsuite.android.sensingmiddleware.PubSubsSensingFramework;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.google.gson.JsonObject;

/*
 * Records when the screen turns off and on.
 */

public class ScreenProbe extends Service implements ScreenKeys {

	private PubSubsSensingFramework pubSubSensingFramework = null;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {

		pubSubSensingFramework = PubSubsSensingFramework.getInstance();

		//
		// An intent filter is an expression in an app's manifest file that
		// specifies
		// the type of intents that the component would like to receive.
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);

		// The following line register to receive information specified in the
		// filters
		// from unknown components, which have the above filter
		// in their manifest files.
		registerReceiver(screenReceiver, filter);

	}

	private final BroadcastReceiver screenReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			JsonObject data = new JsonObject();
			if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				data.addProperty(SCREEN_ON, false);
				sendData(data);
			} else if (Intent.ACTION_SCREEN_ON.equals(action)) {
				data.addProperty(SCREEN_ON, true);
				sendData(data);
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

		pubSubSensingFramework.publish(ScreenEvent, data);

	}

}
