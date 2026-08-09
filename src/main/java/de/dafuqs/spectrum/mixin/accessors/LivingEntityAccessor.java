package de.dafuqs.spectrum.mixin.accessors;


import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import org.jspecify.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
	
	@Invoker
	@Nullable SoundEvent invokeGetDeathSound();
	
}