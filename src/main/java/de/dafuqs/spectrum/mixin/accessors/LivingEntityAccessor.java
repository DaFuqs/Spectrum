package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
	@Accessor()
	Map<StatusEffect, StatusEffectInstance> getActiveStatusEffects();
}