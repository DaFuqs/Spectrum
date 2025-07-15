package de.dafuqs.spectrum.blocks.deeper_down.flora;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class WeepingGalaSprigBlock extends SaplingBlock {
	
	public static final MapCodec<WeepingGalaSprigBlock> CODEC = simpleCodec(WeepingGalaSprigBlock::new);
	
	public WeepingGalaSprigBlock(BlockBehaviour.Properties settings) {
		super(SpectrumSaplingGenerators.WEEPING_GALA_SAPLING_GENERATOR, settings);
	}
	
	@Override
	public MapCodec<? extends WeepingGalaSprigBlock> codec() {
		return CODEC;
	}
	
	@Override
	protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
		return floor.is(SpectrumBlockTags.ASH) || floor.is(SpectrumBlocks.ASHEN_BLACKSLAG) || super.mayPlaceOn(floor, world, pos);
	}
	
}
