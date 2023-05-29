package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.structures.*;
import net.minecraft.registry.*;
import net.minecraft.world.gen.structure.*;

public class SpectrumStructureTypes {
	
	public static StructureType<BlockTargetJigsawStructure> BLOCK_PREDICATE_JIGSAW;
	
	public static void register() {
		BLOCK_PREDICATE_JIGSAW = register("block_target_jigsaw", BlockTargetJigsawStructure.CODEC);
	}
	
	private static <S extends Structure> StructureType<S> register(String id, Codec<S> codec) {
		return Registry.register(Registries.STRUCTURE_TYPE, SpectrumCommon.locate(id), () -> codec);
	}
	
}
