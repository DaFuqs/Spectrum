package de.dafuqs.spectrum.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleEmitterBlock extends Block {

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

    public ParticleEmitterBlock(FabricBlockSettings of) {
        super(of);
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

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        /*if (random.nextInt(this.randomEveryXTicks) == 0) {
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
        }*/
    }


}
