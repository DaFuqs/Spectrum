package de.dafuqs.spectrum.particle.client;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.blocks.pastel_network.payloads.*;
import de.dafuqs.spectrum.config.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.render.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;

import java.util.*;


public class PastelTransmissionParticle extends TransmissionParticle implements EarlyRenderingParticle {
	
	public final ItemRenderer itemRenderer;
	private final List<Vec3> travelPositions;
	private final PastelPayload payload;
	private final ParticleOptions particleEffect;
	
	public PastelTransmissionParticle(ItemRenderer itemRenderer, ClientLevel world, double x, double y, double z, List<BlockPos> travelPositions, PastelPayload payload, int travelTime, int networkColor) {
		super(world, x, y, z, new BlockPositionSource(travelPositions.get(travelPositions.size() - 1)), travelTime);
		
		this.itemRenderer = itemRenderer;
		this.payload = payload;
		this.quadSize = 0.25F;
		this.particleEffect = new DustParticleOptions(SpectrumColorHelper.colorIntToVec(networkColor), 0.8F);
		
		this.travelPositions = new ArrayList<>();
		for (BlockPos p : travelPositions) {
			this.travelPositions.add(Vec3.atCenterOf(p));
		}
		
		// spawning sound & particles
		Vec3 startPos = this.travelPositions.get(0);
		world.playLocalSound(startPos.x(), startPos.y() + 0.25, startPos.z(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.15F * SpectrumConfig.CONFIG.BlockSoundVolume.get().floatValue() + world.getRandom().nextFloat() / 10F, 0.8F + world.getRandom().nextFloat() * 0.3F, true);
		world.addParticle(ParticleTypes.BUBBLE_POP, startPos.x(), startPos.y() + 0.25, startPos.z(), 0, 0, 0);
	}
	
	@Override
	public void tick() {
		this.age++;
		
		int vertexCount = this.travelPositions.size() - 1;
		float travelPercent = (float) this.age / this.lifetime;
		if (travelPercent >= 1.0F) {
			Vec3 destination = this.travelPositions.get(vertexCount);
			level.playLocalSound(destination.x(), destination.y() + 0.25, destination.z(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.1F * SpectrumConfig.CONFIG.BlockSoundVolume.get().floatValue() + random.nextFloat() / 10F, 0.6F + level.getRandom().nextFloat() * 0.3F, true);
			level.addParticle(ParticleTypes.BUBBLE_POP, destination.x(), destination.y() + 0.25, destination.z(), 0, 0, 0);
			this.remove();
			return;
		}
		
		float progress = travelPercent * vertexCount;
		int startNodeID = (int) progress;
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		
		Vec3 source = this.travelPositions.get(startNodeID);
		Vec3 destination = this.travelPositions.get(startNodeID + 1);
		
		float nodeProgress = progress % 1;
		this.x = Mth.lerp(nodeProgress, source.x, destination.x);
		this.y = Mth.lerp(nodeProgress, source.y, destination.y);
		this.z = Mth.lerp(nodeProgress, source.z, destination.z);
		
		payload.tick(level, this);
		
		if (SpectrumConfig.CONFIG.PastelNetworkParticles.get() && this.age % 2 == 0) {
			level.addParticle(particleEffect, x + random.nextDouble() * 0.4 - 0.2, y + random.nextDouble() * 0.4 - 0.2, z + random.nextDouble() * 0.4 - 0.2, random.nextDouble() * 0.4 - 0.2, random.nextDouble() * 0.4 - 0.2, random.nextDouble() * 0.4 - 0.2);
		}
	}
	
	@Override
	public void renderAfterEntities(final PoseStack poseStack, final MultiBufferSource vertexConsumers, final Camera camera, final float tickDelta) {
		payload.renderAfterEntities(this, level, poseStack, vertexConsumers, camera, tickDelta);
	}
	
	public int getLightColor(float partialTick) {
		return super.getLightColor(partialTick);
	}
	
	public Vec3 getPos(float partialTick) {
		return new Vec3(Mth.lerp(partialTick, xo, this.x), Mth.lerp(partialTick, yo, this.y), Mth.lerp(partialTick, zo, this.z));
	}
	
}
