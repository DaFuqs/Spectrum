package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.sound.*;
import net.minecraft.entity.player.*;
import net.minecraft.sound.*;

@Environment(EnvType.CLIENT)
public class GreatswordChargingSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private final PlayerEntity player;
	private boolean done;
    private int ticks;
    private final int groundSlamChargeTicks;

    public GreatswordChargingSoundInstance(PlayerEntity player, int groundSlamChargeTicks) {
        super(SpectrumSoundEvents.GROUND_SLAM_CHARGE, SoundCategory.PLAYERS, SoundInstance.createRandom());
        this.groundSlamChargeTicks = groundSlamChargeTicks;
        this.repeat = false;
        this.ticks = 0;
        this.repeatDelay = 0;
        this.volume = 0.6F;
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
		this.ticks++;
        if (this.ticks > this.groundSlamChargeTicks) {
            // at this point the ground slam was handled and the effect does not need get cancelled anymore
        } else if (player == null || !player.isUsingItem() || !(player.getMainHandStack().getItem() instanceof GreatswordItem)) {
            this.setDone();
        }
		this.x = this.player.getX();
		this.y = this.player.getY();
		this.z = this.player.getZ();
	}
	
	protected final void setDone() {
		this.done = true;
		this.repeat = false;
	}
}