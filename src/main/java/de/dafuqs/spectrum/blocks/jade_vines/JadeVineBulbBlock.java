package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class JadeVineBulbBlock extends Block implements JadeVine, NaturesStaffItem.NaturesStaffTriggered {
	
	public static final BooleanProperty DEAD = JadeVine.DEAD;
	
	public JadeVineBulbBlock(Settings settings) {
		super(settings);
		this.setDefaultState((this.stateManager.getDefaultState()).with(DEAD, false));
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if (!state.get(DEAD)) {
			JadeVine.spawnParticlesClient(world, pos);
		}
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!state.canPlaceAt(world, pos)) {
			world.createAndScheduleBlockTick(pos, this, 1);
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!state.canPlaceAt(world, pos)) {
			world.breakBlock(pos, true);
		}
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return SpectrumItems.GERMINATED_JADE_VINE_BULB.getDefaultStack();
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return BULB_SHAPE;
	}
	
	@Override
	public boolean canPlaceAt(@NotNull BlockState state, WorldView world, BlockPos pos) {
		return world.getBlockState(pos.up()).getBlock() instanceof JadeVineRootsBlock;
	}
	
	@Override
	protected void appendProperties(StateManager.@NotNull Builder<Block, BlockState> builder) {
		builder.add(DEAD);
	}
	
	@Override
	public boolean setToAge(World world, BlockPos blockPos, int age) {
		BlockState currentState = world.getBlockState(blockPos);
		boolean dead = currentState.get(DEAD);
		if (age == 0 && !dead) {
			world.setBlockState(blockPos, currentState.with(DEAD, true));
			return true;
		} else if (age > 0 && dead) {
			world.setBlockState(blockPos, currentState.with(DEAD, false));
			return true;
		}
		return false;
	}
	
	@Override
	public boolean canUseNaturesStaff(World world, BlockPos pos, BlockState state) {
		return state.get(DEAD);
	}
	
	@Override
	public boolean onNaturesStaffUse(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockPos rootsPos = pos.up();
		BlockState rootsState = world.getBlockState(rootsPos);
		if (rootsState.getBlock() instanceof JadeVineRootsBlock jadeVineRootsBlock) {
			jadeVineRootsBlock.onNaturesStaffUse(world, rootsPos, rootsState, player);
		}
		JadeVine.spawnParticlesServer((ServerWorld) world, pos, 16);
		return false;
	}
	
}
