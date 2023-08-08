package de.dafuqs.spectrum.items;

import net.fabricmc.fabric.api.item.v1.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;

// TODO: this item and the `spectrum:spawners` tag can be removed in 1.19.4, since resonance now drops vanilla mob spawners instead
@Deprecated
public class SpectrumMobSpawnerItem extends Item {
	
	public SpectrumMobSpawnerItem(FabricItemSettings fabricItemSettings) {
		super(fabricItemSettings);
	}
	
	public ActionResult place(ItemPlacementContext context) {
		if (!context.canPlace()) {
			return ActionResult.FAIL;
		} else {
			BlockState blockState = Blocks.SPAWNER.getDefaultState();
			if (blockState == null) {
				return ActionResult.FAIL;
			} else if (!context.getWorld().setBlockState(context.getBlockPos(), blockState, 11)) {
				return ActionResult.FAIL;
			} else {
				BlockPos blockPos = context.getBlockPos();
				World world = context.getWorld();
				PlayerEntity playerEntity = context.getPlayer();
				ItemStack itemStack = context.getStack();
				BlockState blockState2 = world.getBlockState(blockPos);
				if (blockState2.isOf(blockState.getBlock())) {
					BlockItem.writeNbtToBlockEntity(world, playerEntity, blockPos, itemStack);
					blockState2.getBlock().onPlaced(world, blockPos, blockState2, playerEntity, itemStack);
					if (playerEntity instanceof ServerPlayerEntity) {
						Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos, itemStack);
					}
				}
				
				BlockSoundGroup blockSoundGroup = blockState2.getSoundGroup();
				world.playSound(playerEntity, blockPos, Blocks.SPAWNER.getSoundGroup(blockState2).getPlaceSound(), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
				world.emitGameEvent(playerEntity, GameEvent.BLOCK_PLACE, blockPos);
				if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
					itemStack.decrement(1);
				}
				
				return ActionResult.success(world.isClient);
			}
		}
	}
	
}
