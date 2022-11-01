package de.dafuqs.spectrum.sound;


import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import de.dafuqs.spectrum.status_effects.AscensionStatusEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.sound.SoundCategory;

@Environment(EnvType.CLIENT)
public class DivinitySoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private static int instances = 0;
	private int time = 0;
	private boolean done;
	
	public DivinitySoundInstance() {
		super(SpectrumSoundEvents.DIVINITY, SoundCategory.PLAYERS);
		this.repeat = true;
		this.repeatDelay = 0;
		this.volume = 0.8F;
		instances++;
		MinecraftClient.getInstance().getSoundManager().stopSounds(null, SoundCategory.MUSIC);
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
		time++;
		if(time > AscensionStatusEffect.MUSIC_INTRO_TICKS) {
			this.volume = 0.8F;
		} else {
			this.volume = 0.5F + ((float) time / AscensionStatusEffect.MUSIC_INTRO_TICKS) * 0.2F;
		}
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (instances > 1 || player == null || !(player.hasStatusEffect(SpectrumStatusEffects.ASCENSION) || player.hasStatusEffect(SpectrumStatusEffects.DIVINITY))) {
			this.setDone();
		} else {
			this.x = ((float) player.getX());
			this.y = ((float) player.getY());
			this.z = ((float) player.getZ());
		}
	}
	
	protected final void setDone() {
		this.done = true;
		this.repeat = false;
		instances--;
	}
}