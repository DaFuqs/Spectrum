package de.dafuqs.spectrum.particle.client;

import com.mojang.blaze3d.vertex.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;

@Environment(EnvType.CLIENT)
public class AzureAuraParticle extends RisingParticle {
	
	private final float alphaMult;
	private float length;
	
	protected AzureAuraParticle(ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
		this.lifetime = 160 + random.nextInt(140);
		this.quadSize = 0.3F;
		var thisLength = 2 + random.nextFloat() * 2;
		this.quadSize += thisLength / 14F;
		this.quadSize *= random.nextFloat() * 0.75F + 0.25F;
		this.length = thisLength * (random.nextFloat() * 0.75F + 0.25F);
		this.yd += this.length / 100;
		this.alpha = 0;
		this.hasPhysics = false;
		
		this.alphaMult = random.nextFloat() * 0.5F + 0.5F;
		
		this.bCol = 1F;
		this.rCol = 0.15F * random.nextFloat();
		this.gCol = 0.3F + random.nextFloat() * 0.55F;
		this.friction = 1;
	}
	
	@Override
	public void tick() {
		adjustAlpha();
		super.tick();
	}
	
	private void adjustAlpha() {
		var pos = BlockPos.containing(x, y, z);
		if (age <= 20) {
			alpha = Mth.clamp(age / 20F, 0, alphaMult);
			return;
		}
		
		var fadeMarker = Math.min(lifetime / 5 * 2, 40);
		var ageFade = Mth.clamp(Math.min(lifetime - age, fadeMarker) / (float) fadeMarker, 0, alphaMult);
		
		if (ageFade < alphaMult) {
			alpha = Math.min(alpha, ageFade);
		} else if (!level.getBlockState(pos).propagatesSkylightDown(level, pos)) {
			alpha = Mth.clamp(alpha - 0.06F, alphaMult / 10, alphaMult);
		} else {
			alpha = Mth.clamp(alpha + 0.0325F, 0F, alphaMult);
		}
		
		
		if (alpha < 0.01F) {
			remove();
		}
	}
	
	// Mildly cursed
	// Dafuqs: Update: Majorly cursed
	// Mostly uncursed by Pizzer
	public void render(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		final Vec3 cameraPos = camera.getPosition();
		final float xOff = (float) (Mth.lerp(tickDelta, this.xo, this.x) - cameraPos.x());
		final float yOff = (float) (Mth.lerp(tickDelta, this.yo, this.y) - cameraPos.y());
		final float zOff = (float) (Mth.lerp(tickDelta, this.zo, this.z) - cameraPos.z());
		final float size = this.getQuadSize(tickDelta);
		
		final float rot = (float) Mth.atan2(xOff, zOff);
		final float sin = org.joml.Math.sin(rot);
		final float cos = org.joml.Math.cosFromSin(sin, rot);

		final float negX = Math.fma(-cos,    size, xOff);
		final float posX = Math.fma( cos,    size, xOff);

		final float negY = Math.fma(-length, size, yOff);
		final float posY = Math.fma( length, size, yOff);

		final float negZ = Math.fma(-sin,    size, zOff);
		final float posZ = Math.fma( sin,    size, zOff);
		
		final float minU = this.getU0();
		final float maxU = this.getU1();
		final float minV = this.getV0();
		final float maxV = this.getV1();
		final int brightness = this.getLightColor(tickDelta);
		vertexConsumer.addVertex(negX, negY, posZ).setUv(maxU, maxV).setColor(this.rCol, this.gCol, this.bCol, 0).setLight(brightness);
		vertexConsumer.addVertex(negX, posY, posZ).setUv(maxU, minV).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(brightness);
		vertexConsumer.addVertex(posX, posY, negZ).setUv(minU, minV).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(brightness);
		vertexConsumer.addVertex(posX, negY, negZ).setUv(minU, maxV).setColor(this.rCol, this.gCol, this.bCol, 0).setLight(brightness);
	}
	
	@Override
	public int getLightColor(float tint) {
		return LightTexture.FULL_BRIGHT;
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	public static class Factory implements ParticleProvider<SimpleParticleType> {
		
		private final SpriteSet spriteProvider;
		
		public Factory(SpriteSet spriteProvider) {
			this.spriteProvider = spriteProvider;
		}
		
		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientLevel clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			AzureAuraParticle particle = new AzureAuraParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
			particle.pickSprite(this.spriteProvider);
			return particle;
		}
	}
	
}
