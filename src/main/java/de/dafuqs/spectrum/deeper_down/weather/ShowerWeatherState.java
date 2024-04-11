package de.dafuqs.spectrum.deeper_down.weather;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.WeightedPool;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class ShowerWeatherState extends SimpleRainLikeState {

    private final WeightedPool<RaindropEntry> DISTRIBUTION;

    public ShowerWeatherState() {
        super(SpectrumCommon.locate("shower"), UniformIntProvider.create(1, 3), 0.25F);

        DISTRIBUTION = WeightedPool.of(
                new RaindropEntry(RaindropType.DISCARD, 1),
                new RaindropEntry(RaindropType.SHORT, 3)
        );
    }

    @Override
    public void spawnCeilingParticle(World world, double x, double y, double z) {}

    @Override
    public void spawnAirParticle(World world, double x, double y, double z) {}

    @Override
    public WeightedPool<RaindropEntry> getRaindropDistribution() {
        return DISTRIBUTION;
    }
}
