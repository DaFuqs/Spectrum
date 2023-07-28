package de.dafuqs.spectrum.helpers;

import net.minecraft.util.math.*;

/**
 * NOTE: Relative to Z axis, Longitude is the azimuth
 */
public class Orientation {
	
	private final double longitude, latitude;
	
	private Orientation(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public static Orientation create(double longitude, double latitude) {
		return new Orientation(longitude, latitude);
	}
	
	public static Orientation fromVector(Vec3d vector) {
		return getVectorOrientation(vector);
	}
	
	public Orientation add(Orientation other) {
		return new Orientation(longitude + other.longitude, latitude + other.latitude);
	}
	
	public Orientation subtract(Orientation other) {
		return new Orientation(longitude - other.longitude, latitude - other.latitude);
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public static Orientation getVectorOrientation(Vec3d vector) {
		var r = vector.length();
		var orientationX = Math.acos(vector.z / Math.sqrt(vector.z * vector.z + vector.x * vector.x)) * (vector.x < 0 ? -1 : 1);
		var orientationY = Math.acos(vector.y / r);
		
		return Orientation.create(orientationX, orientationY);
	}
	
	@Override
	public String toString() {
		return "{ Lat: " + latitude + " rads | Long: " + longitude + " rads }";
	}
	
}
