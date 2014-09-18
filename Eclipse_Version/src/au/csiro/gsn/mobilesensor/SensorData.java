package au.csiro.gsn.mobilesensor;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.CheckBox;

public class SensorData implements Parcelable {
	
	private String Tag = SensorData.class.getSimpleName();
	// ------------Accelerometer-------------------
	private float accelerationX_axis_incl_gravity;
	private float accelerationY_axis_incl_gravity;
	private float accelerationZ_axis_incl_gravity;

	// ------------Gravity-------------------------
	private float gravityX_axis;
	private float gravityY_axis;
	private float gravityZ_axis;

	// ------------Gyroscope-----------------------
	private float rotationX_axis;
	private float rotationY_axis;
	private float rotationZ_axis;

	// ------------LinearAcceleration--------------
	private float accelerationX_axis_excl_gravity;
	private float accelerationY_axis_excl_gravity;
	private float accelerationZ_axis_excl_gravity;

	// ------------RotationVector------------------
	private float rotationVectorX_axis;
	private float rotationVectorY_axis;
	private float rotationVectorZ_axis;
	private float rotationVectorScalar;

	// ------------MagneticField-------------------
	private float geomagneticFieldX_axis;
	private float geomagneticFieldY_axis;
	private float geomagneticFieldZ_axis;

	// ------------Orientation---------------------
	private float azimuthX_axis;
	private float pitchY_axis;
	private float rollZ_axis;

	// ------------Proximity-----------------------
	private float proximityDistance;

	// ------------AmbientTemperature--------------
	private float ambientAirTemperature;

	// ------------Light-----------------------
	private float light;

	// ------------Pressure-----------------------
	private float ambientAirPressure;

	// ------------Humidity-----------------------
	private float ambientRelativeHumidity;

	public SensorData() {
		super();
	}

	public float getAccelerationX_axis_incl_gravity() {
		return accelerationX_axis_incl_gravity;
	}

	public void setAccelerationX_axis_incl_gravity(
			float accelerationX_axis_incl_gravity) {
		this.accelerationX_axis_incl_gravity = accelerationX_axis_incl_gravity;
	}

	public float getAccelerationY_axis_incl_gravity() {
		return accelerationY_axis_incl_gravity;
	}

	public void setAccelerationY_axis_incl_gravity(
			float accelerationY_axis_incl_gravity) {
		this.accelerationY_axis_incl_gravity = accelerationY_axis_incl_gravity;
	}

	public float getAccelerationZ_axis_incl_gravity() {
		return accelerationZ_axis_incl_gravity;
	}

	public void setAccelerationZ_axis_incl_gravity(
			float accelerationZ_axis_incl_gravity) {
		this.accelerationZ_axis_incl_gravity = accelerationZ_axis_incl_gravity;
	}

	public float getGravityX_axis() {
		return gravityX_axis;
	}

	public void setGravityX_axis(float gravityX_axis) {
		this.gravityX_axis = gravityX_axis;
	}

	public float getGravityY_axis() {
		return gravityY_axis;
	}

	public void setGravityY_axis(float gravityY_axis) {
		this.gravityY_axis = gravityY_axis;
	}

	public float getGravityZ_axis() {
		return gravityZ_axis;
	}

	public void setGravityZ_axis(float gravityZ_axis) {
		this.gravityZ_axis = gravityZ_axis;
	}

	public float getRotationX_axis() {
		return rotationX_axis;
	}

	public void setRotationX_axis(float rotationX_axis) {
		this.rotationX_axis = rotationX_axis;
	}

	public float getRotationY_axis() {
		return rotationY_axis;
	}

	public void setRotationY_axis(float rotationY_axis) {
		this.rotationY_axis = rotationY_axis;
	}

	public float getRotationZ_axis() {
		return rotationZ_axis;
	}

	public void setRotationZ_axis(float rotationZ_axis) {
		this.rotationZ_axis = rotationZ_axis;
	}

	public float getAccelerationX_axis_excl_gravity() {
		return accelerationX_axis_excl_gravity;
	}

	public void setAccelerationX_axis_excl_gravity(
			float accelerationX_axis_excl_gravity) {
		this.accelerationX_axis_excl_gravity = accelerationX_axis_excl_gravity;
	}

	public float getAccelerationY_axis_excl_gravity() {
		return accelerationY_axis_excl_gravity;
	}

	public void setAccelerationY_axis_excl_gravity(
			float accelerationY_axis_excl_gravity) {
		this.accelerationY_axis_excl_gravity = accelerationY_axis_excl_gravity;
	}

	public float getAccelerationZ_axis_excl_gravity() {
		return accelerationZ_axis_excl_gravity;
	}

	public void setAccelerationZ_axis_excl_gravity(
			float accelerationZ_axis_excl_gravity) {
		this.accelerationZ_axis_excl_gravity = accelerationZ_axis_excl_gravity;
	}

	public float getRotationVectorX_axis() {
		return rotationVectorX_axis;
	}

	public void setRotationVectorX_axis(float rotationVectorX_axis) {
		this.rotationVectorX_axis = rotationVectorX_axis;
	}

	public float getRotationVectorY_axis() {
		return rotationVectorY_axis;
	}

	public void setRotationVectorY_axis(float rotationVectorY_axis) {
		this.rotationVectorY_axis = rotationVectorY_axis;
	}

	public float getRotationVectorZ_axis() {
		return rotationVectorZ_axis;
	}

	public void setRotationVectorZ_axis(float rotationVectorZ_axis) {
		this.rotationVectorZ_axis = rotationVectorZ_axis;
	}

	public float getRotationVectorScalar() {
		return rotationVectorScalar;
	}

	public void setRotationVectorScalar(float rotationVectorScalar) {
		this.rotationVectorScalar = rotationVectorScalar;
	}

	public float getGeomagneticFieldX_axis() {
		return geomagneticFieldX_axis;
	}

	public void setGeomagneticFieldX_axis(float geomagneticFieldX_axis) {
		this.geomagneticFieldX_axis = geomagneticFieldX_axis;
	}

	public float getGeomagneticFieldY_axis() {
		return geomagneticFieldY_axis;
	}

	public void setGeomagneticFieldY_axis(float geomagneticFieldY_axis) {
		this.geomagneticFieldY_axis = geomagneticFieldY_axis;
	}

	public float getGeomagneticFieldZ_axis() {
		return geomagneticFieldZ_axis;
	}

	public void setGeomagneticFieldZ_axis(float geomagneticFieldZ_axis) {
		this.geomagneticFieldZ_axis = geomagneticFieldZ_axis;
	}

	public float getAzimuthX_axis() {
		return azimuthX_axis;
	}

	public void setAzimuthX_axis(float azimuthX_axis) {
		this.azimuthX_axis = azimuthX_axis;
	}

	public float getPitchY_axis() {
		return pitchY_axis;
	}

	public void setPitchY_axis(float pitchY_axis) {
		this.pitchY_axis = pitchY_axis;
	}

	public float getRollZ_axis() {
		return rollZ_axis;
	}

	public void setRollZ_axis(float rollZ_axis) {
		this.rollZ_axis = rollZ_axis;
	}

	public float getProximityDistance() {
		return proximityDistance;
	}

	public void setProximityDistance(float proximityDistance) {
		this.proximityDistance = proximityDistance;
	}

	public float getAmbientAirTemperature() {
		return ambientAirTemperature;
	}

	public void setAmbientAirTemperature(float ambientAirTemperature) {
		this.ambientAirTemperature = ambientAirTemperature;
	}

	public float getLight() {
		return light;
	}

	public void setLight(float light) {
		this.light = light;
	}

	public float getAmbientAirPressure() {
		return ambientAirPressure;
	}

	public void setAmbientAirPressure(float ambientAirPressure) {
		this.ambientAirPressure = ambientAirPressure;
	}

	public float getAmbientRelativeHumidity() {
		return ambientRelativeHumidity;
	}

	public void setAmbientRelativeHumidity(float ambientRelativeHumidity) {
		this.ambientRelativeHumidity = ambientRelativeHumidity;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}



}
