package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

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