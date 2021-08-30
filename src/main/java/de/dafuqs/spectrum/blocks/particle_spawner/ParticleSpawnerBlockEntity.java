package de.dafuqs.spectrum.blocks.particle_spawner;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.particle.effect.ParticleSpawnerParticleEffect;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ParticleSpawnerBlockEntity extends BlockEntity {

    DefaultParticleType particleEffect;
    SpriteProvider spriteProvider;
    float particlesPerTick; // >1 = every xth tick
    Vec3f position;
    Vec3f positionVariance;
    Vec3f velocity;
    Vec3f velocityVariance;
    Vec3f color;
    float scale;
    float scaleVariance;
    int lifetimeTicks;
    int lifetimeVariance;
    float gravity;
    boolean collisions;

    public ParticleSpawnerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpectrumBlockEntityRegistry.PARTICLE_SPAWNER, blockPos, blockState);

        List<DefaultParticleType> availableParticleEffects = new ArrayList<>();
        availableParticleEffects.add(ParticleTypes.FLAME);
        availableParticleEffects.add(ParticleTypes.BUBBLE);

        this.particleEffect = ParticleTypes.FLAME;
        this.particlesPerTick = 1;
        this.position = new Vec3f(0, 3, 0);
        this.positionVariance = new Vec3f(1, 0, 1);
        this.velocity = new Vec3f(0, 0.1F, 0);
        this.velocityVariance = new Vec3f(0.2F, 0.1F, 0);
        this.color = new Vec3f(1.0F, 1.0F, 1.0F);
        this.scale = 1.0F;
        this.scaleVariance = 0;
        this.lifetimeTicks = 20;
        this.lifetimeVariance = 10;
        this.gravity = 0.0F;
        this.collisions = true;
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, ParticleSpawnerBlockEntity blockEntity) {
        blockEntity.spawnParticles();
    }

    private void spawnParticles() {
        if(world.getBlockState(pos).get(ParticleSpawnerBlock.POWERED).equals(true)) {
            float particlesToSpawn = particlesPerTick;
            while (particlesToSpawn > 1 || world.random.nextFloat() < particlesToSpawn) {
                spawnParticle(world, pos, world.random);
                particlesToSpawn--;
            }
        }
    }

    private void spawnParticle(World world, @NotNull BlockPos pos, Random random) {
        double randomOffsetX = positionVariance.getX() == 0 ? 0 : positionVariance.getX() - random.nextDouble() * positionVariance.getX() * 2.0D;
        double randomOffsetY = positionVariance.getY() == 0 ? 0 : positionVariance.getY() - random.nextDouble() * positionVariance.getY() * 2.0D;
        double randomOffsetZ = positionVariance.getZ() == 0 ? 0 : positionVariance.getZ() - random.nextDouble() * positionVariance.getZ() * 2.0D;

        double randomVelocityX = velocityVariance.getX() == 0 ? 0 : velocityVariance.getX() * random.nextDouble();
        double randomVelocityY = velocityVariance.getY() == 0 ? 0 : velocityVariance.getY() * random.nextDouble();
        double randomVelocityZ = velocityVariance.getZ() == 0 ? 0 : velocityVariance.getZ() * random.nextDouble();

        float randomScale = this.scaleVariance == 0 ? this.scale : (float) (this.scale + this.scaleVariance - random.nextDouble() * this.scaleVariance * 0.2D);
        int randomLifetime = this.lifetimeVariance == 0 ? this.lifetimeTicks : (int) (this.lifetimeTicks + this.lifetimeVariance - random.nextDouble() * this.lifetimeVariance * 0.2D);

        MinecraftClient.getInstance().player.getEntityWorld().addParticle(
                new ParticleSpawnerParticleEffect(new Identifier(SpectrumCommon.MOD_ID, "particle/sparklestone_sparkle"), this.gravity, this.color, randomScale, randomLifetime, this.collisions),
                (double) pos.getX() + position.getX() + randomOffsetX, (double) pos.getY() + position.getY() + randomOffsetY, (double) pos.getZ() + position.getZ() + randomOffsetZ,
                velocity.getX() + randomVelocityX, velocity.getY() + randomVelocityY, velocity.getZ() + randomVelocityZ);
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        return tag;
    }

    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
    }


}
