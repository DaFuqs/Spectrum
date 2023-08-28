package de.dafuqs.spectrum.enchantments.resonance_processors;

import com.google.gson.*;
import de.dafuqs.spectrum.predicate.block.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.item.*;

import java.util.*;

public abstract class ResonanceDropProcessor {
	
	public BrokenBlockPredicate blockPredicate;
	
	public ResonanceDropProcessor(BrokenBlockPredicate blockPredicate) {
		this.blockPredicate = blockPredicate;
	}
	
	public abstract boolean process(BlockState state, BlockEntity blockEntity, List<ItemStack> droppedStacks);
	
	public interface Serializer {
		ResonanceDropProcessor fromJson(JsonObject json);
	}
	
}
