package de.dafuqs.spectrum.blocks.farming;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TilledShaleClayBlock extends ImmutableFarmlandBlock{
    public TilledShaleClayBlock(Settings settings, BlockState bareState) {
        super(settings, bareState);
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.handleFallDamage(fallDistance, 2.0F, DamageSource.FALL);
    }
}
