package de.dafuqs.spectrum.blocks.spirit_instiller;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.helpers.Support;
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

public class SpiritInstillerBlock extends BlockWithEntity {
	
	public static final Identifier UNLOCK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "midgame/build_spirit_instiller_structure");
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
	
	public SpiritInstillerBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SpiritInstillerBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if(world.isClient) {
			return checkType(type, SpectrumBlockEntityRegistry.SPIRIT_INSTILLER, SpiritInstillerBlockEntity::clientTick);
		} else {
			return checkType(type, SpectrumBlockEntityRegistry.SPIRIT_INSTILLER, SpiritInstillerBlockEntity::serverTick);
		}
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		if(world.isClient()) {
			clearCurrentlyRenderedMultiBlock((World) world);
		}
	}
	
	public static void clearCurrentlyRenderedMultiBlock(World world) {
		if(world.isClient) {
			IMultiblock currentlyRenderedMultiBlock = PatchouliAPI.get().getCurrentMultiblock();
			if (currentlyRenderedMultiBlock != null && currentlyRenderedMultiBlock.getID().equals(SpectrumMultiblocks.SPIRIT_INSTILLER_IDENTIFIER)) {
				PatchouliAPI.get().clearMultiblock();
			}
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(world.isClient) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if(blockEntity instanceof SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
				verifyStructure(world, pos, null, spiritInstillerBlockEntity);
			}
			return ActionResult.SUCCESS;
		} else {
			ItemStack handStack = player.getStackInHand(hand);
			boolean itemsChanged = false;
			
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if(blockEntity instanceof SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
				// if the structure is valid the player can put / retrieve blocks into the shrine
				if (player.isSneaking()) {
					Inventory inventory = spiritInstillerBlockEntity.getInventory();
					ItemStack retrievedStack = inventory.removeStack(0);
					if (!retrievedStack.isEmpty()) {
						player.giveItemStack(retrievedStack);
						itemsChanged = true;
					}
				} else {
					Inventory inventory = spiritInstillerBlockEntity.getInventory();
					ItemStack currentStack = inventory.getStack(0);
					if(!handStack.isEmpty() && !currentStack.isEmpty()) {
						inventory.setStack(0, handStack);
						player.setStackInHand(hand, currentStack);
						itemsChanged = true;
					} else {
						if (!handStack.isEmpty()) {
							ItemStack remainingStack = InventoryHelper.smartAddToInventory(handStack, spiritInstillerBlockEntity.getInventory(), null);
							player.setStackInHand(hand, remainingStack);
							itemsChanged = true;
						}
						if (!currentStack.isEmpty()) {
							player.giveItemStack(currentStack);
							itemsChanged = true;
						}
					}
				}
				
				if (verifyStructure(world, pos, (ServerPlayerEntity) player, spiritInstillerBlockEntity)) {
					spiritInstillerBlockEntity.setOwner(player);
				}
				
				if(itemsChanged) {
					spiritInstillerBlockEntity.inventoryChanged();
					world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
				}
			}
			
			return ActionResult.CONSUME;
		}
	}
	
	public static boolean verifyStructure(World world, @NotNull BlockPos blockPos, @Nullable ServerPlayerEntity serverPlayerEntity, @NotNull SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
		IMultiblock multiblock = SpectrumMultiblocks.MULTIBLOCKS.get(SpectrumMultiblocks.SPIRIT_INSTILLER_IDENTIFIER);
		
		BlockRotation lastBlockRotation = spiritInstillerBlockEntity.getMultiblockRotation();
		boolean valid = false;
		
		// try all 4 rotations
		BlockRotation checkRotation = lastBlockRotation;
		for(int i = 0; i < BlockRotation.values().length; i++) {
			valid = multiblock.validate(world, blockPos.down(2).offset(Support.directionFromRotation(checkRotation), 2), checkRotation);
			if(valid) {
				if(i != 0) {
					spiritInstillerBlockEntity.setMultiblockRotation(checkRotation);
				}
				break;
			} else {
				checkRotation = BlockRotation.values()[(checkRotation.ordinal() + 1) % BlockRotation.values().length];
			}
		}
		
		if(valid) {
			if (serverPlayerEntity != null) {
				SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger(serverPlayerEntity, multiblock);
			}
		} else {
			if(world.isClient) {
				IMultiblock currentMultiBlock = PatchouliAPI.get().getCurrentMultiblock();
				if(currentMultiBlock == multiblock) {
					lastBlockRotation = BlockRotation.values()[(lastBlockRotation.ordinal() + 1) % BlockRotation.values().length]; // cycle rotation
					spiritInstillerBlockEntity.setMultiblockRotation(lastBlockRotation);
				} else {
					lastBlockRotation = BlockRotation.NONE;
				}
				PatchouliAPI.get().showMultiblock(multiblock, new TranslatableText("multiblock.spectrum.spirit_instiller.structure"), blockPos.down(3).offset(Support.directionFromRotation(lastBlockRotation), 2), lastBlockRotation);
			} else {
				scatterContents(world, blockPos);
			}
		}
		
		return valid;
	}
	
	// drop all currently stored items
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		scatterContents(world, pos);
		super.onStateReplaced(state, world, pos, newState, moved);
	}
	
	public static void scatterContents(@NotNull World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof SpiritInstillerBlockEntity spiritInstillerBlockEntity) {
			ItemScatterer.spawn(world, pos, spiritInstillerBlockEntity.getInventory());
			spiritInstillerBlockEntity.inventoryChanged = true;
			world.updateComparators(pos, block);
			spiritInstillerBlockEntity.updateInClientWorld();
		}
	}
	
}
