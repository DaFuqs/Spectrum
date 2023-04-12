package de.dafuqs.spectrum.particle.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

public class LightTrailparticle extends AnimatedParticle {
    protected LightTrailparticle(ClientWorld world, double x, double y, double z,  double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 0);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.scale = 0.2F;
        this.maxAge = 25;
        this.setSpriteForAge(spriteProvider);
        setAlpha(0.8f);
        setTargetColor(0xa8baff);
    }

    @Override
    public void tick() {
        super.tick();
        var fadeProgress = MathHelper.clamp((age + MinecraftClient.getInstance().getTickDelta()) / maxAge, 0, 1);
        setAlpha(MathHelper.lerp(fadeProgress, 0.8F, 0F));
        scale = MathHelper.lerp(fadeProgress, 0.2F, 0.1F);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new LightTrailparticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}
