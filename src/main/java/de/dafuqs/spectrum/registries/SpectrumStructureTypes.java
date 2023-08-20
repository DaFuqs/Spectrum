package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.structures.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.structure.*;

public class SpectrumStructureTypes {
	
	public static StructureType<UndergroundJigsawStructure> UNDERGROUND_JIGSAW;
	
	public static void register() {
		UNDERGROUND_JIGSAW = register("underground_jigsaw", UndergroundJigsawStructure.CODEC);
	}
	
	private static <S extends Structure> StructureType<S> register(String id, Codec<S> codec) {
		return Registry.register(Registry.STRUCTURE_TYPE, SpectrumCommon.locate(id), () -> codec);
	}
	
}
