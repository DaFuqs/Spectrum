package de.dafuqs.spectrum.blocks.titration_barrel;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.transfer.v1.context.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class TitrationBarrelBlock extends HorizontalDirectionalBlock implements EntityBlock {
	
	public static final MapCodec<TitrationBarrelBlock> CODEC = simpleCodec(TitrationBarrelBlock::new);
	
	public enum BarrelState implements StringRepresentable {
		EMPTY,
		FILLED,
		SEALED,
		TAPPED;
		
		@Override
		public String getSerializedName() {
			return this.toString().toLowerCase(Locale.ROOT);
		}
	}
	
	public static final EnumProperty<BarrelState> BARREL_STATE = EnumProperty.create("barrel_state", BarrelState.class);
	
	public TitrationBarrelBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(BARREL_STATE, BarrelState.EMPTY));
	}
	
	@Override
	public MapCodec<? extends TitrationBarrelBlock> codec() {
		return CODEC;
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TitrationBarrelBlockEntity(pos, state);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public ItemInteractionResult useItemOn(ItemStack handStack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (world.isClientSide) {
			return ItemInteractionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof TitrationBarrelBlockEntity barrelEntity) {
				
				BarrelState barrelState = state.getValue(BARREL_STATE);
				switch (barrelState) {
					case EMPTY, FILLED -> {
						if (player.isShiftKeyDown() && handStack.isEmpty()) {
							if (barrelState == BarrelState.FILLED) {
								tryExtractLastStack(state, world, pos, player, barrelEntity);
							}
						} else {
							// player is able to put items in
							// or seal it with a piece of colored wood
							if (handStack.isEmpty()) {
								int itemCount = InventoryHelper.countItemsInInventory(barrelEntity);
								Fluid fluid = barrelEntity.fluidStorage.variant.getFluid();
								if (fluid == Fluids.EMPTY) {
									if (itemCount == TitrationBarrelBlockEntity.MAX_ITEM_COUNT) {
										player.displayClientMessage(Component.translatable("block.spectrum.titration_barrel.content_count_without_fluid_full", itemCount), true);
									} else {
										player.displayClientMessage(Component.translatable("block.spectrum.titration_barrel.content_count_without_fluid", itemCount), true);
									}
								} else {
									String fluidName = fluid.defaultFluidState().createLegacyBlock().getBlock().getName().getString();
									if (itemCount == TitrationBarrelBlockEntity.MAX_ITEM_COUNT) {
										player.displayClientMessage(Component.translatable("block.spectrum.titration_barrel.content_count_with_fluid_full", fluidName, itemCount), true);
									} else {
										player.displayClientMessage(Component.translatable("block.spectrum.titration_barrel.content_count_with_fluid", fluidName, itemCount), true);
									}
								}
							} else {
								if (handStack.is(SpectrumItemTags.COLORED_PLANKS)) {
									if (barrelEntity.canBeSealed(player)) {
										if (!player.isCreative()) {
											handStack.shrink(1);
										}
										sealBarrel(world, pos, state, barrelEntity, player);
									} else {
										player.displayClientMessage(Component.translatable("block.spectrum.titration_barrel.invalid_recipe"), true);
									}
									return ItemInteractionResult.CONSUME;
								}
								
								if (ContainerItemContext.forPlayerInteraction(player, hand).find(FluidStorage.ITEM) != null) {
									if (FluidStorageUtil.interactWithFluidStorage(barrelEntity.fluidStorage, player, hand)) {
										if (barrelEntity.getFluidVariant().isBlank()) {
											if (state.getValue(BARREL_STATE) == TitrationBarrelBlock.BarrelState.FILLED && barrelEntity.isEmpty()) {
												world.setBlockAndUpdate(pos, state.setValue(BARREL_STATE, TitrationBarrelBlock.BarrelState.EMPTY));
											}
										} else {
											if (state.getValue(BARREL_STATE) == TitrationBarrelBlock.BarrelState.EMPTY) {
												world.setBlockAndUpdate(pos, state.setValue(BARREL_STATE, TitrationBarrelBlock.BarrelState.FILLED));
											}
										}
										return ItemInteractionResult.CONSUME;
									}
								}
								
								int countBefore = handStack.getCount();
								ItemStack leftoverStack = InventoryHelper.addToInventoryUpToSingleStackWithMaxTotalCount(handStack, barrelEntity, TitrationBarrelBlockEntity.MAX_ITEM_COUNT);
								player.setItemInHand(hand, leftoverStack);
								if (countBefore != leftoverStack.getCount()) {
									world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
									if (barrelState == BarrelState.EMPTY) {
										world.setBlockAndUpdate(pos, state.setValue(BARREL_STATE, BarrelState.FILLED));
									} else {
										world.updateNeighbourForOutputSignal(pos, this);
									}
								}
								
							}
						}
					}
					case SEALED -> {
						// player is able to query the days the barrel already ferments
						// or open it with a shift-click
						var recipe = barrelEntity.getRecipeForInventory(world);
						if (recipe.isPresent()) {
							if (player.isCreative() && player.getMainHandItem().is(SpectrumItems.PAINTBRUSH)) {
								player.displayClientMessage(Component.translatable("block.spectrum.titration_barrel.debug_added_day"), true);
								barrelEntity.addOneDayOfSealTime();
								world.playSound(null, pos, SpectrumSoundEvents.NEW_RECIPE, SoundSource.BLOCKS, 1.0F, 1.0F);
							}
							
							// funky check to allow shenanigans when sealing it when changing the computer's clock to the past
							long sealSeconds = barrelEntity.getSealSeconds();
							if (sealSeconds >= 0 && !recipe.get().value().isFermentingLongEnoughToTap(barrelEntity.getSealSeconds())) {
								player.displayClientMessage(Component.translatable("block.spectrum.titration_barrel.not_yet_ready", barrelEntity.getSealMinecraftDays(), barrelEntity.getSealRealDays()), true);
								break;
							}
						}
						
						if (player.isShiftKeyDown()) {
							unsealBarrel(world, pos, state, barrelEntity);
						} else {
							player.displayClientMessage(Component.translatable("block.spectrum.titration_barrel.days_of_sealing_before_opened", barrelEntity.getSealMinecraftDays(), barrelEntity.getSealRealDays()), true);
						}
					}
					case TAPPED -> {
						// player is able to extract content until it is empty
						// reverting it to the empty state again
						if (player.isShiftKeyDown()) {
							Optional<RecipeHolder<ITitrationBarrelRecipe>> recipe = world.getRecipeManager().getRecipeFor(SpectrumRecipeTypes.TITRATION_BARREL, barrelEntity.getRecipeInput(), world);
							if (recipe.isPresent()) {
								player.displayClientMessage(Component.translatable("block.spectrum.titration_barrel.days_of_sealing_after_opened_with_extractable_amount", recipe.get().value().assemble(barrelEntity.getRecipeInput(), world.registryAccess()).getHoverName().getString(), barrelEntity.getSealMinecraftDays(), barrelEntity.getSealRealDays()), true);
							} else {
								player.displayClientMessage(Component.translatable("block.spectrum.titration_barrel.invalid_recipe_when_tapping"), true);
							}
						} else {
							ItemStack harvestedStack = barrelEntity.tryHarvest(world, pos, state, player.getItemInHand(hand), player);
							if (!harvestedStack.isEmpty()) {
								player.getInventory().placeItemBackInInventory(harvestedStack);
								world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
							}
						}
					}
				}
			}
			
			return ItemInteractionResult.CONSUME;
		}
	}
	
	private void tryExtractLastStack(BlockState state, Level world, BlockPos pos, Player player, TitrationBarrelBlockEntity barrelEntity) {
		Optional<ItemStack> stack = InventoryHelper.extractLastStack(barrelEntity);
		if (stack.isPresent()) {
			player.getInventory().placeItemBackInInventory(stack.get());
			barrelEntity.setChanged();
			if (barrelEntity.isEmpty() && barrelEntity.getFluidVariant().isBlank()) {
				world.setBlockAndUpdate(pos, state.setValue(BARREL_STATE, BarrelState.EMPTY));
			} else {
				// They'll get updated if the block state changes anyway
				world.updateNeighbourForOutputSignal(pos, this);
			}
			
			world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}
	
	private void sealBarrel(Level world, BlockPos pos, BlockState state, TitrationBarrelBlockEntity barrelEntity, Player player) {
		// give recipe remainders
		barrelEntity.giveRecipeRemainders(player);
		
		// seal
		world.setBlockAndUpdate(pos, state.setValue(BARREL_STATE, BarrelState.SEALED));
		barrelEntity.seal();
		world.playSound(null, pos, SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 1.0F, 1.0F);
	}
	
	private void unsealBarrel(Level world, BlockPos pos, BlockState state, TitrationBarrelBlockEntity barrelEntity) {
		world.setBlockAndUpdate(pos, state.setValue(BARREL_STATE, BarrelState.TAPPED));
		barrelEntity.tap();
		world.playSound(null, pos, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 1.0F, 1.0F);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState state = this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
		
		var comp = ctx.getItemInHand().get(DataComponents.BLOCK_ENTITY_DATA);
		if (comp != null) {
			var nbt = comp.copyTag();
			boolean inventoryEmpty = nbt.getList("Inventory", Tag.TAG_COMPOUND).isEmpty();
			long fluidAmount = nbt.getLong("FluidAmount");
			long sealTime = nbt.contains("SealTime", Tag.TAG_LONG) ? nbt.getLong("SealTime") : -1;
			long tapTime = nbt.contains("TapTime", Tag.TAG_LONG) ? nbt.getLong("TapTime") : -1;
			
			BarrelState barrelState = tapTime > -1
					? BarrelState.TAPPED : sealTime > -1
					? BarrelState.SEALED : inventoryEmpty && fluidAmount == 0
					? BarrelState.EMPTY : BarrelState.FILLED;
			state = state.setValue(BARREL_STATE, barrelState);
		}
		
		return state;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, BARREL_STATE);
	}
	
	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	
	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		if (world.getBlockEntity(pos) instanceof TitrationBarrelBlockEntity blockEntity) {
			switch (state.getValue(BARREL_STATE)) {
				case EMPTY -> {
					return 0;
				}
				case FILLED -> {
					int isNotEmpty = blockEntity.isEmpty() ? 0 : 1;
					
					float icurr = InventoryHelper.countItemsInInventory(blockEntity);
					float imax = TitrationBarrelBlockEntity.MAX_ITEM_COUNT;
					
					float fcurr = blockEntity.fluidStorage.amount;
					float fmax = blockEntity.fluidStorage.getCapacity();
					
					return Mth.floor(((icurr / imax) + (fcurr / fmax)) / 2.0f * 14.0f) + isNotEmpty;
				}
				case SEALED -> {
					return 15;
				}
				case TAPPED -> {
					Biome biome = world.getBiome(pos).value();
					Optional<RecipeHolder<ITitrationBarrelRecipe>> recipe = blockEntity.getRecipeForInventory(world);
					if (recipe.isEmpty()) return 0;
					
					float curr = blockEntity.extractedBottles;
					float max = recipe.get().value().getOutputCountAfterAngelsShare(world, biome.getBaseTemperature(), blockEntity.getSealSeconds());
					
					return Mth.floor((1.0f - curr / max) * 15.0f);
				}
			}
			
			
		}
		
		return 0;
	}
	
	// drop all currently stored items
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if (!newState.is(this) && state.getValue(BARREL_STATE) == BarrelState.FILLED) {
			scatterContents(world, pos);
			world.updateNeighbourForOutputSignal(pos, this);
		}
		
		super.onRemove(state, world, pos, newState, moved);
	}
	
	public static void scatterContents(@NotNull Level world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof TitrationBarrelBlockEntity titrationBarrelBlockEntity) {
			Containers.dropContents(world, pos, titrationBarrelBlockEntity);
		}
	}
	
}
