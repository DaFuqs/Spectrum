package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class RadiantGlassBlock extends TransparentBlock {
	
	public static final MapCodec<RadiantGlassBlock> CODEC = simpleCodec(RadiantGlassBlock::new);
	
	public RadiantGlassBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends RadiantGlassBlock> codec() {
		return CODEC;
	}
	
	@Override
	public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
		if (stateFrom.is(this) || stateFrom.is(SpectrumBlocks.RADIANT_SEMI_PERMEABLE_GLASS)) {
			return true;
		}
		return super.skipRendering(state, stateFrom, direction);
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
		return true;
	}
	
}
