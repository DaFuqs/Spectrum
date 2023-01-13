package de.dafuqs.spectrum.particle.client;

import net.fabricmc.api.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.*;
import net.minecraft.client.world.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class PastelTransmissionParticle extends SpriteBillboardParticle {

    private final EntityRenderDispatcher dispatcher;
    private final BufferBuilderStorage bufferStorage;

    private final List<Vec3d> travelNodes;
    private final Entity itemEntity;

    public PastelTransmissionParticle(EntityRenderDispatcher dispatcher, BufferBuilderStorage bufferStorage, ClientWorld world, double x, double y, double z, List<BlockPos> travelNodes, ItemStack stack, int travelTime) {
        super(world, x, y - 0.25, z, 0.0D, 0.0D, 0.0D);
        this.dispatcher = dispatcher;
        this.bufferStorage = bufferStorage;
        this.scale = 1.0F;

        List<Vec3d> vecList = new ArrayList<>();
        for (BlockPos p : travelNodes) {
            vecList.add(new Vec3d(p.getX() + 0.5, p.getY() + 0.25, p.getZ() + 0.5));
        }
        this.travelNodes = vecList;

        this.itemEntity = new ItemEntity(world, x, y, z, stack);
        this.maxAge = travelTime;

        // spawning sound & particles
        Vec3d pos = vecList.get(0);
        world.playSound(pos.getX(), pos.getY() + 0.25, pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.3F, 0.9F + world.random.nextFloat() * 0.2F, true);
        world.addParticle(ParticleTypes.BUBBLE_POP, pos.getX(), pos.getY() + 0.25, pos.getZ(), 0, 0, 0);
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        VertexConsumerProvider.Immediate immediate = this.bufferStorage.getEntityVertexConsumers();
        MatrixStack matrixStack = new MatrixStack();
        EntityRenderer entityRenderer = this.dispatcher.getRenderer(this.itemEntity);
        Vec3d positionOffset = entityRenderer.getPositionOffset(this.itemEntity, tickDelta);

        Vec3d cameraPos = camera.getPos();
        float x = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - cameraPos.getX() + positionOffset.getX());
        float y = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - cameraPos.getY() + positionOffset.getY());
        float z = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - cameraPos.getZ() + positionOffset.getZ());
        int light = this.getBrightness(tickDelta);

        matrixStack.translate(x, y, z);

        // TODO: rendering the ItemEntity 50 % translucent
        entityRenderer.render(this.itemEntity, this.itemEntity.getYaw(), tickDelta, matrixStack, immediate, light);
        immediate.draw();

        // TODO: fix me please
        // this does not render unless the above code is removed o.O
        /*
        Quaternion quaternion = camera.getRotation();
        Vec3f[] vec3fs = new Vec3f[]{new Vec3f(-1.0F, -1.0F, 0.0F), new Vec3f(-1.0F, 1.0F, 0.0F), new Vec3f(1.0F, 1.0F, 0.0F), new Vec3f(1.0F, -1.0F, 0.0F)};
        float j = this.getSize(tickDelta);

        for(int k = 0; k < 4; ++k) {
            Vec3f vec3f2 = vec3fs[k];
            vec3f2.rotate(quaternion);
            vec3f2.scale(j);
            vec3f2.add(x, y, z);
        }

        float l = this.getMinU();
        float m = this.getMaxU();
        float n = this.getMinV();
        float o = this.getMaxV();
        int p = this.getBrightness(tickDelta);
        vertexConsumer.vertex(vec3fs[0].getX(), vec3fs[0].getY(), vec3fs[0].getZ()).texture(m, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        vertexConsumer.vertex(vec3fs[1].getX(), vec3fs[1].getY(), vec3fs[1].getZ()).texture(m, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        vertexConsumer.vertex(vec3fs[2].getX(), vec3fs[2].getY(), vec3fs[2].getZ()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        vertexConsumer.vertex(vec3fs[3].getX(), vec3fs[3].getY(), vec3fs[3].getZ()).texture(l, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        */
    }

    @Override
    public int getBrightness(float tickDelta) {
        return 240;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public void tick() {
        this.age++;

        int vertexCount = this.travelNodes.size() - 1;
        float travelPercent = (float) this.age / this.maxAge;
        if (travelPercent >= 1.0F) {
            Vec3d destination = this.travelNodes.get(vertexCount);
            world.playSound(destination.getX(), destination.getY() + 0.25, destination.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.3F, 0.9F + world.random.nextFloat() * 0.2F, true);
            world.addParticle(ParticleTypes.BUBBLE_POP, destination.getX(), destination.getY() + 0.25, destination.getZ(), 0, 0, 0);
            this.markDead();
            return;
        }

        float progress = travelPercent * vertexCount;
        int startNodeID = (int) progress;
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        Vec3d source = this.travelNodes.get(startNodeID);
        Vec3d destination = this.travelNodes.get(startNodeID + 1);

        float nodeProgress = progress % 1;
        this.x = MathHelper.lerp(nodeProgress, source.x, destination.x);
        this.y = MathHelper.lerp(nodeProgress, source.y, destination.y);
        this.z = MathHelper.lerp(nodeProgress, source.z, destination.z);
    }

}
