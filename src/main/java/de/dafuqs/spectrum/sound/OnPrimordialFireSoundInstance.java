package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.sound.*;
import net.minecraft.entity.player.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;

public class OnPrimordialFireSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private final PlayerEntity player;
	private int fadeInTicks;
	
	public OnPrimordialFireSoundInstance(PlayerEntity player) {
		super(SpectrumSoundEvents.PRIMORDIAL_FIRE_DOT, SoundCategory.PLAYERS, player.getRandom());
		this.repeat = true;
		this.repeatDelay = 0;
		this.volume = 0.05F;
		this.player = player;
		this.relative = true;
	}
	
	@Override
	public void tick() {
		if (player != null) {
			this.pitch = (float) (1 + Math.sin(player.age % 240000 / (Math.E * 100)) / 5);
		}
		this.volume = MathHelper.clampedLerp(0.05F, 0.92F, fadeInTicks / 20F);
		fadeInTicks++;
	}
	
	@Override
	public boolean isDone() {
		return player == null || player.isRemoved() || !OnPrimordialFireComponent.isOnPrimordialFire(player);
	}
}
