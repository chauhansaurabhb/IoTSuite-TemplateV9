package iotsuite.java.extended.sensor.factory;

import iotsuite.java.extended.simulated.actuatingframework.SimulatedHeater;
import iotsuite.java.extended.simulated.sensingframework.*;

public class SensorFactory {
	public static void initializeSensingFramework(String simualtedSensor) {
		if (simualtedSensor.equals("BadgeReader"))
			new SimulatedBadgeReader();
		
		if (simualtedSensor.equals("Heater"))
			new SimulatedHeater();

		if (simualtedSensor.equals("TemperatureSensor"))
			new SimulatedTemperatureSensor();
		

		// Keep Adding additional simulated Sensor Name here as you add
		// into
		// package iotsuite.java.extended.sensingframework package

	}

}
