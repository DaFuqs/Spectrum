package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.world.biome.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(Biome.class)
public interface BiomeAccessor {
	
	@Accessor(value = "weather")
	Biome.Weather getWeather();
	
}