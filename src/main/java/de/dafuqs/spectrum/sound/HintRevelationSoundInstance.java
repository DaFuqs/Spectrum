package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.registries.SpectrumItems;
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
public class HintRevelationSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {

	private final PlayerEntity player;
	private boolean done;
	private final int duration;
	private int playtime;

	public HintRevelationSoundInstance(PlayerEntity player, int duration) {
		super(SpectrumSoundEvents.TEXT_REVEALED, SoundCategory.PLAYERS);
		this.repeat = true;
		this.repeatDelay = 0;
		this.volume = 1.0F;
		this.player = player;
		this.duration = duration;
		this.x = player.getX();
		this.y = player.getY();
		this.z = player.getZ();
		
		this.playtime = 0;
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
		playtime++;
		if (player == null || !player.getMainHandStack().isOf(SpectrumItems.MANUAL) || playtime > duration) {
			this.setDone();
		}
	}

	protected final void setDone() {
		this.done = true;
		this.repeat = false;
	}
}