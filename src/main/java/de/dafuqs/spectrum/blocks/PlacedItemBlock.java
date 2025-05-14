package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class PlacedItemBlock extends BaseEntityBlock {
	
	public PlacedItemBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PlacedItemBlockEntity(pos, state);
	}
	
	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof PlacedItemBlockEntity placedItemBlockEntity) {
			ItemStack placedStack = stack.copy();
			placedStack.setCount(1);
			placedItemBlockEntity.setStack(placedStack);
			if (placer instanceof Player playerPlacer) {
				placedItemBlockEntity.setOwner(playerPlacer);
			}
		}
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
		Optional<PlacedItemBlockEntity> blockEntity = world.getBlockEntity(pos, SpectrumBlockEntities.PLACED_ITEM);
		if (blockEntity.isPresent()) {
			return blockEntity.get().getStack().copy();
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		BlockEntity blockEntity = builder.getParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity instanceof PlacedItemBlockEntity placedItemBlockEntity) {
			return List.of(placedItemBlockEntity.getStack());
		} else {
			return super.getDrops(state, builder);
		}
	}

}
