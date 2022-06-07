package de.dafuqs.spectrum.render;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.dimension.DimensionType;

public class SkyLerper {
	
	public static final int ANIMATION_TICKS = 40;
	
	private DimensionType activeDimensionType = null;
	private long sourceDayTime = -1; // -1 when not active
	private float sourceTimeDelta;
	private long targetDayTime;
	private int elapsedTicks;
	private long lastWorldTime;
	
	public static float easeQuart(float num) {
		if (num < 0.5) {
			return 4 * num * num * num;
		} else {
			return (float) (1 - Math.pow(-2 * num + 2, 3) / 2);
		}
	}
	
	public boolean isActive(DimensionType dimensionType) {
		return this.sourceDayTime != -1 && dimensionType == activeDimensionType;
	}
	
	public void trigger(DimensionType dimension, long sourceDayTime, float tickDelta, long targetDayTime) {
		if (this.sourceDayTime == -1) {
			this.sourceDayTime = sourceDayTime;
		} else {
			// keep the last time
			this.sourceDayTime = getLerp(tickDelta);
		}
		this.elapsedTicks = 0;
		this.activeDimensionType = dimension;
		this.sourceTimeDelta = tickDelta;
		this.targetDayTime = targetDayTime;
	}
	
	public long tickLerp(long worldTime, float tickDelta) {
		if (this.lastWorldTime != worldTime) {
			this.lastWorldTime = worldTime;
			this.elapsedTicks++;
			
			if (this.elapsedTicks > ANIMATION_TICKS) {
				this.activeDimensionType = null;
				this.sourceDayTime = -1;
			}
		}
		
		return getLerp(tickDelta);
	}
	
	private long getLerp(float tickDelta) {
		float delta = (this.elapsedTicks + tickDelta) / (float) ANIMATION_TICKS;
		return (long) MathHelper.lerp(easeQuart(delta), this.sourceDayTime + this.sourceTimeDelta, this.targetDayTime + tickDelta);
	}
	
}
