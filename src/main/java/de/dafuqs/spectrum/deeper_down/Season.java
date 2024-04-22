package de.dafuqs.spectrum.deeper_down;

import de.dafuqs.spectrum.cca.DDWorldEffectsComponent;
import de.dafuqs.spectrum.deeper_down.weather.WeatherState;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.Locale;

import static de.dafuqs.spectrum.registries.SpectrumWeatherStates.*;

/**
 * Seasons last 13 real life days each.
 * <p>
 * Every season is subdivided into three blocks which determine effect intensities.
 * <p>
 * If this seems a lil bit excessive... yeah - but hey, being fancy is fun.
 */
public enum Season {
    FLOW(1.115F, 1F, 0F, PLAIN_WEATHER, PLAIN_WEATHER, PLAIN_WEATHER),
    MONSOON(1.85F, 1.4F, 1F, PLAIN_WEATHER, PLAIN_WEATHER, PLAIN_WEATHER),
    STEAMY(0.5F, 2F, 1.5F, PLAIN_WEATHER, PLAIN_WEATHER, PLAIN_WEATHER);

    public final float aquiferMod, windMod, tempMod;
    public final WeatherState earlyDefault, midDefault, lateDefault;
    public final String translationKey;

    Season(float aquiferMod, float windMod, float tempMod, WeatherState earlyDefault, WeatherState midDefault, WeatherState lateDefault) {
        this.aquiferMod = aquiferMod;
        this.windMod = windMod;
        this.tempMod = tempMod;
        this.earlyDefault = earlyDefault;
        this.midDefault = midDefault;
        this.lateDefault = lateDefault;
        this.translationKey = "season.spectrum." + name().toLowerCase();
    }

    public Text getName() {
        return Text.translatable(translationKey);
    }

    public static Period getPeriodByDate(int date) {
        return Period.values()[date];
    }

    public static Season getSeasonByDate(int date) {
        return Season.values()[date];
    }

    public WeatherState getStateFor(Period period) {
        return switch (period) {
            case EARLY -> earlyDefault;
            case MIDDLE -> midDefault;
            case LATE -> lateDefault;
        };
    }

    public enum Period {
        EARLY(0.667F, DDWorldEffectsComponent.DAY_LENGTH / 3),
        MIDDLE(1.25F, DDWorldEffectsComponent.DAY_LENGTH * 2),
        LATE(1F, DDWorldEffectsComponent.DAY_LENGTH);

        public final float effectMod;
        public final long minDefaultWeatherDuration;
        public final String translationKey;

        Period(float effectMod, long minDefaultWeatherDuration) {
            this.effectMod = effectMod;
            this.minDefaultWeatherDuration = minDefaultWeatherDuration;
            translationKey = "season.spectrum.period." + name().toLowerCase();
        }

        public Text getName() {
            return Text.translatable(translationKey);
        }
    }
}
