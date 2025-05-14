package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.world.level.biome.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(Biome.class)
public interface BiomeAccessor {
	
	@Accessor
	Biome.ClimateSettings getClimateSettings();
	
}