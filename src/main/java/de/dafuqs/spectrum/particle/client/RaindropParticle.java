package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RaindropParticle extends SpriteBillboardParticle {

    private static final Vec3d VERTICAL = new Vec3d(0, 1,0);
    private final int simInterval = SpectrumCommon.CONFIG.WindSimInterval, simOffset;

    public RaindropParticle(ClientWorld clientWorld, double d, double e, double f, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f);
        setSprite(spriteProvider);
        gravityStrength = 5.25F;
        scale = 0.25F + random.nextFloat() * 0.2F;
        this.simOffset = random.nextInt(simInterval);
        maxAge = 25;
    }

    @Override
    public void tick() {
        if (onGround) {
            markDead();
            return;
        }

        adjustAlpha();
        super.tick();
    }

    private void adjustAlpha() {
        if (age <= 5) {
            alpha = MathHelper.clamp(age / 5F, 0, 1F);
            return;
        }

        var ageFade = MathHelper.clamp(Math.min(maxAge - age, 5) / 5F, 0, 1F);
        alpha = Math.min(alpha, ageFade);

        if (alpha < 0.01F) {
            markDead();
        }
    }

    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d vec3d = camera.getPos();
        float f = (float)(MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float)(MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float)(MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
        var xOffset = x - camera.getPos().x;
        var zOffset = z - camera.getPos().z;

        Quaternionf quaternionf = RotationAxis.POSITIVE_Y.rotation((float) MathHelper.atan2(xOffset, zOffset));

        Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float i = this.getSize(tickDelta);

        for(int j = 0; j < 4; ++j) {
            Vector3f vector3f = vector3fs[j];
            vector3f.rotate(quaternionf);
            vector3f.mul(i);
            vector3f.add(f, g, h);
        }

        float k = this.getMinU();
        float l = this.getMaxU();
        float m = this.getMinV();
        float n = this.getMaxV();
        int o = this.getBrightness(tickDelta);
        vertexConsumer.vertex(vector3fs[0].x(), (double)vector3fs[0].y(), (double)vector3fs[0].z()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
        vertexConsumer.vertex(vector3fs[1].x(), (double)vector3fs[1].y(), (double)vector3fs[1].z()).texture(l, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
        vertexConsumer.vertex(vector3fs[2].x(), (double)vector3fs[2].y(), (double)vector3fs[2].z()).texture(k, m).color(this.red, this.green, this.blue, this.alpha).light(o).next();
        vertexConsumer.vertex(vector3fs[3].x(), (double)vector3fs[3].y(), (double)vector3fs[3].z()).texture(k, n).color(this.red, this.green, this.blue, this.alpha).light(o).next();
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
            return new RaindropParticle(clientWorld, d, e, f, spriteProvider);
        }
    }
}
