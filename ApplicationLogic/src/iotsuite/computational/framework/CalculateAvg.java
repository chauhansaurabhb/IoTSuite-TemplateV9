package iotsuite.computational.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.gson.JsonObject;


import iotsuite.localmiddleware.IDataListener;
import iotsuite.localmiddleware.PubSubsSensingFramework;

public class CalculateAvg implements IDataListener {

	private PubSubsSensingFramework pubSubSensingFramework = null;

	final List<Double> temps = Collections
			.synchronizedList(new ArrayList<Double>());
	private double currentAverage;
	private int numSample = 0;
	static int NUM_SAMPLE_FOR_AVG;

	public CalculateAvg() {
		pubSubSensingFramework = PubSubsSensingFramework.getInstance();
		pubSubSensingFramework.registerForSensorData(this, "AVG-REQUEST");
	}

	
	
	@Override
	public void onDataReceived(String eventName, JsonObject data) {

		// Unwrapping is performed here	
		double value = 0;
		String unitOfMeasurement = null;
		value=data.get("tempValue").getAsDouble();
		unitOfMeasurement=data.get("unitOfMeasurement").getAsString();		
	    NUM_SAMPLE_FOR_AVG=data.get("sampleValue").getAsInt();
	

		synchronized (this.temps) {
			numSample = numSample + 1;
			if (numSample <= NUM_SAMPLE_FOR_AVG) {
				// Handling of temperature Value with different unit.
				if (unitOfMeasurement.equals("F")) {
					double convertToCelcius = (value - 32) * (5 / 9);
					temps.add(convertToCelcius);
				} else {
					temps.add(value);
				}
				currentAverage = 0;
				for (Double temp : temps) {
					currentAverage += temp;
				}
				currentAverage /= temps.size();

			}

			if (numSample == NUM_SAMPLE_FOR_AVG) {
				numSample = 0;

			    JsonObject publishData = new JsonObject();
				publishData.addProperty("tempValue", currentAverage);
				publishData.addProperty("unitOfMeasurement", "C");
				pubSubSensingFramework.publish("AVG-RESULT", publishData);

			}

		}

	}
	
	
	
}