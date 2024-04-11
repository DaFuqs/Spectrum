package de.dafuqs.spectrum.deeper_down.weather;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.deeper_down.Season;
import de.dafuqs.spectrum.helpers.WeightedPool;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

import java.util.Collections;
import java.util.List;

public class PlainWeather extends NoParticleState {

    public static final PlainWeather INSTANCE = new PlainWeather();

    private PlainWeather() {
        super(SpectrumCommon.locate("plain_weather"));
    }

    @Override
    public float getThirst() {
        return 0F;
    }
}
