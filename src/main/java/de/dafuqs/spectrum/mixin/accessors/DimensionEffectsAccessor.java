package de.dafuqs.spectrum.mixin.accessors;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(DimensionSpecialEffects.class)
public interface DimensionEffectsAccessor {
	
	@Accessor
	static Object2ObjectMap<ResourceLocation, DimensionSpecialEffects> getEFFECTS() {
		throw new AssertionError();
	}
	
}