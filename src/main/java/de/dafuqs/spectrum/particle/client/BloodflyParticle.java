package de.dafuqs.spectrum.particle.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class BloodflyParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    private double lastVelX, lastVelZ;
    private int switchTicks = 10;
    private float r, g, b;

    protected BloodflyParticle(ClientWorld clientWorld, double d, double e, double f, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f, velocityX, velocityY, velocityZ);
        this.spriteProvider = spriteProvider;
        this.setSpriteForAge(spriteProvider);
        this.velocityX = random.nextFloat() * 0.1F - 0.05F;
        this.velocityY = 0;
        this.velocityZ = random.nextFloat() * 0.1F - 0.05F;
        var random = clientWorld.getRandom();
        this.gravityStrength = random.nextFloat() * 0.05F - 0.025F;

        this.collidesWithWorld = true;
        this.maxAge = 30 + random.nextInt(15);

        r = 255;
        g = MathHelper.lerp(random.nextFloat(), 110, 175);
        b = MathHelper.lerp(random.nextFloat(), 60, 100);

        r /= 255F;
        g /= 255F;
        b /= 255F;
        
        this.scale = 0.0125F + random.nextFloat() * 0.35F;
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

        if (switchTicks < 10)
            switchTicks++;

        var water = !this.world.getFluidState(BlockPos.ofFloored(this.x, this.y, this.z)).isEmpty();

        if (age % 7 == 0 && random.nextBoolean()) {
            switchTicks = 0;
            gravityStrength = random.nextFloat() * 0.05F - 0.025F;
            lastVelX = velocityX;
            lastVelZ = velocityZ;
            velocityX = random.nextFloat()  * 0.1F - 0.05F;
            velocityZ = random.nextFloat()  * 0.1F - 0.05F;
        }

        var flutter = Math.sin(age / 5F) / 20F;

        var curVelX = MathHelper.lerp(switchTicks / 10F, lastVelX, velocityX);
        var curVelZ = MathHelper.lerp(switchTicks / 10F, lastVelZ, velocityZ);

        if (this.onGround || water) {
            curVelX *= 0.7F;
            curVelZ *= 0.7F;
            gravityStrength = random.nextFloat() * 0.03F;
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
            return new BloodflyParticle(clientWorld, d, e, f, 0.0, 0.0, 0.0, 1.0F, this.spriteProvider);
        }
    }
}
