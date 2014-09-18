package au.csiro.gsn.mobilesensor;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class SensingService extends Service {
	private String Tag = SensingService.class.getSimpleName();
	private String sensorDataPacket;
	// private SensorData sensorData;
	private boolean[] enabledSensors;

	private SharedPreferences preferences;
	private String ipAddressGSNServer;
	private int portNumberGSNServer;
	// private int sampleRate;
	private static SensorData sensorData;
	private Updater updater;

	private int samplingRate;

	// private boolean runFlag = false;

	private boolean[] defaultEnabledSensors = new boolean[] { false, false,
			false, false, false, false, false, false, false, false, false,
			false, false };
	private MainActivity mainactivity;

	@Override
	public void onCreate() {
		this.updater = new Updater();

		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		super.onStartCommand(intent, flags, startId);

		// this.runFlag = true;

		return 1;
	}

	public static void setSensorData(SensorData sensorData) {
		SensingService.sensorData = sensorData;
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// this.runFlag = false;
		// this.updater.interrupt();
		// this.updater = null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		enabledSensors = intent.getBooleanArrayExtra("Enabled_Sensors");

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		ipAddressGSNServer = preferences.getString("IPAddress", "0.0.0.0");
		portNumberGSNServer = Integer.parseInt(preferences.getString(
				"PortNumber", "00000"));
		samplingRate = Integer.parseInt(preferences.getString("SamplingRate",
				"1"));
		sensorDataPacket = createDataPacketForTransmission(enabledSensors);
		this.updater.start();
		// // sentToServer(sensorDataPacket);

	}

	private String createDataPacketForTransmission(boolean[] enabledSensors) {
		String strEnabledSensors = NetworkCommunication
				.convertBooleanValuesToChar(enabledSensors);
		sensorDataPacket = null;
		sensorDataPacket = strEnabledSensors;
		if (enabledSensors[1]) {
			sensorDataPacket = sensorDataPacket
					+ ":"
					+ Float.toString(sensorData
							.getAccelerationX_axis_incl_gravity())
					+ ":"
					+ Float.toString(sensorData
							.getAccelerationY_axis_incl_gravity())
					+ ":"
					+ Float.toString(sensorData
							.getAccelerationZ_axis_incl_gravity());
		}
		if (enabledSensors[2]) {
			sensorDataPacket = sensorDataPacket + ":"
					+ Float.toString(sensorData.getGravityX_axis()) + ":"
					+ Float.toString(sensorData.getGravityY_axis()) + ":"
					+ Float.toString(sensorData.getGravityZ_axis());
		}
		if (enabledSensors[3]) {
			sensorDataPacket = sensorDataPacket + ":"
					+ Float.toString(sensorData.getRotationX_axis()) + ":"
					+ Float.toString(sensorData.getRotationY_axis()) + ":"
					+ Float.toString(sensorData.getRotationZ_axis());
		}
		if (enabledSensors[4]) {
			sensorDataPacket = sensorDataPacket
					+ ":"
					+ Float.toString(sensorData
							.getAccelerationX_axis_excl_gravity())
					+ ":"
					+ Float.toString(sensorData
							.getAccelerationY_axis_excl_gravity())
					+ ":"
					+ Float.toString(sensorData
							.getAccelerationZ_axis_excl_gravity());
		}
		if (enabledSensors[5]) {
			sensorDataPacket = sensorDataPacket + ":"
					+ Float.toString(sensorData.getRotationVectorX_axis())
					+ ":"
					+ Float.toString(sensorData.getRotationVectorY_axis())
					+ ":"
					+ Float.toString(sensorData.getRotationVectorZ_axis())
					+ ":"
					+ Float.toString(sensorData.getRotationVectorScalar());
		}
		if (enabledSensors[6]) {
			sensorDataPacket = sensorDataPacket + ":"
					+ Float.toString(sensorData.getGeomagneticFieldX_axis())
					+ ":"
					+ Float.toString(sensorData.getGeomagneticFieldY_axis())
					+ ":"
					+ Float.toString(sensorData.getGeomagneticFieldZ_axis());
		}
		if (enabledSensors[7]) {
			sensorDataPacket = sensorDataPacket + ":"
					+ Float.toString(sensorData.getAzimuthX_axis()) + ":"
					+ Float.toString(sensorData.getPitchY_axis()) + ":"
					+ Float.toString(sensorData.getRollZ_axis());
		}
		if (enabledSensors[8]) {
			sensorDataPacket = sensorDataPacket + ":"
					+ Float.toString(sensorData.getProximityDistance());
		}
		if (enabledSensors[9]) {
			sensorDataPacket = sensorDataPacket + ":"
					+ Float.toString(sensorData.getAmbientAirTemperature());
		}
		if (enabledSensors[10]) {
			sensorDataPacket = sensorDataPacket + ":"
					+ Float.toString(sensorData.getLight());
		}
		if (enabledSensors[11]) {
			sensorDataPacket = sensorDataPacket + ":"
					+ Float.toString(sensorData.getAmbientAirPressure());
		}
		if (enabledSensors[12]) {
			sensorDataPacket = sensorDataPacket + ":"
					+ Float.toString(sensorData.getAmbientRelativeHumidity());
		}

		return sensorDataPacket;
	}

	private class Updater extends Thread {

		public Updater() {
			super("UpdaterService-Updater");
		}

		@Override
		public void run() {
			SensingService sensingService = SensingService.this;
			while (MainActivity.isSensingServiceRunning()) {
				// Log.d(TAG, "Updater running");
				try {

					NetworkCommunication
							.sendSensorDataToGSNServer(createDataPacketForTransmission(enabledSensors));

					// Log.d(TAG, "Updater ran");
					Thread.sleep(samplingRate * 1000);
				} catch (InterruptedException e) {
					MainActivity.setSensingServiceRunning(false);
				}
			}
		}
	} // Updater

}
