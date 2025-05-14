package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.core.*;
import net.minecraft.resources.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(RegistryOps.HolderLookupAdapter.class)
public interface CachedRegistryInfoGetterAccessor {
	
	@Accessor
	HolderLookup.Provider getLookupProvider();
	
}
