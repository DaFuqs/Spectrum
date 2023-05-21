package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.entity.passive.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;

@Mixin(FoxEntity.class)
public interface FoxEntityAccessor {
	
	@Invoker("addTrustedUuid")
	void invokeAddTrustedUuid(@Nullable UUID uuid);
	
}