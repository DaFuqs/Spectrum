package de.dafuqs.spectrum.blocks.geology;

import de.dafuqs.spectrum.blocks.conditional.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
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
        if (world.isClient() && !entity.bypassesSteppingEffects() && world.random.nextInt(3) == 0 && entity instanceof PlayerEntity player && this.isVisibleTo(player)) {
            ParticleHelper.playParticleAroundBlockSides(world, SpectrumParticleTypes.AZURE_MOTE_SMALL, Vec3d.of(pos), new Direction[]{Direction.UP}, 1, Vec3d.ZERO);
        }
    }
    
    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        
        if (world.isClient() && this.isVisibleTo(player)) {
            ParticleHelper.playTriangulatedParticle(world, SpectrumParticleTypes.AZURE_AURA, 1, false, Vec3d.ZERO, 0, true, Vec3d.ofCenter(pos), new Vec3d(0, 0.08D + world.getRandom().nextDouble() * 0.04, 0));
            ParticleHelper.playParticleAroundBlockSides(world, SpectrumParticleTypes.AZURE_MOTE_SMALL, Vec3d.of(pos), Direction.values(), 3, Vec3d.ZERO);
        }
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        super.onBlockBreakStart(state, world, pos, player);
        
        if (world.isClient() && this.isVisibleTo(player)) {
            ParticleHelper.playParticleAroundBlockSides(world, SpectrumParticleTypes.AZURE_MOTE, Vec3d.of(pos), Direction.values(), 1, Vec3d.ZERO);
        }
    }
    
    @Environment(EnvType.CLIENT)
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        
        if (this.isVisibleTo(MinecraftClient.getInstance().player) && world.getRandom().nextFloat() < 0.02) {
            ParticleHelper.playTriangulatedParticle(world, SpectrumParticleTypes.AZURE_AURA, 8, false, new Vec3d(32, 8, 32), -12, true, Vec3d.of(pos), new Vec3d(0, 0.04D + random.nextDouble() * 0.06, 0));
            ParticleHelper.playTriangulatedParticle(world, SpectrumParticleTypes.AZURE_AURA, 12, true, new Vec3d(32, 8, 32), -12, true, Vec3d.of(pos), new Vec3d(0, 0.04D + random.nextDouble() * 0.06, 0));
            ParticleHelper.playTriangulatedParticle(world, SpectrumParticleTypes.AZURE_MOTE_SMALL, 19, false, new Vec3d(24, 8, 24), -8, false, Vec3d.of(pos), Vec3d.ZERO);
            ParticleHelper.playTriangulatedParticle(world, SpectrumParticleTypes.AZURE_MOTE, 17, true, new Vec3d(24, 6, 24), -6, false, Vec3d.of(pos), Vec3d.ZERO);
            ParticleHelper.playTriangulatedParticle(world, SpectrumParticleTypes.AZURE_AURA, 5, false, new Vec3d(2, 0, 2), 0, true, Vec3d.of(pos), new Vec3d(0, 0.07D + random.nextDouble() * 0.06, 0));
            ParticleHelper.playParticleAroundBlockSides(world, SpectrumParticleTypes.AZURE_MOTE, Vec3d.of(pos), Direction.values(), random.nextBetween(1, 3), Vec3d.ZERO);
            world.playSound(null, pos, SpectrumSoundEvents.SOFT_HUM, SoundCategory.BLOCKS, 1F, random.nextFloat() * 0.5F + 1F);
        }
    }
    
}
