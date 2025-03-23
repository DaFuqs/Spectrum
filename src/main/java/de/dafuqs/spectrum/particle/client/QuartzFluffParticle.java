package de.dafuqs.spectrum.particle.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

public class QuartzFluffParticle extends AscendingParticle {

    protected QuartzFluffParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteProvider spriteProvider) {
        super(world, x, y, z, 0.0725F, -0.1F, 0.0725F, velocityX, velocityY, velocityZ, scaleMultiplier, spriteProvider, 1F, 0, 0.08F, false);
        alpha = 0;
        this.maxAge = 15 + world.random.nextInt(16);
        this.scale = (0.25F + random.nextFloat() * 0.5F) * 0.25F;
        this.red = 1F;
        this.green = 0.975F;
        this.blue = 0.9125F;
    }

    @Override
    public void tick() {
        super.tick();
        adjustAlpha();
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    private void adjustAlpha() {
        if (age <= 7) {
            alpha = MathHelper.clamp(age / 7F, 0, 1F);
            return;
        }

        var ageFade = MathHelper.clamp(Math.min(maxAge - age, 7) / 7F, 0, 1F);

        if (ageFade < 1) {
            alpha = Math.min(alpha, ageFade);
        } else if (onGround) {
            alpha = MathHelper.clamp(alpha - 0.02F, 0, 1F);
        } else {
            alpha = MathHelper.clamp(alpha + 0.05F, 0F, 1F);
        }

        if (alpha < 0.01F) {
            markDead();
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new QuartzFluffParticle(clientWorld, d, e, f, 0.0, 0.0, 0.0, 1.0F, this.spriteProvider);
        }
    }
}
