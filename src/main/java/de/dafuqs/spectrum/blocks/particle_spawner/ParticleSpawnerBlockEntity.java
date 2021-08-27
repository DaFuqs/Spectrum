package de.dafuqs.spectrum.blocks.particle_spawner;

import de.dafuqs.spectrum.blocks.chests.SpectrumChestBlockEntity;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlock;
import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class ParticleSpawnerBlockEntity extends BlockEntity {

    ParticleEffect particleEffect;
    float particlesPerTick; // >1 = every xth tick
    double sourcePositionOffsetX;
    double sourcePositionOffsetY;
    double sourcePositionOffsetZ;
    double randomPositionOffsetX;
    double randomPositionOffsetY;
    double randomPositionOffsetZ;
    double sourceVelocityX;
    double sourceVelocityY;
    double sourceVelocityZ;
    double randomVelocityX;
    double randomVelocityY;
    double randomVelocityZ;

    public ParticleSpawnerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpectrumBlockEntityRegistry.PARTICLE_SPAWNER, blockPos, blockState);

        List<ParticleEffect> availableParticleEffects = new ArrayList<>();
        availableParticleEffects.add(ParticleTypes.FLAME);
        availableParticleEffects.add(ParticleTypes.BUBBLE);

        this.particleEffect = ParticleTypes.FLAME;
        this.particlesPerTick = 1;
        this.sourcePositionOffsetX = 0;
        this.sourcePositionOffsetY = 3;
        this.sourcePositionOffsetZ = 0;
        this.randomPositionOffsetX = 1;
        this.randomPositionOffsetY = 0;
        this.randomPositionOffsetZ = 1;
        this.sourceVelocityX = 0;
        this.sourceVelocityY = 0.1;
        this.sourceVelocityZ = 0;
        this.randomVelocityX = 0.2;
        this.randomVelocityY = 0.1;
        this.randomVelocityZ = 0;
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, ParticleSpawnerBlockEntity blockEntity) {
        blockEntity.spawnParticles();
    }

    private void spawnParticles() {
        if(world.getBlockState(pos).get(ParticleSpawnerBlock.STATE).equals(PedestalBlock.RedstonePowerState.POWERED)) {
            float particlesToSpawn = particlesPerTick;
            while (particlesToSpawn > 1 || world.random.nextFloat() < particlesToSpawn) {
                spawnParticle(world, pos, world.random);
                particlesToSpawn--;
            }
        }
    }

    private void spawnParticle(World world, BlockPos pos, Random random) {
        double randomOffsetX = randomPositionOffsetX == 0 ? 0 : randomPositionOffsetX - random.nextDouble() * randomPositionOffsetX * 2.0D;
        double randomOffsetY = randomPositionOffsetY == 0 ? 0 : randomPositionOffsetY - random.nextDouble() * randomPositionOffsetY * 2.0D;
        double randomOffsetZ = randomPositionOffsetZ == 0 ? 0 : randomPositionOffsetZ - random.nextDouble() * randomPositionOffsetZ * 2.0D;

        double randomVelocityX = this.randomVelocityX == 0 ? 0 : this.randomVelocityX * random.nextDouble();
        double randomVelocityY = this.randomVelocityY == 0 ? 0 : this.randomVelocityY * random.nextDouble();
        double randomVelocityZ = this.randomVelocityZ == 0 ? 0 : this.randomVelocityZ * random.nextDouble();

        world.addParticle(particleEffect,
                (double) pos.getX() + sourcePositionOffsetX + randomOffsetX,
                (double) pos.getY() + sourcePositionOffsetY + randomOffsetY,
                (double) pos.getZ() + sourcePositionOffsetZ + randomOffsetZ,
                sourceVelocityX + randomVelocityX,
                sourceVelocityY + randomVelocityY,
                sourceVelocityZ + randomVelocityZ);
    }


    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        return tag;
    }

    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

    }

}
