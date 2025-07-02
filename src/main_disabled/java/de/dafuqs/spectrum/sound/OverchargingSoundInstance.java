package de.dafuqs.spectrum.sound;

import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;

@Environment(EnvType.CLIENT)
public class OverchargingSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
	
	private final Player player;
    private final long lastParticleTick;
    private boolean done;
	
	public OverchargingSoundInstance(Player player) {
		super(SpectrumSoundEvents.OVERCHARGING, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = player;
		this.looping = false;
		this.delay = 0;
        this.volume = 0.4F;
		this.lastParticleTick = player.level().getGameTime() + TakeOffBeltItem.CHARGE_TIME_TICKS * TakeOffBeltItem.MAX_CHARGES;
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
		if (player == null || !player.isShiftKeyDown() || !player.isUsingItem() || !(player.getItemInHand(player.getUsedItemHand()).getItem() instanceof GlassCrestCrossbowItem)) {
            this.setDone();
        } else {
            this.x = ((float) player.getX());
            this.y = ((float) player.getY());
            this.z = ((float) player.getZ());
			
			if (player.level() != null && player.level().getGameTime() < lastParticleTick) {
                spawnParticles(player);
            } else {
                this.volume = 0.0F;
            }
        }
    }
	
	private void spawnParticles(Player player) {
		Level world = player.getCommandSenderWorld();
		RandomSource random = world.random;
		
		Vec3 pos = player.position();
		player.getCommandSenderWorld().addParticle(ColoredCraftingParticleEffect.WHITE,
				pos.x + random.nextDouble() * 0.8 - 0.4, pos.y, pos.z + random.nextDouble() * 0.8 - 0.4,
				0, random.nextDouble() * 0.5, 0);
	}

    protected final void setDone() {
        this.done = true;
		this.looping = false;
    }
}
