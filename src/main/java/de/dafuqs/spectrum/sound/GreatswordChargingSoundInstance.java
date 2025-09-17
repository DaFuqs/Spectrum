package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.player.*;
import net.neoforged.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class GreatswordChargingSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private final Player player;
	private boolean done;
	private int ticks;
	private final int groundSlamChargeTicks;
	
	public GreatswordChargingSoundInstance(Player player, int groundSlamChargeTicks) {
		super(SpectrumSoundEvents.GROUND_SLAM_CHARGE, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
		this.groundSlamChargeTicks = groundSlamChargeTicks;
		this.looping = false;
		this.ticks = 0;
		this.delay = 0;
		this.volume = 0.5F;
		this.player = player;
		this.x = player.getX();
		this.y = player.getY();
		this.z = player.getZ();
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
		this.ticks++;
		// If ticks > groundSlamChargeTicks, the ground slam was handled already and the effect does not need get cancelled
		if (this.ticks <= this.groundSlamChargeTicks) {
			if (player == null || !player.isUsingItem() || !(player.getMainHandItem().getItem() instanceof GreatswordItem)) {
				this.setDone();
			}
		}
		this.x = this.player.getX();
		this.y = this.player.getY();
		this.z = this.player.getZ();
	}
	
	protected final void setDone() {
		this.done = true;
		this.looping = false;
	}
}