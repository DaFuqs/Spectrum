package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class NaturesStaffUseSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private final PlayerEntity player;
	private float distance = 0.0F;
	private boolean done;
	
	public NaturesStaffUseSoundInstance(PlayerEntity player) {
		super(SpectrumSoundEvents.NATURES_STAFF_USE, SoundCategory.PLAYERS);
		this.repeat = true;
		this.repeatDelay = 0;
		this.volume = 0.3F;
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
		if (player == null || !player.isUsingItem()) {
			this.setDone();
		} else {
			this.x = ((float) this.player.getX());
			this.y = ((float) this.player.getY());
			this.z = ((float) this.player.getZ());
			this.distance = MathHelper.clamp(this.distance + 0.0025F, 0.0F, 1.0F);
		}
	}
	
	protected final void setDone() {
		this.done = true;
		this.repeat = false;
	}
}