package au.csiro.gsn.mobilesensor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import au.csiro.openiot.R;

public class NetworkCommunication {

	private static String Tag = NetworkCommunication.class.getSimpleName();

	private static PrintWriter out;
	private static Socket socket;
	private static InetAddress gsnServerAddress;
	private static SharedPreferences preferences;
	private static String ipAddressGSNServer;
	private static int portNumberGSNServer;
	private static String serverRespondMessage;
	private static MainActivity mainActivity;

	public static void connectToGSNServer(boolean[] enabledSensors,
			MainActivity mainActivity) {
		try {
			String metaDataPacket = NetworkCommunication
					.convertBooleanValuesToChar(enabledSensors);
			out = getConnectionConfiguration();
			out.println(metaDataPacket);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			String serverRespond = null;
			serverRespond = in.readLine();
			if (Integer.parseInt(serverRespond) == 1) {
				serverRespondMessage = mainActivity.getString(R.string.msgConnectionEstablished);
			} else {
				serverRespondMessage = mainActivity.getString(R.string.msgConnectionFailed);
			}

		} catch (Exception e) {
			Log.d(Tag, e.toString());
			serverRespondMessage = mainActivity.getString(R.string.msgConnectionFailed);
		}
		Toast.makeText(mainActivity,serverRespondMessage, Toast.LENGTH_LONG).show();
		

		if (serverRespondMessage == "Connection Failed") {
	
		}

	}

	public static String convertBooleanValuesToChar(boolean[] enabledSensors) {
		String strEnabledSensors = null;
		for (int i = 1; i < enabledSensors.length; i++) {
			if (enabledSensors[i]) {
				if (strEnabledSensors != null) {
					strEnabledSensors = strEnabledSensors + "1";
				} else {
					strEnabledSensors = "1";
				}
			} else {
				if (strEnabledSensors != null) {
					strEnabledSensors = strEnabledSensors + "0";
				} else {
					strEnabledSensors = "0";
				}
			}
		}
		return strEnabledSensors;
	}

	public static void sendSensorDataToGSNServer(String sensorDataPacket) {

		out = getConnectionConfiguration();
		out.println(sensorDataPacket);
	}

	public static PrintWriter getConnectionConfiguration() {
		try {
			gsnServerAddress = InetAddress.getByName(ipAddressGSNServer);
			Log.d("ClientActivity", "C: Connecting...");
			socket = new Socket(gsnServerAddress, portNumberGSNServer);
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);

		} catch (Exception e) {
			Log.d(Tag, e.toString());

		}
		return out;
	}

	public static boolean setPreferencesData(MainActivity mainActivity) {
		preferences = PreferenceManager
				.getDefaultSharedPreferences(mainActivity);
		ipAddressGSNServer = preferences.getString("IPAddress", "0.0.0.0");
		portNumberGSNServer = Integer.parseInt(preferences.getString(
				"PortNumber", "00000"));
		if (portNumberGSNServer == 00000 || ipAddressGSNServer == "0.0.0.0") {
			
			return false;
		}else{
			return true;
		}
	}
}
