package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class AirLaunchBeltSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {

	private final PlayerEntity player;
	private float distance = 0.0F;
	private boolean done;
	private int ticksPlayed = 0;

	public AirLaunchBeltSoundInstance(PlayerEntity player) {
		super(SpectrumSoundEvents.AIR_LAUNCH_BELT_CHARGING, SoundCategory.PLAYERS);
		this.repeat = false;
		this.repeatDelay = 0;
		this.volume = 0.05F;
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
		if (player == null || !player.isSneaking() || !player.isOnGround()) {
			this.setDone();
		} else {
			this.x = ((float)this.player.getX());
			this.y = ((float)this.player.getY());
			this.z = ((float)this.player.getZ());
			this.distance = MathHelper.clamp(this.distance + 0.0025F, 0.0F, 1.0F);

			if(ticksPlayed < 250) {
				showParticles();
			} else {
				this.volume = 0.0F;
			}
			ticksPlayed++;
		}
	}

	private void showParticles() {
		Random random = player.getEntityWorld().random;
		
		if(random.nextInt(50) == 0) {
			Vec3d pos = player.getPos();
			player.getEntityWorld().addParticle(SpectrumParticleTypes.LIGHT_BLUE_CRAFTING,
					pos.x + random.nextDouble() * 0.8 - 0.4,
					pos.y,
					pos.z + random.nextDouble() * 0.8 - 0.4,
					0,
					random.nextDouble() * 0.5,
					0);
		}
	}

	protected final void setDone() {
		this.done = true;
		this.repeat = false;
	}
}