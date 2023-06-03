package de.dafuqs.spectrum.helpers;

import net.minecraft.util.math.Vec3d;

/**
 * NOTE: Relative to Z axis, Longitude is the azimuth
 */
public class Orientation {

    private final double longitude, lattitude;

    private Orientation (double longitude, double lattitude) {
        this.longitude = longitude;
        this.lattitude = lattitude;
    }

    public static Orientation create(double longitude, double lattitude) {
        return new Orientation(longitude, lattitude);
    }

    public static Orientation fromVector(Vec3d vector) {
        return MethHelper.getVectorOrientation(vector);
    }

    public Orientation add(Orientation other) {
        return new Orientation(longitude + other.longitude, lattitude + other.lattitude);
    }

    public Orientation sub(Orientation other) {
        return new Orientation(longitude - other.longitude, lattitude - other.lattitude);
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLattitude() {
        return lattitude;
    }

    public float getLongitudeF() {
        return (float) longitude;
    }

    public float getLattitudeF() {
        return (float) lattitude;
    }

    @Override
    public String toString() {
        return "{ Lat: " + lattitude + " rads | Long: " + longitude + " rads }";
    }
}
