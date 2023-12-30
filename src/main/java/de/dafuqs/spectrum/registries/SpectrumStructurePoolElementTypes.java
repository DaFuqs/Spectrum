package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.worldgen.*;
import net.minecraft.registry.*;
import net.minecraft.structure.pool.*;

public class SpectrumStructurePoolElementTypes {
	
	/**
	 * Pool element that replaces the jigsaw with a single block
	 * that block supports state tags and block entity nbt
	 */
	public static final StructurePoolElementType<SingleBlockPoolElement> SINGLE_BLOCK_ELEMENT = registerType("single_block_element", SingleBlockPoolElement.CODEC);

	static <P extends StructurePoolElement> StructurePoolElementType<P> registerType(String id, Codec<P> codec) {
		return Registry.register(Registries.STRUCTURE_POOL_ELEMENT, SpectrumCommon.locate(id), () -> codec);
	}
	
	public static void register() {
	
	}
	
}
