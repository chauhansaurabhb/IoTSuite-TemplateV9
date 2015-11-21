package iotsuite.android.sensingframework;

public class AveragePower

{
	// Number of updates over which we average the VU meter to get
	// a rolling average. 32 is about 2 seconds.
	private static final int METER_AVERAGE_COUNT = 32;
	// Current and previous power levels.
	private float currentPower = 0f;
	private float prevPower = 0f;

	// Buffered old meter levels, used to calculate the rolling average.
	// Index of the most recent value.
	private float[] powerHistory = null;

	private int historyIndex = 0;

	// Rolling average power value, calculated from the history buffer.
	public float averagePower = -100.0f;

	// ******************************************************************** //
	// Data Updates.
	// ******************************************************************** //

	/**
	 * New data from the instrument has arrived. This method is called on the
	 * thread of the instrument.
	 * 
	 * @param power
	 *            The current instantaneous signal power level in dB, from -Inf
	 *            to 0+. Typical range is -100dB to 0dB, 0dB representing max.
	 *            input power.
	 */
	final float CalculateAvgPower(double power) {
		synchronized (this) {
			// Save the current level. Clip it to a reasonable range.
			if (power < -100.0)
				power = -100.0;
			else if (power > 0.0)
				power = 0.0;
			currentPower = (float) power;

			if (++historyIndex >= powerHistory.length)
				historyIndex = 0;

			prevPower = powerHistory[historyIndex];

			powerHistory[historyIndex] = (float) power;

			averagePower -= prevPower / METER_AVERAGE_COUNT;

			averagePower += (float) power / METER_AVERAGE_COUNT;
		}
		return averagePower;

	}

	// ******************************************************************** //
	// Private Data.
	// ******************************************************************** //

}
