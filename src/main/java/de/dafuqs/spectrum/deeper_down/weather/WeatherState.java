package de.dafuqs.spectrum.deeper_down.weather;

import de.dafuqs.spectrum.deeper_down.Season;
import de.dafuqs.spectrum.helpers.WeightedPool;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public abstract class WeatherState {

    private final Identifier id;

    public WeatherState(Identifier id) {
        this.id = id;
    }

    public abstract void spawnCeilingParticle(World world, double x, double y, double z);

    public abstract void spawnAirParticle(World world, double x, double y, double z);

    public abstract void spawnGroundParticle(World world, double x, double y, double z);

    public abstract WeightedPool<RaindropEntry> getRaindropDistribution();

    public abstract IntProvider getPrecipitation(RegistryKey<Biome> biome);

    public WeatherState getStateFor(RegistryKey<Biome> biome) {
        return this;
    }

    public abstract float getThirst();

    public float rippleChance(RegistryKey<Biome> biome) {
        return 0.125F;
    }

    public float getWindIntensityModifier() {
        return 1F;
    }

    public abstract boolean hasCeilingParticles();

    public abstract boolean hasAirParticles();

    public abstract boolean hasGroundParticles();

    public Identifier getId() {
        return id;
    }

    public void save(NbtCompound tag) {
        tag.putString("weatherState", id.toString());
    }
}
