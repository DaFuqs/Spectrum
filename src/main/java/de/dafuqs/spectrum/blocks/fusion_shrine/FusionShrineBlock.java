package de.dafuqs.spectrum.blocks.fusion_shrine;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumMultiblocks;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.fabric.mixin.transfer.BucketItemAccessor;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Optional;

public class FusionShrineBlock extends BlockWithEntity {
	
	public static final Identifier UNLOCK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "collect_all_basic_pigments_besides_brown");
	public static final IntProperty LIGHT_LEVEL = IntProperty.of("light_level", 0, 15);
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 15.0D, 14.0D);
	
	public FusionShrineBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(LIGHT_LEVEL, 0));
	}
	
	public static void clearCurrentlyRenderedMultiBlock(World world) {
		if (world.isClient) {
			IMultiblock currentlyRenderedMultiBlock = PatchouliAPI.get().getCurrentMultiblock();
			if (currentlyRenderedMultiBlock != null && currentlyRenderedMultiBlock.getID().equals(SpectrumMultiblocks.FUSION_SHRINE_IDENTIFIER)) {
				PatchouliAPI.get().clearMultiblock();
			}
		}
	}
	
	public static boolean verifyStructureWithSkyAccess(World world, BlockPos blockPos, @Nullable ServerPlayerEntity serverPlayerEntity) {
		if (!world.getBlockState(blockPos.up()).isAir()) {
			world.playSound(null, blockPos, SpectrumSoundEvents.USE_FAIL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			return false;
		}
		if (!world.isSkyVisible(blockPos)) {
			if (world.isClient) {
				world.addParticle(SpectrumParticleTypes.WHITE_SPARKLE_RISING, blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, 0, 0.5, 0);
				MinecraftClient.getInstance().player.playSound(SpectrumSoundEvents.USE_FAIL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			}
			return false;
		}
		return FusionShrineBlock.verifyStructure(world, blockPos, serverPlayerEntity);
	}
	
	private static boolean verifyStructure(World world, BlockPos blockPos, @Nullable ServerPlayerEntity serverPlayerEntity) {
		IMultiblock multiblock = SpectrumMultiblocks.MULTIBLOCKS.get(SpectrumMultiblocks.FUSION_SHRINE_IDENTIFIER);
		boolean valid = multiblock.validate(world, blockPos.down(), BlockRotation.NONE);
		
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
					PatchouliAPI.get().showMultiblock(multiblock, new TranslatableText("multiblock.spectrum.fusion_shrine.structure"), blockPos.down(2), BlockRotation.NONE);
				}
			} else {
				scatterContents(world, blockPos);
			}
		}
		
		return valid;
	}
	
	public static void scatterContents(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof FusionShrineBlockEntity fusionShrineBlockEntity) {
			ItemScatterer.spawn(world, pos, fusionShrineBlockEntity.getInventory());
			world.updateComparators(pos, block);
			fusionShrineBlockEntity.inventoryChanged();
		}
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LIGHT_LEVEL);
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new FusionShrineBlockEntity(pos, state);
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		if (world.isClient()) {
			clearCurrentlyRenderedMultiBlock((World) world);
		}
	}
	
	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (!world.isClient && entity instanceof ItemEntity itemEntity) {
			if (itemEntity.getPos().x % 0.5 != 0 && itemEntity.getPos().z % 0.5 != 0) { // do not pick up items that were results of crafting
				ItemStack remainingStack = inputItemStack(world, pos, itemEntity.getStack());
				if (remainingStack.isEmpty()) {
					itemEntity.remove(Entity.RemovalReason.DISCARDED);
				} else {
					itemEntity.setStack(remainingStack);
				}
			}
		} else {
			super.onLandedUpon(world, state, pos, entity, fallDistance);
		}
	}
	
	public ItemStack inputItemStack(World world, BlockPos pos, ItemStack itemStack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof FusionShrineBlockEntity fusionShrineBlockEntity) {
			if (itemStack.getItem() instanceof BucketItem) {
				return inputFluidViaBucket(world, pos, itemStack);
			} else {
				int previousCount = itemStack.getCount();
				ItemStack remainingStack = InventoryHelper.smartAddToInventory(itemStack, fusionShrineBlockEntity.getInventory(), null);
				
				if (remainingStack.getCount() != previousCount) {
					fusionShrineBlockEntity.inventoryChanged();
					world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
					return remainingStack;
				}
			}
			
		}
		return itemStack;
	}
	
	public ItemStack inputFluidViaBucket(World world, BlockPos blockPos, ItemStack bucketStack) {
		if (bucketStack.getItem() instanceof BucketItem) {
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			if (blockEntity instanceof FusionShrineBlockEntity fusionShrineBlockEntity) {
				Fluid storedFluid = fusionShrineBlockEntity.getFluid();
				Fluid bucketFluid = ((BucketItemAccessor) bucketStack.getItem()).fabric_getFluid();
				if (storedFluid == Fluids.EMPTY && bucketFluid != Fluids.EMPTY) {
					fusionShrineBlockEntity.setFluid(bucketFluid);
					fusionShrineBlockEntity.inventoryChanged();
					
					Optional<SoundEvent> soundEvent = storedFluid.getBucketFillSound();
					if (soundEvent.isPresent()) {
						world.playSound(null, blockPos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
					}
					
					return new ItemStack(Items.BUCKET);
				} else if (storedFluid != Fluids.EMPTY && bucketFluid == Fluids.EMPTY) {
					fusionShrineBlockEntity.setFluid(Fluids.EMPTY);
					fusionShrineBlockEntity.inventoryChanged();
					
					Optional<SoundEvent> soundEvent = storedFluid.getBucketFillSound();
					if (soundEvent.isPresent()) {
						world.playSound(null, blockPos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
					}
					
					return storedFluid.getBucketItem().getDefaultStack();
				}
			}
		}
		return bucketStack;
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			verifyStructureWithSkyAccess(world, pos, null);
			return ActionResult.SUCCESS;
		} else {
			ItemStack itemStack = player.getStackInHand(hand);
			boolean itemsChanged = false;
			Optional<SoundEvent> soundToPlay = Optional.empty();
			
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof FusionShrineBlockEntity fusionShrineBlockEntity) {
				if (itemStack.getItem() instanceof BucketItem) {
					fusionShrineBlockEntity.setOwner(player);
					
					Fluid storedFluid = fusionShrineBlockEntity.getFluid();
					Fluid bucketFluid = ((BucketItemAccessor) itemStack.getItem()).fabric_getFluid();
					if (storedFluid == Fluids.EMPTY && bucketFluid != Fluids.EMPTY) {
						fusionShrineBlockEntity.setFluid(bucketFluid);
						if (!player.isCreative()) {
							ItemStack handStack = player.getStackInHand(hand);
							handStack.decrement(1);
							player.setStackInHand(hand, handStack);
							
							player.giveItemStack(Items.BUCKET.getDefaultStack());
						}
						
						soundToPlay = bucketFluid.getBucketFillSound();
						itemsChanged = true;
					} else if (storedFluid != Fluids.EMPTY && bucketFluid == Fluids.EMPTY) {
						fusionShrineBlockEntity.setFluid(Fluids.EMPTY);
						if (!player.isCreative()) {
							ItemStack handStack = player.getStackInHand(hand);
							handStack.decrement(1);
							player.setStackInHand(hand, handStack);
							
							player.giveItemStack(new ItemStack(storedFluid.getBucketItem()));
						}
						
						soundToPlay = storedFluid.getBucketFillSound();
						itemsChanged = true;
					}
				} else {
					// if the structure is valid the player can put / retrieve blocks into the shrine
					if (player.isSneaking()) {
						ItemStack retrievedStack = ItemStack.EMPTY;
						Inventory inventory = fusionShrineBlockEntity.getInventory();
						for (int i = inventory.size() - 1; i >= 0; i--) {
							retrievedStack = inventory.removeStack(i);
							if (!retrievedStack.isEmpty()) {
								break;
							}
						}
						if (!retrievedStack.isEmpty()) {
							player.giveItemStack(retrievedStack);
							soundToPlay = Optional.of(SoundEvents.ENTITY_ITEM_PICKUP);
							itemsChanged = true;
						}
					} else if (!itemStack.isEmpty() && verifyStructure(world, pos, (ServerPlayerEntity) player)) {
						fusionShrineBlockEntity.setOwner(player);
						
						ItemStack remainingStack = InventoryHelper.smartAddToInventory(itemStack, fusionShrineBlockEntity.getInventory(), null);
						player.setStackInHand(hand, remainingStack);
						
						soundToPlay = Optional.of(SoundEvents.ENTITY_ITEM_PICKUP);
						itemsChanged = true;
					}
				}
				
				if (itemsChanged) {
					fusionShrineBlockEntity.inventoryChanged();
					soundToPlay.ifPresent(soundEvent -> world.playSound(null, player.getBlockPos(), soundEvent, SoundCategory.PLAYERS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F));
				}
			}
			
			return ActionResult.CONSUME;
		}
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient) {
			return checkType(type, SpectrumBlockEntityRegistry.FUSION_SHRINE, FusionShrineBlockEntity::clientTick);
		} else {
			return checkType(type, SpectrumBlockEntityRegistry.FUSION_SHRINE, FusionShrineBlockEntity::serverTick);
		}
	}
	
	// drop all currently stored items
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!newState.getBlock().equals(this)) { // happens when filling with fluid
			scatterContents(world, pos);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}
	
}
