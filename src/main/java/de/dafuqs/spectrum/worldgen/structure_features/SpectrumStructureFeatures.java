package de.dafuqs.spectrum.worldgen.structure_features;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.mixin.accessors.StructureFeatureAccessor;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.StructureFeature;

public class SpectrumStructureFeatures {
	
	public static StructureFeature<?> UNDERGROUND_STRUCTURES = new SpectrumUndergroundStructures();
	
	public static void register() {
		StructureFeatureAccessor.callRegister(SpectrumCommon.MOD_ID + ":underground_structures", UNDERGROUND_STRUCTURES, GenerationStep.Feature.UNDERGROUND_STRUCTURES);
	}
	
}
