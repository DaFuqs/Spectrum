package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.structures.*;
import net.minecraft.structure.pool.*;
import net.minecraft.util.registry.*;

public class SpectrumStructurePoolElementTypes {
	
	/**
	 * Pool element that replaces the jigsaw with a single block
	 * that block supports state tags and block entity nbt
	 */
	public static final StructurePoolElementType<SingleBlockPoolElement> SINGLE_BLOCK_ELEMENT = registerType("single_block_element", SingleBlockPoolElement.CODEC);
	public static final StructurePoolElementType<?> DEPTH_AWARE_SINGLE_POOL_ELEMENT = registerType("depth_aware_single_pool_element", DepthAwareSinglePoolElement.CODEC);
	
	static <P extends StructurePoolElement> StructurePoolElementType<P> registerType(String id, Codec<P> codec) {
		return Registry.register(Registry.STRUCTURE_POOL_ELEMENT, SpectrumCommon.locate(id), () -> codec);
	}
	
	public static void register() {
	
	}
	
}
