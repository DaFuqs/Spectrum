package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.core.particles.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.monster.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(Slime.class)
public interface SlimeEntityAccessor {
	
	@Invoker
	void invokeSetSize(int newSize, boolean heal);
	
	@Invoker
	ParticleOptions invokeGetParticleType();
	
	@Invoker
	SoundEvent invokeGetSquishSound();
	
	@Invoker
	float invokeGetSoundVolume();
	
}