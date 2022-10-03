package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.UUID;

@Mixin(ShulkerEntity.class)
public interface ShulkerEntityAccessor {
	
	@Invoker("setColor")
	void invokeSetColor(DyeColor dyeColor);
	
}