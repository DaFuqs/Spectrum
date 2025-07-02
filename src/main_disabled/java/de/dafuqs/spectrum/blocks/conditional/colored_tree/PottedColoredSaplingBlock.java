package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.api.energy.color.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PottedColoredSaplingBlock extends FlowerPotBlock implements RevelationAware, ColoredTree {
	
	protected final InkColor color;
	
	public PottedColoredSaplingBlock(Block content, Properties settings, InkColor color) {
		super(content, settings);
		this.color = color;
		RevelationAware.register(this);
	}

//	@Override
//	public MapCodec<? extends PottedColoredSaplingBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}
	
	@Override
	public ResourceLocation getCloakAdvancementIdentifier() {
		return ColoredTree.getTreeCloakAdvancementIdentifier(TreePart.SAPLING, this.color);
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		map.put(this.defaultBlockState(), Blocks.POTTED_OAK_SAPLING.defaultBlockState());
		return map;
	}
	
	@Override
	public @Nullable Tuple<Item, Item> getItemCloak() {
		return null; // does not exist in item form
	}
	
	@Override
	public InkColor getColor() {
		return this.color;
	}
	
}
