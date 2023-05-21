package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.entity.mob.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(ShulkerEntity.class)
public interface ShulkerEntityAccessor {
	
	@Invoker("setColor")
	void invokeSetColor(DyeColor dyeColor);
	
}