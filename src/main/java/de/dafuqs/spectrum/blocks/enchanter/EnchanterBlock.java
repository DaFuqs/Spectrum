package de.dafuqs.spectrum.blocks.enchanter;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.InWorldInteractionBlock;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import de.dafuqs.spectrum.registries.SpectrumMultiblocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

public class EnchanterBlock extends InWorldInteractionBlock {
	
	public static final Identifier UNLOCK_IDENTIFIER = SpectrumCommon.locate("midgame/build_enchanting_structure");
	
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
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new EnchanterBlockEntity(pos, state);
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
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
							if(retrieveSingle(world, pos, player, hand, handStack, enchanterBlockEntity, i)) {
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
						if(exchangeSingle(world, pos, player, hand, handStack, enchanterBlockEntity, inputInventorySlotIndex)) {
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
