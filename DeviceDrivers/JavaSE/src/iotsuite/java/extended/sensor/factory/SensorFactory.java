package iotsuite.java.extended.sensor.factory;

import iotsuite.java.extended.sensingframework.*;

public class SensorFactory {
	public static void initializeSensingFramework(String simualtedSensor) {
		if (simualtedSensor.equals("BadgeReader"))
			new SimulatedBadgeReader();

		

		// Keep Adding additional simulated Sensor Name here as you add
		// into
		// package iotsuite.java.extended.sensingframework package

	}

}
