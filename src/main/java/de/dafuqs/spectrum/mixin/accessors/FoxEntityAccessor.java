package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.entity.passive.FoxEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.UUID;

@Mixin(FoxEntity.class)
public interface FoxEntityAccessor {
	
	@Invoker("addTrustedUuid")
	public void invokeAddTrustedUuid(@Nullable UUID uuid);
	
}