package de.dafuqs.spectrum.blocks.spirit_sallow;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.world.gen.ChunkRandom;

public class WindStrength {
	
	private static SimplexNoiseSampler samplerX;
	private static SimplexNoiseSampler samplerY;
	private static SimplexNoiseSampler samplerZ;
	
	private static long cachedTick;
	
	public Vec3d cachedValue;
	
	public Vec3d getWindStrength(long tick) {
		if(samplerX == null) {
			samplerX = new SimplexNoiseSampler(new ChunkRandom());
			samplerY = new SimplexNoiseSampler(new ChunkRandom());
			samplerZ = new SimplexNoiseSampler(new ChunkRandom());
		}
		
		if(tick != cachedTick) {
			cachedValue = new Vec3d(
					samplerX.sample(0, 0, (tick + MinecraftClient.getInstance().getTickDelta()) / 512D),
					samplerY.sample(0, 0, (tick + MinecraftClient.getInstance().getTickDelta()) / 512D),
					samplerZ.sample(0, 0, (tick + MinecraftClient.getInstance().getTickDelta()) / 512D)
			);
			
			cachedTick = tick;
		}
		
		return cachedValue;
	}
	
}
