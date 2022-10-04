package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ShulkerEntity.class)
public interface ShulkerEntityAccessor {
	
	@Invoker("setColor")
	void invokeSetColor(DyeColor dyeColor);
	
}