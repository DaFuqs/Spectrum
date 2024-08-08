package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.sound.*;
import net.minecraft.entity.player.*;
import net.minecraft.sound.*;

@Environment(EnvType.CLIENT)
public class HintRevelationSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private final PlayerEntity player;
	private boolean done;
	
	public HintRevelationSoundInstance(PlayerEntity player) {
		super(SpectrumSoundEvents.TEXT_REVEALED, SoundCategory.PLAYERS, SoundInstance.createRandom());
		this.repeat = true;
		this.repeatDelay = 0;
		this.volume = 1.0F;
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
		return false;
	}
	
	@Override
	public void tick() {
		if (this.player != null) {
			this.x = player.getX();
			this.y = player.getY();
			this.z = player.getZ();
		}
		
		if (player == null || !player.getMainHandStack().isOf(SpectrumItems.GUIDEBOOK)) {
			this.setDone();
		}
	}
	
	public final void setDone() {
		this.done = true;
		this.repeat = false;
	}
}