package de.dafuqs.spectrum.blocks.deeper_down;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.block.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;

public class StrippingLootPillarBlock extends RotatedPillarBlock implements StrippableDrop {
	
	private final Block sourceBlock;
	private final ResourceKey<LootTable> strippingLootTableKey;
	
	public StrippingLootPillarBlock(Properties settings, Block sourceBlock, ResourceKey<LootTable> strippingLootTableKey) {
		super(settings);
		this.sourceBlock = sourceBlock;
		this.strippingLootTableKey = strippingLootTableKey;
	}
	
	@Override
	public MapCodec<? extends StrippingLootPillarBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	
	@Override
	public Block getStrippedBlock() {
		return sourceBlock;
	}
	
	@Override
	public ResourceKey<LootTable> getStrippingLootTableKey() {
		return strippingLootTableKey;
	}
	
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		checkAndDropStrippedLoot(state, world, pos, newState, moved);
		super.onRemove(state, world, pos, newState, moved);
	}
	
}
