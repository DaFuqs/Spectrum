package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumBiomes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class FallingAshParticle extends SpriteBillboardParticle {

    private static final Vec3d VERTICAL = new Vec3d(0, 1,0);
    private static final float GRAVITY = 0.15F;
    private static double targetVelocity = 0.215, ashScaleA = 20000, ashScaleB = 2200, ashScaleC = 200;
    private static Direction.Axis primaryAxis = Direction.Axis.X;
    private static Direction.Axis lastAxis = primaryAxis;
    private final float rotateFactor, lightness;
    private final int simInterval = SpectrumCommon.CONFIG.WindSimInterval, simOffset;
    private int slowTicks, axisTicks = 0;

    protected FallingAshParticle(ClientWorld clientWorld, double x, double y, double z,  double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(clientWorld, x, y, z);
        setSprite(spriteProvider);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        var random = clientWorld.getRandom();

        this.collidesWithWorld = true;
        this.gravityStrength = GRAVITY;
        this.maxAge = 150 + random.nextInt(50);

        this.rotateFactor = ((float) Math.random() - 0.5F) * 0.002F;
        this.scale = (float) (0.06 + (random.nextDouble() / 14));
        this.lightness = random.nextFloat() * 0.6F + 0.4F;
        this.simOffset = random.nextInt(simInterval);
        setAlpha(0F);
    }

    @Override
    public void tick() {
        this.prevAngle = this.angle;
        var water = !this.world.getFluidState(new BlockPos((int) this.x, (int) this.y, (int) this.z)).isEmpty();
        var time = world.getTime() % 432000;

        if ((age + 2 < maxAge)
                && world.getBiome(new BlockPos((int) x, (int) y, (int) z)).getKey().map(key -> !key.equals(SpectrumBiomes.HOWLING_SPIRES)).orElse(true)) {
            age++;
        }

        if (lastAxis != primaryAxis) {
            lastAxis = primaryAxis;
            axisTicks = 0;
        }

        switch(primaryAxis) {
            case X -> {
                velocityX = MathHelper.clampedLerp(velocityX, adjustVelocity(velocityX, water), axisTicks / 20F);
                velocityZ = MathHelper.clampedLerp(velocityZ, getNonPrimaryVelocity(time), axisTicks / 20F);
            }
            case Z -> {
                velocityZ = MathHelper.clampedLerp(velocityZ, adjustVelocity(velocityZ, water), axisTicks / 20F);
                velocityX = MathHelper.clampedLerp(velocityX, getNonPrimaryVelocity(time), axisTicks / 20F);
            }
        }

        if (axisTicks < 20) {
            axisTicks++;
        }

        if (Math.abs(velocityX) + Math.abs(velocityZ) < 0.1) {
            if (slowTicks < 20)
                slowTicks++;
        }
        else {
            slowTicks = 0;
        }

        if (!this.onGround && !water) {
            this.angle += (float) (Math.PI * Math.sin(this.rotateFactor * this.age) / 2);

            if(verifySimConfig(time)) {
                adjustGravityForLift();
            }
        }
        else if (water) {
            this.velocityY /= 4;
            this.velocityX /= 4;
            this.velocityZ /= 4;
            this.gravityStrength = 0;
        } else {
            this.gravityStrength = GRAVITY;
        }

        adjustAlpha(water);


        if (verifySimConfig(time) && Math.abs(velocityX) + Math.abs(velocityZ) > 0.125) {
            applyAirflowTransforms();
        }
        super.tick();
    }

    private boolean verifySimConfig(long time) {
        return SpectrumCommon.CONFIG.WindSim && (time + simOffset) % simInterval == 0;
    }

    private void adjustGravityForLift() {
        var pos = new BlockPos.Mutable((int) x, (int) y, (int) z);
        var height = 0F;
        var groundFound = false;
        for (; height < 20; ++height) {
            pos.move(Direction.DOWN);
            if (!world.getFluidState(pos).isEmpty()) {
                gravityStrength = 0F;
                return;
            }
            else if (world.getBlockState(pos).isSideSolidFullSquare(world, pos, Direction.UP)) {
                groundFound = true;
                break;
            }
        }

        height += (float) (y - (int) y);

        if (!groundFound) {
            gravityStrength = GRAVITY * 2F * (1F + (1 - lightness));
            return;
        }

        var heightFactor = height / 14F;

        if (height < 4) {
            gravityStrength = GRAVITY - GRAVITY * (1F - heightFactor * 2) * 2 * lightness;
            return;
        }

        if (height > 14F) {
            gravityStrength = GRAVITY * (1 - ((height - 14) / 7)) * 0.225F;
            return;
        }

        gravityStrength = GRAVITY * heightFactor;
    }

    private void applyAirflowTransforms() {
        var velocity = new Vec3d(velocityX, velocityY, velocityZ);
        var direction = velocity.normalize();
        var movementNormal = direction.crossProduct(VERTICAL);

        for (int i = 0; i <= 6; i++) {
            var deflection = -0.0125F * (1 - (i / 24F)) * lightness * simInterval;
            var shift = velocity.multiply(i).add(x, y, z);
            var maxDist = 6 - i;
            for (int orthogonal = 1; orthogonal <= maxDist; orthogonal++) {
                var leftShift = movementNormal.multiply(orthogonal).add(shift);
                var rightShift = movementNormal.multiply(-orthogonal).add(shift);
                var leftPos = new BlockPos((int) leftShift.x, (int) leftShift.y, (int) leftShift.z);
                var rightPos = new BlockPos((int) rightShift.x, (int) rightShift.y, (int) rightShift.z);

                if (world.getBlockState(leftPos).isSolidBlock(world, leftPos)) {
                    var collisionDirection = leftShift.subtract(x, y, z).normalize();
                    velocityX += collisionDirection.x * deflection;
                    velocityZ += collisionDirection.z * deflection;
                }
                if (world.getBlockState(rightPos).isSolidBlock(world, rightPos)) {
                    var collisionDirection = rightShift.subtract(x, y, z).normalize();
                    velocityX += collisionDirection.x * deflection;
                    velocityZ += collisionDirection.z * deflection;
                }
            }
        }
    }

    private static double getNonPrimaryVelocity(long time) {
        var scale = Math.sin(time / ashScaleA + ashScaleB);
        scale = scale * (Math.cos(time / ashScaleB + ashScaleC) / 2);
        scale = scale * (Math.cos(time / ashScaleC + ashScaleA) / 4);
        scale *= 2;
        return scale;
    }

    private void adjustAlpha(boolean water) {
        if (age <= 20) {
            alpha = MathHelper.clamp(age / 20F, 0, 1F);
            return;
        }

        var ageFade = MathHelper.clamp(Math.min(maxAge - age, 40) / 40F, 0, 1F);

        if (ageFade < 1) {
            alpha = Math.min(alpha, ageFade);
        }
        else if(onGround || slowTicks == 20) {
            alpha = MathHelper.clamp(alpha - 0.02F, 0, 1F);
        }
        else if(water) {
            alpha = MathHelper.clamp(alpha - 0.02F, 0.5F, 1F);
        }
        else {
            alpha = MathHelper.clamp(alpha + 0.05F, 0F, 1F);
        }

        if (alpha < 0.01F) {
            markDead();
        }
    }

    private double adjustVelocity(double velocity, boolean water) {
        if (water)
            return velocity / 1.5;

        if (velocity != targetVelocity && velocity >= targetVelocity - 0.15 && velocity <= targetVelocity + 0.15) {
            velocity = targetVelocity;
        }
        else if (velocity > targetVelocity) {
            velocity = velocity - 0.125;
        }
        else if(velocity < targetVelocity) {
            velocity = velocity + 0.0334;
        }

        return velocity;
    }

    public static void setTargetVelocity(double targetVelocity) {
        FallingAshParticle.targetVelocity = targetVelocity;
    }

    public static void setPrimaryAxis(Direction.Axis primaryAxis) {
        FallingAshParticle.primaryAxis = primaryAxis;
    }

    public static void setAshScaleA(double ashScaleA) {
        FallingAshParticle.ashScaleA = ashScaleA;
    }

    public static void setAshScaleB(double ashScaleB) {
        FallingAshParticle.ashScaleB = ashScaleB;
    }

    public static void setAshScaleC(double ashScaleC) {
        FallingAshParticle.ashScaleC = ashScaleC;
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

        @Override
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new FallingAshParticle(clientWorld, d, e, f, g, h, i, spriteProvider);
        }
    }
}