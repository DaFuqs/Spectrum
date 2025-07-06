package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.resources.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(RegistryOps.class)
public interface RegistryOpsAccessor {
	
	@Accessor
	RegistryOps.RegistryInfoLookup getLookupProvider();
	
}
