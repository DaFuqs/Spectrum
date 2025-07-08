package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.worldgen.structures.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.levelgen.structure.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumStructureTypes {
	
	private static final DeferredRegister<StructureType<?>> REGISTRAR = DeferredRegister.create(Registries.STRUCTURE_TYPE, SpectrumCommon.MOD_ID);
	
	public static StructureType<UndergroundJigsawStructure> UNDERGROUND_JIGSAW = () -> UndergroundJigsawStructure.CODEC;
	
	public static void register(IEventBus bus) {
		REGISTRAR.register("underground_jigsaw", () -> UNDERGROUND_JIGSAW);
		
		REGISTRAR.register(bus);
	}
	
}
