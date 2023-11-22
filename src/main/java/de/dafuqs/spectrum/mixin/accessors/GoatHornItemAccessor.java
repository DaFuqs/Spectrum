package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.item.*;
import net.minecraft.registry.entry.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;

@Mixin(GoatHornItem.class)
public interface GoatHornItemAccessor {
	
	@Invoker("getInstrument")
	Optional<RegistryEntry<Instrument>> invokeGetInstrument(ItemStack stack);
	
}