package de.dafuqs.spectrum.blocks.conditional.blood_orchid;

import de.dafuqs.revelationary.api.revelations.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PottedBloodOrchidBlock extends FlowerPotBlock implements RevelationAware {
	
	public PottedBloodOrchidBlock(Block content, Settings settings) {
		super(content, settings);
		RevelationAware.register(this);
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return BloodOrchidBlock.ADVANCEMENT_IDENTIFIER;
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		map.put(this.getDefaultState(), Blocks.POTTED_RED_TULIP.getDefaultState());
		return map;
	}
	
	@Override
	public @Nullable Pair<Item, Item> getItemCloak() {
		return null; // does not exist in item form
	}
	
}