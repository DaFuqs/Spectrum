package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.deeper_down.weather.PlainWeather;
import de.dafuqs.spectrum.deeper_down.weather.ShowerWeatherState;
import de.dafuqs.spectrum.deeper_down.weather.WeatherState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class SpectrumWeatherStates {

    //base states
    public static final WeatherState PLAIN_WEATHER = PlainWeather.INSTANCE;

    //rain-likes
    public static final WeatherState SHOWER = new ShowerWeatherState();

    public static void register() {
        register(PLAIN_WEATHER);
        register(SHOWER);
    }

    private static void register(WeatherState state) {
        Registry.register(SpectrumRegistries.WEATHER_STATES, state.getId(), state);
    }

    public static WeatherState fromTag(NbtCompound tag) {
        return SpectrumRegistries.WEATHER_STATES.get(new Identifier(tag.getString("weatherState")));
    }
}
