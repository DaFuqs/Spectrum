package de.dafuqs.spectrum.blocks.farming;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
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
			setBare(state, world, pos);
		}
	}
	
	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (!world.isClient && world.random.nextFloat() < fallDistance - 1F
				&& entity instanceof LivingEntity
				&& (entity instanceof PlayerEntity || world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING))
				&& entity.getWidth() * entity.getWidth() * entity.getHeight() > 0.512F) {
			
			setBare(state, world, pos);
		}
		
		entity.handleFallDamage(fallDistance, 1.0F, DamageSource.FALL);
	}
	
	public void setBare(BlockState state, World world, BlockPos pos) {
		world.setBlockState(pos, pushEntitiesUpBeforeBlockChange(state, bareState, world, pos));
	}
	
	public static boolean hasCrop(@NotNull BlockView world, @NotNull BlockPos pos) {
		Block block = world.getBlockState(pos.up()).getBlock();
		return block instanceof CropBlock || block instanceof StemBlock || block instanceof AttachedStemBlock;
	}
	
}
