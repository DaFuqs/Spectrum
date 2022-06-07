package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

public class BlackMateriaBlock extends FallingBlock {
	
	public static final int PROPAGATION_TRIES = 3;
	
	public static final int MAX_AGE = 3;
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
		return state.get(AGE) != Properties.AGE_3_MAX;
	}
	
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int age = state.get(AGE);
		if (age < MAX_AGE) {
			BlockState targetState = state.with(AGE, age + 1);
			spreadBlackMateria(world, pos, random, targetState);
			world.setBlockState(pos, targetState);
		}
	}
	
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}
	
}
