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

public class NephriteBlossomBulbBlock extends BushBlock implements BonemealableBlock {
	
	public static final MapCodec<NephriteBlossomBulbBlock> CODEC = simpleCodec(NephriteBlossomBulbBlock::new);
	
	public NephriteBlossomBulbBlock(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState());
	}
	
	@Override
	public MapCodec<? extends NephriteBlossomBulbBlock> codec() {
		return CODEC;
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
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		world.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).get(SpectrumConfiguredFeatures.NEPHRITE_BLOSSOM_BULB).place(world, world.getChunkSource().getGenerator(), random, pos);
	}
}
