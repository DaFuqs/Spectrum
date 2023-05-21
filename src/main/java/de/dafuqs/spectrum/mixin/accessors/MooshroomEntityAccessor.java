package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.entity.effect.*;
import net.minecraft.entity.passive.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

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