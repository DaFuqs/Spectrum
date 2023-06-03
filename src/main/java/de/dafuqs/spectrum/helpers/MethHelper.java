package de.dafuqs.spectrum.helpers;

import net.minecraft.util.math.Vec3d;

public class MethHelper {

    public static Orientation getVectorOrientation(Vec3d vector) {
        var r = vector.length();
        var orientationX = Math.acos(vector.z / Math.sqrt(vector.z * vector.z + vector.x * vector.x)) * (vector.x < 0 ? -1 : 1);
        var orientationY = Math.acos(vector.y / r);

        return Orientation.create(orientationX, orientationY);
    }
}
