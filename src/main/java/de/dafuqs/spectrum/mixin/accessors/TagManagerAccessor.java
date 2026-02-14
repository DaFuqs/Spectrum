package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.core.*;
import net.minecraft.tags.*;
import net.neoforged.neoforge.common.conditions.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(TagManager.class)
public interface TagManagerAccessor {
	
	@Accessor
	RegistryAccess getRegistryAccess();
	
}