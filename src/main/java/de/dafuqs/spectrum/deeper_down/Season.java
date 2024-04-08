package de.dafuqs.spectrum.deeper_down;

import de.dafuqs.spectrum.cca.DDWorldEffectsComponent;
import de.dafuqs.spectrum.deeper_down.weather.WeatherState;
import net.minecraft.util.math.MathHelper;

import static de.dafuqs.spectrum.registries.SpectrumWeatherStates.*;

/**
 * Season last 60 ingame days each.
 * <p>
 * Every season is subdivided into three 20 day blocks which determine effect intensities.
 * <p>
 * If this seems a lil bit excessive... yeah - but hey, being fancy is fun.
 */
public enum Season {
    FLOW(1.115F, 1F, 0F, PLAIN_WEATHER, PLAIN_WEATHER, PLAIN_WEATHER),
    MONSOON(1.85F, 1.4F, 1F, PLAIN_WEATHER, PLAIN_WEATHER, PLAIN_WEATHER),
    STEAMY(0.5F, 2F, 1.5F, PLAIN_WEATHER, PLAIN_WEATHER, PLAIN_WEATHER);

    public final float aquiferMod, windMod, tempMod;
    public final WeatherState earlyDefault, midDefault, lateDefault;

    Season(float aquiferMod, float windMod, float tempMod, WeatherState earlyDefault, WeatherState midDefault, WeatherState lateDefault) {
        this.aquiferMod = aquiferMod;
        this.windMod = windMod;
        this.tempMod = tempMod;
        this.earlyDefault = earlyDefault;
        this.midDefault = midDefault;
        this.lateDefault = lateDefault;
    }

    public Period getPeriod(long seasonProgress) {
        return Period.values()[MathHelper.clamp((int) (seasonProgress / DDWorldEffectsComponent.SEASON_PERIOD_INTERVAL), 0, 2)];
    }

    public Season getNextSeason() {
        return Season.values()[(this.ordinal() + 1) % 3];
    }

    public WeatherState getStateFor(Period period) {
        return switch (period) {
            case EARLY -> earlyDefault;
            case MIDDLE -> midDefault;
            case LATE -> lateDefault;
        };
    }

    public enum Period {
        EARLY(0.667F),
        MIDDLE(1.25F),
        LATE(1F);

        public final float effectMod;

        Period(float effectMod) {
            this.effectMod = effectMod;
        }
    }
}
