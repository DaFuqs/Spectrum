package de.dafuqs.spectrum.blocks.farming;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import net.minecraft.world.event.*;
import org.jetbrains.annotations.*;

public class SpectrumFarmlandBlock extends FarmlandBlock {
	
	protected final BlockState bareState;
	
	public SpectrumFarmlandBlock(Settings settings, BlockState bareState) {
		super(settings);
		this.bareState = bareState;
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!state.canPlaceAt(world, pos)) {
			setBare(null, state, world, pos);
		}
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int i = state.get(MOISTURE);
		if (!isWaterNearby(world, pos) && !world.hasRain(pos.up())) {
			if (i > 0) {
				world.setBlockState(pos, state.with(MOISTURE, i - 1), 2);
			} else if (!hasCrop(world, pos)) {
				setBare(null, state, world, pos);
			}
		} else if (i < 7) {
			world.setBlockState(pos, state.with(MOISTURE, 7), 2);
		}
	}
	
	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (!world.isClient && world.random.nextFloat() < fallDistance - 1F
				&& entity instanceof LivingEntity
				&& (entity instanceof PlayerEntity || world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING))
				&& entity.getWidth() * entity.getWidth() * entity.getHeight() > 0.512F) {
			
			setBare(entity, state, world, pos);
		}
		
		entity.handleFallDamage(fallDistance, 1.0F, world.getDamageSources().fall());
	}
	
	public void setBare(@Nullable Entity entity, BlockState state, World world, BlockPos pos) {
		BlockState blockState = pushEntitiesUpBeforeBlockChange(state, bareState, world, pos);
		world.setBlockState(pos, blockState);
		world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(entity, blockState));
	}
	
	public static boolean hasCrop(@NotNull BlockView world, @NotNull BlockPos pos) {
		Block block = world.getBlockState(pos.up()).getBlock();
		return block instanceof CropBlock || block instanceof StemBlock || block instanceof AttachedStemBlock;
	}

	protected boolean isWaterNearby(WorldView world, BlockPos pos) {
		for (BlockPos testPos : BlockPos.iterate(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
			if (world.getFluidState(testPos).isIn(FluidTags.WATER)) {
				return true;
			}
		}
		return false;
	}

}
