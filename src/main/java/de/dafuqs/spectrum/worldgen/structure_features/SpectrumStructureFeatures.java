package de.dafuqs.spectrum.worldgen.structure_features;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.structure.StructureType;

public class SpectrumStructureFeatures {
	
	public static StructureType<SpectrumUndergroundStructures> UNDERGROUND_STRUCTURES;
	
	public static void register() {
		UNDERGROUND_STRUCTURES = Registry.register(Registry.STRUCTURE_TYPE, SpectrumCommon.locate("underground_structures"), () -> SpectrumUndergroundStructures.CODEC);
	}
	
}
