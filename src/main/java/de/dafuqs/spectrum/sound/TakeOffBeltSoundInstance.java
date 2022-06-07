package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.items.trinkets.TakeOffBeltItem;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class TakeOffBeltSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private final long lastParticleTick;
	private float distance = 0.0F;
	private boolean done;
	
	public TakeOffBeltSoundInstance() {
		super(SpectrumSoundEvents.AIR_LAUNCH_BELT_CHARGING, SoundCategory.PLAYERS);
		PlayerEntity player = MinecraftClient.getInstance().player;
		this.repeat = false;
		this.repeatDelay = 0;
		this.volume = 0.4F;
		this.lastParticleTick = player.getWorld().getTime() + TakeOffBeltItem.CHARGE_TIME_TICKS * TakeOffBeltItem.MAX_CHARGES;
		this.x = player.getX();
		this.y = player.getY();
		this.z = player.getZ();
	}
	
	public static void startSoundInstance() {
		TakeOffBeltSoundInstance soundInstance = new TakeOffBeltSoundInstance();
		if (!SpectrumClient.minecraftClient.getSoundManager().isPlaying(soundInstance)) {
			SpectrumClient.minecraftClient.getSoundManager().play(soundInstance);
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
			this.distance = MathHelper.clamp(this.distance + 0.0025F, 0.0F, 1.0F);
			
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