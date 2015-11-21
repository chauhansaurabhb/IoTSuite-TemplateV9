package iotsuite.android.sensingframework;

import iotsuite.android.sensingframework.ProbeKeys.ProximitySensorKeys;
import iotsuite.android.sensingmiddleware.PubSubsSensingFramework;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import com.google.gson.JsonObject;

import edu.mit.media.funf.time.TimeUtil;

public class ProximitySensorProbe extends Service implements
		SensorEventListener, ProximitySensorKeys {

	private SensorManager mSensorManager;
	private Sensor mSensor;

	private PubSubsSensingFramework pubSubSensingFramework = null;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {

		pubSubSensingFramework = PubSubsSensingFramework.getInstance();

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(getSensorType());
		mSensorManager.registerListener(this, mSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onDestroy() {
		mSensorManager.unregisterListener(this);
	}

	private int getSensorType() {
		return Sensor.TYPE_PROXIMITY;
	}

	public String[] getValueNames() {
		return new String[] { DISTANCE };
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		JsonObject sensingData = new JsonObject();

		sensingData.addProperty(TIMESTAMP,
				TimeUtil.uptimeNanosToTimestamp(event.timestamp));
		sensingData.addProperty(ACCURACY, event.accuracy);

		final String[] valueNames = getValueNames();
		int valuesLength = Math.min(event.values.length, valueNames.length);

		for (int i = 0; i < valuesLength; i++) {
			String valueName = valueNames[i];
			sensingData.addProperty(valueName, event.values[i]);
		}

		sendData(sensingData);

	}

	private void sendData(final JsonObject data) {

		if (data == null) {
			return;
		}

		pubSubSensingFramework.publish(ProximitySensorEvent, data);

	}

}
