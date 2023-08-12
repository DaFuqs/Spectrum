package de.dafuqs.spectrum.enchantments.resonance_processors;

import com.google.common.base.*;
import com.mojang.brigadier.exceptions.*;
import com.mojang.datafixers.util.*;
import net.minecraft.block.*;
import net.minecraft.command.argument.*;
import net.minecraft.util.*;
import net.minecraft.registry.*;

public class ResonanceBlockTarget {
	
	// we have to use a lazy here, since at the stage this is needed
	// block tags are not initialized yet
	public Supplier<Either<BlockArgumentParser.BlockResult, BlockArgumentParser.TagResult>> block;
	
	public ResonanceBlockTarget(String string) {
		block = Suppliers.memoize(() -> {
			try {
				return BlockArgumentParser.blockOrTag(Registries.BLOCK.getReadOnlyWrapper(), string, false);
			} catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        });
	}
	
	public boolean test(BlockState state) {
		Either<BlockArgumentParser.BlockResult, BlockArgumentParser.TagResult> target = block.get();
		if (target.left().isPresent()) {
			return state.isOf(target.left().get().blockState().getBlock());
		}
		if (target.right().isPresent()) {
			return target.right().get().tag().contains(state.getBlock().getRegistryEntry());
		}
		return false;
	}
	
}
