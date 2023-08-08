package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

@Environment(EnvType.CLIENT)
public class PipeBombChargingSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {

	private final PlayerEntity player;
	private boolean done;

	public PipeBombChargingSoundInstance(PlayerEntity player) {
		super(SpectrumSoundEvents.INCANDESCENT_CHARGE, SoundCategory.NEUTRAL, SoundInstance.createRandom());
		this.repeat = true;
		this.repeatDelay = 0;
		this.volume = 1F;
		this.player = player;
		this.x = player.getX();
		this.y = player.getY();
		this.z = player.getZ();
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
		if (player == null || player.getItemUseTimeLeft() <= 0 || player.getItemUseTime() > 54) {
			this.setDone();
		} else {
			this.x = this.player.getX();
			this.y = this.player.getY();
			this.z = this.player.getZ();
			
			showParticles();
		}
	}
	
	@SuppressWarnings("resource")
	private void showParticles() {
		Vec3d pos = player.getPos();
		Random random = player.getEntityWorld().random;
		
		for (int i = 0; i < 2; i++) {
			player.getEntityWorld().addParticle(
					SpectrumParticleTypes.PRIMORDIAL_FLAME,
					pos.x,
					pos.y + 1,
					pos.z,
					random.nextDouble() - 0.5D,
					random.nextDouble() - 0.5D,
					random.nextDouble() - 0.5D);
		}
	}
	
	protected final void setDone() {
		this.done = true;
		this.repeat = false;
	}
}