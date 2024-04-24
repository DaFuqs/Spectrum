package de.dafuqs.spectrum.deeper_down.weather;

import de.dafuqs.spectrum.deeper_down.Season;
import de.dafuqs.spectrum.helpers.WeightedPool;
import de.dafuqs.spectrum.registries.SpectrumRegistries;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Collections;
import java.util.Map;

public abstract class WeatherState {

    private final Map<RegistryKey<Biome>, WeatherState> BLANK = Collections.emptyMap();
    private final Identifier id;
    private String translationKey;

    public WeatherState(Identifier id) {
        this.id = id;
    }

    public abstract void spawnCeilingParticle(World world, double x, double y, double z);

    public abstract void spawnAirParticle(World world, double x, double y, double z);

    public abstract void spawnGroundParticle(World world, double x, double y, double z);

    public abstract WeightedPool<RaindropEntry> getRaindropDistribution();

    public abstract float getPrecipitationChance(Biome biome);

    public Map<RegistryKey<Biome>, WeatherState> getAltStates() {
        return BLANK;
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

    public String getOrCreateTranslationKey() {
        if (this.translationKey == null) {
            this.translationKey = Util.createTranslationKey("weather", SpectrumRegistries.WEATHER_STATES.getId(this));
        }

        return this.translationKey;
    }

    public Text getName() {
        return Text.translatable(getOrCreateTranslationKey());
    }

    public Identifier getId() {
        return id;
    }

    public void save(NbtCompound tag) {
        tag.putString("weatherState", id.toString());
    }
}
