package de.dafuqs.spectrum.blocks.spirit_sallow;

import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkRandom;

public class WindStrength {
	
	private static PerlinNoiseSampler sampler;
	
	private static long cachedTick;
	private static World cachedWorld;
	private static double cachedValue;
	
	public static double getStrength(World world, long tick) {
		if(sampler == null) {
			sampler = new PerlinNoiseSampler(new ChunkRandom());
		}
		
		if(tick != cachedTick || world != cachedWorld) {
			cachedValue = sampler.sample(0, 0, 0);
			cachedTick = tick;
			cachedWorld = world;
		}
		
		return cachedValue;
	}
	
}
