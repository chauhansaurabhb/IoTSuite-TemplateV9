package iotsuite.computational.factory;

import iotsuite.computational.framework.CalculateAvg;

public class ComputationalFactory {

	public static void initializeCompuatioanlComponent(String computationName) {
		if (computationName.equals("AVG-CALCULATE"))
			new CalculateAvg();

		// if (computationName.equals("SUM-CALCULATE"))
		// new CalculateSum();

		// Keep Adding additional computational ComponentName here as you add
		// into
		// iotsuite.computational.framework package

	}

}
