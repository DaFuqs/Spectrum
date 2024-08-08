package de.dafuqs.spectrum.particle.client;

import net.fabricmc.api.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.world.*;
import net.minecraft.particle.*;
import net.minecraft.util.math.*;

public class FireflyParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    private double lastVelX, lastVelZ;
    private int switchTicks = 10, blinkTicks = 11;
    private float r, g, b;

    protected FireflyParticle(ClientWorld clientWorld, double d, double e, double f, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;
        this.setSpriteForAge(spriteProvider);
        this.velocityX = random.nextFloat() * 0.1F - 0.05F;
        this.velocityY = 0;
        this.velocityZ = random.nextFloat() * 0.1F - 0.05F;
        var random = clientWorld.getRandom();
        this.gravityStrength = random.nextFloat() * 0.075F - 0.0375F;

        this.collidesWithWorld = true;
        this.maxAge = 30 + random.nextInt(10);

        r = 255;
        g = 232;
        b = 173;
        var edit = random.nextInt(3);

        if (edit == 0) {
            r = MathHelper.lerp(random.nextFloat(), 255, 190);
            g *= 0.9F;
            b *= 0.9F;
        }
        else if (edit == 1) {
            r *= 0.9F;
            g = MathHelper.lerp(random.nextFloat(), 232, 203);
            b *= 0.9F;
        }
        else {
            r *= 0.9F;
            g *= 0.9F;
            b = MathHelper.lerp(random.nextFloat(), 232, 230);
        }

        r /= 255F;
        g /= 255F;
        b /= 255F;
        
        this.scale = 0.1F + random.nextFloat() * 0.3F;
        scale *= scaleMultiplier;
        setColor(r, g, b);
        setAlpha(0F);
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        if (this.age++ >= this.maxAge) {
            this.markDead();
            return;
        }

        if (blinkTicks <= 10) {
            var delta = Math.abs(blinkTicks) / 10F;

            setColor(
                    (MathHelper.lerp(delta, 0.25F * r, r)),
                    (MathHelper.lerp(delta, 0.25F * g, g)),
                    (MathHelper.lerp(delta, 0.25F * b, b))
            );

            blinkTicks++;
            alpha = Math.min(1F, delta + 0.5F);
        }

        if (switchTicks < 10)
            switchTicks++;

        var water = !this.world.getFluidState(BlockPos.ofFloored(this.x, this.y, this.z)).isEmpty();

        if (age % 10 == 0 && random.nextBoolean()) {
            switchTicks = 0;
            gravityStrength = random.nextFloat() * 0.075F - 0.0375F;
            lastVelX = velocityX;
            lastVelZ = velocityZ;
            velocityX = random.nextFloat()  * 0.1F - 0.05F;
            velocityZ = random.nextFloat()  * 0.1F - 0.05F;
        }

        if (age % 13 == 0 && random.nextFloat() < 0.334F) {
            blinkTicks = -10;
        }

        var flutter = Math.sin(age / 17F) / 15F;

        var curVelX = MathHelper.lerp(switchTicks / 10F, lastVelX, velocityX);
        var curVelZ = MathHelper.lerp(switchTicks / 10F, lastVelZ, velocityZ);

        if (this.onGround || water) {
            curVelX *= 0.7F;
            curVelZ *= 0.7F;
            gravityStrength = random.nextFloat() * 0.1334F;
        }

        this.velocityY -= 0.04 * (double)this.gravityStrength;
        this.move(curVelX, this.velocityY + flutter, curVelZ);
        if (this.ascending && this.y == this.prevPosY) {
            this.velocityX *= 1.1;
            this.velocityZ *= 1.1;
        }

        adjustAlpha(water);
    }

    private void adjustAlpha(boolean water) {
        if (age <= 5) {
            alpha = MathHelper.clamp(age / 5F, 0, 1F);
            return;
        }

        var ageFade = MathHelper.clamp(Math.min(maxAge - age, 5) / 5F, 0, 1F);

        if (ageFade < 1) {
            alpha = Math.min(alpha, ageFade);
        } else if (onGround) {
            alpha = MathHelper.clamp(alpha - 0.02F, 0, 1F);
        } else if (water) {
            alpha = MathHelper.clamp(alpha - 0.02F, 0.5F, 1F);
        } else {
            alpha = MathHelper.clamp(alpha + 0.05F, 0F, 1F);
        }

        if (alpha < 0.01F) {
            markDead();
        }
    }

    @Override
    public int getBrightness(float tint) {
        return LightmapTextureManager.MAX_LIGHT_COORDINATE;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new FireflyParticle(clientWorld, d, e, f, 0.0, 0.0, 0.0, 1.0F, this.spriteProvider);
        }
    }
}
