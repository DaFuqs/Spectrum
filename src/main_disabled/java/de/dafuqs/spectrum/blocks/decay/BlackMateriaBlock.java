package de.dafuqs.spectrum.blocks.decay;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;

public class BlackMateriaBlock extends FallingBlock {
	
	public static final MapCodec<BlackMateriaBlock> CODEC = simpleCodec(BlackMateriaBlock::new);

	public static final int PROPAGATION_TRIES = 3;
	
	public static final int MAX_AGE = BlockStateProperties.MAX_AGE_3;
	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
	
	public BlackMateriaBlock(Properties settings) {
		super(settings);
		registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.AGE_3, BlockStateProperties.MAX_AGE_3));
	}

	@Override
	protected MapCodec<? extends BlackMateriaBlock> codec() {
		return CODEC;
	}

    @Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (direction == Direction.DOWN) {
			super.updateShape(state, direction, neighborState, world, pos, neighborPos);
		}
		return state;
	}
	
	public static boolean spreadBlackMateria(Level world, BlockPos pos, RandomSource random, BlockState targetState) {
		boolean replacedAny = false;
		for (int i = 0; i < PROPAGATION_TRIES; i++) {
			Direction randomDirection = Direction.getRandom(random);
			BlockPos neighborPos = pos.relative(randomDirection);
			BlockState neighborBlockState = world.getBlockState(neighborPos);
			if (!(neighborBlockState.getBlock() instanceof BlackMateriaBlock) && neighborBlockState.is(SpectrumBlockTags.BLACK_MATERIA_CONVERSIONS)) {
				world.setBlockAndUpdate(neighborPos, targetState);
				world.playSound(null, neighborPos, SoundEvents.GRAVEL_PLACE, SoundSource.BLOCKS, 1.0F, 0.9F + random.nextFloat() * 0.2F);
				replacedAny = true;
			}
		}
		return replacedAny;
	}

	@Override
	protected int getDelayAfterPlace() {
		return 20;
	}
	
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(AGE) != MAX_AGE;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		int age = state.getValue(AGE);
		if (age < MAX_AGE) {
			BlockState targetState = state.setValue(AGE, age + 1);
			spreadBlackMateria(world, pos, random, targetState);
			world.setBlockAndUpdate(pos, targetState);
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}
	
	@Override
	public void onBrokenAfterFall(Level world, BlockPos pos, FallingBlockEntity fallingBlockEntity) {
		BlockState state = world.getBlockState(pos);
		if (!state.isSolid() && state.getPistonPushReaction() == PushReaction.DESTROY) {
			world.destroyBlock(pos, true, fallingBlockEntity, 4);
		}
		super.onBrokenAfterFall(world, pos, fallingBlockEntity);
	}
	
}
