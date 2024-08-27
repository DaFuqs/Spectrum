package de.dafuqs.spectrum.blocks;

import de.dafuqs.spectrum.blocks.conditional.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.world.*;
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
        if (!world.isClient() && !entity.bypassesSteppingEffects() && random.nextInt(3) == 0) {
            SpectrumS2CPacketSender.playParticleAroundBlockSides((ServerWorld) world, 1, Vec3d.of(pos), new Vec3d(0, 0.05, 0), SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, this::isVisibleTo, Direction.UP);
            if (random.nextInt(3) == 0) {
                SpectrumS2CPacketSender.playParticleAroundBlockSides((ServerWorld) world, 1, Vec3d.of(pos), new Vec3d(0, 0.025, 0), SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, this::isVisibleTo, Direction.values());

            }
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!world.isClient()) {
            var random = world.getRandom();
            if (random.nextBoolean()) {
                var amount = (int) Math.ceil(MathHelper.clamp(fallDistance / 2, 1, 10));
                SpectrumS2CPacketSender.playParticleAroundBlockSides((ServerWorld) world, amount, Vec3d.of(pos), new Vec3d(0, 0.05 + amount / 30.0, 0), SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, this::isVisibleTo, Direction.UP);
            }
        }
        super.onLandedUpon(world, state, pos, entity, fallDistance);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        SpectrumS2CPacketSender.playParticleAroundBlockSides(world, 1, Vec3d.of(pos), new Vec3d(0, 0.025, 0), SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, player -> {
            if (!isVisibleTo(player))
                return false;

            return player.getBlockPos().isWithinDistance(pos, 7);
            }, Direction.values());
        super.randomTick(state, world, pos, random);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient()) {
            SpectrumS2CPacketSender.playParticleAroundBlockSides((ServerWorld) world, 3, Vec3d.of(pos), new Vec3d(0, 0.05, 0), SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, this::isVisibleTo, Direction.values());

        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        super.onBlockBreakStart(state, world, pos, player);
        if (!world.isClient()) {
            SpectrumS2CPacketSender.playParticleAroundBlockSides((ServerWorld) world, 1, Vec3d.of(pos), new Vec3d(0, 0.01, 0), SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, this::isVisibleTo, Direction.values());
        }
    }
}
