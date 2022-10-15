package de.dafuqs.spectrum.blocks.titration_barrel;

import de.dafuqs.spectrum.helpers.InventoryHelper;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumItemTags;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Optional;

public class TitrationBarrelBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	
	enum BarrelState implements StringIdentifiable {
		EMPTY,
		FILLED,
		SEALED,
		TAPPED;
		
		@Override
		public String asString() {
			return this.toString().toLowerCase(Locale.ROOT);
		}
	}
	
	public static final EnumProperty<BarrelState> BARREL_STATE = EnumProperty.of("barrel_state", BarrelState.class);
	
	public TitrationBarrelBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(BARREL_STATE, BarrelState.EMPTY));
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TitrationBarrelBlockEntity(pos, state);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof TitrationBarrelBlockEntity barrelEntity) {
				
				BarrelState barrelState = state.get(BARREL_STATE);
				switch (barrelState) {
					case EMPTY, FILLED -> {
						if(player.isSneaking()) {
							if(barrelState == BarrelState.FILLED) {
								tryExtractLastStack(state, world, pos, player, barrelEntity);
							}
						} else {
							// player is able to put items in
							// or seal it with a piece of colored wood
							ItemStack handStack = player.getStackInHand(hand);
							if (!handStack.isEmpty()) {
								if (handStack.isIn(SpectrumItemTags.COLORED_PLANKS)) {
									sealBarrel(world, pos, state, barrelEntity);
								} else if (handStack.isOf(Items.WATER_BUCKET)) {
									if (barrelEntity.addWaterBucket()) {
										ItemUsage.exchangeStack(handStack, player, Items.BUCKET.getDefaultStack());
										if(barrelState == BarrelState.EMPTY) {
											world.setBlockState(pos, state.with(BARREL_STATE, BarrelState.FILLED));
										}
									}
								} else {
									if(InventoryHelper.addToInventoryUpToSingleStackWithMaxTotalCount(handStack, barrelEntity.getContent(), TitrationBarrelBlockEntity.MAX_ITEM_COUNT)) {
										world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.6F);
										if(barrelState == BarrelState.EMPTY) {
											world.setBlockState(pos, state.with(BARREL_STATE, BarrelState.FILLED));
										}
									}
								}
							}
						}
					}
					case SEALED -> {
						// player is able to query the days the barrel already ferments
						// or open it with a shift-click
						if(player.isSneaking()) {
							unsealBarrel(world, pos, state, barrelEntity);
						} else {
							if(player.isCreative() && player.getMainHandStack().isOf(SpectrumItems.PAINTBRUSH)) {
								player.sendMessage(new TranslatableText("block.spectrum.titration_barrel.debug_added_day"), false);
								barrelEntity.addDayOfSealTime();
							}
							player.sendMessage(new TranslatableText("block.spectrum.titration_barrel.days_of_sealing_before_opened", barrelEntity.getSealMinecraftDays()), false);
						}
					}
					case TAPPED -> {
						// player is able to extract content until it is empty
						// reverting it to the empty state again
						ItemStack handStack = player.getStackInHand(hand);
						if(handStack.isOf(Items.GLASS_BOTTLE)) {
							ItemStack harvestedStack = barrelEntity.tryHarvest(world, pos, state, player.getMainHandStack(), player);
							if(!harvestedStack.isEmpty()) {
								Support.givePlayer(player, harvestedStack);
							}
						} else {
							player.sendMessage(new TranslatableText("block.spectrum.titration_barrel.days_of_sealing_after_opened_with_extractable_amount", barrelEntity.getSealMinecraftDays(), barrelEntity.getExtractableBottleCount(world, pos)), false);
						}
					}
				}
			}
			
			return ActionResult.CONSUME;
		}
	}
	
	private void tryExtractLastStack(BlockState state, World world, BlockPos pos, PlayerEntity player, TitrationBarrelBlockEntity barrelEntity) {
		Optional<ItemStack> stack = InventoryHelper.extractLastStack(barrelEntity.content);
		if (stack.isPresent()) {
			Support.givePlayer(player, stack.get());
			barrelEntity.markDirty();
			if (barrelEntity.content.isEmpty()) {
				world.setBlockState(pos, state.with(BARREL_STATE, BarrelState.EMPTY));
			}
		}
	}
	
	private void sealBarrel(World world, BlockPos pos, BlockState state, TitrationBarrelBlockEntity barrelEntity) {
		world.setBlockState(pos, state.with(BARREL_STATE, BarrelState.SEALED));
		barrelEntity.seal();
	}
	
	private void unsealBarrel(World world, BlockPos pos, BlockState state, TitrationBarrelBlockEntity barrelEntity) {
		world.setBlockState(pos, state.with(BARREL_STATE, BarrelState.TAPPED));
		barrelEntity.tap();
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, BARREL_STATE);
	}
	
	// drop all currently stored items
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.get(BARREL_STATE) == BarrelState.FILLED) {
			scatterContents(world, pos);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}
	
	public static void scatterContents(@NotNull World world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof TitrationBarrelBlockEntity titrationBarrelBlockEntity) {
			ItemScatterer.spawn(world, pos, titrationBarrelBlockEntity.getContent());
		}
	}
	
}
