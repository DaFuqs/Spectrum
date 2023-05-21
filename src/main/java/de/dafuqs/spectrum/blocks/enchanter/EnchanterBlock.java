package de.dafuqs.spectrum.blocks.enchanter;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.api.*;

public class EnchanterBlock extends InWorldInteractionBlock {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("midgame/build_enchanting_structure");
	
	public EnchanterBlock(Settings settings) {
		super(settings);
	}
	
	public static void clearCurrentlyRenderedMultiBlock(World world) {
		if (world.isClient) {
			IMultiblock currentlyRenderedMultiBlock = PatchouliAPI.get().getCurrentMultiblock();
			if (currentlyRenderedMultiBlock != null && currentlyRenderedMultiBlock.getID().equals(SpectrumMultiblocks.ENCHANTER_IDENTIFIER)) {
				PatchouliAPI.get().clearMultiblock();
			}
		}
	}
	
	public static boolean verifyStructure(World world, BlockPos blockPos, @Nullable ServerPlayerEntity serverPlayerEntity) {
		IMultiblock multiblock = SpectrumMultiblocks.MULTIBLOCKS.get(SpectrumMultiblocks.ENCHANTER_IDENTIFIER);
		boolean valid = multiblock.validate(world, blockPos.down(3), BlockRotation.NONE);
		
		if (valid) {
			if (serverPlayerEntity != null) {
				SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger(serverPlayerEntity, multiblock);
			}
		} else {
			if (world.isClient) {
				IMultiblock currentMultiBlock = PatchouliAPI.get().getCurrentMultiblock();
				if (currentMultiBlock == multiblock) {
					PatchouliAPI.get().clearMultiblock();
				} else {
					PatchouliAPI.get().showMultiblock(multiblock, Text.translatable("multiblock.spectrum.enchanter.structure"), blockPos.down(4), BlockRotation.NONE);
				}
			}
		}
		
		return valid;
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new EnchanterBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient) {
			return checkType(type, SpectrumBlockEntities.ENCHANTER, EnchanterBlockEntity::clientTick);
		} else {
			return checkType(type, SpectrumBlockEntities.ENCHANTER, EnchanterBlockEntity::serverTick);
		}
	}
	
	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		if (world.isClient()) {
			clearCurrentlyRenderedMultiBlock((World) world);
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			verifyStructure(world, pos, null);
			return ActionResult.SUCCESS;
		} else {
			if (verifyStructure(world, pos, (ServerPlayerEntity) player)) {
				
				// if the structure is valid the player can put / retrieve blocks into the shrine
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity instanceof EnchanterBlockEntity enchanterBlockEntity) {
					
					ItemStack handStack = player.getStackInHand(hand);
					if (player.isSneaking() || handStack.isEmpty()) {
						// sneaking or empty hand: remove items
						for (int i = 0; i < EnchanterBlockEntity.INVENTORY_SIZE; i++) {
							if (retrieveStack(world, pos, player, hand, handStack, enchanterBlockEntity, i)) {
								enchanterBlockEntity.setItemFacingDirection(player.getHorizontalFacing());
								enchanterBlockEntity.setOwner(player);
								break;
							}
						}
						return ActionResult.CONSUME;
					} else {
						// hand is full and inventory is empty: add
						// hand is full and inventory already contains item: exchange them
						int inputInventorySlotIndex = handStack.getItem() instanceof ExperienceStorageItem ? enchanterBlockEntity.getStack(1).isEmpty() ? 1 : 0 : 0;
						if (exchangeStack(world, pos, player, hand, handStack, enchanterBlockEntity, inputInventorySlotIndex)) {
							enchanterBlockEntity.setItemFacingDirection(player.getHorizontalFacing());
							enchanterBlockEntity.setOwner(player);
						}
					}
				}
			}
			return ActionResult.CONSUME;
		}
	}
	
}
