package de.dafuqs.spectrum.blocks.spirit_sallow;

import net.minecraft.client.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.noise.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

public class WindStrength {
	
	private static final SimplexNoiseSampler SAMPLER = new SimplexNoiseSampler(new CheckedRandom(0));
	
	private static long cachedTick;
	
	public Vec3d cachedValue;
	
	public Vec3d getWindStrength(World world) {
		long tick = world.getTime();
		if (tick != cachedTick) {
			float tickDelta = MinecraftClient.getInstance().getTickDelta();
			cachedValue = new Vec3d(
					SAMPLER.sample((tick + tickDelta) / 512D, 0, 0),
					SAMPLER.sample(0, (tick + tickDelta) / 512D, 0),
					SAMPLER.sample(0, 0, (tick + tickDelta) / 512D)
			);
			cachedTick = tick;
		}
		
		return cachedValue;
	}
	
}
