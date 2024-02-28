package de.dafuqs.spectrum.api.interaction;

import com.google.gson.*;
import de.dafuqs.spectrum.api.predicate.block.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.item.*;

import java.util.*;

public abstract class ResonanceDropProcessor {
	
	public BrokenBlockPredicate blockPredicate;
	
	public ResonanceDropProcessor(BrokenBlockPredicate blockPredicate) throws Exception {
		this.blockPredicate = blockPredicate;

		if(blockPredicate.test(Blocks.AIR.getDefaultState())) {
			throw new Exception("Registering a Resonance Drop that matches on everything!");
		}
	}
	
	public abstract boolean process(BlockState state, BlockEntity blockEntity, List<ItemStack> droppedStacks);
	
	public interface Serializer {
		ResonanceDropProcessor fromJson(JsonObject json) throws Exception;
	}
	
}
