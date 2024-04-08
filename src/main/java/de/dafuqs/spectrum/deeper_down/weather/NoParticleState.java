package de.dafuqs.spectrum.deeper_down.weather;

import de.dafuqs.spectrum.helpers.WeightedPool;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public abstract class NoParticleState extends WeatherState {

    public static final WeightedPool<RaindropEntry> NO_PRECIPITATION = WeightedPool.empty();

    public NoParticleState(Identifier id, boolean affectsMobs, boolean affectsBlocks) {
        super(id, false, false, affectsMobs, affectsBlocks);
    }

    @Override
    void spawnCeilingParticle(double x, double y, double z) {}

    @Override
    void spawnAirParticle(double x, double y, double z) {}

    @Override
    int getPrecipitation(RegistryKey<Biome> biome) {
        return 0;
    }

    @Override
    WeightedPool<RaindropEntry> getRaindropDistribution() {
        return NO_PRECIPITATION;
    }
}
