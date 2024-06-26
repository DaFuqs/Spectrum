package de.dafuqs.spectrum.deeper_down.weather;

import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.world.*;
import net.minecraft.particle.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.*;
import org.joml.*;

import java.util.*;

public abstract class NoParticleState extends WeatherState {
	
	public static final WeightedPool<RaindropEntry> NO_PRECIPITATION = WeightedPool.empty();
	
	public NoParticleState(Identifier id) {
		super(id);
	}
	
	@Override
	public void spawnCeilingParticle(ClientWorld world, Vector3d pos, Random random) {
	}
	
	@Override
	public void spawnAirParticle(ClientWorld world, Vector3d pos, Random random) {
	}
	
	@Override
	public void spawnGroundParticle(ClientWorld world, Vector3d pos, Random random) {
	}
	
	@Override
	public float getPrecipitationChance(Biome biome, RegistryKey<Biome> key) {
		return 0;
	}
	
	@Override
	public int getPrecipitationQuantity() {
		return 0;
	}
	
	@Override
	public Optional<ParticleType<?>> getCeilingDropletType() {
		return Optional.empty();
	}
	
	@Override
	public boolean hasAirParticles() {
		return false;
	}
	
	@Override
	public boolean hasCeilingParticles() {
		return false;
	}
	
	@Override
	public boolean hasGroundParticles() {
		return false;
	}
}
