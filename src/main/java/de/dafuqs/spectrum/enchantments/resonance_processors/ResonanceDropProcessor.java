package de.dafuqs.spectrum.enchantments.resonance_processors;

import com.google.gson.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.item.*;

import java.util.*;

public abstract class ResonanceDropProcessor {
	
	public ResonanceBlockTarget blockTarget;
	
	public ResonanceDropProcessor(ResonanceBlockTarget blockTarget) {
		this.blockTarget = blockTarget;
	}
	
	public abstract boolean process(BlockState minedState, BlockEntity blockEntity, List<ItemStack> droppedStacks);
	
	public interface Serializer {
		ResonanceDropProcessor fromJson(JsonObject json);
	}
	
}
