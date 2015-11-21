package iotsuite.android.sensingframework;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AudioAnalyser {

	// *******************************************************//
	// Private data
	// ********************************************************//

	// The desired sampling rate for this analyser, in samples/sec.
	private int sampleRate = 8000;

	// Audio input block size, in samples.
	private int inputBlockSize = 8000;

	// The desired decimation rate for this analyser. Only 1 in
	// sampleDecimate blocks will actually be processed.
	private int sampleDecimate = 1;

	// Current signal power level, in dB relative to max. input power.
	private double currentPower = 0f;

	// Maximum signal amplitude for 16-bit data.
	private static final float MAX_16_BIT = 32768;

	// This fudge factor is added to the output to make a realistically
	// fully-saturated signal come to 0dB. Without it, the signal would
	// have to be solid samples of -32768 to read zero, which is not
	// realistic. This really is a fudge, because the best value depends
	// on the input frequency and sampling rate. We optimise here for
	// a 1kHz signal at 16,000 samples/sec.
	private static final float FUDGE = 0.6f;

	public float averagePower = -100.0f;

	public float noise = -100.0f;

	// If we got a read error, the error code.
	private int readError = AudioReader.Listener.ERR_OK;

	AudioReader audioReader = new AudioReader();
	AveragePower averagepower = new AveragePower();

	// Handler from the UI thread that has started me.
	Handler h;

	public AudioAnalyser(Handler newH) {
		this.h = newH;
	}

	/**
	 * Starting main run. Measuring Audio from the Device.
	 */

	public void measureStart() {

		audioReader.startReader(sampleRate, inputBlockSize * sampleDecimate,
				new AudioReader.Listener() {

					public final void onReadComplete(short[] buffer) {
						Log.d(Constants.LOG_TAG, "Reading Complete");

						// compute average and store in local variable
						processAudio(buffer);

						// send message to UI handler.
						Message m = Message.obtain();
						m.obj = Double.toString(getCurrentNoise());
						m.arg1 = Constants.SUCCESS;
						h.sendMessage(m);

					}

					public void onReadError(int error) {
						handleError(error);
						// send message to UI handler about error.
						Message m = Message.obtain();
						m.obj = "ERROR!! Code = " + error;
						m.arg1 = Constants.FAILURE;
						h.sendMessage(m);

					}
				});
	}

	public void measureStop() {
		// stop the audioreader
		audioReader.stopReader();

	}

	// ******************************************************************** //
	// Audio Processing.
	// ******************************************************************** //

	/**
	 * Handle audio input. This is called on the thread of the parent surface.
	 * 
	 * @param buffer
	 *            Audio data that was just read.
	 */
	private final void processAudio(short[] buffer) {
		// Process the buffer. While reading it, it needs to be locked.
		synchronized (buffer) {
			// Calculate the power now, while we have the input buffer;

			final int len = buffer.length;

			// calculate the signal power.
			currentPower = calculatePowerDb(buffer, 0, len);
			// Tell the reader we're done with the buffer.
			buffer.notify();
		}
		// noise = averagepower.CalculateAvgPower(currentPower);
		// setAverageNoiseLevel(noise);
		setCurrentPower(currentPower);

	}

	// ******************************************************************** //
	// Error Handling
	// ******************************************************************** //

	/**
	 * An error has occurred. The reader has been terminated.
	 * 
	 * @param error
	 *            ERR_XXX code describes the error.
	 */
	private void handleError(int error) {
		synchronized (this) {
			readError = error;
		}
	}

	/**
	 * Calculate the power of the given input signal.
	 * 
	 * @param sdata
	 *            Buffer containing the input samples to process.
	 * @param off
	 *            Offset in sdata of the data of interest.
	 * @param samples
	 *            Number of data samples to process.
	 * @return The calculated power in dB relative to the maximum input level;
	 *         hence 0dB represents maximum power, and minimum power is about
	 *         -95dB. Particular cases of interest:
	 *         <ul>
	 *         <li>A non-clipping full-range sine wave input is about -2.41dB.
	 *         <li>Saturated input (heavily clipped) approaches 0dB.
	 *         <li>A low-frequency fully saturated input can get above 0dB, but
	 *         this would be pretty artificial.
	 *         <li>A really tiny signal, which only occasionally deviates from
	 *         zero, can get below -100dB.
	 *         <li>A completely zero input will produce an output of -Infinity.
	 *         </ul>
	 *         <b>You must be prepared to handle this infinite result and
	 *         results greater than zero,</b> although clipping them off would
	 *         be quite acceptable in most cases.
	 */
	public final static double calculatePowerDb(short[] sdata, int off,
			int samples) {
		// Calculate the sum of the values, and the sum of the squared values.
		// We need longs to avoid running out of bits.
		double sum = 0;
		double sqsum = 0;
		for (int i = 0; i < samples; i++) {
			final long v = sdata[off + i];
			sum += v;
			sqsum += v * v;
		}

		double power = (sqsum - sum * sum / samples) / samples;

		// Scale to the range 0 - 1.
		power /= MAX_16_BIT * MAX_16_BIT;

		// Convert to dB, with 0 being max power. Add a fudge factor to make
		// a "real" fully saturated input come to 0 dB.
		return Math.log10(power) * 10f + FUDGE;
	}

	public void setAverageNoiseLevel(float noisedata) {
		noise = noisedata;
	}

	public float getAverageNoiseLevel() {
		return noise;
	}

	public void setCurrentPower(double power) {
		Log.d(Constants.LOG_TAG, "Setting current power as " + power);
		currentPower = power;
	}

	public double getCurrentNoise() {
		Log.d(Constants.LOG_TAG, "Returning current power as " + currentPower);
		return currentPower;
	}

}
