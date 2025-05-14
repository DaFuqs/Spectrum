package de.dafuqs.spectrum.mixin.client.accessors;

import net.minecraft.client.particle.*;
import net.minecraft.resources.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;

@Mixin(ParticleEngine.class)
public interface ParticleManagerAccessor {
	
	@Accessor
	Map<ResourceLocation, SpriteSet> getSpriteSets();
	
}
