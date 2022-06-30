package de.dafuqs.spectrum.blocks.cinderhearth;

import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumMultiblocks;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Random;

public class CinderhearthBlock extends BlockWithEntity {
	
	public static final EnumProperty<DoubleBlockHalf> HALF = TallPlantBlock.HALF;
	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	
	public CinderhearthBlock(Settings settings) {
		super(settings);
		this.setDefaultState((this.stateManager.getDefaultState()).with(HALF, DoubleBlockHalf.LOWER).with(FACING, Direction.NORTH));
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		if(state.get(HALF) == DoubleBlockHalf.UPPER) {
			return new CinderhearthBlockEntity(pos, state);
		}
		return null;
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient || state.get(HALF) == DoubleBlockHalf.LOWER) {
			return null;
		} else {
			return checkType(type, SpectrumBlockEntityRegistry.CINDERHEARTH, CinderhearthBlockEntity::serverTick);
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			if(state.get(HALF) == DoubleBlockHalf.UPPER) {
				verifyStructure(world, pos, null);
			}
			return ActionResult.SUCCESS;
		} else {
			if(state.get(HALF) == DoubleBlockHalf.UPPER) {
				if (verifyStructure(world, pos, (ServerPlayerEntity) player)) {
					this.openScreen(world, pos, player);
				}
			}
			return ActionResult.CONSUME;
		}
		
	}
	
	protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof CinderhearthBlockEntity cinderhearthBlockEntity) {
			player.openHandledScreen(cinderhearthBlockEntity);
		}
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		BlockPos blockPos = pos.up();
		world.setBlockState(blockPos, this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER));
		if (itemStack.hasCustomName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof CinderhearthBlockEntity cinderhearthBlockEntity) {
				cinderhearthBlockEntity.setCustomName(itemStack.getName());
			}
		}
	}
	
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient) {
			if (player.isCreative()) {
				onBreakInCreative(world, pos, state, player);
			} else {
				dropStacks(state, world, pos, null, player, player.getMainHandStack());
			}
		}
		
		super.onBreak(world, pos, state, player);
	}
	
	protected static void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		DoubleBlockHalf doubleBlockHalf = state.get(HALF);
		if (doubleBlockHalf == DoubleBlockHalf.UPPER) {
			BlockPos blockPos = pos.down();
			BlockState blockState = world.getBlockState(blockPos);
			if (blockState.isOf(state.getBlock()) && blockState.get(HALF) == DoubleBlockHalf.LOWER) {
				world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 35);
				world.syncWorldEvent(player, 2001, blockPos, Block.getRawIdFromState(blockState));
			}
		}
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		DoubleBlockHalf doubleBlockHalf = state.get(HALF);
		if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP) && (!neighborState.isOf(this) || neighborState.get(HALF) == doubleBlockHalf)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		}
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		if (state.get(HALF) != DoubleBlockHalf.UPPER) {
			return super.canPlaceAt(state, world, pos);
		} else {
			BlockState blockState = world.getBlockState(pos.down());
			return blockState.isOf(this) && blockState.get(HALF) == DoubleBlockHalf.LOWER;
		}
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof CinderhearthBlockEntity cinderhearthBlockEntity) {
				if (world instanceof ServerWorld) {
					ItemScatterer.spawn(world, pos, cinderhearthBlockEntity);
				}
				world.updateComparators(pos, this);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
	
	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF);
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		double d = (double)pos.getX() + 0.5D;
		double e = pos.getY();
		double f = (double)pos.getZ() + 0.5D;
		if (random.nextDouble() < 0.1D) {
			world.playSound(d, e, f, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
		}
		
		Direction direction = state.get(FACING);
		Direction.Axis axis = direction.getAxis();
		double g = 0.52D;
		double h = random.nextDouble() * 0.6D - 0.3D;
		double i = axis == Direction.Axis.X ? (double)direction.getOffsetX() * g : h;
		double j = random.nextDouble() * 6.0D / 16.0D;
		double k = axis == Direction.Axis.Z ? (double)direction.getOffsetZ() * g : h;
		world.addParticle(ParticleTypes.SMOKE, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
		world.addParticle(ParticleTypes.FLAME, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
	}
	
	public static boolean verifyStructure(World world, @NotNull BlockPos blockPos, @Nullable ServerPlayerEntity serverPlayerEntity) {
		BlockRotation rotation = Support.rotationFromDirection(world.getBlockState(blockPos).get(FACING));
		
		IMultiblock multiblockWithLava = SpectrumMultiblocks.MULTIBLOCKS.get(SpectrumMultiblocks.CINDERHEARTH_IDENTIFIER);
		if (multiblockWithLava.validate(world, blockPos, rotation)) {
			if (serverPlayerEntity != null) {
				SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger(serverPlayerEntity, multiblockWithLava);
			}
			return true;
		} else {
			IMultiblock multiblockWithoutLava = SpectrumMultiblocks.MULTIBLOCKS.get(SpectrumMultiblocks.CINDERHEARTH_WITHOUT_LAVA_IDENTIFIER);
			if (multiblockWithoutLava.validate(world, blockPos, rotation)) {
				if (serverPlayerEntity != null) {
					SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger(serverPlayerEntity, multiblockWithoutLava);
				}
				return true;
			} else if (world.isClient) {
				PatchouliAPI.get().showMultiblock(multiblockWithLava, new TranslatableText("multiblock.spectrum.cinderhearth.structure"), blockPos.down(1), rotation);
			}
			return false;
		}
	}
	
}
