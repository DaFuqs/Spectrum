package de.dafuqs.spectrum.blocks.farming;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

public class ImmutableFarmlandBlock extends FarmlandBlock {

    protected final BlockState bareState;

    public ImmutableFarmlandBlock(Settings settings, BlockState bareState) {
        super(settings);
        this.bareState = bareState;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.canPlaceAt(world, pos)) {
            setBare(state, world, pos);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {}

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return false;
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!world.isClient && world.random.nextFloat() < fallDistance - 1F && entity instanceof LivingEntity && (entity instanceof PlayerEntity || world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) && entity.getWidth() * entity.getWidth() * entity.getHeight() > 0.512F) {
            setBare(state, world, pos);
        }

        entity.handleFallDamage(fallDistance, 1.0F, world.getDamageSources().fall());
    }

    public void setBare(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, pushEntitiesUpBeforeBlockChange(state, bareState, world, pos));
    }
}
