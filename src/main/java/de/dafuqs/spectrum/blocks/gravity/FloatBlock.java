package de.dafuqs.spectrum.blocks.gravity;

import de.dafuqs.spectrum.entity.entity.FloatBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class FloatBlock extends FallingBlock {
	
	private final float gravityMod;
	
	public FloatBlock(Settings settings, float gravityMod) {
		super(settings);
		this.gravityMod = gravityMod;
	}
	
	public float getGravityMod() {
		return gravityMod;
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos blockPos, BlockState oldState, boolean notify) {
		world.createAndScheduleBlockTick(blockPos, this, this.getFallDelay());
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState facingState, WorldAccess world, BlockPos blockPos, BlockPos facingPos) {
		world.createAndScheduleBlockTick(blockPos, this, this.getFallDelay());
		return super.getStateForNeighborUpdate(state, direction, facingState, world, blockPos, facingPos);
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		this.checkForLaunch(world, pos);
	}
	
	private void checkForLaunch(World world, BlockPos pos) {
		if (!world.isClient) {
			BlockPos collisionBlockPos;
			if (gravityMod > 0) {
				collisionBlockPos = pos.up();
			} else {
				collisionBlockPos = pos.down();
			}
			
			if ((world.isAir(collisionBlockPos) || canFallThrough(world.getBlockState(collisionBlockPos)))) {
				FloatBlockEntity blockEntity = new FloatBlockEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world.getBlockState(pos));
				world.spawnEntity(blockEntity);
			}
		}
	}
	
	@Override
	protected int getFallDelay() {
		return 2;
	}
	
}