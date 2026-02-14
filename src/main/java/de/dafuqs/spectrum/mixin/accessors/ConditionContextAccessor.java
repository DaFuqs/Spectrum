package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.tags.*;
import net.neoforged.neoforge.common.conditions.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(ConditionContext.class)
public interface ConditionContextAccessor {
	
	@Accessor
	TagManager getTagManager();
	
}