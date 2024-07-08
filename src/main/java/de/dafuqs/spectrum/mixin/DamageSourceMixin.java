package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.api.damage_type.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;

import java.util.*;

@Mixin(DamageSource.class)
public class DamageSourceMixin implements StackTracking {
	
	@Unique
	private Optional<ItemStack> trackedStack = Optional.empty();
	
	@Override
	public Optional<ItemStack> spectrum$getTrackedStack() {
		return trackedStack;
	}
	
	@Override
	public void spectrum$setTrackedStack(@Nullable ItemStack stack) {
		trackedStack = Optional.ofNullable(stack);
	}
}
