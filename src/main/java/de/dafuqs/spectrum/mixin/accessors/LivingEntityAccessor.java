package de.dafuqs.spectrum.mixin.accessors;


import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import org.jspecify.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
	
	@Invoker
	@Nullable SoundEvent invokeGetDeathSound();
	
}