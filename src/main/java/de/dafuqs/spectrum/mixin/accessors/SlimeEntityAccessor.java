package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.entity.mob.*;
import net.minecraft.particle.*;
import net.minecraft.sound.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(SlimeEntity.class)
public interface SlimeEntityAccessor {
	
	@Invoker("setSize")
	void invokeSetSize(int newSize, boolean heal);
	
	@Invoker("getParticles")
	ParticleEffect invokeGetParticles();
	
	@Invoker("getSquishSound")
	SoundEvent invokeGetSquishSound();
	
	@Invoker("getSoundVolume")
	float invokeGetSoundVolume();
	
}