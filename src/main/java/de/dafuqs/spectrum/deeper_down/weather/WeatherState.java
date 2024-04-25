package de.dafuqs.spectrum.deeper_down.weather;

import de.dafuqs.spectrum.deeper_down.Season;
import de.dafuqs.spectrum.helpers.WeightedPool;
import de.dafuqs.spectrum.registries.SpectrumRegistries;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.joml.Vector3d;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public abstract class WeatherState {

    private final Identifier id;
    private String translationKey;

    public WeatherState(Identifier id) {
        this.id = id;
    }

    public abstract void spawnCeilingParticle(ClientWorld world, Vector3d position, Random random);

    public abstract void spawnAirParticle(ClientWorld world, Vector3d position, Random random);

    public abstract void spawnGroundParticle(ClientWorld world, Vector3d position, Random random);

    public abstract Optional<ParticleType<?>> getCeilingDropletType();

    public abstract float getPrecipitationChance(Biome biome, RegistryKey<Biome> key);

    public abstract int getPrecipitationQuantity();

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
