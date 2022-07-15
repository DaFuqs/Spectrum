package de.dafuqs.spectrum.worldgen.structure_features;

import com.mojang.serialization.Codec;
import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class SpectrumStructureFeatures {

	public static StructureType<?> UNDERGROUND_STRUCTURES;

	public static void register() {
		UNDERGROUND_STRUCTURES = register(new Identifier(SpectrumCommon.MOD_ID, "underground_structures"), SpectrumUndergroundStructures.CODEC);
		//StructureFeatureAccessor.callRegister(SpectrumCommon.MOD_ID + ":underground_structures", UNDERGROUND_STRUCTURES, GenerationStep.Feature.UNDERGROUND_STRUCTURES);
	}

	private static <S extends Structure> StructureType<S> register(Identifier id, Codec<S> codec) {
		return Registry.register(Registry.STRUCTURE_TYPE, id, () -> codec);
	}

}
