package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.sound.*;
import net.minecraft.entity.player.*;
import net.minecraft.sound.*;

@Environment(EnvType.CLIENT)
public class HintRevelationSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private final PlayerEntity player;
	private final int duration;
	private boolean done;
	private int playtime;
	
	public HintRevelationSoundInstance(PlayerEntity player, int duration) {
		super(SpectrumSoundEvents.TEXT_REVEALED, SoundCategory.PLAYERS, SoundInstance.createRandom());
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
		
		if (this.player != null) {
			this.x = player.getX();
			this.y = player.getY();
			this.z = player.getZ();
		}
		
		if (player == null || !player.getMainHandStack().isOf(SpectrumItems.GUIDEBOOK) || playtime > duration) {
			this.setDone();
		}
	}
	
	protected final void setDone() {
		this.done = true;
		this.repeat = false;
	}
}