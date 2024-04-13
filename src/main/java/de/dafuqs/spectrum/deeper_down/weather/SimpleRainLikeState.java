package de.dafuqs.spectrum.deeper_down.weather;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public abstract class SimpleRainLikeState extends WeatherState {

    protected final float thirst;
    private final IntProvider precipitation;

    public SimpleRainLikeState(Identifier id, IntProvider precipitation, float thirst) {
        super(id);
        this.thirst = thirst;
        this.precipitation = precipitation;
    }

    @Override
    public float getThirst() {
        return thirst;
    }

    @Override
    public IntProvider getPrecipitation(RegistryKey<Biome> biome) {
        return precipitation;
    }

    @Override
    public void spawnGroundParticle(World world, double x, double y, double z) {}

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
