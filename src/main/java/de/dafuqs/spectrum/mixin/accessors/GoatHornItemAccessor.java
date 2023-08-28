package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.item.*;
import net.minecraft.util.registry.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;

@Mixin(GoatHornItem.class)
public interface GoatHornItemAccessor {
	
	@Invoker("getInstrument")
	Optional<RegistryEntry<Instrument>> invokeGetInstrument(ItemStack stack);
	
}