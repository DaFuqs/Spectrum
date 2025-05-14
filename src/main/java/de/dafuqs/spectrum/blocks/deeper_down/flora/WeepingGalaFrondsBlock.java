package de.dafuqs.spectrum.blocks.deeper_down.flora;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class WeepingGalaFrondsBlock extends BushBlock {
	
	public static final MapCodec<WeepingGalaFrondsBlock> CODEC = simpleCodec(WeepingGalaFrondsBlock::new);
	
	public WeepingGalaFrondsBlock(Properties settings) {
        super(settings);
    }

    @Override
	public MapCodec<? extends WeepingGalaFrondsBlock> codec() {
        return CODEC;
    }

    @Override
	public float getMaxHorizontalOffset() {
        return 0.1F;
    }

    @Override
	public boolean mayPlaceOn(BlockState roof, BlockGetter world, BlockPos pos) {
		return roof.is(SpectrumBlocks.WEEPING_GALA_LEAVES) || roof.is(SpectrumBlocks.WEEPING_GALA_FRONDS);
    }

    @Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		var roof = pos.above();
        var roofState = world.getBlockState(roof);
		
		if (roofState.is(this))
            return true;
		
		return mayPlaceOn(roofState, world, roof);
    }
}
