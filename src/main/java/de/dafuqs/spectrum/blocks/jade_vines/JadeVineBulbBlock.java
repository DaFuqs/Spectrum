package de.dafuqs.spectrum.blocks.jade_vines;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

public class JadeVineBulbBlock extends Block implements JadeVine, NaturesStaffTriggered {
	
	public static final MapCodec<JadeVineBulbBlock> CODEC = simpleCodec(JadeVineBulbBlock::new);

	public static final BooleanProperty DEAD = JadeVine.DEAD;
	
	public JadeVineBulbBlock(Properties settings) {
		super(settings);
		this.registerDefaultState((this.stateDefinition.any()).setValue(DEAD, false));
	}

	@Override
	public MapCodec<? extends JadeVineBulbBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);
		if (!state.getValue(DEAD)) {
			JadeVine.spawnParticlesClient(world, pos);
		}
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (!state.canSurvive(world, pos)) {
			world.scheduleTick(pos, this, 1);
		}
		return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (!state.canSurvive(world, pos)) {
			world.destroyBlock(pos, true);
		}
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
		return SpectrumItems.GERMINATED_JADE_VINE_BULB.getDefaultInstance();
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return BULB_SHAPE;
	}
	
	@Override
	public boolean canSurvive(@NotNull BlockState state, LevelReader world, BlockPos pos) {
		return world.getBlockState(pos.above()).getBlock() instanceof JadeVineRootsBlock;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(DEAD);
	}
	
	@Override
	public boolean setToAge(Level world, BlockPos blockPos, int age) {
		BlockState currentState = world.getBlockState(blockPos);
		boolean dead = currentState.getValue(DEAD);
		if (age == 0 && !dead) {
			world.setBlockAndUpdate(blockPos, currentState.setValue(DEAD, true));
			return true;
		} else if (age > 0 && dead) {
			world.setBlockAndUpdate(blockPos, currentState.setValue(DEAD, false));
			return true;
		}
		return false;
	}
	
	@Override
	public boolean canUseNaturesStaff(Level world, BlockPos pos, BlockState state) {
		return state.getValue(DEAD);
	}
	
	@Override
	public boolean onNaturesStaffUse(Level world, BlockPos pos, BlockState state, Player player) {
		BlockPos rootsPos = pos.above();
		BlockState rootsState = world.getBlockState(rootsPos);
		if (rootsState.getBlock() instanceof JadeVineRootsBlock jadeVineRootsBlock) {
			jadeVineRootsBlock.onNaturesStaffUse(world, rootsPos, rootsState, player);
		}
		JadeVine.spawnParticlesServer((ServerLevel) world, pos, 16);
		return false;
	}
	
}
