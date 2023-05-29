package de.dafuqs.spectrum.blocks.farming;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class TilledShaleClayBlock extends ImmutableFarmlandBlock{
    public TilledShaleClayBlock(Settings settings, BlockState bareState) {
        super(settings, bareState);
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.handleFallDamage(fallDistance, 2.0F, world.getDamageSources().fall());
    }
}
