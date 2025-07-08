package de.dafuqs.spectrum.registries;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.worldgen.structure_pool_elements.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.levelgen.structure.pools.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumStructurePoolElementTypes {
	
	private static final DeferredRegister<StructurePoolElementType<?>> REGISTRAR = DeferredRegister.create(Registries.STRUCTURE_POOL_ELEMENT, SpectrumCommon.MOD_ID);
	
	/**
	 * WeightedPool element that replaces the jigsaw with a single block
	 * that block supports state tags and block entity nbt
	 */
	public static final StructurePoolElementType<SingleBlockPoolElement> SINGLE_BLOCK_ELEMENT = registerType("single_block_element", SingleBlockPoolElement.CODEC);

	static <P extends StructurePoolElement> StructurePoolElementType<P> registerType(String id, MapCodec<P> codec) {
		StructurePoolElementType<P> type = () -> codec;
		REGISTRAR.register(id, () -> type);
		return type;
	}
	
	public static void register(IEventBus modBus) {
		REGISTRAR.register(modBus);
	}
	
}
