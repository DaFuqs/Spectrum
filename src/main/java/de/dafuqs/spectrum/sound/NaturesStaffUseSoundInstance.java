package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.sound.*;
import net.minecraft.entity.player.*;
import net.minecraft.sound.*;

@Environment(EnvType.CLIENT)
public class NaturesStaffUseSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private final PlayerEntity player;
	private boolean done;
	
	public NaturesStaffUseSoundInstance(PlayerEntity player) {
		super(SpectrumSoundEvents.NATURES_STAFF_USE, SoundCategory.PLAYERS, SoundInstance.createRandom());
		this.repeat = true;
		this.repeatDelay = 0;
		this.volume = 0.25F;
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
		if (player == null || !player.isUsingItem() || !player.getActiveItem().isOf(SpectrumItems.NATURES_STAFF)) {
			this.setDone();
		} else {
			this.x = ((float) this.player.getX());
			this.y = ((float) this.player.getY());
			this.z = ((float) this.player.getZ());
		}
	}
	
	protected final void setDone() {
		this.done = true;
		this.repeat = false;
	}
}