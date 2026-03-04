package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

public class CloverBlock extends BushBlock implements BonemealableBlock {
	
	public static final MapCodec<CloverBlock> CODEC = simpleCodec(CloverBlock::new);
	
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D);
	
	public CloverBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends CloverBlock> codec() {
		return CODEC;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE;
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
		world.registryAccess()
				.registryOrThrow(Registries.CONFIGURED_FEATURE)
				.get(SpectrumConfiguredFeatureKeys.CLOVER_PATCH)
				.place(world, world.getChunkSource().getGenerator(), random, pos);
	}
	
	@Override
	public int getFireSpreadSpeed(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction direction) {
		return 60;
	}
	
	@Override
	public int getFlammability(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction direction) {
		return 100;
	}
	
}
