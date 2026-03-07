package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.*;

import java.util.function.*;

@Mixin(SimpleCriterionTrigger.class)
public interface SimpleCriterionAccessor {
	
	@Invoker("trigger")
	void spectrum$invokeTrigger(ServerPlayer player, Predicate predicate);
	
}
