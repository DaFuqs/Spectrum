package de.dafuqs.spectrum.deeper_down.weather;

import de.dafuqs.spectrum.deeper_down.Season;
import de.dafuqs.spectrum.helpers.WeightedPool;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public abstract class WeatherState {

    private final Identifier id;
    protected final boolean airParticles, groundParticles, affectsMobs, affectsBlocks;

    public WeatherState(Identifier id, boolean airParticles, boolean groundParticles, boolean affectsMobs, boolean affectsBlocks) {
        this.id = id;
        this.airParticles = airParticles;
        this.groundParticles = groundParticles;
        this.affectsMobs = affectsMobs;
        this.affectsBlocks = affectsBlocks;
    }

    abstract void spawnCeilingParticle(double x, double y, double z);

    abstract void spawnAirParticle(double x, double y, double z);

    abstract void applyEnvironmentalEffects(Entity target, RegistryKey<Biome> biome);

    abstract WeightedPool<RaindropEntry> getRaindropDistribution();

    abstract WeightedPool<? extends WeatherState> getTransitionOptions();

    abstract boolean hasAltStates();

    abstract int getPrecipitation(RegistryKey<Biome> biome);

    public WeatherState getAltState(RegistryKey<Biome> biome) {
        return this;
    }

    abstract boolean canReturnToBaseState(Season season, Season.Period period);

    public float rippleChance(RegistryKey<Biome> biome) {
        return 0.125F;
    }

    public float getWindIntensityModifier() {
        return 1F;
    }

    public boolean hasAirParticles() {
        return airParticles;
    }

    public boolean hasGroundParticles() {
        return groundParticles;
    }

    public boolean affectsBlocks() {
        return affectsBlocks;
    }

    public boolean affectsMobs() {
        return affectsMobs;
    }

    public Identifier getId() {
        return id;
    }

    public void save(NbtCompound tag) {
        tag.putString("weatherState", id.toString());
    }
}
