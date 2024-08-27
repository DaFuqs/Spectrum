package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.blocks.conditional.*;
import de.dafuqs.spectrum.particle.*;
import net.minecraft.block.*;
import net.minecraft.client.world.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

public class ShimmerstoneOreBlock extends CloakedOreBlock {

    public ShimmerstoneOreBlock(Settings settings, UniformIntProvider uniformIntProvider, Identifier cloakAdvancementIdentifier, BlockState cloakBlockState) {
        super(settings, uniformIntProvider, cloakAdvancementIdentifier, cloakBlockState);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        var random = world.getRandom();
        if (!entity.bypassesSteppingEffects() && isVisibleTo(ShapeContext.of(entity)) && random.nextInt(3) == 0) {
            spawnParticlesOnSide(state, world, pos, random, Direction.UP);
        }

        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        var random = world.getRandom();
        if (isVisibleTo(ShapeContext.of(entity)) && random.nextBoolean()) {
            var amount = Math.ceil(MathHelper.clamp(fallDistance / 2, 1, 10));
            for (int i = 0; i < amount; i++) {
                spawnParticlesOnSide(state, world, pos, random, Direction.UP);
            }
        }
        super.onLandedUpon(world, state, pos, entity, fallDistance);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        var random = world.getRandom();
        if (isVisibleTo(player) && random.nextBoolean()) {
            for (int i = 0; i < 3; i++) {
                for (Direction direction : Direction.values()) {
                    spawnParticlesOnSide(state, world, pos, random, direction);
                }
            }
        }
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        var random = world.getRandom();
        if (isVisibleTo(player) && random.nextBoolean()) {
            for (Direction direction : Direction.values()) {
                spawnParticlesOnSide(state, world, pos, random, direction);
            }
        }
        super.onBlockBreakStart(state, world, pos, player);
    }

    private static void spawnParticlesOnSide(BlockState state, World world, BlockPos pos, Random random, Direction direction) {
        if (direction != Direction.DOWN) {
            BlockPos blockPos = pos.offset(direction);
            BlockState blockState = world.getBlockState(blockPos);
            if (!state.isOpaque() || !blockState.isSideSolidFullSquare(world, blockPos, direction.getOpposite())) {
                double d = direction.getOffsetX() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetX() * 0.6D;
                double e = direction.getOffsetY() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetY() * 0.6D;
                double f = direction.getOffsetZ() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetZ() * 0.6D;
                world.addParticle(SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, (double) pos.getX() + d, (double) pos.getY() + e, (double) pos.getZ() + f, 0.0D, 0.05D, 0.0D);
            }
        }
    }
}
