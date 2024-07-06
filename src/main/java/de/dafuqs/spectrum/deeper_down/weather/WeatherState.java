package de.dafuqs.spectrum.deeper_down.weather;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.world.*;
import net.minecraft.nbt.*;
import net.minecraft.particle.*;
import net.minecraft.registry.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.*;
import org.joml.*;

import java.util.*;

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
