package de.dafuqs.spectrum.mixin.accessors;


import net.minecraft.world.entity.animal.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;

@Mixin(Fox.class)
public interface FoxEntityAccessor {
	
	@Invoker
	void invokeAddTrustedUuid(@Nullable UUID uuid);
	
}