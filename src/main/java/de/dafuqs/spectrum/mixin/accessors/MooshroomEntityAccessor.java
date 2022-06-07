package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.passive.MooshroomEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MooshroomEntity.class)
public interface MooshroomEntityAccessor {
	
	@Accessor("stewEffect")
	StatusEffect getStewEffect();
	
	@Accessor("stewEffect")
	void setStewEffect(StatusEffect statusEffect);
	
	@Accessor("stewEffectDuration")
	int getStewEffectDuration();
	
	@Accessor("stewEffectDuration")
	void setStewEffectDuration(int stewEffectDuration);
	
}