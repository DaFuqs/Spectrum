package de.dafuqs.spectrum.deeper_down.weather;

import de.dafuqs.spectrum.helpers.WeightedPool;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public abstract class NoParticleState extends WeatherState {

    public static final WeightedPool<RaindropEntry> NO_PRECIPITATION = WeightedPool.empty();

    public NoParticleState(Identifier id) {
        super(id);
    }

    @Override
    public void spawnCeilingParticle(World world, double x, double y, double z) {}

    @Override
    public void spawnAirParticle(World world, double x, double y, double z) {}

    @Override
    public void spawnGroundParticle(World world, double x, double y, double z) {}

    @Override
    public float getPrecipitationChance(Biome biome) {
        return 0;
    }

    @Override
    public WeightedPool<RaindropEntry> getRaindropDistribution() {
        return NO_PRECIPITATION;
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
