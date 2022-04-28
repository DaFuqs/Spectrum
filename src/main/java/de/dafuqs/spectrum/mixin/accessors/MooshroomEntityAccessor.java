package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.UUID;

@Mixin(MooshroomEntity.class)
public interface MooshroomEntityAccessor {
	
	@Accessor("stewEffect")
	StatusEffect getStewEffect();
	
	@Accessor("stewEffectDuration")
	int getStewEffectDuration();
	
	@Accessor("stewEffect")
	void setStewEffect(StatusEffect statusEffect);
	
	@Accessor("stewEffectDuration")
	void setStewEffectDuration(int stewEffectDuration);
	
}