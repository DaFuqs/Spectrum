package de.dafuqs.pigment.blocks.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleEmitterBlock extends Block {

    ParticleEffect particleEffect;
    int randomEveryXTicks;
    Vector3d sourcePositionOffset;
    Vector3d randomPositionOffset;
    Vector3d sourceVelocity;
    Vector3d randomVelocity;

    public ParticleEmitterBlock(FabricBlockSettings of) {
        super(of);
        List<ParticleEffect> availableParticleEffects = new ArrayList<>();
        availableParticleEffects.add(ParticleTypes.FLAME);
        availableParticleEffects.add(ParticleTypes.BUBBLE);

        this.particleEffect = ParticleTypes.FLAME;
        this.randomEveryXTicks = 1;
        this.sourcePositionOffset = new Vector3d(0, 3, 0);
        this.randomPositionOffset = new Vector3d(1, 0, 1);
        this.sourceVelocity = new Vector3d(0, 0.1, 0);
        this.randomVelocity = new Vector3d(0.3, 0.1, 0);
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(this.randomEveryXTicks) == 0) {
            double randomOffsetX = randomPositionOffset.x == 0 ? 0 : randomPositionOffset.x - random.nextDouble() * randomPositionOffset.x * 2.0D;
            double randomOffsetY = randomPositionOffset.y == 0 ? 0 : randomPositionOffset.y - random.nextDouble() * randomPositionOffset.y * 2.0D;
            double randomOffsetZ = randomPositionOffset.z == 0 ? 0 : randomPositionOffset.z - random.nextDouble() * randomPositionOffset.z * 2.0D;

            double randomVelocityX = randomVelocity.x == 0 ? 0 : randomVelocity.x * random.nextDouble();
            double randomVelocityY = randomVelocity.y == 0 ? 0 : randomVelocity.y * random.nextDouble();
            double randomVelocityZ = randomVelocity.z == 0 ? 0 : randomVelocity.z * random.nextDouble();
            
            world.addParticle(particleEffect, 
                    (double) pos.getX() + sourcePositionOffset.x + randomOffsetX,
                    (double) pos.getY() + sourcePositionOffset.y + randomOffsetY,
                    (double) pos.getZ() + sourcePositionOffset.z + randomOffsetZ,
                    sourceVelocity.x + randomVelocityX,
                    sourceVelocity.y + randomVelocityY,
                    sourceVelocity.z + randomVelocityZ);
        }
    }


}
