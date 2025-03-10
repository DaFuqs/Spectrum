package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.particle.render.*;
import net.fabricmc.api.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.math.*;
import net.minecraft.client.world.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.event.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class PastelTransmissionParticle extends TransmissionParticle implements EarlyRenderingParticle {

    private final ItemRenderer itemRenderer;
    private final List<Vec3d> travelPositions;
    private final ItemStack itemStack;
	private final ParticleEffect particleEffect;
	
	public PastelTransmissionParticle(ItemRenderer itemRenderer, ClientWorld world, double x, double y, double z, List<BlockPos> travelPositions, ItemStack stack, int travelTime, int networkColor) {
        super(world, x, y, z, new BlockPositionSource(travelPositions.get(travelPositions.size()-1)), travelTime);

        this.itemRenderer = itemRenderer;
        this.itemStack = stack;
        this.scale = 0.25F;
		this.particleEffect = new DustParticleEffect(ColorHelper.colorIntToVec(networkColor), 1.0F);

        this.travelPositions = new ArrayList<>();
        for (BlockPos p : travelPositions) {
            this.travelPositions.add(Vec3d.ofCenter(p));
        }

        // spawning sound & particles
        Vec3d startPos = this.travelPositions.get(0);
		world.playSound(startPos.getX(), startPos.getY() + 0.25, startPos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.25F * SpectrumCommon.CONFIG.BlockSoundVolume, 0.9F + world.random.nextFloat() * 0.2F, true);
        world.addParticle(ParticleTypes.BUBBLE_POP, startPos.getX(), startPos.getY() + 0.25, startPos.getZ(), 0, 0, 0);
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
		
		if (SpectrumCommon.CONFIG.PastelNetworkParticles && this.age % 2 == 0) {
			world.addParticle(particleEffect, x + random.nextDouble() * 0.4 - 0.2, y + random.nextDouble() * 0.4 - 0.2, z + random.nextDouble() * 0.4 - 0.2, random.nextDouble() * 0.4 - 0.2, random.nextDouble() * 0.4 - 0.2, random.nextDouble() * 0.4 - 0.2);
		}
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
        matrixStack.scale(0.65F, 0.65F, 0.65F);
        matrixStack.translate(0, -0.15, 0);
		itemRenderer.renderItem(itemStack, ModelTransformationMode.GROUND, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumers, world, 0);

        matrixStack.pop();
    }

}
