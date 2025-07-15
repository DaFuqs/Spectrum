package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class TransientLightBlock extends PersistentLightBlock {
	
	public static final MapCodec<TransientLightBlock> CODEC = simpleCodec(TransientLightBlock::new);
	
	public TransientLightBlock(Properties settings) {
		super(settings);
	}

//	@Override
//	public MapCodec<? extends DecayingLightBlock> getCodec() {
//		//TODO: Make the codec
//		return CODEC;
//	}
	
	@Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		super.randomTick(state, world, pos, random);
		int light = state.getValue(LightBlock.LEVEL);
		if (light < 2) {
			if (state.getValue(WATERLOGGED)) {
				world.setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
			} else {
				world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
			}
		} else {
			world.setBlock(pos, state.setValue(LightBlock.LEVEL, light - 1), 3);
		}
	}
	
}
