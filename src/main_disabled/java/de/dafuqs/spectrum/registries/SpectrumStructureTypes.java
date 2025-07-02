package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.worldgen.structures.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.levelgen.structure.*;

public class SpectrumStructureTypes {
	
	public static StructureType<UndergroundJigsawStructure> UNDERGROUND_JIGSAW;
	
	public static void register() {
		UNDERGROUND_JIGSAW = register("underground_jigsaw", UndergroundJigsawStructure.CODEC);
	}
	
	private static <S extends Structure> StructureType<S> register(String id, MapCodec<S> codec) {
		return Registry.register(BuiltInRegistries.STRUCTURE_TYPE, SpectrumCommon.locate(id), () -> codec);
	}
	
}
