package de.dafuqs.spectrum.blocks.particle_spawner;

import de.dafuqs.spectrum.blocks.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ParticleSpawnerBlock extends AbstractParticleSpawnerBlock implements RedstonePoweredBlock {
	
	public static final BooleanProperty POWERED = BooleanProperty.of("powered");
	protected static final VoxelShape SHAPE = Block.createCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 13.0D, 15.0D);
	
	public ParticleSpawnerBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(POWERED, false));
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.particle_spawner.tooltip").formatted(Formatting.GRAY));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(POWERED);
	}
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if (!world.isClient) {
			if (this.checkGettingPowered(world, pos)) {
				this.power(world, pos);
			} else {
				this.unPower(world, pos);
			}
		}
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState placementState = this.getDefaultState();
		if (ctx.getWorld().getReceivedRedstonePower(ctx.getBlockPos()) > 0) {
			placementState = placementState.with(POWERED, true);
		}
		return placementState;
	}
	
	@Override
	public boolean shouldSpawnParticles(World world, BlockPos pos) {
		return world.getBlockState(pos).get(ParticleSpawnerBlock.POWERED).equals(true);
	}
	
}
