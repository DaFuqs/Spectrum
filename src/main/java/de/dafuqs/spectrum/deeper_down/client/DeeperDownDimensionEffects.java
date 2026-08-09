package de.dafuqs.spectrum.deeper_down.client;

import net.minecraft.client.renderer.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.*;


public class DeeperDownDimensionEffects extends DimensionSpecialEffects {
	
	public DeeperDownDimensionEffects() {
		super(Float.NaN, false, DimensionSpecialEffects.SkyType.NONE, false, true);
	}
	
	@Override
	public @Nullable float[] getSunriseColor(float skyAngle, float tickDelta) {
		return null;
	}
	
	@Override
	public Vec3 getBrightnessDependentFogColor(Vec3 color, float sunHeight) {
		return color;
	}
	
	@Override
	public boolean isFoggyAt(int camX, int camY) {
		return true;
	}
	
}