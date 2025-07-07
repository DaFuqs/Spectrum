package de.dafuqs.spectrum.mixin.accessors;

import net.minecraft.core.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;

@Mixin(InstrumentItem.class)
public interface GoatHornItemAccessor {
	
	@Invoker
	Optional<Holder<Instrument>> invokeGetInstrument(ItemStack stack);
	
}