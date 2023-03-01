package de.dafuqs.spectrum.blocks.conditional.blood_orchid;

import de.dafuqs.revelationary.api.revelations.RevelationAware;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;
import java.util.Map;

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
		return new Pair<>(this.asItem(), Blocks.POTTED_RED_TULIP.asItem());
	}
	
}