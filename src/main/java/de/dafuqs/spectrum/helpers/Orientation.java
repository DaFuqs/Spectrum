package de.dafuqs.spectrum.helpers;

import net.minecraft.util.*;
import net.minecraft.world.phys.*;

/**
 * NOTE: Longitude is on the XZ plane. Latitude is on the radius-Y plane.
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
	
	public Vec3 toVector(double r) {
		return new Vec3(
				r * Math.sin(latitude) * Math.cos(longitude),
				r * Math.cos(latitude),
				r * Math.sin(latitude) * Math.sin(longitude)
		);
	}
	
	public Orientation add(Orientation other) {
		return new Orientation(longitude + other.longitude, latitude + other.latitude);
	}

	public Orientation add(double longitude, double latitude) {
		return new Orientation(this.longitude + longitude, this.latitude + latitude);
	}
	
	public Orientation subtract(Orientation other) {
		return new Orientation(longitude - other.longitude, latitude - other.latitude);
	}

	/**
	 * <a href="http://www.vias.org/comp_geometry/math_coord_sphere.htm">Phi my phucking phnuts</a>
	 */
	public static Orientation getVectorOrientation(Vec3 vector) {
		var r = vector.length();
		var longitude = Mth.atan2(vector.z, vector.x);
		var latitude = Math.acos(vector.y / r) - (Math.PI / 2);
		latitude *= -1;
		
		return Orientation.create(longitude, latitude);
	}
	
	@Override
	public String toString() {
		return "{ Longitude: " + longitude + " rads | Latitude: " + latitude + " rads }";
	}
	
}
