package de.dafuqs.spectrum.blocks.deeper_down.flora;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

public class NightdewBlock extends TriStateVineBlock {
	
	public static final MapCodec<NightdewBlock> CODEC = simpleCodec(NightdewBlock::new);
	
	public NightdewBlock(Properties settings) {
        super(settings, 6, 1F, 0.3F, 0.85F);
    }

    @Override
	public MapCodec<? extends NightdewBlock> codec() {
        return CODEC;
    }

    @Override
	public boolean mayPlaceOn(BlockState roof, BlockGetter world, BlockPos pos) {
		return super.mayPlaceOn(roof, world, pos) && roof.is(SpectrumBlockTags.NIGHTDEW_SOILS);
    }

    @Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
		return SpectrumItems.NIGHTDEW_SPROUT.getDefaultInstance();
    }

    @Override
    boolean hasGrowthActions() {
        return false;
    }
	
}
