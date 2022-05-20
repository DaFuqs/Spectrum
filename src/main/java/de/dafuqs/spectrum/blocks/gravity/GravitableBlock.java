package de.dafuqs.spectrum.blocks.gravity;

import de.dafuqs.spectrum.entity.entity.GravityBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

public class GravitableBlock extends FallingBlock {

	private final float gravityMod;

	public GravitableBlock(Settings settings, float gravityMod) {
		super(settings);
		this.gravityMod = gravityMod;
	}

	public float getGravityMod() {
		return gravityMod;
	}

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos posIn, BlockState oldState, boolean notify) {
		worldIn.createAndScheduleBlockTick(posIn, this, this.getFallDelay());
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facingIn, BlockState facingState, WorldAccess worldIn, BlockPos posIn, BlockPos facingPosIn) {
		worldIn.createAndScheduleBlockTick(posIn, this, this.getFallDelay());
		return super.getStateForNeighborUpdate(stateIn, facingIn, facingState, worldIn, posIn, facingPosIn);
	}

	@Override
	public void scheduledTick(BlockState stateIn, ServerWorld worldIn, BlockPos posIn, Random randIn) {
		this.checkGravitable(worldIn, posIn);
	}

	private void checkGravitable(World world, BlockPos pos) {
		if (!world.isClient) {
			BlockPos collisionBlockPos;
			if (gravityMod > 0) {
				collisionBlockPos = pos.up();
			} else {
				collisionBlockPos = pos.down();
			}

			if ((world.isAir(collisionBlockPos) || canFallThrough(world.getBlockState(collisionBlockPos)))) {
				GravityBlockEntity blockEntity = new GravityBlockEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, world.getBlockState(pos));
				world.spawnEntity(blockEntity);
			}
		}
	}

	@Override
	protected int getFallDelay() {
		return 2;
	}

}