package de.dafuqs.spectrum.particle.client;

import de.dafuqs.spectrum.particle.effect.ParticleSpawnerParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.mixin.client.particle.ParticleManagerAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class ParticleSpawnerParticle extends SpriteBillboardParticle {
	
	protected boolean glowInTheDark;
	
	public ParticleSpawnerParticle(ClientWorld clientWorld, double d, double e, double f, double velocityX, double velocityY, double velocityZ) {
		super(clientWorld, d, e, f, velocityX, velocityY, velocityZ);
		// Override the default random particle velocities again.
		// Not performant, but super() has to be called here :/
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;
		this.glowInTheDark = false;
	}
	
	public int getBrightness(float tint) {
		if (glowInTheDark) {
			return 16777215; // #FFFFFF
		} else {
			return super.getBrightness(tint);
		}
	}
	
	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}
	
	public void apply(@NotNull ParticleSpawnerParticleEffect effect) {
		Sprite sprite = ((ParticleManagerAccessor) MinecraftClient.getInstance().particleManager).getParticleAtlasTexture().getSprite(effect.textureIdentifier);
		this.setSprite(sprite);
		this.setMaxAge(effect.lifetimeTicks);
		this.scale(effect.scale);
		this.setColor(effect.color.getX(), effect.color.getY(), effect.color.getZ());
		this.gravityStrength = effect.gravity;
		this.collidesWithWorld = effect.collisions;
		this.glowInTheDark = effect.glowInTheDark;
	}
	
}