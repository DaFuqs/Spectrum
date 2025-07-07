package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.data_loaders.resonance_processors.*;
import net.minecraft.core.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.event.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumResonanceProcessorTypes {
	private static final DeferredRegister<MapCodec<? extends ResonanceProcessor>> DR = DeferredRegister.create(SpectrumRegistryKeys.RESONANCE_PROCESSOR_TYPE, SpectrumCommon.MOD_ID);
	
	public static void register(IEventBus modBus) {
		DR.register("drop_self", () -> DropSelfResonanceProcessor.CODEC);
		DR.register("modify_drops", () -> ModifyDropsResonanceProcessor.CODEC);
		
		DR.register(modBus);
	}
	
}
