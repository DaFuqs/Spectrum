package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.particle.render.*;
import net.fabricmc.api.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.*;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.math.*;
import net.minecraft.client.world.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import org.joml.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class PastelTransmissionParticle extends SpriteBillboardParticle implements EarlyRenderingParticle {

    private final ItemRenderer itemRenderer;

    private final List<Vec3d> travelPositions;
    private final ItemStack itemStack;

    public PastelTransmissionParticle(ItemRenderer itemRenderer, ClientWorld world, double x, double y, double z, List<BlockPos> travelPositions, ItemStack stack, int travelTime) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.itemRenderer = itemRenderer;
        this.itemStack = stack;
        this.scale = 0.25F;

        this.travelPositions = new ArrayList<>();
        for (BlockPos p : travelPositions) {
            this.travelPositions.add(Vec3d.ofCenter(p));
        }

        this.maxAge = travelTime;

        // spawning sound & particles
        Vec3d startPos = this.travelPositions.get(0);
        world.playSound(startPos.getX(), startPos.getY() + 0.25, startPos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS,
                0.25F * SpectrumCommon.CONFIG.BlockSoundVolume, 0.9F + world.random.nextFloat() * 0.2F, true);
        world.addParticle(ParticleTypes.BUBBLE_POP, startPos.getX(), startPos.getY() + 0.25, startPos.getZ(), 0, 0, 0);
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        final Vec3d cameraPos = camera.getPos();
        final float x = (float) (MathHelper.lerp(tickDelta, prevPosX, this.x) - cameraPos.getX());
        final float y = (float) (MathHelper.lerp(tickDelta, prevPosY, this.y) - cameraPos.getY());
        final float z = (float) (MathHelper.lerp(tickDelta, prevPosZ, this.z) - cameraPos.getZ());
        final int light = getBrightness(tickDelta);

        // TODO - This code is present in all transmission particles. Perhaps a static method to perform all these?

        // TODO - Test this code to see if the quaternions are calculated correctly
        final Quaternionf quaternion = camera.getRotation();
        final Vector3f[] vec3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        final float size = getSize(tickDelta);

        for (int k = 0; k < 4; ++k) {
            final Vector3f vec2 = vec3fs[k];
            vec2.rotate(quaternion);
            vec2.mul(size);
            vec2.add(x, y, z);
        }

        final float minU = getMinU();
        final float maxU = getMaxU();
        final float minV = getMinV();
        final float maxV = getMaxV();
        vertexConsumer.vertex(vec3fs[0].x(), vec3fs[0].y(), vec3fs[0].z()).texture(maxU, maxV).color(red, green, blue, alpha).light(light).next();
        vertexConsumer.vertex(vec3fs[1].x(), vec3fs[1].y(), vec3fs[1].z()).texture(maxU, minV).color(red, green, blue, alpha).light(light).next();
        vertexConsumer.vertex(vec3fs[2].x(), vec3fs[2].y(), vec3fs[2].z()).texture(minU, minV).color(red, green, blue, alpha).light(light).next();
        vertexConsumer.vertex(vec3fs[3].x(), vec3fs[3].y(), vec3fs[3].z()).texture(minU, maxV).color(red, green, blue, alpha).light(light).next();
    }

    @Override
    public int getBrightness(float tickDelta) {
        return 240;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.age++;

        int vertexCount = this.travelPositions.size() - 1;
        float travelPercent = (float) this.age / this.maxAge;
        if (travelPercent >= 1.0F) {
            Vec3d destination = this.travelPositions.get(vertexCount);
            world.playSound(destination.getX(), destination.getY() + 0.25, destination.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS,
                    0.2F * SpectrumCommon.CONFIG.BlockSoundVolume, 0.7F + world.random.nextFloat() * 0.2F, true);
            world.addParticle(ParticleTypes.BUBBLE_POP, destination.getX(), destination.getY() + 0.25, destination.getZ(), 0, 0, 0);
            this.markDead();
            return;
        }

        float progress = travelPercent * vertexCount;
        int startNodeID = (int) progress;
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        Vec3d source = this.travelPositions.get(startNodeID);
        Vec3d destination = this.travelPositions.get(startNodeID + 1);

        float nodeProgress = progress % 1;
        this.x = MathHelper.lerp(nodeProgress, source.x, destination.x);
        this.y = MathHelper.lerp(nodeProgress, source.y, destination.y);
        this.z = MathHelper.lerp(nodeProgress, source.z, destination.z);
    }

    @Override
    public void renderAsEntity(final MatrixStack matrixStack, final VertexConsumerProvider vertexConsumers, final Camera camera, final float tickDelta) {
        final Vec3d cameraPos = camera.getPos();
        final float x = (float) (MathHelper.lerp(tickDelta, prevPosX, this.x));
        final float y = (float) (MathHelper.lerp(tickDelta, prevPosY, this.y));
        final float z = (float) (MathHelper.lerp(tickDelta, prevPosZ, this.z));

        matrixStack.push();
        matrixStack.translate(x - cameraPos.x, y - cameraPos.y, z - cameraPos.z);
        final int light = getBrightness(tickDelta);
        matrixStack.multiply(camera.getRotation());
        matrixStack.translate(0, -0.2, 0);

        SpectrumClient.FORCE_TRANSLUCENT = true;
        BakedModel bakedModel = itemRenderer.getModel(itemStack, world, null, getMaxAge());
        itemRenderer.renderItem(itemStack, ModelTransformationMode.GROUND, false, matrixStack, new TransparentVertexConsumerProvider(vertexConsumers), light, OverlayTexture.DEFAULT_UV, bakedModel);
        SpectrumClient.FORCE_TRANSLUCENT = false;
        matrixStack.pop();
    }
}
