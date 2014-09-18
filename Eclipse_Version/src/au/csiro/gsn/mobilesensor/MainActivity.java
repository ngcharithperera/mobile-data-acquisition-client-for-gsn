package au.csiro.gsn.mobilesensor;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import au.csiro.openiot.R;

public class MainActivity extends Activity implements SensorEventListener {


	/**--------------NetworkProtocol--------------
	 *...........Motion Sensors...................
	 *		(1) - Accelerometer         [3 Values]
	 *		(2) - Gravity               [3 Values]
	 *		(3) - Gyroscope             [3 Values]
	 *		(4) - Linear Acceleration   [3 Values]
	 *		(5) - Rotation Vector       [4 Values]
	 *
	 *...........Position Sensors.................
	 *		(6) - Magnetic Field		[3 Values]
	 *		(7) - Orientation			[3 Values]
	 *		(8) - Proximity				[1 Values]
	 *
	 *...........Environment Sensors..............	
	 *		(9) - Ambient Temperature 	[1 Values]
	 *		(10)- Light					[1 Values]
	 *		(11)- Pressure				[1 Values]
	 *		(12)- Humidity				[1 Values]
	**/

	
	
	/**
	 * UI Variables for 12 Sensors
	 */
	private CheckBox cbxAccelerometer;
	private CheckBox cbxAmbientTemperature;
	private CheckBox cbxGyroscope;
	private CheckBox cbxLight;
	private CheckBox cbxPressure;
	private CheckBox cbxProximity;
	private CheckBox cbxMagneticField;
	private CheckBox cbxGravity;
	private CheckBox cbxOrientation;
	private CheckBox cbxHumidity;
	private CheckBox cbxLinearAcceleration;
	private CheckBox cbxRotationVector;

	
	/**
	 * Sensor Variables for 12 Sensors
	 */
	private Sensor snAccelerometer;
	private Sensor snAmbientTemperature;
	private Sensor snGyroscope;
	private Sensor snLight;
	private Sensor snPressure;
	private Sensor snProximity;
	private Sensor snRotationVector;
	private Sensor snLinearAcceleration;
	private Sensor snMagneticField;
	private Sensor snGravity;
	private Sensor snOrientation;
	private Sensor snHumidity;

	
	private boolean[] enabledSensors;
	private SensingService sensingService;
	private SensorManager mSensorManager;
	private SensorData sensorData;

	private static boolean isSensingServiceRunning;
	
	public static  String TAG = MainActivity.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		/**
		 * UI Settings
		 */
		initializeUIComponents();
		disableAllSensors();
		enableSupportedSensors();
		
		
		sensorData = new SensorData();		
		//sensingService = new SensingService(sensorData);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mInflater = getMenuInflater();
		mInflater.inflate(R.menu.option_menu_main_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		try {
			
		
		if (item.getItemId() == R.id.itmStartSensingService) {
			if (!isSensingServiceRunning()) {
				isSensingServiceRunning=true;
				enabledSensors = detectSelectedSensors();
				Intent iStartSensing = new Intent(this, SensingService.class);
				iStartSensing.putExtra("Enabled_Sensors", enabledSensors);
				SensingService.setSensorData(sensorData);
				//sensingService=
			//	Bundle b = new Bundle();
			//	b.putParcelable("SensorData", this.sensorData);
			//	iStartSensing.putExtras(b);
				startService(iStartSensing);
			}
		} else if (item.getItemId() == R.id.itmStopSensingService) {
			if(isSensingServiceRunning()){
			isSensingServiceRunning=false;
			Intent iStopSensing = new Intent(this, SensingService.class);
			stopService(iStopSensing);
			}
		} else if (item.getItemId() == R.id.itmPreferences) {
			Intent iPreferences = new Intent(this, Preferences.class);
			startActivity(iPreferences);
		}else if (item.getItemId() == R.id.itmServerConnection){
			NetworkCommunication.connectToGSNServer(detectSelectedSensors(), this);
		}
		
		
		} catch (Exception e) {
			System.out.println(e.toString());
		}finally{return super.onOptionsItemSelected(item);}
	}

	public static boolean isSensingServiceRunning() {
		
		return isSensingServiceRunning;
	}

	public static void setSensingServiceRunning(boolean isSensingServiceRunning) {
		MainActivity.isSensingServiceRunning = isSensingServiceRunning;
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		boolean isConfigured = NetworkCommunication.setPreferencesData(this);
		if(!isConfigured){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
					
			builder.setMessage(getString(R.string.alertMsgConfigurePreferences));
			builder.setCancelable(false);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					Intent iPreferences = new Intent(MainActivity.this, Preferences.class);				
					startActivity(iPreferences);
					
				}
			});
			
			
			AlertDialog alertDialog = builder.create();
			alertDialog.show();
			
		}
		return super.onMenuOpened(featureId, menu);
	}

	private boolean[] detectSelectedSensors() {
		enabledSensors = new boolean[] { false, false, false, false, false,
				false, false, false, false, false, false, false, false };

		if (this.cbxAccelerometer.isChecked()) {
			enabledSensors[1] = true;
		}
		if (this.cbxGravity.isChecked()) {
			enabledSensors[2] = true;
		}
		if (this.cbxGyroscope.isChecked()) {
			enabledSensors[3] = true;
		}
		if (this.cbxLinearAcceleration.isChecked()) {
			enabledSensors[4] = true;
		}
		if (this.cbxRotationVector.isChecked()) {
			enabledSensors[5] = true;
		}
		if (this.cbxMagneticField.isChecked()) {
			enabledSensors[6] = true;
		}
		if (this.cbxOrientation.isChecked()) {
			enabledSensors[7] = true;
		}
		if (this.cbxProximity.isChecked()) {
			enabledSensors[8] = true;
		}
		if (this.cbxAmbientTemperature.isChecked()) {
			enabledSensors[9] = true;
		}
		if (this.cbxLight.isChecked()) {
			enabledSensors[10] = true;
		}
		if (this.cbxPressure.isChecked()) {
			enabledSensors[11] = true;
		}
		if (this.cbxHumidity.isChecked()) {
			enabledSensors[12] = true;
		}

		return enabledSensors;
	}

	private void enableSupportedSensors() {
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
			cbxAccelerometer.setChecked(true);
			cbxAccelerometer.setClickable(true);
			snAccelerometer = mSensorManager
					.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			mSensorManager.registerListener(this, snAccelerometer,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
			cbxAmbientTemperature.setChecked(true);
			cbxAmbientTemperature.setClickable(true);
			snAmbientTemperature = mSensorManager
					.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
			mSensorManager.registerListener(this, snAmbientTemperature,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
			cbxGyroscope.setChecked(true);
			cbxGyroscope.setClickable(true);
			snGyroscope = mSensorManager
					.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
			mSensorManager.registerListener(this, snGyroscope,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
			cbxLight.setChecked(true);
			cbxLight.setClickable(true);
			snLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
			mSensorManager.registerListener(this, snLight,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
			cbxPressure.setChecked(true);
			cbxPressure.setClickable(true);
			snPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
			mSensorManager.registerListener(this, snPressure,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
			cbxProximity.setChecked(true);
			cbxProximity.setClickable(true);
			snProximity = mSensorManager
					.getDefaultSensor(Sensor.TYPE_PROXIMITY);
			mSensorManager.registerListener(this, snProximity,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null) {
			cbxRotationVector.setChecked(true);
			cbxRotationVector.setClickable(true);
			snRotationVector = mSensorManager
					.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
			mSensorManager.registerListener(this, snRotationVector,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null) {
			cbxLinearAcceleration.setChecked(true);
			cbxLinearAcceleration.setClickable(true);
			snLinearAcceleration = mSensorManager
					.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
			mSensorManager.registerListener(this, snLinearAcceleration,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
			cbxMagneticField.setChecked(true);
			cbxMagneticField.setClickable(true);
			snMagneticField = mSensorManager
					.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
			mSensorManager.registerListener(this, snMagneticField,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
			cbxGravity.setChecked(true);
			cbxGravity.setClickable(true);
			snGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
			mSensorManager.registerListener(this, snGravity,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) != null) {
			cbxOrientation.setChecked(true);
			cbxOrientation.setClickable(true);
			snOrientation = mSensorManager
					.getDefaultSensor(Sensor.TYPE_ORIENTATION);
			mSensorManager.registerListener(this, snOrientation,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null) {
			cbxHumidity.setChecked(true);
			cbxHumidity.setClickable(true);
			snHumidity = mSensorManager
					.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
			mSensorManager.registerListener(this, snHumidity,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	private void initializeUIComponents() {
		cbxAccelerometer = (CheckBox) findViewById(R.id.cbxAccelerometer);
		cbxAmbientTemperature = (CheckBox) findViewById(R.id.cbxAmbientTemperature);
		cbxGyroscope = (CheckBox) findViewById(R.id.cbxGyroscope);
		cbxLight = (CheckBox) findViewById(R.id.cbxLight);
		cbxPressure = (CheckBox) findViewById(R.id.cbxPressure);
		cbxProximity = (CheckBox) findViewById(R.id.cbxProximity);
		cbxMagneticField = (CheckBox) findViewById(R.id.cbxMagneticField);
		cbxGravity = (CheckBox) findViewById(R.id.cbxGravity);
		cbxOrientation = (CheckBox) findViewById(R.id.cbxOrientation);
		cbxHumidity = (CheckBox) findViewById(R.id.cbxHumidity);
		cbxLinearAcceleration = (CheckBox) findViewById(R.id.cbxLinearAcceleration);
		cbxRotationVector = (CheckBox) findViewById(R.id.cbxRotationVector);
	}

	private void disableAllSensors() {
		cbxAccelerometer.setChecked(false);
		cbxAmbientTemperature.setChecked(false);
		cbxGyroscope.setChecked(false);
		cbxLight.setChecked(false);
		cbxPressure.setChecked(false);
		cbxProximity.setChecked(false);
		cbxMagneticField.setChecked(false);
		cbxGravity.setChecked(false);
		cbxOrientation.setChecked(false);
		cbxHumidity.setChecked(false);
		cbxLinearAcceleration.setChecked(false);
		cbxRotationVector.setChecked(false);

		cbxAccelerometer.setClickable(false);
		cbxAmbientTemperature.setClickable(false);
		cbxGyroscope.setClickable(false);
		cbxLight.setClickable(false);
		cbxPressure.setClickable(false);
		cbxProximity.setClickable(false);
		cbxProximity.setClickable(false);
		cbxGravity.setClickable(false);
		cbxOrientation.setClickable(false);
		cbxHumidity.setClickable(false);
		cbxLinearAcceleration.setClickable(false);
		cbxRotationVector.setClickable(false);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			sensorData.setAccelerationX_axis_incl_gravity(event.values[0]);
			sensorData.setAccelerationY_axis_incl_gravity(event.values[1]);
			sensorData.setAccelerationZ_axis_incl_gravity(event.values[2]);
		}
		if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
			sensorData.setAmbientAirTemperature(event.values[0]);
		}
		if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
			sensorData.setGravityX_axis(event.values[0]);
			sensorData.setGravityY_axis(event.values[1]);
			sensorData.setGravityZ_axis(event.values[2]);
		}
		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			sensorData.setRotationX_axis(event.values[0]);
			sensorData.setRotationY_axis(event.values[1]);
			sensorData.setRotationZ_axis(event.values[2]);
		}
		if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
			sensorData.setLight(event.values[0]);
		}
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			sensorData.setAccelerationX_axis_excl_gravity(event.values[0]);
			sensorData.setAccelerationY_axis_excl_gravity(event.values[1]);
			sensorData.setAccelerationZ_axis_excl_gravity(event.values[2]);
		}
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			sensorData.setGeomagneticFieldX_axis(event.values[0]);
			sensorData.setGeomagneticFieldY_axis(event.values[1]);
			sensorData.setGeomagneticFieldZ_axis(event.values[2]);
		}
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			sensorData.setAzimuthX_axis(event.values[0]);
			sensorData.setPitchY_axis(event.values[1]);
			sensorData.setRollZ_axis(event.values[2]);
		}
		if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
			sensorData.setAmbientAirPressure(event.values[0]);
		}
		if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
			sensorData.setProximityDistance(event.values[0]);
		}
		if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
			sensorData.setAmbientRelativeHumidity(event.values[0]);
		}
		if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			sensorData.setRotationVectorX_axis(event.values[0]);
			sensorData.setRotationVectorY_axis(event.values[1]);
			sensorData.setRotationVectorZ_axis(event.values[2]);
			// sensorData.setRotationVectorScalar(event.values[3]);
		}
	}
}