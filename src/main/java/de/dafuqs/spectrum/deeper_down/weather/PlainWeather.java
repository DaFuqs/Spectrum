package de.dafuqs.spectrum.deeper_down.weather;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.deeper_down.Season;
import de.dafuqs.spectrum.helpers.WeightedPool;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class PlainWeather extends NoParticleState {

    public static final PlainWeather INSTANCE = new PlainWeather();

    private PlainWeather() {
        super(SpectrumCommon.locate("plain_weather"),false, false);
    }

    @Override
    void applyEnvironmentalEffects(Entity target, RegistryKey<Biome> biome) {}

    @Override
    WeightedPool<? extends WeatherState> getTransitionOptions() {
        return null;
    }

    @Override
    boolean canReturnToBaseState(Season season, Season.Period period) {
        return true;
    }

    @Override
    boolean hasAltStates() {
        return false;
    }
}
