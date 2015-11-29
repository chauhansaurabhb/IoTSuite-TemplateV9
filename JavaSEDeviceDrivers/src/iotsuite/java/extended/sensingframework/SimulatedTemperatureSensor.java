package iotsuite.java.extended.sensingframework;

import iotsuite.localmiddleware.IDataListener;
import iotsuite.localmiddleware.PubSubsSensingFramework;

import java.util.Random;

import com.google.gson.JsonObject;


public class SimulatedTemperatureSensor implements IDataListener {

	private PubSubsSensingFramework pubSubSensingFramework = null;
	private static int SAMPLE_PERIOD ;
	private static int SAMPLE_DURATION ;
	private static int temp ;
	static double tempValue;
	
	public SimulatedTemperatureSensor(){
		
		pubSubSensingFramework = PubSubsSensingFramework.getInstance();
		pubSubSensingFramework.registerForSensorData(this, "TemperatureSensor-Parameter");
		pubSubSensingFramework.registerForSensorData(this, "startPeriodicTemperatureSensorSampling");

		
		/*while (temp <= SAMPLE_DURATION) {
			try {
				gettempMeasurement();
				Thread.sleep(SAMPLE_PERIOD);
				temp = temp + SAMPLE_PERIOD;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
	}
	private static double showRandomInteger(int aStart, int aEnd, Random aRandom) {
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		double range = (double) aEnd - (double) aStart + 1;
		double fraction = (long) (range * aRandom.nextDouble());
		double randomNumber = (fraction + aStart);
		return randomNumber;
	}

	private void  gettempMeasurement() {

		int START = 20;
		int END = 40;
		Random random = new Random();
		 tempValue = showRandomInteger(START, END, random);
		 	JsonObject publishData = new JsonObject();
			publishData.addProperty("tempValue", tempValue);
			publishData.addProperty("unitOfMeasurement", "C");
			pubSubSensingFramework.publish("TemperatureSensor-Result", publishData);
		
	}
	
	@Override
	public void onDataReceived(String eventName, JsonObject data) {
		
		 if (eventName.equals("TemperatureSensor-Parameter")){	
			 SAMPLE_PERIOD = data.get("SAMPLE_PERIOD").getAsInt();
			 SAMPLE_DURATION = data.get("SAMPLE_DURATION").getAsInt();
			 temp = data.get("temp").getAsInt();
		 }
		 
		 if(eventName.equalsIgnoreCase("startPeriodicTemperatureSensorSampling")){
			 while (temp <= SAMPLE_DURATION) {
				try {
					gettempMeasurement();
					Thread.sleep(SAMPLE_PERIOD);
					temp = temp + SAMPLE_PERIOD;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}			 
		 }		
	}
}
