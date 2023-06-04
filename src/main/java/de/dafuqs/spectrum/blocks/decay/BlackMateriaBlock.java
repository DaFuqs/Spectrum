package de.dafuqs.spectrum.blocks.decay;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

public class BlackMateriaBlock extends FallingBlock {
	
	public static final int PROPAGATION_TRIES = 3;

	public static final int MAX_AGE = Properties.AGE_3_MAX;
	public static final IntProperty AGE = Properties.AGE_3;
	
	public BlackMateriaBlock(Settings settings) {
		super(settings);
		setDefaultState(this.stateManager.getDefaultState().with(Properties.AGE_3, 3));
	}
	
	public static boolean spreadBlackMateria(World world, BlockPos pos, Random random, BlockState targetState) {
		boolean replacedAny = false;
		for (int i = 0; i < PROPAGATION_TRIES; i++) {
			Direction randomDirection = Direction.random(random);
			BlockPos neighborPos = pos.offset(randomDirection);
			BlockState neighborBlockState = world.getBlockState(neighborPos);
			if (!(neighborBlockState.getBlock() instanceof BlackMateriaBlock) && neighborBlockState.isIn(SpectrumBlockTags.BLACK_MATERIA_CONVERSIONS)) {
				world.setBlockState(neighborPos, targetState);
				world.playSound(null, neighborPos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1.0F, 0.9F + random.nextFloat() * 0.2F);
				replacedAny = true;
			}
		}
		return replacedAny;
	}
	
	@Override
	public boolean hasRandomTicks(BlockState state) {
		return state.get(AGE) != MAX_AGE;
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int age = state.get(AGE);
		if (age < MAX_AGE) {
			BlockState targetState = state.with(AGE, age + 1);
			spreadBlackMateria(world, pos, random, targetState);
			world.setBlockState(pos, targetState);
		}
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}
	
	@Override
    public void onDestroyedOnLanding(World world, BlockPos pos, FallingBlockEntity fallingBlockEntity) {
		// To prevent infinite materia, we eat any non-solid blocks that we land on. Since the block item
		// is going to be dropped regardless, we just replace the non-solid block with air.
		world.setBlockState(pos, Blocks.AIR.getDefaultState());
    }
}
