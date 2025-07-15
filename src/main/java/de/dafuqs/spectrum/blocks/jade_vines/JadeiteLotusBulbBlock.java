package de.dafuqs.spectrum.blocks.jade_vines;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class JadeiteLotusBulbBlock extends BushBlock implements BonemealableBlock {
	
	public static final MapCodec<JadeiteLotusBulbBlock> CODEC = simpleCodec(JadeiteLotusBulbBlock::new);
	
	public JadeiteLotusBulbBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends JadeiteLotusBulbBlock> codec() {
		return CODEC;
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return mayPlaceOn(world.getBlockState(pos.above()), world, pos.above());
	}
	
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return true;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (random.nextFloat() < 0.025) {
			performBonemeal(world, random, pos, state);
		}
	}
	
	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		return true;
	}
	
	@Override
	public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
		return super.mayPlaceOn(floor, world, pos) || floor.is(SpectrumBlockTags.BASE_STONE_DEEPER_DOWN);
	}
	
	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		world.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).get(SpectrumConfiguredFeatures.JADEITE_LOTUS).place(world, world.getChunkSource().getGenerator(), random, pos);
	}
	
}
