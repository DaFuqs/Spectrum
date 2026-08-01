package de.dafuqs.spectrum.compat.exclusions_lib;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.compat.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.blockpredicates.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

/**
 * Loaded when Exclusions Lib is *not* present
 * So the game can be loaded without complaining about a missing Block Predicate
 */
public class ExclusionsLibCompat {
	
	private static final DeferredRegister<BlockPredicateType<?>> REGISTRAR = DeferredRegister.create(Registries.BLOCK_PREDICATE_TYPE, SpectrumIntegrationPacks.EXCLUSIONS_LIB_ID);
	
	public static class AlwaysFalseBlockPredicate implements BlockPredicate {
		
		public static AlwaysFalseBlockPredicate INSTANCE = new AlwaysFalseBlockPredicate();
		public static final MapCodec<AlwaysFalseBlockPredicate> CODEC = MapCodec.unit(() -> INSTANCE);
		
		private AlwaysFalseBlockPredicate() {
		}
		
		public boolean test(WorldGenLevel structureWorldAccess, BlockPos blockPos) {
			return false;
		}
		
		public BlockPredicateType<?> type() {
			return BlockPredicateType.TRUE;
		}
	}
	
	public static DeferredHolder<BlockPredicateType<?>, BlockPredicateType<?>> OVERLAPS_STRUCTURE_DUMMY = REGISTRAR.register("overlaps_structure",
			() -> (BlockPredicateType<AlwaysFalseBlockPredicate>) () -> AlwaysFalseBlockPredicate.CODEC);
	
	public static void registerNotPresent(IEventBus modBus) {
		REGISTRAR.register(modBus);
	}
	
}