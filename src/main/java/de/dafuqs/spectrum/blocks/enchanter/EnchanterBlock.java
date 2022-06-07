package de.dafuqs.spectrum.blocks.enchanter;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumMultiblocks;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

public class EnchanterBlock extends BlockWithEntity {
	
	public static final Identifier UNLOCK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "midgame/build_enchanting_structure");
	
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 11.0D, 14.0D);
	
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
					PatchouliAPI.get().showMultiblock(multiblock, new TranslatableText("multiblock.spectrum.enchanter.structure"), blockPos.down(4), BlockRotation.NONE);
				}
			}
		}
		
		return valid;
	}
	
	public static void scatterContents(@NotNull World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof EnchanterBlockEntity enchanterBlockEntity) {
			ItemScatterer.spawn(world, pos, enchanterBlockEntity.getInventory());
			enchanterBlockEntity.inventoryChanged = true;
			world.updateComparators(pos, block);
		}
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new EnchanterBlockEntity(pos, state);
	}
	
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient) {
			return checkType(type, SpectrumBlockEntityRegistry.ENCHANTER, EnchanterBlockEntity::clientTick);
		} else {
			return checkType(type, SpectrumBlockEntityRegistry.ENCHANTER, EnchanterBlockEntity::serverTick);
		}
	}
	
	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		if (world.isClient()) {
			clearCurrentlyRenderedMultiBlock((World) world);
		}
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		scatterContents(world, pos);
		super.onStateReplaced(state, world, pos, newState, moved);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			verifyStructure(world, pos, null);
			return ActionResult.SUCCESS;
		} else {
			if (verifyStructure(world, pos, (ServerPlayerEntity) player)) {
				ItemStack handStack = player.getStackInHand(hand);
				
				// if the structure is valid the player can put / retrieve blocks into the shrine
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity instanceof EnchanterBlockEntity enchanterBlockEntity) {
					boolean itemsChanged = false;
					Inventory inventory = enchanterBlockEntity.getInventory();
					
					int inputInventorySlotIndex = handStack.getItem() instanceof ExperienceStorageItem ? inventory.getStack(1).isEmpty() ? 1 : 0 : 0;
					if (player.isSneaking() || handStack.isEmpty()) {
						// sneaking or empty hand: remove items
						for (int i = 0; i < EnchanterBlockEntity.INVENTORY_SIZE; i++) {
							ItemStack retrievedStack = inventory.removeStack(i);
							if (!retrievedStack.isEmpty()) {
								player.giveItemStack(retrievedStack);
								itemsChanged = true;
								break;
							}
						}
					} else {
						// hand is full and inventory is empty: add
						// hand is full and inventory already contains item: exchange them
						ItemStack currentStack = inventory.getStack(inputInventorySlotIndex);
						inventory.setStack(inputInventorySlotIndex, handStack);
						if (currentStack.isEmpty()) {
							player.setStackInHand(hand, ItemStack.EMPTY);
						} else {
							player.setStackInHand(hand, currentStack);
						}
						itemsChanged = true;
					}
					
					if (itemsChanged) {
						enchanterBlockEntity.inventoryChanged();
						enchanterBlockEntity.setItemFacingDirection(player.getHorizontalFacing());
						enchanterBlockEntity.setOwner(player);
						
						enchanterBlockEntity.markDirty();
						enchanterBlockEntity.updateInClientWorld();
						world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
					}
				}
			}
			return ActionResult.CONSUME;
		}
	}
	
}
