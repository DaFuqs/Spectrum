package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class EnderSpliceChargingSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private final PlayerEntity player;
	private float distance = 0.0F;
	private boolean done;
	
	public EnderSpliceChargingSoundInstance(PlayerEntity player) {
		super(SpectrumSoundEvents.ENDER_SPLICE_CHARGES, SoundCategory.NEUTRAL);
		this.repeat = true;
		this.repeatDelay = 0;
		this.volume = 0.6F;
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
		if (player == null || player.getItemUseTimeLeft() <= 0 || player.getItemUseTime() > 47) {
			this.setDone();
		} else {
			this.x = ((float) this.player.getX());
			this.y = ((float) this.player.getY());
			this.z = ((float) this.player.getZ());
			this.distance = MathHelper.clamp(this.distance + 0.0025F, 0.0F, 1.0F);
			
			showParticles();
		}
	}
	
	private void showParticles() {
		Vec3d pos = player.getPos();
		Random random = player.getEntityWorld().random;
		
		for (int i = 0; i < 10; i++) {
			player.getEntityWorld().addParticle(
					ParticleTypes.PORTAL,
					pos.x,
					pos.y + 1,
					pos.z,
					random.nextDouble() * 1.6D - 0.8D,
					random.nextDouble() * 1.6D - 0.8D,
					random.nextDouble() * 1.6D - 0.8D);
		}
	}
	
	protected final void setDone() {
		this.done = true;
		this.repeat = false;
	}
}