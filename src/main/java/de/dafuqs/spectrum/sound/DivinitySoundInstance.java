package de.dafuqs.spectrum.sound;


import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.mob_effect.*;
import net.minecraft.client.*;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.player.*;


public class DivinitySoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private static DivinitySoundInstance INSTANCE = null;
	
	private int time = 0;
	private boolean done;
	
	public DivinitySoundInstance() {
		super(SpectrumSoundEvents.MUSIC_DIVINITY, SoundSource.RECORDS, SoundInstance.createUnseededRandom());
		this.looping = true;
		this.delay = 0;
		this.volume = 0.8F;
		
		if(INSTANCE == null) {
			INSTANCE = this;
			Minecraft.getInstance().getSoundManager().stop(null, SoundSource.MUSIC);
		} else {
			setDone();
		}
	}
	
	@Override
	public boolean isStopped() {
		return this.done;
	}
	
	@Override
	public boolean canStartSilent() {
		return true;
	}
	
	@Override
	public void tick() {
		Minecraft client = Minecraft.getInstance();
		time++;
		if (time > AscensionMobEffect.MUSIC_INTRO_TICKS) {
			this.volume = 0.8F;
		} else {
			this.volume = 0.5F + ((float) time / AscensionMobEffect.MUSIC_INTRO_TICKS) * 0.2F;
		}
		Player player = client.player;
		if (INSTANCE != this || player == null || !(player.hasEffect(SpectrumMobEffects.ASCENSION) || player.hasEffect(SpectrumMobEffects.DIVINITY))) {
			this.setDone();
		} else {
			this.x = ((float) player.getX());
			this.y = ((float) player.getY());
			this.z = ((float) player.getZ());
		}
	}
	
	protected final void setDone() {
		this.done = true;
		this.looping = false;
		if(INSTANCE == this) {
			INSTANCE = null;
		}
	}
}
