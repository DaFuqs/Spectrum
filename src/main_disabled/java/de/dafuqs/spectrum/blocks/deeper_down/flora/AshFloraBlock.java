package de.dafuqs.spectrum.blocks.deeper_down.flora;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

public class AshFloraBlock extends SpreadableFloraBlock {
	
	public static final MapCodec<AshFloraBlock> CODEC = simpleCodec(AshFloraBlock::new);
	
	public AshFloraBlock(Properties settings) {
		super(7, settings);
	}

//	@Override
//	public MapCodec<? extends AshFloraBlock> getCodec() {
//		//TODO: Make the codec
//		return CODEC;
//	}

	@Override
	protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
		return (floor.is(SpectrumBlockTags.ASH) || floor.is(SpectrumBlocks.ASHEN_BLACKSLAG) || super.mayPlaceOn(floor, world, pos))
				&& floor.isFaceSturdy(world, pos, Direction.UP);
	}
}
