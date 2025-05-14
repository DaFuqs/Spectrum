package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;

public class OnPrimordialFireSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private final Player player;
	private int fadeInTicks;
	
	public OnPrimordialFireSoundInstance(Player player) {
		super(SpectrumSoundEvents.PRIMORDIAL_FIRE_DOT, SoundSource.PLAYERS, player.getRandom());
		this.looping = true;
		this.delay = 0;
		this.volume = 0.05F;
		this.player = player;
		this.relative = true;
	}
	
	@Override
	public void tick() {
		if (player != null) {
			this.pitch = (float) (1 + Math.sin(player.tickCount % 240000 / (Math.E * 100)) / 5);
		}
		this.volume = Mth.clampedLerp(0.05F, 0.92F, fadeInTicks / 20F);
		fadeInTicks++;
	}
	
	@Override
	public boolean isStopped() {
		return player == null || player.isRemoved() || !OnPrimordialFireComponent.isOnPrimordialFire(player);
	}
}
