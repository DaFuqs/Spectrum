package de.dafuqs.spectrum.deeper_down.weather;

import de.dafuqs.spectrum.helpers.WeightedPool;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.joml.Vector3d;

import java.util.Optional;

public abstract class NoParticleState extends WeatherState {

    public static final WeightedPool<RaindropEntry> NO_PRECIPITATION = WeightedPool.empty();

    public NoParticleState(Identifier id) {
        super(id);
    }

    @Override
    public void spawnCeilingParticle(ClientWorld world, Vector3d pos, Random random) {}

    @Override
    public void spawnAirParticle(ClientWorld world, Vector3d pos, Random random) {}

    @Override
    public void spawnGroundParticle(ClientWorld world, Vector3d pos, Random random) {}

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
