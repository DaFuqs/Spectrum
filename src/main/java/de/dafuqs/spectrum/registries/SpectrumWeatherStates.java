package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.deeper_down.weather.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;

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
