package de.dafuqs.spectrum.enchantments.resonance_processors;

import com.mojang.brigadier.exceptions.*;
import com.mojang.datafixers.util.*;
import net.minecraft.block.*;
import net.minecraft.command.argument.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;

public class ResonanceBlockTarget {
	
	// we have to use a lazy here, since at the stage this is needed
	// block tags are not initialized yet
	public Lazy<Either<BlockArgumentParser.BlockResult, BlockArgumentParser.TagResult>> block;
	
	public ResonanceBlockTarget(String string) {
		block = new Lazy<>(() -> {
			try {
				return BlockArgumentParser.blockOrTag(Registry.BLOCK, string, false);
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
