package de.dafuqs.spectrum.deeper_down.weather;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.world.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.*;
import org.joml.*;

public abstract class SimpleRainLikeState extends WeatherState {
	
	protected final float thirst, precipitation;
	private final int quantity;
	
	public SimpleRainLikeState(Identifier id, float precipitation, int quantity, float thirst) {
		super(id);
		this.thirst = thirst;
		this.precipitation = precipitation;
		this.quantity = quantity;
	}
	
	@Override
	public float getThirst() {
		return thirst;
	}
	
	@Override
	public float getPrecipitationChance(Biome biome, RegistryKey<Biome> key) {
		if (key.equals(SpectrumBiomes.HOWLING_SPIRES))
			return 0F;
		return precipitation;
	}
	
	@Override
	public int getPrecipitationQuantity() {
		return quantity;
	}
	
	@Override
	public void spawnGroundParticle(ClientWorld world, Vector3d position, Random random) {
	}
	
	@Override
	public boolean hasCeilingParticles() {
		return true;
	}
	
	@Override
	public boolean hasAirParticles() {
		return true;
	}
	
	@Override
	public boolean hasGroundParticles() {
		return false;
	}
}
