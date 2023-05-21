package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.sound.*;
import net.minecraft.entity.player.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;

@Environment(EnvType.CLIENT)
public class TakeOffBeltSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private final long lastParticleTick;
	private boolean done;
	
	public TakeOffBeltSoundInstance() {
		super(SpectrumSoundEvents.AIR_LAUNCH_BELT_CHARGING, SoundCategory.PLAYERS, SoundInstance.createRandom());
		PlayerEntity player = MinecraftClient.getInstance().player;
		this.repeat = false;
		this.repeatDelay = 0;
		this.volume = 0.4F;
		this.lastParticleTick = player.getWorld().getTime() + TakeOffBeltItem.CHARGE_TIME_TICKS * TakeOffBeltItem.MAX_CHARGES;
		this.x = player.getX();
		this.y = player.getY();
		this.z = player.getZ();
	}
	
	@Environment(EnvType.CLIENT)
	public static void startSoundInstance() {
		TakeOffBeltSoundInstance soundInstance = new TakeOffBeltSoundInstance();
		if (!MinecraftClient.getInstance().getSoundManager().isPlaying(soundInstance)) {
			MinecraftClient.getInstance().getSoundManager().play(soundInstance);
		}
	}
	
	@Override
	public boolean isDone() {
		return this.done;
	}
	
	@Override
	public boolean shouldAlwaysPlay() {
		return true;
	}
	
	@Override
	public void tick() {
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null || !player.isSneaking() || !player.isOnGround()) {
			this.setDone();
		} else {
			this.x = ((float) player.getX());
			this.y = ((float) player.getY());
			this.z = ((float) player.getZ());
			
			if (player.getWorld() != null && player.getWorld().getTime() < lastParticleTick) {
				spawnParticles(player);
			} else {
				this.volume = 0.0F;
			}
		}
	}
	
	private void spawnParticles(PlayerEntity player) {
		Random random = player.getEntityWorld().random;
		
		Vec3d pos = player.getPos();
		player.getEntityWorld().addParticle(SpectrumParticleTypes.LIGHT_BLUE_CRAFTING,
				pos.x + random.nextDouble() * 0.8 - 0.4,
				pos.y,
				pos.z + random.nextDouble() * 0.8 - 0.4,
				0,
				random.nextDouble() * 0.5,
				0);
	}
	
	protected final void setDone() {
		this.done = true;
		this.repeat = false;
	}
}