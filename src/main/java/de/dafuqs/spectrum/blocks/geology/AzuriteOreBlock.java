package de.dafuqs.spectrum.blocks.geology;

import de.dafuqs.spectrum.blocks.conditional.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

public class AzuriteOreBlock extends CloakedOreBlock {

    public AzuriteOreBlock(Settings settings, UniformIntProvider uniformIntProvider, Identifier cloakAdvancementIdentifier, BlockState cloakBlockState) {
        super(settings, uniformIntProvider, cloakAdvancementIdentifier, cloakBlockState);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        super.onSteppedOn(world, pos, state, entity);
        if (!world.isClient() && !entity.bypassesSteppingEffects() && world.random.nextInt(3) == 0) {
            SpectrumS2CPacketSender.playParticleAroundBlockSides((ServerWorld) world, 1, Vec3d.of(pos), Vec3d.ZERO, SpectrumParticleTypes.AZURE_MOTE_SMALL, this::isVisibleTo, Direction.UP);
        }
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        if (!world.isClient()) {
            SpectrumS2CPacketSender.playParticleAroundArea((ServerWorld) world, 1, 0, false, true, Vec3d.ZERO, Vec3d.ofCenter(pos), new Vec3d(0, 0.08D + world.getRandom().nextDouble() * 0.04, 0), SpectrumParticleTypes.AZURE_AURA, this::isVisibleTo);
            SpectrumS2CPacketSender.playParticleAroundBlockSides((ServerWorld) world, 3, Vec3d.of(pos), Vec3d.ZERO, SpectrumParticleTypes.AZURE_MOTE_SMALL, this::isVisibleTo, Direction.values());
        }
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        super.onBlockBreakStart(state, world, pos, player);
        if (!world.isClient()) {
            SpectrumS2CPacketSender.playParticleAroundBlockSides((ServerWorld) world, 1, Vec3d.of(pos), Vec3d.ZERO, SpectrumParticleTypes.AZURE_MOTE, this::isVisibleTo, Direction.values());
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        SpectrumS2CPacketSender.playParticleAroundArea(world, 19, -12, true, true, new Vec3d(32, 8, 32), Vec3d.of(pos), new Vec3d(0, 0.04D + random.nextDouble() * 0.06, 0), SpectrumParticleTypes.AZURE_AURA, this::isVisibleTo);
        SpectrumS2CPacketSender.playParticleAroundArea(world, 25, -8, false, false, new Vec3d(24, 8,24), Vec3d.of(pos), Vec3d.ZERO, SpectrumParticleTypes.AZURE_MOTE_SMALL, this::isVisibleTo);
        SpectrumS2CPacketSender.playParticleAroundArea(world, 18, -6, false, false, new Vec3d(12, 6,12), Vec3d.of(pos), Vec3d.ZERO, SpectrumParticleTypes.AZURE_MOTE, this::isVisibleTo);
        SpectrumS2CPacketSender.playParticleAroundArea(world, 5, 0, false, true, new Vec3d(2, 0, 2), Vec3d.of(pos), new Vec3d(0, 0.07D + random.nextDouble() * 0.06, 0), SpectrumParticleTypes.AZURE_AURA, this::isVisibleTo);
        SpectrumS2CPacketSender.playParticleAroundBlockSides(world, random.nextBetween(1, 3), Vec3d.of(pos), Vec3d.ZERO, SpectrumParticleTypes.AZURE_MOTE, this::isVisibleTo, Direction.values());
        world.playSound(null, pos, SpectrumSoundEvents.SOFT_HUM, SoundCategory.BLOCKS, 1F, random.nextFloat() * 0.5F + 1F);
    }
}
